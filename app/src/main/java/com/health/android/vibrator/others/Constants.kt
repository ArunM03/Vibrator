package com.health.android.vibrator.others

import com.health.android.vibrator.R
import com.health.android.vibrator.adapter.PatternAdapter
import com.health.android.vibrator.data.PatternData

object Constants {


    var isRunning = false
    var selectedPattern = "Ant"


    val ant = PatternData("Ant", R.drawable.ant)
    val leaf = PatternData("Leaf", R.drawable.leaficon2)
    val rain = PatternData("Rain", R.drawable.rain2)
    val flower = PatternData("Flower", R.drawable.flowericon2)
    val sand = PatternData("Sand", R.drawable.sand)
    val butterfly = PatternData("Butterfly", R.drawable.butterfly)

    val drill = PatternData("Drill", R.drawable.drill)
    val falcon = PatternData("Falcon", R.drawable.falcon)
    val cheetah = PatternData("Cheeta", R.drawable.cheetaicon)
    val gun = PatternData("Gun", R.drawable.machinegun,isPremium = true)
    val wind = PatternData("Wind", R.drawable.wind,isPremium = true)

    val dino = PatternData("Dino", R.drawable.dinosaur)
    val volcano = PatternData("Volcano", R.drawable.volcano)
    val steamEngine = PatternData("Steam Engine", R.drawable.steamengine4,isPremium = true)
    val cyclone = PatternData("Cyclone", R.drawable.cyclone2,isPremium = true)
    val waterfalls = PatternData("Waterfalls", R.drawable.waterfalls3,isPremium = true)
    val rocket = PatternData("Rocket", R.drawable.rocket,isPremium = true)

    val patternListSoft = listOf<PatternData>(ant, leaf, rain, flower, sand, butterfly)
    val patternListMedium = listOf<PatternData>(drill, falcon, cheetah, gun, wind)
    val patternListIntensive = listOf<PatternData>(dino, volcano, steamEngine, cyclone, waterfalls,
        rocket)

}