package com.example.playlistmaker.domain.history.impl


import com.example.playlistmaker.data.history.HistoryRepository
import com.example.playlistmaker.domain.favourite.FavouriteRepository
import com.example.playlistmaker.domain.history.HistoryInteractor
import com.example.playlistmaker.domain.model.Track
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap

const val MAX_NUMBER_OF_TRACK = 10
const val INDEX_OF_OLDEST_TRACK = 9
const val INDEX_FOR_NEW_TRACK = 0


class HistoryInteractorImpl (
    private val historyRepository: HistoryRepository,
    private val favouriteRepository: FavouriteRepository
) : HistoryInteractor {


    override fun getHistoryString(): String {
        var historyString = historyRepository.getHistoryString()
        return if (historyString !=null) {
            historyString
        } else {
            ""
        }
    }

    override fun getHistoryList() : ArrayList<Track> {
        if (getHistoryString().isNotEmpty()) {
            var a = createTrackListFromJson(getHistoryString())
            return a
        }
        else {
            return arrayListOf<Track>()
        }

    }




    override fun addTrackToHistory(track: Track) {
        var currentHistoryString = getHistoryString()
        if (currentHistoryString.isNotEmpty()) {
            var currentHistoryArrayList = currentHistoryString?.let { createTrackListFromJson(it) }
            var currentHistoryArray = createTrackList1FromJson(currentHistoryString) // создаем из строки список треков array
            if (currentHistoryArrayList?.size !== 0) { // если история поиска не пустая
                // проверка на дубликат
                for (i in 0..(currentHistoryArrayList!!.size - 1)){
                    if (track.trackId == currentHistoryArray[i].trackId) {
                        currentHistoryArrayList.removeAt(i)
                        break
                    }
                }
                if (currentHistoryArrayList.size >= MAX_NUMBER_OF_TRACK) {
                    currentHistoryArrayList.removeAt(INDEX_OF_OLDEST_TRACK)
                    currentHistoryArrayList.add(INDEX_FOR_NEW_TRACK, track)

                } else {
                    currentHistoryArrayList.add(INDEX_FOR_NEW_TRACK, track)

                }

                updateHistory(currentHistoryArrayList)

            } }
        else {
            var currentHistoryArrayList: ArrayList<Track> = arrayListOf() // создаем пустой список истории
            currentHistoryArrayList.add(INDEX_FOR_NEW_TRACK, track)
            updateHistory(currentHistoryArrayList)
        }
    }

    override fun updateHistory(updatedHistory: ArrayList<Track>) {
        val updatedHistoryJson = createJsonFromTrackList(updatedHistory)
        historyRepository.updateTrackHistory(updatedHistoryJson)
    }


    override fun clearHistory() {
        historyRepository.clearHistory()
    }


    fun createTrackListFromJson(json: String): ArrayList<Track> {
        var a = Gson().fromJson(json, ArrayList<LinkedTreeMap<String, Any>>()::class.java)
        var b = ArrayList<Track>()
        a.forEach { aa ->
            var g:Double =  aa.get("trackTimeMillis") as Double
            var g2:Double =  aa.get("trackId") as Double

            b.add(
                Track(
                    aa.get("trackName") as String,
                    aa.get("artistName") as String,
                    g.toInt(),
                    aa.get("artworkUrl100") as String,
                    g2.toInt(),
                    aa.get("collectionName") as String,
                    aa.get("releaseDate") as String,
                    aa.get("primaryGenreName") as String,
                    aa.get("country") as String,
                    aa.get("previewUrl") as String
                )
            )
        }

        return b
    }

    fun createTrackList1FromJson(json: String): Array<Track> {
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    fun createJsonFromTrackList(trackList: ArrayList<Track>): String {
        return Gson().toJson(trackList)
    }

}



