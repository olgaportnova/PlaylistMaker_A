package com.example.playlistmaker.domain.setting.sharing
interface SharingInteractor {
    fun shareApp()
    fun openTerms()
    fun openSupport(subject:String, text:String)
    fun shareTracks(text: String)

}