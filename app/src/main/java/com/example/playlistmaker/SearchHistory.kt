package com.example.playlistmaker

import android.content.SharedPreferences
import com.example.playlistmaker.data.dto.TrackDto
import com.google.gson.Gson

const val SEARCH_HISTORY = "search_history"
const val TRACK_LIST_KEY = "track_list_key"


class SearchHistory {
    var trackList = arrayListOf<TrackDto>()


    // добавление нового трека в sharedPref
    fun addTrackToHistory(sharedPreferences: SharedPreferences, track: TrackDto) {

        val tracks = sharedPreferences.getString(TRACK_LIST_KEY, null)
        if (tracks != null) {

            // проверка на дубликат
            trackList = createTrackListFromJson(tracks)
            var trackList1 = createTrackList1FromJson(tracks)
            for (i in 0..(trackList.size - 1)) {


                if (track.trackId == trackList1[i].trackId) {
                    trackList.removeAt(i)
                    break
                }
            }

            if (trackList.size == 10) {
                trackList.removeAt(9)
                trackList.add(0, track)

            }

            if (trackList.size < 10) {
                trackList.add(0, track)
            }

        }
        if (tracks == null) {
            trackList = arrayListOf(track)

        }

        sharedPreferences.edit().putString(TRACK_LIST_KEY, createJsonFromTrackList(trackList))
            .apply()

    }





}
fun createTrackList1FromJson(json: String): Array<TrackDto> {
    return Gson().fromJson(json, Array<TrackDto>::class.java)

}
fun createJsonFromTrackList(trackList: ArrayList<TrackDto>): String {
    return Gson().toJson(trackList)
}
fun createTrackListFromJson(json: String): ArrayList<TrackDto> {
    return Gson().fromJson(json, ArrayList<TrackDto>()::class.java)

}

fun createJsonFromTrack(track: TrackDto): String {
    return Gson().toJson(track)
}


















