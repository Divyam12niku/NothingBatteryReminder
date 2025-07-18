package com.divyam.batteryreminder

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri

object AudioManager {
    private var mediaPlayer: MediaPlayer? = null

    fun play(context: Context, uri: Uri) {
        stop()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(context, uri)
            isLooping = true
            prepare()
            start()
        }
    }

    fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
