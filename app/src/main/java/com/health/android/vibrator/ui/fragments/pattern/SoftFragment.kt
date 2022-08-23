package com.health.android.vibrator.ui.fragments.pattern

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.health.android.vibrator.MainActivity
import com.health.android.vibrator.R
import com.health.android.vibrator.adapter.PatternAdapter
import com.health.android.vibrator.databinding.FragmentPatternlistBinding
import com.health.android.vibrator.others.Constants
import com.health.android.vibrator.others.SharedPref

class SoftFragment(val type : String = "Soft") : Fragment(R.layout.fragment_patternlist) {

    lateinit var binding : FragmentPatternlistBinding
    lateinit var patternAdapter : PatternAdapter
    lateinit var sharedPref: SharedPref
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPatternlistBinding.bind(view)

        setUI()

    }
    @SuppressLint("NotifyDataSetChanged")
    private fun setUI() {

        sharedPref = SharedPref(requireContext())

        patternAdapter = PatternAdapter(sharedPref.getPremiumStatus())



        Constants.isRunning = sharedPref.getRunning()
        Constants.selectedPattern = sharedPref.getCurrentPattern()

        binding.rvPatterns.adapter = patternAdapter
        binding.rvPatterns.layoutManager = GridLayoutManager(requireContext(),3)

        patternAdapter.patternList = when(type){
            "Soft" -> Constants.patternListSoft
            "Medium" -> Constants.patternListMedium
            else -> Constants.patternListIntensive
        }

        patternAdapter.setOnItemClickListener {
           // Navigation.findNavController(requireActivity(),R.id.nav_host_fragment_content_main).navigate(R.id.action_homeFragment_to_becomePremiumFragment)

                   if (it.isPremium && !sharedPref.getPremiumStatus()){
                        Navigation.findNavController(requireActivity(),R.id.nav_host_fragment_content_main).navigate(R.id.action_homeFragment_to_becomePremiumFragment)
                    }else{
                        sharedPref.setCurrentPattern(it.patternName)
                        Constants.selectedPattern = it.patternName
                        patternAdapter.notifyDataSetChanged()
                        if(sharedPref.getRunning()){
                            (activity as MainActivity).startVibrator(sharedPref.getCurrentSpeed(),it.patternName)
                        }
                    }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()

        patternAdapter.notifyDataSetChanged()

    }


}