package com.example.playlistmaker.domain.setting.sharing.impl

import android.net.Uri
import com.example.playlistmaker.data.setting.sharing.ExternalNavigator
import com.example.playlistmaker.domain.setting.sharing.SharingInteractor
import com.example.playlistmaker.domain.setting.sharing.model.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return ("https://practicum.yandex.ru/profile/android-developer/")
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData ("yep4yep@gmail.com",
            "Сообщение разработчикам и разработчицам приложения Playlist Maker",
            "Спасибо разработчикам и разработчицам за крутое приложение!")
    }

    private fun getTermsLink(): Uri {
        return Uri.parse("https://yandex.ru/legal/practicum_offer/")
    }
}