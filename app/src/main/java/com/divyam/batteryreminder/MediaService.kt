package com.divyam.batteryreminder

import android.app.*
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class MediaService : Service() {

    private var player: MediaPlayer? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val level = intent?.getIntExtra("battery_level", -1) ?: return START_NOT_STICKY

        val uri = when (level) {
            in 0..25 -> MainActivity().viewModel.lowUri
            in 26..75 -> MainActivity().viewModel.midUri
            in 76..99 -> MainActivity().viewModel.highUri
            else -> null
        }

        uri?.let {
            player = MediaPlayer().apply {
                setDataSource(applicationContext, it)
                isLooping = true
                prepare()
                start()
            }
        }

        if (level == 100) {
            player?.stop()
            sendFullBatteryNotification()
            stopSelf()
        }

        return START_STICKY
    }

    private fun sendFullBatteryNotification() {
        val notif = Notification.Builder(this)
            .setContentTitle("Battery Full")
            .setContentText("Charging complete.")
            .setSmallIcon(android.R.drawable.ic_lock_idle_charging)
            .build()

        val notifManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notifManager.notify(1, notif)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
