package com.health.android.vibrator

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.browser.trusted.sharing.ShareData
import com.health.android.vibrator.databinding.ActivityMainBinding
import com.health.android.vibrator.others.SharedPref
import com.health.android.vibrator.service.LocalBinder
import com.health.android.vibrator.service.PatternService
import android.content.Intent

import android.content.IntentFilter

import android.R.string.no
import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.*
import com.health.android.vibrator.others.Constants
import android.os.VibrationEffect

import android.media.AudioAttributes
import android.net.Uri
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.android.billingclient.api.*
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {

    lateinit var serviceIntent: Intent

    lateinit var vibrator : Vibrator

    var bounded = false

    private lateinit var billingClient: BillingClient
    lateinit var sharedPref : SharedPref

    var patternService: PatternService? = null
    lateinit var connection : ServiceConnection
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        serviceIntent = Intent(this,PatternService::class.java)


        sharedPref = SharedPref(this)

        handleRating()

        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(VIBRATOR_SERVICE) as Vibrator
        }

        checkVibrationFeature(vibrator)

        connection  = object : ServiceConnection{

            override fun onServiceConnected(p0: ComponentName?, service : IBinder?) {
                bounded = true
                val mLocalBinder = service as LocalBinder
                patternService = mLocalBinder.getServerInstance()
            }

            override fun onServiceDisconnected(p0: ComponentName?) {
                bounded = false
                patternService = null
            }

        }

        startService()

        val vibratorRecevier = object : BroadcastReceiver() {

            override fun onReceive(p0: Context?, intent: Intent) {
                if(intent.action.equals(Intent.ACTION_SCREEN_OFF)) {
                    if(sharedPref.getRunning()){
                        startVibrator(sharedPref.getCurrentSpeed(),sharedPref.getCurrentPattern())
                    }
                }
            }

        }

        val filter = IntentFilter(Intent.ACTION_SCREEN_OFF)
        filter.addAction(Intent.ACTION_SCREEN_ON)
        registerReceiver(vibratorRecevier, filter)


        //if (!sharedPref.getPaymentSaveStatus()){
            setBilling()
         // }

    }

    private fun handleRating() {

        val ratingCount = sharedPref.getRatingCount() + 1
        sharedPref.setRatingCount(ratingCount)

        if (ratingCount%3 == 0){
            if (!sharedPref.getRatedStatus()){
                showRatingDialog()
            }
        }

    }


    @SuppressLint("SetTextI18n")
    fun showRatingDialog(){

        lateinit var dialog : androidx.appcompat.app.AlertDialog

        val customview =  layoutInflater.inflate(R.layout.dialog_rating,null,false)

        val  happy = customview.findViewById<MaterialCardView>(R.id.cd_happy)
        val notHappy = customview.findViewById<MaterialCardView>(R.id.cd_nothappy)

        val title = customview.findViewById<MaterialTextView>(R.id.tv_title)
        val message = customview.findViewById<MaterialTextView>(R.id.tv_message)

        val sendFeedback = customview.findViewById<ExtendedFloatingActionButton>(R.id.fab_send)
        val rateOnGoogleplay = customview.findViewById<ExtendedFloatingActionButton>(R.id.fab_rate)

        notHappy.setOnClickListener {
            title.visibility = View.VISIBLE
            message.visibility = View.VISIBLE
            title.text = "Oh Sorry !"
            message.text = "Please give us some feedback"
            sendFeedback.visibility = View.VISIBLE
            rateOnGoogleplay.visibility = View.GONE
            setSelectedColor(happy,notHappy,notHappy)
        }

        happy.setOnClickListener {
            title.visibility = View.VISIBLE
            message.visibility = View.VISIBLE
            title.text = "Happy to hear this !"
            message.text = "Can you please give us good rating in Google play ?"
            sendFeedback.visibility = View.GONE
            rateOnGoogleplay.visibility = View.VISIBLE
            setSelectedColor(happy,notHappy,happy)
        }


        val builder = MaterialAlertDialogBuilder(this)
            .setView(customview)

        sendFeedback.setOnClickListener {
            sharedPref.setRatedStatus()
            Navigation.findNavController(this,R.id.nav_host_fragment_content_main).navigate(R.id.sendFeedbackFragment)
            dialog.dismiss()
        }

        rateOnGoogleplay.setOnClickListener {
            sharedPref.setRatedStatus()
            val uri: Uri = Uri.parse("market://details?id=${packageName}")
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            try {
                startActivity(goToMarket)
            } catch (e: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=${packageName}")))
            }
            dialog.dismiss()
        }

        dialog = builder.show()


    }


    private fun setSelectedColor(currentView : MaterialCardView,cd1 : MaterialCardView,cd2 : MaterialCardView) {

        currentView.setStrokeColor(ContextCompat.getColor(this,R.color.white))
        cd1.setStrokeColor(ContextCompat.getColor(this,R.color.white))

        cd2.setStrokeColor(ContextCompat.getColor(this,R.color.purple_500))

    }



    fun startService(){

        startService(serviceIntent)
        bindService(serviceIntent,connection, Context.BIND_AUTO_CREATE)
    }

    fun stopService(){

        if (bounded) {
            connection.let { unbindService(it) };
            bounded = false;
        }

        stopService(serviceIntent)

    }

    fun showServiceToast(){

        patternService?.showToast(this)

    }


    fun startVibrator(speed : Long,pattern : String){

        sharedPref.setRunning(true)

        Constants.isRunning = true

        patternService?.startVibration(vibrator,speed,pattern)

    }


    fun stopVibrator(){

       Constants.isRunning = false

       sharedPref.setRunning(false)

       patternService?.stopVibration(vibrator,this)

    }


    fun checkVibrationFeature(v : Vibrator){

        if(!v.hasVibrator()){
            Toast.makeText(this, "Sorry, This device has not vibrator.", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onDestroy() {
        super.onDestroy()

        sharedPref.setCurrentSpeed(2L)
        sharedPref.setCurrentPattern("Ant")

        stopVibrator()

        stopService()

    }

    private fun setBilling(){

        val purchaseUpdateListener =  PurchasesUpdatedListener { billingResult, purchases ->

        }


        billingClient = BillingClient.newBuilder(this)
            .enablePendingPurchases()
            .setListener(purchaseUpdateListener)
            .build()

        //   Toast.makeText(requireContext(),"billinclient is ready ${billingClient.isReady}", Toast.LENGTH_SHORT).show()

        val connectionListener = object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode ==  BillingClient.BillingResponseCode.OK) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val params = QueryPurchasesParams.newBuilder()
                            .setProductType(BillingClient.ProductType.INAPP)
                        val purchasesResult = billingClient.queryPurchasesAsync(params.build())
                        if(purchasesResult.purchasesList.isNotEmpty()){
                          /*    withContext(Dispatchers.Main){
                                  Toast.makeText(this@MainActivity,"purchaseList ${purchasesResult.purchasesList}", Toast.LENGTH_SHORT).show()
                              }*/
                            sharedPref.setPremiumStatus()
                        }else{
                    /*         withContext(Dispatchers.Main){
                                  Toast.makeText(this@MainActivity,"purchaseList empty ${purchasesResult.purchasesList}", Toast.LENGTH_SHORT).show()
                              }*/
                            sharedPref.setPremiumStatus(false)
                        }
                        sharedPref.setPaymentSaveStatus(true)
                    }
                } else {
                    Toast.makeText(this@MainActivity,billingResult.debugMessage + "error", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onBillingServiceDisconnected() {
                //       Toast.makeText(this@MainActivity,"Billing disconnected",Toast.LENGTH_SHORT).show()
            }


        }

        lifecycleScope.launch {
            delay(500)
            billingClient.startConnection(connectionListener)
        }
    }


}