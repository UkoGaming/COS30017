package com.example.assignment3

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool

class SoundManager(private val context: Context) {

    private val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    private var soundPool: SoundPool
    private var clickSoundId: Int = -1

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build()

        // Load sound immediately
        clickSoundId = soundPool.load(context, R.raw.ui_clicked, 1)
    }

    fun playClickSound() {
        if (isSoundEnabled() && clickSoundId != -1) {
            soundPool.play(clickSoundId, 1f, 1f, 0, 0, 1f)
        }
    }

    private fun isSoundEnabled(): Boolean {
        return prefs.getBoolean("app_sounds", true)
    }

    fun release() {
        soundPool.release()
    }
}