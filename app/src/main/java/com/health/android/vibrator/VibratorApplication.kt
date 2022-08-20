package com.health.android.vibrator

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class VibratorApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setTheme(R.style.Theme_Vibrator)

    }

}
