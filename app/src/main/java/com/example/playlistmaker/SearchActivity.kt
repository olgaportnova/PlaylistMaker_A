package com.example.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.util.Creator
import com.example.playlistmaker.data.history.impl.SEARCH_HISTORY
import com.example.playlistmaker.data.history.impl.TRACK_LIST_KEY
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.tracks.TrackSearchPresenter
import com.example.playlistmaker.presentation.tracks.TracksView
import com.example.playlistmaker.ui.tracks.models.TracksState
import com.google.gson.Gson


private lateinit var context: Context



class SearchActivity : AppCompatActivity(), TracksView, TrackAdapter.Listener, HistoryAdapter.Listener {

    companion object {
        const val SEARCH_TYPE = "SEARCH_TYPE"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }


    var tracks = ArrayList<Track>()
    private lateinit var binding: ActivitySearchBinding
    lateinit var sharedPref: SharedPreferences
    private val adapter = TrackAdapter(tracks, this)
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true
    lateinit var trackHistory: String
    private var textWatcher: TextWatcher? = null

    private lateinit var inputEditText: EditText
    private lateinit var placeholderMessage: LinearLayout
    private lateinit var rcTrackList: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var buttonUpdatePlaceholder: Button
    private lateinit var imagePlaceholder: ImageView
    private lateinit var textPlaceholder: TextView
    private lateinit var clearIcon: ImageView


    private var trackSearchPresenter: TrackSearchPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {


        context = applicationContext

        var historyInteractor = Creator.provideHistoryInteractor(context)
        historyInteractor.getHistoryString()

        sharedPref = getSharedPreferences(SEARCH_HISTORY, MODE_PRIVATE)
        trackHistory = sharedPref.getString(TRACK_LIST_KEY, null).toString()


        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)


        trackSearchPresenter = (this.application as? SearchActivity)?.trackSearchPresenter
        if (trackSearchPresenter == null) {
            trackSearchPresenter = Creator.provideTrackSearchPresenter(
                context = this.applicationContext, adapter = adapter
            )
            (this.application as? SearchActivity)?.trackSearchPresenter = trackSearchPresenter
        }

        trackSearchPresenter?.attachView(this)


        init()

        placeholderMessage = findViewById(R.id.placeholderMessage)
        inputEditText = findViewById(R.id.inputEditText)
        rcTrackList = findViewById(R.id.rcTrackList)
        progressBar = findViewById(R.id.progressBar)
        buttonUpdatePlaceholder = findViewById(R.id.buttonUpdatePlaceholder)
        imagePlaceholder = findViewById(R.id.imagePlaceholder)
        textPlaceholder = findViewById(R.id.textPlaceholder)
        clearIcon = findViewById(R.id.clearIcon)


        rcTrackList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rcTrackList.adapter = adapter

//        buttonUpdatePlaceholder.setOnClickListener {
//            trackSearchPresenter.searchDebounce( )
//        }



        inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                clearIcon.visibility = trackSearchPresenter!!.clearButtonVisibility(p0)
                trackSearchPresenter!!.searchDebounce(
                    changedText = p0?.toString() ?: ""
                )
            }

            override fun afterTextChanged(p0: Editable?) {
                }
            }
        )
        trackSearchPresenter!!.onCreate()



        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                trackSearchPresenter!!.searchDebounce(
                    changedText = s?.toString() ?: ""
                )
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        textWatcher?.let { inputEditText.addTextChangedListener(it) }




        // стрелка назад в меню
        binding.back.setOnClickListener {
            finish()
        }

        // очистка строки поиска
        binding.clearIcon.setOnClickListener {
            binding.inputEditText.setText("")
            val a = historyInteractor.getHistoryString()
            tracks.clear()
            adapter.notifyDataSetChanged()
            binding.rcTrackList.visibility=View.GONE
            binding.placeholderMessage.visibility = View.GONE
            showHistory()


        }

        // очистка истории поиска
        binding.buttonClearHistory.setOnClickListener {
            binding.searchHistoryLayout.visibility = View.VISIBLE
            historyInteractor.clearHistory()
            showHistory()

        }
    }

    override fun onStart() {
        super.onStart()
        trackSearchPresenter?.attachView(this)
    }

    override fun onResume() {
        super.onResume()
        trackSearchPresenter?.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        textWatcher?.let { inputEditText.removeTextChangedListener(it) }
        trackSearchPresenter?.detachView()
        trackSearchPresenter?.onDestroy()


        if (isFinishing()) {
            (this.application as? SearchActivity)?.trackSearchPresenter = null
        }
    }

    override fun onPause() {
        super.onPause()
        trackSearchPresenter?.detachView()
    }

    override fun onStop() {
        super.onStop()
        trackSearchPresenter?.detachView()
    }


    private fun init() {
        binding.apply {
            rcTrackList.layoutManager = LinearLayoutManager(this@SearchActivity)
            rcTrackList.adapter = adapter
        }
        showHistory()
    }


    // сохранение состояния
    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        outState.putString(SEARCH_TYPE, inputEditText.text.toString())
        trackSearchPresenter?.detachView()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        inputEditText.text = savedInstanceState.getString(SEARCH_TYPE) as Editable
    }


    // добавление трека в историю по клику и открыте в аудиоплеере
    override fun onClick(track: Track) {
        if (clickDebounce()) {
            val track = track
            var historyInteractor = Creator.provideHistoryInteractor(context)
            historyInteractor.addTrackToHistory(track)
            historyInteractor.openTrack(track)
        }
    }


    // оторбражение истории поиска
    private fun showHistory() {
        // достаем историю из sharedpref

        var historyInteractor = Creator.provideHistoryInteractor(context)
        var sharedPrefTest2 = historyInteractor.getHistoryString()
        binding.searchHistoryLayout.visibility = View.GONE
        // если пустая ничего не показываем

        if (sharedPrefTest2 == null) {
            binding.searchHistoryLayout.visibility = View.GONE
            return
        }
        if (sharedPrefTest2 != null) {
            val layoutManager = LinearLayoutManager(this)
            binding.trackHistoryRecyclerView.setLayoutManager(layoutManager)
            var adapterHistory = HistoryAdapter(createTrackList1FromJson(sharedPrefTest2), this)
            binding.trackHistoryRecyclerView.adapter = adapterHistory
            binding.placeholderMessage.visibility = View.GONE


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


    // контроль нажатий на трек (не быстрее чем 1 сек)
    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    fun createTrackList1FromJson(json: String?): Array<Track> {
        return Gson().fromJson(json, Array<Track>::class.java)
    }


    // cостояния и отрисовка элементов UI
    override fun render(state: TracksState) {
       when {
           state.isLoading -> showLoading()
           state.placeholderMessage !=null ->
               when {
                   state.needToUpdate -> showError(state.placeholderMessage, state.placeholderImage!!)
                   else -> showEmpty(state.placeholderMessage, state.placeholderImage!!)
               }
           else ->showContent(state.tracks)
               }
        }

    fun showLoading() {
        rcTrackList.visibility = View.GONE
        placeholderMessage.visibility = View.GONE
        progressBar.visibility = View.VISIBLE

    }

    fun showError(errorMessage: String, errorImage:Int) {
        rcTrackList.visibility = View.GONE
        progressBar.visibility = View.GONE
        placeholderMessage.visibility = View.VISIBLE
        buttonUpdatePlaceholder.visibility = View.VISIBLE

        textPlaceholder.text = errorMessage
        imagePlaceholder.setImageResource(errorImage)

    }

    fun showEmpty(emptyMessage: String, emptyImage:Int) {
        rcTrackList.visibility = View.GONE
        progressBar.visibility = View.GONE
        placeholderMessage.visibility = View.VISIBLE

        textPlaceholder.text = emptyMessage
        imagePlaceholder.setImageResource(emptyImage)
    }

    fun showContent(tracks: List<Track>) {
        rcTrackList.visibility = View.VISIBLE
        placeholderMessage.visibility = View.GONE
        progressBar.visibility = View.GONE

//        adapter.tracks.clear()
//        adapter.tracks.addAll(tracks)
//        adapter.notifyDataSetChanged()
    }


}


