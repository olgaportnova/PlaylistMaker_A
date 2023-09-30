package com.example.playlistmaker.presentation.search

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.audioPlayer.AudioPlayerViewModel
import com.example.playlistmaker.ui.tracks.models.TracksState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment(), TrackAdapter.Listener, TrackAdapter.LongClickListener {

    private lateinit var binding: FragmentSearchBinding
    private val searchTrackViewModel: SearchViewModel by viewModel()
    private val playerViewModel: AudioPlayerViewModel by viewModel()
    private var isClickAllowed = true
    private var textWatcher: TextWatcher? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        binding.clearIcon.visibility =
            if (binding.inputEditText.text.isNotEmpty()) View.VISIBLE else View.GONE


    }

    override fun onResume() {
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)





        searchTrackViewModel.getSearchTrackStatusLiveData().observe(viewLifecycleOwner) { updatedStatus ->
            updatedViewBasedOnStatus(updatedStatus)
        }
        binding.clearIcon.visibility =
            if (binding.inputEditText.text.isNotEmpty()) View.VISIBLE else View.GONE



        init()

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    searchTrackViewModel.searchDebounce(
                        changedText = s.toString()
                    )
                    binding.clearIcon.visibility =
                        if (binding.inputEditText.hasFocus() && s?.isNotEmpty() == true) View.VISIBLE else View.GONE
                } else {
                    searchTrackViewModel.showHistory()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        textWatcher?.let {
            binding.inputEditText.addTextChangedListener(it)
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

        binding.buttonUpdatePlaceholder.setOnClickListener {
            var searchTextRequest = binding.inputEditText.text.toString()
            searchTrackViewModel.searchAction(searchTextRequest)
        }

    }

    private fun init() {
        binding.apply {
            rcTrackList.layoutManager = LinearLayoutManager(requireContext())

        }
        searchTrackViewModel.showHistory()
    }


    // добавление трека в историю по клику и открыте в аудиоплеере
    override fun onClick(track: Track) {
        if (clickDebounce()) {
            searchTrackViewModel.addNewTrackToHistory(track)
            searchTrackViewModel.getHistory()

            searchTrackViewModel.openTrackAudioPlayer(track)
        }
    }

    override fun onLongClick(track: Track): Boolean {
        return true
    }

    // контроль нажатий на трек (не быстрее чем 1 сек)
    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MC)
                isClickAllowed = true
            }
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
            imagePlaceholder.visibility = View.GONE
            textPlaceholder.visibility = View.GONE
            buttonUpdatePlaceholder.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            buttonClearHistory.visibility = View.GONE
            textSearchHistory.visibility = View.GONE
        }
    }

    fun showError() {
        binding.apply {
            rcTrackList.visibility = View.GONE
            progressBar.visibility = View.GONE
            imagePlaceholder.visibility = View.VISIBLE
            textPlaceholder.visibility = View.VISIBLE
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
            imagePlaceholder.visibility = View.VISIBLE
            textPlaceholder.visibility = View.VISIBLE
            buttonUpdatePlaceholder.visibility = View.GONE
            buttonClearHistory.visibility = View.GONE
            textSearchHistory.visibility = View.GONE
            textPlaceholder.text = getString(R.string.nothing_found)
            imagePlaceholder.setImageResource(R.drawable.nothing_found)
        }

    }

    fun showContent(tracks: List<Track>) {
        binding.apply {
            rcTrackList.adapter = TrackAdapter(ArrayList(tracks), this@SearchFragment, this@SearchFragment )
            rcTrackList.visibility = View.VISIBLE
            imagePlaceholder.visibility = View.GONE
            textPlaceholder.visibility = View.GONE
            buttonUpdatePlaceholder.visibility = View.GONE
            progressBar.visibility = View.GONE
            buttonClearHistory.visibility = View.GONE
            textSearchHistory.visibility = View.GONE
        }
    }

    fun showHistoryUI(updatedHistory: List<Track>) {
        binding.apply {
            rcTrackList.adapter = TrackAdapter(ArrayList(updatedHistory), this@SearchFragment, this@SearchFragment)
            rcTrackList.visibility = View.VISIBLE
            imagePlaceholder.visibility = View.GONE
            textPlaceholder.visibility = View.GONE
            buttonUpdatePlaceholder.visibility = View.GONE
            progressBar.visibility = View.GONE
            buttonClearHistory.visibility = View.VISIBLE
            textSearchHistory.visibility = View.VISIBLE

        }
    }

    fun showHistoryIsEmpty(updatedHistory: List<Track>) {
        binding.apply {
            rcTrackList.adapter = TrackAdapter(ArrayList(updatedHistory), this@SearchFragment, this@SearchFragment )
            rcTrackList.visibility = View.VISIBLE
            imagePlaceholder.visibility = View.GONE
            textPlaceholder.visibility = View.GONE
            buttonUpdatePlaceholder.visibility = View.GONE
            progressBar.visibility = View.GONE
            buttonClearHistory.visibility = View.GONE
            textSearchHistory.visibility = View.GONE

        }
    }





    override fun onDestroy() {
        super.onDestroy()
        searchTrackViewModel.onDestroy()
    }



    companion object {
        const val SEARCH_TYPE = "SEARCH_TYPE"
        const val CLICK_DEBOUNCE_DELAY_MC = 1000L
    }



}