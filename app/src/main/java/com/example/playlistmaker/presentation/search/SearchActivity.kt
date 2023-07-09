package com.example.playlistmaker.presentation.search

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.tracks.models.TracksState

class SearchActivity : AppCompatActivity(), TrackAdapter.Listener
    {


    private lateinit var searchTrackViewModel: SearchViewModel

    private lateinit var binding: ActivitySearchBinding
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true
    private var textWatcher: TextWatcher? = null
    private lateinit var context: Context



    override fun onCreate(savedInstanceState: Bundle?) {

        context = applicationContext
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)



        // cоздание viewModel
        searchTrackViewModel = ViewModelProvider(
            this,
            SearchViewModel.getViewModelFactory()
        )[SearchViewModel::class.java]

        searchTrackViewModel.getSearchTrackStatusLiveData().observe(this) { updatedStatus ->
            updatedViewBasedOnStatus(updatedStatus)
        }


        searchTrackViewModel.onCreate()
        init()



        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!=null) {
                    searchTrackViewModel.searchDebounce(
                        changedText = s.toString()
                    )
                    binding.clearIcon.visibility =
                        if (binding.inputEditText.hasFocus() && s?.isNotEmpty() == true) View.VISIBLE else View.GONE
                }
                else {searchTrackViewModel.showHistory()}
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
            searchTrackViewModel.showHistory()

        }

        // очистка истории поиска
        binding.buttonClearHistory.setOnClickListener {
            searchTrackViewModel.clearHistory()
            searchTrackViewModel.showHistory()

        }
    }

    private fun init() {
        binding.apply {
            rcTrackList.layoutManager = LinearLayoutManager(this@SearchActivity)

        }
        searchTrackViewModel.showHistory()
    }



    // добавление трека в историю по клику и открыте в аудиоплеере
    override fun onClick(track: Track) {
        if (clickDebounce()) {
            searchTrackViewModel.onClick(track)

        }
    }



    // контроль нажатий на трек (не быстрее чем 1 сек)
    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY_MC)
        }
        return current
    }


    // cостояния и отрисовка элементов UI

     fun updatedViewBasedOnStatus(updatedStatus: TracksState) {
        when {
            updatedStatus.isLoading -> showLoading()
            updatedStatus.toShowHistory ->
                when {
                    updatedStatus.history.isNotEmpty() -> showHistoryUI(updatedStatus.history)
                    else -> showHistoryIsEmpty(updatedStatus.history)
                }
            updatedStatus.placeholderMessage != null ->
                when {
                    updatedStatus.needToUpdate -> showError()
                    else -> showEmpty()
                }
            else -> showContent(updatedStatus.tracks)
        }

    }
    fun showLoading() {
        binding.apply {
            rcTrackList.visibility = View.GONE
            placeholderMessage.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            buttonClearHistory.visibility = View.GONE
            textSearchHistory.visibility = View.GONE
        }
    }
    fun showError() {
        binding.apply {
            rcTrackList.visibility = View.GONE
            progressBar.visibility = View.GONE
            placeholderMessage.visibility = View.VISIBLE
            buttonUpdatePlaceholder.visibility = View.VISIBLE
            buttonClearHistory.visibility = View.GONE
            textSearchHistory.visibility = View.GONE
            textPlaceholder.text = getString(R.string.something_went_wrong)
            imagePlaceholder.setImageResource(R.drawable.something_wrong)

        }
    }
    fun showEmpty() {

            binding.apply {
                rcTrackList.visibility = View.GONE
                progressBar.visibility = View.GONE
                placeholderMessage.visibility = View.VISIBLE
                buttonClearHistory.visibility = View.GONE
                textSearchHistory.visibility = View.GONE
                textPlaceholder.text =  getString(R.string.nothing_found)
                imagePlaceholder.setImageResource(R.drawable.nothing_found)
            }

    }
    fun showContent(tracks: List<Track>) {
        binding.apply {
            rcTrackList.adapter = TrackAdapter(ArrayList(tracks),this@SearchActivity)
            rcTrackList.visibility = View.VISIBLE
            placeholderMessage.visibility = View.GONE
            progressBar.visibility = View.GONE
            buttonClearHistory.visibility = View.GONE
            textSearchHistory.visibility = View.GONE
        }
    }
    fun showHistoryUI(updatedHistory: List<Track>) {
        binding.apply {
            rcTrackList.adapter = TrackAdapter(ArrayList(updatedHistory),this@SearchActivity)
            rcTrackList.visibility = View.VISIBLE
            placeholderMessage.visibility = View.GONE
            progressBar.visibility = View.GONE
            buttonClearHistory.visibility = View.VISIBLE
            textSearchHistory.visibility = View.VISIBLE

        }
    }
    fun showHistoryIsEmpty(updatedHistory: List<Track>) {
        binding.apply {
            rcTrackList.adapter = TrackAdapter(ArrayList(updatedHistory),this@SearchActivity)
            rcTrackList.visibility = View.VISIBLE
            placeholderMessage.visibility = View.GONE
            progressBar.visibility = View.GONE
            buttonClearHistory.visibility = View.GONE
            textSearchHistory.visibility = View.GONE

        }
    }

        override fun onDestroy() {
            super.onDestroy()
            searchTrackViewModel.onDestroy()
        }

        override fun onResume() {
            super.onResume()
            searchTrackViewModel.onResume()
        }


        companion object {
            const val SEARCH_TYPE = "SEARCH_TYPE"
            private const val CLICK_DEBOUNCE_DELAY_MC= 1000L
        }

}