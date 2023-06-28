package com.example.playlistmaker.data.sharing.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.data.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.model.EmailData


class ExternalNavigationImpl(private val context: Context) : ExternalNavigator {
    override fun shareLink(link: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        shareIntent.putExtra(Intent.EXTRA_TEXT, link)
        shareIntent.type = "text/plain"
        context.startActivity(shareIntent)
    }

    override fun openLink(link: Uri) {
        val agreementIntent = Intent(Intent.ACTION_VIEW).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        agreementIntent.data = link
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