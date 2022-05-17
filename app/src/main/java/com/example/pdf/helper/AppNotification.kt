package com.example.pdf.helper

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build


class AppNotification : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel1 = NotificationChannel(
                CHANNEL_1_ID,
                "Channel 1",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel1.description = "This is channel 1"
            val channel2 = NotificationChannel(
                CHANNEL_2_ID,
                "Channel 2",
                NotificationManager.IMPORTANCE_LOW
            )
            channel1.description = "This is channel 2"
            val manager = this.getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(channel1)
            manager.createNotificationChannel(channel2)
        }
    }

    companion object {
        const val CHANNEL_1_ID = "channel1"
        const val CHANNEL_2_ID = "channel2"
    }
}