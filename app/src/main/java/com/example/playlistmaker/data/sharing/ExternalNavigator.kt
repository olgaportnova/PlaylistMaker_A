package com.example.playlistmaker.data.sharing

import android.net.Uri
import com.example.playlistmaker.domain.sharing.model.EmailData

interface ExternalNavigator {

    fun shareLink(link:String)

    fun openLink(link: Uri)

    fun openEmail(emailToSupport: EmailData)
}