package com.example.playlistmaker.data.setting.sharing

import com.example.playlistmaker.domain.setting.sharing.model.EmailData

interface ExternalNavigator {

    fun shareLink(link:String)

    fun openLink(link: String)

    fun openEmail(emailToSupport: EmailData)
}