package com.example.playlistmaker.presentation.search

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.tracks.TracksView
import com.example.playlistmaker.ui.tracks.models.TracksState

class SearchActivity : AppCompatActivity(), TracksView, TrackAdapter.Listener,
    HistoryAdapter.Listener {

    companion object {
        const val SEARCH_TYPE = "SEARCH_TYPE"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var searchTrackViewModel: SearchViewModel


    var tracks = ArrayList<Track>()
    private lateinit var binding: ActivitySearchBinding
    private val adapter = TrackAdapter(tracks, this)
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true
    private var textWatcher: TextWatcher? = null
    private lateinit var context: Context

    private lateinit var newUpdatedHistory: Array<Track>


    override fun onCreate(savedInstanceState: Bundle?) {

        context = applicationContext
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter.tracks = tracks


        // cоздание viewModel для истории
        historyViewModel = ViewModelProvider(
            this,
            HistoryViewModel.getViewModelFactory(context)
        )[HistoryViewModel::class.java]

        historyViewModel.getHistoryLiveData().observe(this) { updatedHistory ->
            updatedHistory(updatedHistory)
        }
        newUpdatedHistory = historyViewModel.getUpdatedHistory()




        // cоздание viewModel для поиска
        searchTrackViewModel = ViewModelProvider(
            this,
            SearchViewModel.getViewModelFactory(context)
        )[SearchViewModel::class.java]

        searchTrackViewModel.getSearchTrackStatusLiveData().observe(this) { updatedStatus ->
            updatedViewBasedOnStatus(updatedStatus)
        }
        newUpdatedHistory = historyViewModel.getUpdatedHistory()




//        trackSearchPresenter = (this.application as? SearchActivity)?.trackSearchPresenter
//        if (trackSearchPresenter == null) {
//            trackSearchPresenter = Creator.provideTrackSearchPresenter(
//                context = this.applicationContext, adapter = adapter
//            )
//            (this.application as? SearchActivity)?.trackSearchPresenter = trackSearchPresenter
//        }
//
//        trackSearchPresenter?.attachView(this)
//

        init()


        binding.inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchTrackViewModel.searchDebounce(
                    changedText = p0?.toString() ?: ""
                )
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }
        )
   //     searchTrackViewModel.onCreate()



        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchTrackViewModel.searchDebounce(
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
            showHistory(historyViewModel.getUpdatedHistory())
        }

        // очистка истории поиска
        binding.buttonClearHistory.setOnClickListener {
            binding.searchHistoryLayout.visibility = View.VISIBLE
            historyViewModel.clearHistory()
            showHistory(historyViewModel.getUpdatedHistory())

        }
    }


    private fun updatedHistory(updatedHistory: Array<Track>): Array<Track> {
        newUpdatedHistory = updatedHistory
        return newUpdatedHistory

    }

    private fun init() {
        binding.apply {
            rcTrackList.layoutManager = LinearLayoutManager(this@SearchActivity)
            rcTrackList.adapter = adapter
        }
        showHistory(newUpdatedHistory)
    }


    // сохранение состояния
//    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
//        super.onSaveInstanceState(outState, outPersistentState)
//        outState.putString(SEARCH_TYPE, binding.inputEditText.text.toString())
//  //      trackSearchPresenter?.detachView()
//    }
//
//    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//        super.onRestoreInstanceState(savedInstanceState)
//        binding.inputEditText.text = savedInstanceState.getString(SEARCH_TYPE) as Editable
//    }


    // добавление трека в историю по клику и открыте в аудиоплеере
    override fun onClick(track: Track) {
        if (clickDebounce()) {
            historyViewModel.addNewTrackToHistory(track)
            showHistory(historyViewModel.getUpdatedHistory())
            historyViewModel.openTrackAudioPlayer(track)
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



    // cостояния и отрисовка элементов UI

    override fun updatedViewBasedOnStatus(updatedStatus: TracksState) {
        when {
            updatedStatus.isLoading -> showLoading()
            updatedStatus.placeholderMessage != null ->
                when {
                    updatedStatus.needToUpdate -> showError(
                        updatedStatus.placeholderMessage,
                        updatedStatus.placeholderImage!!
                    )
                    else -> showEmpty(updatedStatus.placeholderMessage, updatedStatus.placeholderImage!!)
                }
            else -> showContent(updatedStatus.tracks)
        }
    }
    fun showLoading() {
        binding.apply {
            rcTrackList.visibility = View.GONE
            placeholderMessage.visibility = View.GONE
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
            adapter.tracks = ArrayList(tracks)
            rcTrackList.visibility = View.VISIBLE
            placeholderMessage.visibility = View.GONE
            progressBar.visibility = View.GONE
        }
    }

}