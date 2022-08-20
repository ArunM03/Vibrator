package com.health.android.vibrator.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.os.*
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class PatternService : Service() {

    var iBinder  : IBinder = LocalBinder()


    override fun onBind(p0: Intent?): IBinder? {
        return iBinder
    }

    override fun onCreate() {
        super.onCreate()

  }


    override fun onDestroy() {
        super.onDestroy()

  }

    fun showToast(context : Context){
        Toast.makeText(context, "Toast showing", Toast.LENGTH_SHORT).show()
    }


    fun startVibration(vibrator : Vibrator,curSpeed : Long,pattern : String){

        val curPattern = getPattern(pattern,curSpeed)

        vibrator.let {
            if (Build.VERSION.SDK_INT >= 26) {
                val audioAttributes =
                    AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .build()
                val vibrationEffect = VibrationEffect.createWaveform(curPattern,0)
                it.vibrate(vibrationEffect,audioAttributes)
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(curPattern,0)
            }
        }

    }

    fun getPattern(patternName : String,curSpeed : Long) : LongArray{

        val ant = longArrayOf(0,100,3000/curSpeed)
        val leaf = longArrayOf(0,100,3000/curSpeed,75,1500/curSpeed,75,1500/curSpeed,100,3000/curSpeed,500,1000/curSpeed)
        val rain = longArrayOf(0,100,500/curSpeed,100,500/curSpeed,100,500/curSpeed,100,500/curSpeed,100,500/curSpeed)
        val flower = longArrayOf(0,100,500/curSpeed,50 * curSpeed,500/curSpeed,100,500/curSpeed,50 * curSpeed,500/curSpeed,100,500/curSpeed)
        val sand = longArrayOf(0,50,50/curSpeed,50,50/curSpeed,75,50,75,50,50,50/curSpeed,50,50/curSpeed)
        val butterfly = longArrayOf(0,250 + (curSpeed * 25),250/curSpeed,300 + (curSpeed * 30),300/curSpeed,350 + (curSpeed * 35),350/curSpeed,400 + (curSpeed * 40) ,400/curSpeed,450 + (curSpeed * 45),450/curSpeed,500 + (curSpeed * 50),500/curSpeed)

        val drill = longArrayOf(0,100,100/curSpeed,100,100/curSpeed)
        val falcon = longArrayOf(0,500,1000/curSpeed,250,500/curSpeed,500,1000/curSpeed,250,500/curSpeed)
        val cheetah = longArrayOf(0,30,90/curSpeed,30,90/curSpeed,30,90/curSpeed,50,150/curSpeed,50,150/curSpeed,50,150/curSpeed,50,150/curSpeed,75,225/curSpeed,75,225/curSpeed,75,225/curSpeed,75,225/curSpeed,75,225/curSpeed,
            100,300/curSpeed,100,300/curSpeed,100,300/curSpeed,100,300/curSpeed,100,300/curSpeed,100,300/curSpeed,200,200/curSpeed,200,200/curSpeed)
        val gun = longArrayOf(0,500 * curSpeed ,1000)
        val wind = longArrayOf(0,30 * curSpeed ,500,40 * curSpeed,500,50 * curSpeed, 500, 60 * curSpeed,500, 70 * curSpeed, 500, 80 * curSpeed,500,
        90 * curSpeed, 500, 100 * curSpeed, 500)

        val volcano = longArrayOf(0,50 * curSpeed,3000/curSpeed,50 * curSpeed,3000/curSpeed,50 * curSpeed,3000/curSpeed,5000,5000/curSpeed,50 * curSpeed,3000/curSpeed,
            50 * curSpeed,4000/curSpeed,50 * curSpeed,4000/curSpeed, 50 * curSpeed,4000/curSpeed,50 * curSpeed,4000/curSpeed, 50 * curSpeed,4000/curSpeed,50 * curSpeed,4000/curSpeed,
            50 * curSpeed,4000/curSpeed,50 * curSpeed,4000/curSpeed,5000,5000/curSpeed)
        val steamEngine = longArrayOf(0,100,100/curSpeed,100,100/curSpeed,100,100/curSpeed,2000 * curSpeed,100
            ,2000 * curSpeed,100,2000 * curSpeed,100,100,100/curSpeed,100,100/curSpeed)
        val cyclone = longArrayOf(0,30 * curSpeed,30)
        val waterfalls = longArrayOf(0,500,500/curSpeed)
        val rocket = longArrayOf(0,50 * curSpeed,50,50 * curSpeed,50,50 * curSpeed,50,50 * curSpeed ,50,100 * curSpeed,100,
            100 * curSpeed,100,100 * curSpeed,100,100 * curSpeed,100,250 * curSpeed ,250,250 * curSpeed ,250,250 * curSpeed ,250,500,500,500,500,500,500,10000,2000)
        val dino = longArrayOf(0,3000,3000/curSpeed,2000,2000/curSpeed,1000,1000/curSpeed,500,500/curSpeed,250,250/curSpeed,125,4000/curSpeed)
       // val dino = longArrayOf(0,30,100/curSpeed)


      return  when(patternName){
            "Dino" -> dino
            "Volcano" -> volcano
            "Ant" -> ant
            "Leaf" -> leaf
            "Rain" -> rain
            "Flower" -> flower
            "Sand" -> sand
            "Butterfly" -> butterfly
            "Drill" -> drill
            "Falcon" -> falcon
            "Cheeta" -> cheetah
            "Gun" -> gun
            "Wind" -> wind
            "Steam Engine" -> steamEngine
            "Cyclone" -> cyclone
            "Waterfalls" -> waterfalls
            "Rocket" -> rocket
          else -> ant
        }

    }

    fun stopVibration(vibrator: Vibrator,context: Context){

        vibrator.cancel()

    }



}

class LocalBinder : Binder() {
    fun getServerInstance(): PatternService{
        return PatternService()
    }
}