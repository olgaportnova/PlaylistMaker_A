package com.example.playlistmaker.presentation.search

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.tracks.TrackSearchPresenter
import com.example.playlistmaker.presentation.tracks.TracksView
import com.example.playlistmaker.ui.tracks.models.TracksState
import com.example.playlistmaker.util.Creator
import com.google.gson.Gson

class SearchActivity : AppCompatActivity(), TracksView, TrackAdapter.Listener,
    HistoryAdapter.Listener {

    companion object {
        const val SEARCH_TYPE = "SEARCH_TYPE"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private lateinit var searchViewModel: SearchViewModel


    var tracks = ArrayList<Track>()
    private lateinit var binding: ActivitySearchBinding
    private val adapter = TrackAdapter(tracks, this)
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true
    private var textWatcher: TextWatcher? = null
    private lateinit var context: Context

    private lateinit var placeholderMessage: LinearLayout
    private lateinit var rcTrackList: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var newUpdatedHistory: Array<Track>


    private var trackSearchPresenter: TrackSearchPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {


        // cоздание viewModel для истории

        context = applicationContext
        searchViewModel = ViewModelProvider(
            this,
            SearchViewModel.getViewModelFactory(context)
        )[SearchViewModel::class.java]

        searchViewModel.getHistoryLiveData().observe(this) { updatedHistory ->
            updatedHistory(updatedHistory)
        }
        newUpdatedHistory = searchViewModel.getUpdatedHistory()



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


        binding.inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                trackSearchPresenter?.searchDebounce(
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
        textWatcher?.let {
            binding.inputEditText.addTextChangedListener(it)
        }


        // стрелка назад в меню
        binding.back.setOnClickListener {
            finish()
        }

        // очистка строки поиска
        binding.clearIcon.setOnClickListener {
            binding.inputEditText.setText("")
            tracks.clear()
            adapter.notifyDataSetChanged()
            binding.rcTrackList.visibility = View.GONE
            binding.placeholderMessage.visibility = View.GONE
            showHistory(searchViewModel.getUpdatedHistory())
        }

        // очистка истории поиска
        binding.buttonClearHistory.setOnClickListener {
            binding.searchHistoryLayout.visibility = View.VISIBLE
            searchViewModel.clearHistory()
            showHistory(searchViewModel.getUpdatedHistory())

        }
    }

    private fun updatedHistory(updatedHistory: Array<Track>): Array<Track> {
        newUpdatedHistory = updatedHistory
        return newUpdatedHistory

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
        textWatcher?.let { binding.inputEditText.removeTextChangedListener(it) }
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
        showHistory(newUpdatedHistory)
    }


    // сохранение состояния
    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putString(SEARCH_TYPE, binding.inputEditText.text.toString())
        trackSearchPresenter?.detachView()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding.inputEditText.text = savedInstanceState.getString(SEARCH_TYPE) as Editable
    }


    // добавление трека в историю по клику и открыте в аудиоплеере
    override fun onClick(track: Track) {
        if (clickDebounce()) {
            searchViewModel.addNewTrackToHistory(track)
            showHistory(searchViewModel.getUpdatedHistory())
            searchViewModel.openTrackAudioPlayer(track)
        }
    }


    // оторбражение истории поиска
    private fun showHistory(newUpdatedHistory: Array<Track>) {
        if (newUpdatedHistory.isEmpty()) {
            binding.searchHistoryLayout.visibility = View.GONE
        }
        val layoutManager = LinearLayoutManager(this)
        binding.trackHistoryRecyclerView.setLayoutManager(layoutManager)
        var adapterHistory = HistoryAdapter(newUpdatedHistory, this)
        binding.trackHistoryRecyclerView.adapter = adapterHistory
        binding.placeholderMessage.visibility = View.GONE
        binding.rcTrackList.visibility = View.GONE


        binding.inputEditText.setOnFocusChangeListener { view, hasFocus ->
            binding.searchHistoryLayout.visibility =
                if (hasFocus && binding.inputEditText.text.isEmpty() && (newUpdatedHistory.isNotEmpty())) View.VISIBLE else View.GONE
        }

        binding.inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.searchHistoryLayout.visibility =
                    if (binding.inputEditText.hasFocus() && p0?.isEmpty() == true && (newUpdatedHistory.isNotEmpty())) View.VISIBLE else View.GONE

                binding.clearIcon.visibility =
                    if (binding.inputEditText.hasFocus() && p0?.isNotEmpty() == true) View.VISIBLE else View.GONE
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
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
    override fun render(
        state: TracksState
    ) {
        when {
            state.isLoading -> showLoading()
            state.placeholderMessage != null ->
                when {
                    state.needToUpdate -> showError(
                        state.placeholderMessage,
                        state.placeholderImage!!
                    )
                    else -> showEmpty(state.placeholderMessage, state.placeholderImage!!)
                }
            else -> showContent(state.tracks)
        }
    }

    fun showLoading() {
        binding.apply {
            binding.rcTrackList.visibility = View.GONE
            binding.placeholderMessage.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
    }

    fun showError(errorMessage: String, errorImage: Int) {
        binding.apply {
            rcTrackList.visibility = View.GONE
            progressBar.visibility = View.GONE
            placeholderMessage.visibility = View.VISIBLE
            buttonUpdatePlaceholder.visibility = View.VISIBLE

            textPlaceholder.text = errorMessage
            imagePlaceholder.setImageResource(errorImage)

        }
    }

    fun showEmpty(emptyMessage: String, emptyImage: Int) {
        binding.apply {
            rcTrackList.visibility = View.GONE
            progressBar.visibility = View.GONE
            placeholderMessage.visibility = View.VISIBLE

            textPlaceholder.text = emptyMessage
            imagePlaceholder.setImageResource(emptyImage)
        }
    }

    fun showContent(tracks: List<Track>) {
        binding.apply {
            rcTrackList.visibility = View.VISIBLE
            placeholderMessage.visibility = View.GONE
            progressBar.visibility = View.GONE
        }
    }

    fun showContentHistory(tracks: List<Track>) {
        rcTrackList.visibility = View.VISIBLE
        placeholderMessage.visibility = View.GONE
        progressBar.visibility = View.GONE

    }


}