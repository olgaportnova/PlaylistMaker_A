package com.example.playlistmaker.di

import com.example.playlistmaker.data.db.AppDatabase
import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.history.HistoryRepository
import com.example.playlistmaker.data.history.impl.HistoryRepositoryImpl
import com.example.playlistmaker.data.network.Itunes
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.setting.settings.SettingsRepository
import com.example.playlistmaker.data.setting.settings.impl.SettingsRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val ITUNES_BASE_URL = "http://itunes.apple.com"
const val SHARED_PREFS_DARK_MODE = "shared_prefs_dark_mode"
const val SHARED_PREFS_SEARCH_HISTORY = "search_history"


    val dataModule = module {



            single {
                Room.databaseBuilder(androidContext(), AppDatabase::class.java, "playlist_app_database")
                    .addMigrations(AppDatabase.MIGRATION_3_4)
                    .build()
            }
            single { get<AppDatabase>().trackDao() }
            single { get<AppDatabase>().playlistDao() }
            single { get<AppDatabase>().trackInPlaylistDao() }

        single<Itunes> {
            Retrofit.Builder()
                .baseUrl(ITUNES_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Itunes::class.java)

        }

        factory (named(SHARED_PREFS_SEARCH_HISTORY)){
            androidContext()
                .getSharedPreferences(SHARED_PREFS_SEARCH_HISTORY, Context.MODE_PRIVATE)
        }

        factory (named(SHARED_PREFS_DARK_MODE)){
            androidContext()
                .getSharedPreferences(SHARED_PREFS_DARK_MODE, Context.MODE_PRIVATE)
        }



        single<HistoryRepository> {
            HistoryRepositoryImpl(get(),get())
        }

        single<NetworkClient> {
            RetrofitNetworkClient(get(), androidContext())
        }

        single<SettingsRepository> {
            SettingsRepositoryImpl(get())
        }

        single {
            return@single MediaPlayer()
        }
    }
