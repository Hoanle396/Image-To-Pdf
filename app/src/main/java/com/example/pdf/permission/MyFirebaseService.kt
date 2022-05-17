package com.example.pdf.permission

import DBHelper
import android.app.NotificationManager
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.pdf.MainActivity
import com.example.pdf.helper.AppNotification
import com.example.pdf.models.NotificationPDF
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*


class MyFirebaseService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // handle a notification payload.
        if (remoteMessage.notification != null) {
            Log.d(
                TAG, "Message Notification Body: " + remoteMessage.notification!!
                    .body
            )
            sendNotification(remoteMessage.notification!!.body,remoteMessage.notification!!.title)
        }
    }

    private fun sendNotification(messageBody:String?,title:String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
       val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        var builder = NotificationCompat.Builder(this, AppNotification.CHANNEL_1_ID)
            .setSmallIcon(com.example.pdf.R.drawable.pdflogo)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)

        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(1, builder.build())
        val db = DBHelper(this, null)
        val start = Date()
        db.addNoti(NotificationPDF(title.toString(), messageBody.toString(),start.toString()))
    }

    companion object {
        private const val TAG = "MyFirebaseService"
    }
}
