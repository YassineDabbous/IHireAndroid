package tn.core.util

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import androidx.core.content.ContextCompat.startActivity
import android.content.Intent
import android.net.Uri


public class EMailUtils{
    public    fun sentEmail(acticity:Context, email:String, sender:String, subject:String, bodyText:String){
        val mailto = "mailto:" + email
        "?cc=" + sender +
                "&subject=" + Uri.encode(subject) +
                "&body=" + Uri.encode(bodyText)

        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse(mailto)

        try {
            acticity.startActivity(emailIntent)
        } catch (e: ActivityNotFoundException) {
            //TODO: Handle case where no email app is available
        }

    }
}