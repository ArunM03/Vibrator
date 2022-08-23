package com.health.android.vibrator.others

import android.content.Context

class SharedPref(val context : Context) {

    val sharedPref = context.getSharedPreferences("vibrator_pref", Context.MODE_PRIVATE)
    val editor = sharedPref.edit()


    val RUNNINGSTATUS = "RunningStatus"
    val CURRENTSPEED = "CurSpeed"
    val CURRENTPATTERN = "CurPattern"
    val RATINGCOUNT = "RatingCount"
    val RATEDSTATUS = "RatingStatus"
    val CHANGEAPPICON = "ChangeAppIcon"
    val PREMIUMSTATUS = "PremiumStatus"
    val PAYMENTSAVED = "PaymentSaved"
    val INTERSTITIALSHOWN = "InterstitialShown"



    fun setRunning(status : Boolean){
        editor.apply {
            putBoolean(RUNNINGSTATUS,status)
            apply()
        }
    }

    fun getRunning() : Boolean{
       return sharedPref.getBoolean(RUNNINGSTATUS,false)
    }

    fun setCurrentSpeed(speed : Long){
        editor.apply {
            putLong(CURRENTSPEED,speed)
            apply()
        }
    }

    fun setRatingCount(count : Int){
        editor.apply {
            putInt(RATINGCOUNT,count)
            apply()
        }
    }

    fun setInterstitialShown(shown : Boolean){
        editor.apply {
            putBoolean(INTERSTITIALSHOWN,shown)
            apply()
        }
    }

    fun setCurrentIcon(icon : String){
        editor.apply {
            putString(CHANGEAPPICON,icon)
            apply()
        }
    }

    fun setPremiumStatus(status : Boolean = true){
        editor.apply {
            putBoolean(PREMIUMSTATUS,status)
            putBoolean(PAYMENTSAVED,status)
            apply()
        }
    }

    fun setPaymentSaveStatus(status : Boolean){
        editor.apply {
            putBoolean(PAYMENTSAVED,status)
            apply()
        }
    }

    fun getPremiumStatus() : Boolean{
        return sharedPref.getBoolean(PREMIUMSTATUS,false)
    }

    fun getPaymentSaveStatus() : Boolean{
        return sharedPref.getBoolean(PAYMENTSAVED,false)
    }

    fun setRatedStatus(){
        editor.apply {
            putBoolean(RATEDSTATUS,true)
            apply()
        }
    }

    fun getRatedStatus() : Boolean{
        return sharedPref.getBoolean(RATEDSTATUS,false)
    }

    fun getRatingCount() : Int{
        return sharedPref.getInt(RATINGCOUNT,1)
    }

    fun getInterstitialShown() : Boolean{
        return sharedPref.getBoolean(INTERSTITIALSHOWN,true)
    }


    fun getCurrentSpeed() : Long{
        return sharedPref.getLong(CURRENTSPEED,2L)
    }

    fun setCurrentPattern(pattern : String){
        editor.apply {
            putString(CURRENTPATTERN,pattern)
            apply()
        }
    }

    fun getCurrentPattern() : String{
        return sharedPref.getString(CURRENTPATTERN,"Ant") ?: "Ant"
    }


}