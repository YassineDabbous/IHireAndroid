package tn.core.util

import android.app.Notification
import android.content.Context
import android.app.NotificationManager
import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import tn.core.R


class NotificationsUtils {
    companion object {

        fun notify(ctx:Context, id:Int ,title:String, body:String, smallIcon:Int, sound:Uri, intent: PendingIntent){
            val bldr = NotificationCompat.Builder(ctx, "0").setContentTitle(title)
                    .setContentText(body)
                    .setSmallIcon(smallIcon)
                    .setSound(sound)
                    .setColorized(true)
                    //.setOngoing(true)
                    .setAutoCancel(true)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            val notification = bldr.build()
            notification.contentIntent = intent
            val manager = NotificationManagerCompat.from(ctx)
            manager.notify(id, notification)
        }
        fun notify(ctx:Context, id:Int, bldr:NotificationCompat.Builder, intent: PendingIntent){
            val notification = bldr.build()
            notification.contentIntent = intent
            val manager = NotificationManagerCompat.from(ctx)
            manager.notify(id, notification)
        }
        fun builder(ctx:Context, title:String, body:String, smallIcon:Int, sound:Uri) : NotificationCompat.Builder{
            val bldr = NotificationCompat.Builder(ctx, "0").setContentTitle(title)
                    .setContentText(body)
                    .setSmallIcon(smallIcon)
                    .setSound(sound)
                    .setColorized(true)
                    .setAutoCancel(true)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            return bldr;
        }







        ///////////////////// TODO

        fun notify(ctx:Context, icon:Int, light:Int=Color.GREEN) {
            val notificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
            val NOTIFICATION_CHANNEL_ID = "my_channel_id_01"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_HIGH)

                // Configure the notification channel.
                notificationChannel.description = "Channel description"
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = light
                notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
                notificationChannel.enableVibration(true)
                notificationManager!!.createNotificationChannel(notificationChannel)
            }


            val notificationBuilder = NotificationCompat.Builder(ctx, NOTIFICATION_CHANNEL_ID)

            notificationBuilder.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(icon)
                    .setTicker("Hearty365")
                    //     .setPriority(Notification.PRIORITY_MAX)
                    .setContentTitle("Default notification")
                    .setContentText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
                    .setContentInfo("Info")

            notificationManager!!.notify(/*notification id*/1, notificationBuilder.build())
        }
    }
}