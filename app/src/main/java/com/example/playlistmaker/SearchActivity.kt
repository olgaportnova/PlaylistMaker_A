package com.example.playlistmaker

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.presentation.AudioPlayerActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList


class SearchActivity : AppCompatActivity(), TrackAdapter.Listener, HistoryAdapter.Listener {
    var tracks = ArrayList<Track>()
    private lateinit var binding: ActivitySearchBinding
    lateinit var sharedPref: SharedPreferences
    lateinit var trackHistory: String
    private val adapter = TrackAdapter(tracks, this)
    private val searchRunnable = Runnable { searchAction() }
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true


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
        binding.inputEditText.addTextChangedListener {

                        binding.clearIcon.visibility = clearButtonVisibility(trackHistory)
                            searchDebounce()




        }
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
        if (binding.inputEditText.text.isEmpty()) {
            tracks.clear()
            binding.rcTrackList.visibility=View.GONE
        }
        if (binding.inputEditText.text.isNotEmpty()) {


            binding.placeholderMessage.visibility = View.GONE
            binding.rcTrackList.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE


            itunesService.search(binding.inputEditText.text.toString())
                .enqueue(object : Callback<SongsResponse> {
                    override fun onResponse(
                        call: Call<SongsResponse>, response: Response<SongsResponse>
                    ) {
                        binding.progressBar.visibility = View.GONE
                        if (response.code() == 200) {
                            tracks.clear()
                            if (response.body()?.results?.isNotEmpty() == true) {
                                tracks.addAll(response.body()?.results!!)
                                adapter.notifyDataSetChanged()
                                binding.placeholderMessage.visibility = View.GONE
                                binding.rcTrackList.visibility = View.VISIBLE
                            }
                            if ((tracks.isEmpty()) or (response.code() == 404)) {
                                showMessage(
                                    getString(R.string.nothing_found), R.drawable.nothing_found
                                )
                            }
                        } else {
                            showMessage(
                                getString(R.string.something_went_wrong), R.drawable.something_wrong
                            )
                            binding.buttonUpdate.visibility = View.VISIBLE
                            repeatSearch()

                        }
                    }

                    override fun onFailure(
                        call: Call<SongsResponse>, t: Throwable
                    ) {
                        binding.progressBar.visibility = View.GONE
                        showMessage(
                            getString(R.string.something_went_wrong), R.drawable.something_wrong
                        )
                        binding.buttonUpdate.visibility = View.VISIBLE
                        repeatSearch()
                    }
                })

        }
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


    // добавление трека в историю по клику и открыте в аудиоплеере
    override fun onClick(track: Track) {
        if (clickDebounce()) {

            val displayIntent = Intent(this, AudioPlayerActivity::class.java)
            displayIntent.putExtra("item", track)
            startActivity(displayIntent)


            var trackHistory = SearchHistory()
            trackHistory.addTrackToHistory(sharedPref, track)
            showHistory()
        }


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
            var adapterHistory = HistoryAdapter(createTrackList1FromJson(trackHistory), this)
            binding.trackHistoryRecyclerView.adapter = adapterHistory
            binding.placeholderMessage.visibility=View.GONE



            binding.inputEditText.setOnFocusChangeListener { view, hasFocus ->
                binding.searchHistoryLayout.visibility =
                    if (hasFocus && binding.inputEditText.text.isEmpty()) View.VISIBLE else View.GONE
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

    // поиск по вводу каждые 2 сек
    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)

    }

    // контроль нажатий на трек (не быстрее чем 1 сек)
    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    companion object {
        const val SEARCH_TYPE = "SEARCH_TYPE"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }


}



