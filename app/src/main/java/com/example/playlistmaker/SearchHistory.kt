package com.example.playlistmaker

import android.content.SharedPreferences
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.gson.Gson

const val SEARCH_HISTORY = "search_history"
const val TRACK_LIST_KEY = "track_list_key"


class SearchHistory() {
    var trackList = arrayListOf<Track>()


    // добавление нового трека в sharedPref
    fun addTrackToHistory(sharedPreferences: SharedPreferences, track: Track) {

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
            trackList = arrayListOf<Track>(track)

        }

        sharedPreferences.edit().putString(TRACK_LIST_KEY, createJsonFromTrackList(trackList))
            .apply()

    }





}
fun createTrackList1FromJson(json: String): Array<Track> {
    return Gson().fromJson(json, Array<Track>::class.java)

}
fun createJsonFromTrackList(trackList: ArrayList<Track>): String {
    return Gson().toJson(trackList)
}
fun createTrackListFromJson(json: String): ArrayList<Track> {
    return Gson().fromJson(json, ArrayList<Track>()::class.java)

}

fun createJsonFromTrack(track: Track): String {
    return Gson().toJson(track)
}


















