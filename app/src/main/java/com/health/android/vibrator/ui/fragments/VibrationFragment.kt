package com.health.android.vibrator.ui.fragments

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.EditText
import android.widget.SeekBar
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textview.MaterialTextView
import com.health.android.vibrator.BuildConfig
import com.health.android.vibrator.MainActivity
import com.health.android.vibrator.R
import com.health.android.vibrator.databinding.FragmentHomeBinding
import com.health.android.vibrator.databinding.FragmentVibrationBinding
import com.health.android.vibrator.others.Constants
import com.health.android.vibrator.others.SharedPref
import com.health.android.vibrator.ui.HomeFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VibrationFragment : Fragment(R.layout.fragment_vibration) {

    lateinit var binding : FragmentVibrationBinding
    var isRunning = false
    lateinit var sharedPref: SharedPref
    private var mInterstitialAd: InterstitialAd? = null
    val aliases = listOf<String>(".icon1",".icon2",".icon3")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentVibrationBinding.bind(view)

        setUI(view)

    }

    private fun setUI(view: View) {

        sharedPref = SharedPref(requireContext())

        isRunning = sharedPref.getRunning()

        setColor()

        binding.sbSpeed.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{

            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
               // sharedPref.setCurrentSpeed(6000L-p1*1000L)
                if(p1 == 0){
                    binding.tvSpeed.text =  0.5.toString()
                    binding.waveHeader.velocity = (0.5 * 2).toFloat()
                    sharedPref.setCurrentSpeed(1L)
                }else{
                    binding.tvSpeed.text = p1.toString()
                    binding.waveHeader.velocity = (p1 * 2).toFloat()
                    sharedPref.setCurrentSpeed(1L+p1)
                }
                if(isRunning){
                    (activity as MainActivity).startVibrator(sharedPref.getCurrentSpeed(),sharedPref.getCurrentPattern())
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })

        binding.cdPattern.setOnClickListener {
            (parentFragment as HomeFragment).swipe(2)
        }

        binding.ibLock.setOnClickListener {
            binding.ctLock.visibility = View.VISIBLE
        }

        binding.tvNovibration.setOnClickListener {
            Navigation.findNavController(requireActivity(),R.id.nav_host_fragment_content_main).navigate(R.id.action_homeFragment_to_noVibrationFragment)
        }

        binding.tvSendfeedback.setOnClickListener {
            Navigation.findNavController(requireActivity(),R.id.nav_host_fragment_content_main).navigate(R.id.action_homeFragment_to_sendFeedbackFragment)
        }

        binding.ibSettings.setOnClickListener {
            showSettingsDialog()
        }


        binding.cdUnlock.setOnClickListener {
            binding.ctLock.visibility = View.GONE
        }


        binding.cdSwitch.setOnClickListener {

            isRunning = !isRunning

            setColor()

            if(isRunning){

                binding.waveHeader.visibility = View.VISIBLE
                (activity as MainActivity).startVibrator(sharedPref.getCurrentSpeed(),sharedPref.getCurrentPattern())

            }else{

                (activity as MainActivity).stopVibrator()
                binding.waveHeader.visibility = View.GONE

            }

        }

    }

    fun setInterstitialAd(){
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(requireContext(),getString(R.string.main_interstitialad_real_id), adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
                if (mInterstitialAd != null) {
                    mInterstitialAd?.show(requireActivity())
                    sharedPref.setInterstitialShown(true)
                } else {

                }
            }
        })


    }

    @SuppressLint("SetTextI18n")
    fun showSettingsDialog(){

        lateinit var dialog : androidx.appcompat.app.AlertDialog

        val customview =  layoutInflater.inflate(R.layout.dialog_settings,null,false)

        val  changeAppIcon = customview.findViewById<MaterialCardView>(R.id.cd_changeappicon)
        val share = customview.findViewById<MaterialCardView>(R.id.cd_shareus)
        val ratus = customview.findViewById<MaterialCardView>(R.id.cd_rateus)



        val builder = MaterialAlertDialogBuilder(requireContext())
            .setView(customview)


        changeAppIcon.setOnClickListener {
            dialog.dismiss()
            Navigation.findNavController(requireActivity(),R.id.nav_host_fragment_content_main).navigate(R.id.action_homeFragment_to_sendFeedbackFragment)
        }
        share.setOnClickListener {
            dialog.dismiss()
            share()
        }
        ratus.setOnClickListener {
            (activity as MainActivity).showRatingDialog()
            dialog.dismiss()
        }

        dialog = builder.show()


    }

    private fun share() {
        try {
            val intent = Intent(Intent.ACTION_SEND)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            val text = "Hey, please check this app https://play.google.com/store/apps/details?id=" + requireActivity().packageName
            intent.putExtra(Intent.EXTRA_TEXT,text)
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "share by"))
        }catch (e: Exception){
            Toast.makeText(requireContext(),"${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()

        binding.tvPattername.text = sharedPref.getCurrentPattern()
    }

    private fun setColor() {

        if(isRunning){

            binding.ctSwitch.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.main_color))
            binding.ivSwitch.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.white))
            binding.waveHeader.visibility = View.VISIBLE
            binding.waveHeader.velocity = (1 * 2).toFloat()
            binding.ibLock.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.white))
            binding.ibSettings.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.white))

        }else{

            binding.ctSwitch.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.secondary_color))
            binding.ivSwitch.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.main_color))
            binding.waveHeader.visibility = View.GONE
            binding.ibLock.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.main_color))
            binding.ibSettings.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.main_color))

        }

        if (isRunning && sharedPref.getPaymentSaveStatus() && !sharedPref.getPremiumStatus()){
            if (sharedPref.getInterstitialShown()){
                sharedPref.setInterstitialShown(false)
            }else{
                setInterstitialAd()
            }
        }

    }

}