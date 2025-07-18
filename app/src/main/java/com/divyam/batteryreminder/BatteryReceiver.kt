package com.divyam.batteryreminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager

class BatteryReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val charging = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) > 0

        if (charging) {
            val serviceIntent = Intent(context, MediaService::class.java)
            serviceIntent.putExtra("battery_level", level)
            context.startService(serviceIntent)
        }
    }
}
