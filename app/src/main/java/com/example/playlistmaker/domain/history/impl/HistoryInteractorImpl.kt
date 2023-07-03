package com.example.playlistmaker.domain.history.impl


import com.example.playlistmaker.presentation.search.HistoryAdapter
import com.example.playlistmaker.data.history.HistoryRepository
import com.example.playlistmaker.domain.history.HistoryInteractor
import com.example.playlistmaker.domain.model.Track
import com.google.gson.Gson

//private var isClickAllowed = true
//private val handler = Handler(Looper.getMainLooper())

class HistoryInteractorImpl (private val historyRepository: HistoryRepository) : HistoryInteractor, HistoryAdapter.Listener {


    override fun getHistoryString(): String {
        var historyString = historyRepository.getHistoryString()
        if (historyString !=null) {
            return historyString}
        else {
                return ""
        }
    }

    override fun getHistoryList() : Array<Track> {
        if (getHistoryString().isNotEmpty()) {
            return createTrackList1FromJson(getHistoryString())}
        else {
            return arrayOf<Track>()
        }

    }


    override fun addTrackToHistory(track: Track) {
    //    val currentHistoryArrayList = getHistory() // достаем из SH текущую историю
        var currentHistoryString = getHistoryString()
        if (currentHistoryString.isNotEmpty()) {
        var currentHistoryArrayList = currentHistoryString?.let { createTrackListFromJson(it) }
        var currentHistoryArray = createTrackList1FromJson(currentHistoryString) // создаем из строки список треков array
        if (currentHistoryArrayList!!.size !== 0) { // если история поиска не пустая
            // проверка на дубликат
            for (i in 0..(currentHistoryArrayList!!.size - 1)){
                if (track.trackId == currentHistoryArray[i].trackId) {
                    currentHistoryArrayList.removeAt(i)
                    break
                }
            }
            if (currentHistoryArrayList.size >= 10) {
                currentHistoryArrayList.removeAt(9)
                currentHistoryArrayList.add(0, track)

            } else {
                currentHistoryArrayList.add(0, track)

            }

            updateHistory(currentHistoryArrayList)

        } }
        else {
            var currentHistoryArrayList: ArrayList<Track> = arrayListOf() // создаем пустой список истории
            currentHistoryArrayList.add(0, track)
            var a = currentHistoryArrayList
            updateHistory(currentHistoryArrayList)
        }
    }

    override fun updateHistory(updatedHistory: ArrayList<Track>) {
        val updatedHistoryJson = createJsonFromTrackList(updatedHistory)
        historyRepository.updateTrackHistory(updatedHistoryJson)
    }

    override fun openTrack(track:Track) {
        historyRepository.openTrack(track)
    }

    override fun clearHistory() {
        historyRepository.clearHistory()
    }

    override fun onClick(track: Track) {


    }

//    fun clickDebounce(): Boolean {
//        val current = isClickAllowed
//        if (isClickAllowed) {
//            isClickAllowed = false
//            handler.postDelayed({ isClickAllowed = true }, 1000L)
//        }
//        return current
//    }


    fun createTrackListFromJson(json: String): ArrayList<Track> {
        return Gson().fromJson(json, ArrayList<Track>()::class.java)
    }

    fun createTrackList1FromJson(json: String): Array<Track> {
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    fun createJsonFromTrackList(trackList: ArrayList<Track>): String {
        return Gson().toJson(trackList)
    }

    fun createJsonFromTrack(track: Track): String {
        return Gson().toJson(track)
    }
}



