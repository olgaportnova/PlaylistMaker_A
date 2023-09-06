package com.example.playlistmaker.data.history.impl


import com.example.playlistmaker.data.db.AppDatabase
import android.content.SharedPreferences
import com.example.playlistmaker.data.history.HistoryRepository

const val SEARCH_HISTORY = "search_history"
const val TRACK_LIST_KEY = "track_list_key"
const val TRACK_TO_OPEN = "item"

class HistoryRepositoryImpl(
    private val sharedPref: SharedPreferences,
    private val appDatabase: AppDatabase,
): HistoryRepository {

    override fun updateTrackHistory(updatedHistory:String) {
        sharedPref.edit().
        putString(TRACK_LIST_KEY, updatedHistory)
            .apply()
    }


    override fun getHistoryString():String{
        return sharedPref.getString(TRACK_LIST_KEY, null) ?: ""
    }

    override fun clearHistory() {
        sharedPref.edit().remove(TRACK_LIST_KEY).apply()
    }

    override suspend fun getFavTracksIdList():List<Int> {
       return appDatabase.trackDao().getIdsOfFavouriteTracks()
    }

}