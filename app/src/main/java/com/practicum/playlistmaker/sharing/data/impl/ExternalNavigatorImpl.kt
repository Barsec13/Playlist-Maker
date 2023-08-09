package com.practicum.playlistmaker.sharing.data.impl

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.sharing.data.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.model.EmailData

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {

    private val linkToTheCourse = context.getString(R.string.link_to_the_course)
    private val message = context.getString(R.string.message)
    private val subject = context.getString(R.string.subject)
    private val emailDeveloper = context.getString(R.string.email_developer)
    private val linkToTheOffer = context.getString(R.string.link_to_the_offer)

    private val emailData = EmailData(emailDeveloper = emailDeveloper)


    override fun shareLink() {
        try {
            context.startActivity(
                Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, linkToTheCourse)
                    setFlags(FLAG_ACTIVITY_NEW_TASK)
                })
        }

        catch (_: Exception){ }

    }

    override fun openLink() {
        try {
            val uriUserAgreement = Uri.parse(linkToTheOffer)
            context.startActivity(
                Intent(Intent.ACTION_VIEW, uriUserAgreement).setFlags(FLAG_ACTIVITY_NEW_TASK)
            )
        }

        catch (_: Exception){ }
    }

    override fun openEmail() {
        try{
            context.startActivity(
                Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse(emailData.mailto)
                    putExtra(Intent.EXTRA_EMAIL, emailData.emailDeveloper)
                    putExtra(Intent.EXTRA_SUBJECT, subject)
                    putExtra(Intent.EXTRA_TEXT, message)
                    setFlags(FLAG_ACTIVITY_NEW_TASK)
                })
        }

        catch (_: Exception){ }
    }
}