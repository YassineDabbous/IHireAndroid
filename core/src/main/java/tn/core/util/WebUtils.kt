package tn.core.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.webkit.URLUtil
import android.widget.Toast

import java.text.SimpleDateFormat
import java.util.Date

import tn.core.util.time.TimeAgo

object WebUtils {

    fun viewOnMap(context: Context, lat: String, lng: String, zoom:String="17") {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("geo:$lat,$lng?z=$zoom")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "عذرا، ليس لديك متصفح خرائط !", Toast.LENGTH_LONG).show()
        }

    }

    fun openLink(context: Context, url: String) {
        try {
            if (!URLUtil.isValidUrl(url)) {
                Log.e("error", "This is not a valid link: $url")
            } else {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            }
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "عذرا، ليس لديك متصفح واب لفتح الرابط !", Toast.LENGTH_LONG).show()
        }

    }
}
