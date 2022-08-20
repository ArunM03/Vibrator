package com.health.android.vibrator.service

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import com.health.android.vibrator.BuildConfig


/*
class ChangeAppIconService: Service() {
    private val aliases = arrayOf(".one", ".two")

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onTaskRemoved(rootIntent: Intent?) {
        changeAppIcon()
        stopSelf()
    }

    fun changeAppIcon() {
        val sp = getSharedPreferences("vibrator_pref", Context.MODE_PRIVATE)

        sp.getString("ChangeAppIcon", ".two").let { aliasName ->
            if (!aliasName?.let { isAliasEnabled(it) }!!) {
                setAliasEnabled(aliasName)
            }
        }
    }

    private fun isAliasEnabled(aliasName: String): Boolean {
        return packageManager.getComponentEnabledSetting(
            ComponentName(
                this,
                "${BuildConfig.APPLICATION_ID}$aliasName"
            )
        ) == PackageManager.COMPONENT_ENABLED_STATE_ENABLED
    }

    private fun setAliasEnabled(aliasName: String) {
        aliases.forEach {
            val action = if (it == aliasName)
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED
            else
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                
            packageManager.setComponentEnabledSetting(
                ComponentName(
                    this,
                    "${BuildConfig.APPLICATION_ID}$aliasName"
                ),
                action,
                PackageManager.DONT_KILL_APP
            )
        }
    }
}*/
