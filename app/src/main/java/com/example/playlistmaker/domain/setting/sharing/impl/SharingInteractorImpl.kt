package com.example.playlistmaker.domain.setting.sharing.impl


import com.example.playlistmaker.data.setting.sharing.ExternalNavigator
import com.example.playlistmaker.domain.setting.sharing.SharingInteractor
import com.example.playlistmaker.domain.setting.sharing.model.EmailData

const val LINK_TO_SHARE = "https://practicum.yandex.ru/profile/android-developer/"
const val SENDER_EMAIL = "yep4yep@gmail.com"
const val LINK_TERMS = "https://yandex.ru/legal/practicum_offer/"
class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport(subject:String, text: String) {
        externalNavigator.openEmail(EmailData(SENDER_EMAIL,subject,text))
    }

    private fun getShareAppLink(): String {
        return (LINK_TO_SHARE)
    }


    private fun getTermsLink(): String {
        return LINK_TERMS
    }
}