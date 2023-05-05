package com.example.playlistmaker

import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList


 class SearchActivity : AppCompatActivity(), TrackAdapter.Listener {
    var tracks = ArrayList<Track>()
    private lateinit var binding: ActivitySearchBinding
    lateinit var sharedPref:SharedPreferences
    lateinit var trackHistory:String
    private val adapter = TrackAdapter(tracks, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPref = getSharedPreferences(SEARCH_HISTORY, MODE_PRIVATE)
        trackHistory = sharedPref.getString(TRACK_LIST_KEY, null).toString()
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()


        // стрелка назад в меню
        binding.back.setOnClickListener {
            finish()
        }

        // очистка строки поиска
        binding.clearIcon.setOnClickListener {
            binding.inputEditText.setText("")
            tracks.clear()
            adapter.notifyDataSetChanged()
            binding.placeholderMessage.visibility = View.INVISIBLE
        }

        // очистка истории поиска
        binding.buttonClearHistory.setOnClickListener {
            sharedPref.edit().remove(TRACK_LIST_KEY).apply()
            showHistory()
        }


        // поиск треков по вводу
        binding.inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.inputEditText.text.isNotEmpty()) {

                    searchAction()
                }
                return@setOnEditorActionListener true
            }
            false
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {


                binding.clearIcon.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        binding.inputEditText.addTextChangedListener(simpleTextWatcher)
    }

    // сообщение плейсхолдера
    private fun showMessage(text: String, image: Int) {
        if (text.isNotEmpty()) {
            binding.placeholderMessage.visibility = View.VISIBLE
            binding.imageNothingFound.setImageResource(image)
            binding.textNothingFound.text = text
            tracks.clear()
            adapter.notifyDataSetChanged()

        } else {
            binding.placeholderMessage.visibility = View.GONE
        }
    }

    // функция получение информации из iTunes
    private fun searchAction() {

        itunesService.search(binding.inputEditText.text.toString())
            .enqueue(object : Callback<SongsResponse> {
                override fun onResponse(
                    call: Call<SongsResponse>,
                    response: Response<SongsResponse>
                ) {
                    if (response.code() == 200) {
                        tracks.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            tracks.addAll(response.body()?.results!!)
                            adapter.notifyDataSetChanged()
                            binding.placeholderMessage.visibility = View.GONE
                        }
                        if ((tracks.isEmpty()) or (response.code() == 404)) {
                            showMessage(
                                getString(R.string.nothing_found),
                                R.drawable.nothing_found
                            )
                        }
                    } else {
                        showMessage(
                            getString(R.string.something_went_wrong),
                            R.drawable.something_wrong
                        )
                        binding.buttonUpdate.visibility = View.VISIBLE
                        repeatSearch()

                    }
                }

                override fun onFailure(
                    call: Call<SongsResponse>,
                    t: Throwable
                ) {
                    showMessage(
                        getString(R.string.something_went_wrong),
                        R.drawable.something_wrong
                    )
                    binding.buttonUpdate.visibility = View.VISIBLE
                    repeatSearch()
                }
            })
    }

    // повторный поиск после нажатия на кнопку "Обновить"
    private fun repeatSearch() {
        binding.buttonUpdate.setOnClickListener {
            searchAction()
        }
    }


    private fun init() {
        binding.apply {
            rcTrackList.layoutManager = LinearLayoutManager(this@SearchActivity)
            rcTrackList.adapter = adapter
        }
        showHistory()
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    // сохранение состояния
    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        outState.putString(SEARCH_TYPE, inputEditText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        inputEditText.text = savedInstanceState.getString(SEARCH_TYPE) as Editable
    }

    companion object {
        const val SEARCH_TYPE = "SEARCH_TYPE"
    }

    // добавление трека в историю по клику
    override fun onClick(track: Track) {

        var trackHistory = SearchHistory()
        trackHistory.addTrackToHistory(sharedPref, track)
        showHistory()

    }


    // оторбражение истории поиска
    private fun showHistory() {
        // достаем историю из sharedpref
        var trackHistory = sharedPref.getString(TRACK_LIST_KEY, null)
        // если пустая ничего не показываем
        if (trackHistory == null) {
            binding.searchHistoryLayout.visibility = View.GONE
            return
        }

        if (trackHistory != null) {
            val layoutManager = LinearLayoutManager(this)
            binding.trackHistoryRecyclerView.setLayoutManager(layoutManager)
            var adapterHistory = HistoryAdapter(createTrackList1FromJson(trackHistory))
            binding.trackHistoryRecyclerView.adapter = adapterHistory



            binding.inputEditText.setOnFocusChangeListener { view, hasFocus ->
                binding.searchHistoryLayout.visibility = if (hasFocus && binding.inputEditText.text.isEmpty()) View.VISIBLE else View.GONE
            }

            binding.inputEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    binding.searchHistoryLayout.visibility =
                        if (binding.inputEditText.hasFocus() && p0?.isEmpty() == true) View.VISIBLE else View.GONE
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })


        }


    }


}



