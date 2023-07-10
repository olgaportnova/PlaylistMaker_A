package com.example.playlistmaker.data.setting.sharing.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.data.setting.sharing.ExternalNavigator
import com.example.playlistmaker.domain.setting.sharing.model.EmailData


class ExternalNavigationImpl(private val context: Context) : ExternalNavigator {
    override fun shareLink(link: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        shareIntent.putExtra(Intent.EXTRA_TEXT, link)
        shareIntent.type = "text/plain"
        context.startActivity(shareIntent)
    }

    override fun openLink(link: String) {
        val agreementIntent = Intent(Intent.ACTION_VIEW).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        agreementIntent.data = Uri.parse(link)
        context.startActivity(agreementIntent)
    }

    override fun openEmail(emailToSupport: EmailData) {
        val supportIntent = Intent(Intent.ACTION_SENDTO).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        supportIntent.data = Uri.parse("mailto:")
        supportIntent.putExtra(Intent.EXTRA_EMAIL, emailToSupport.email)
        supportIntent.putExtra(Intent.EXTRA_SUBJECT, emailToSupport.subject)
        supportIntent.putExtra(Intent.EXTRA_TEXT,emailToSupport.text)
        context.startActivity(supportIntent)
    }

}