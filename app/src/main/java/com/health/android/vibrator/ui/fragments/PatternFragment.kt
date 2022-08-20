package com.health.android.vibrator.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.textview.MaterialTextView
import com.health.android.vibrator.MainActivity
import com.health.android.vibrator.R
import com.health.android.vibrator.adapter.PatternAdapter
import com.health.android.vibrator.adapter.PatternViewPagerAdapter
import com.health.android.vibrator.adapter.ViewPagerAdapter
import com.health.android.vibrator.databinding.FragmentPatternBinding
import com.health.android.vibrator.others.Constants
import com.health.android.vibrator.others.SharedPref
import com.health.android.vibrator.ui.HomeFragment

class PatternFragment : Fragment(R.layout.fragment_pattern) {

    lateinit var binding : FragmentPatternBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPatternBinding.bind(view)

        setUI(view)

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUI(view: View) {



        binding.ibBack.setOnClickListener {
            (parentFragment as HomeFragment).swipe(0)
        }




        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                (parentFragment as HomeFragment).swipe(0)
            }
        })

        binding.tvSoft.setOnClickListener {

            setColor(binding.tvSoft)
            binding.viewPager.setCurrentItem(0)

        }
        binding.tvMedium.setOnClickListener {

            setColor(binding.tvMedium)
            binding.viewPager.setCurrentItem(1)

        }
        binding.tvIntensive.setOnClickListener {
            setColor(binding.tvIntensive)
            binding.viewPager.setCurrentItem(2)
        }

        val adapter = PatternViewPagerAdapter(this)
        binding.viewPager.adapter = adapter

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when(position){
                    0 ->       setColor(binding.tvSoft)
                    1 ->       setColor(binding.tvMedium)
                    else ->       setColor(binding.tvIntensive)
                }
            }
        })

        binding.tvAskmorepatterns.setOnClickListener {
            Navigation.findNavController(requireActivity(),R.id.nav_host_fragment_content_main).navigate(R.id.action_homeFragment_to_sendFeedbackFragment)
        }


    }

    private fun setColor(textview : MaterialTextView) {

        binding.tvSoft.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.secondary_color))
        binding.tvSoft.setTextColor(ContextCompat.getColor(requireContext(),R.color.main_color))
        binding.tvIntensive.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.secondary_color))
        binding.tvIntensive.setTextColor(ContextCompat.getColor(requireContext(),R.color.main_color))
        binding.tvMedium.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.secondary_color))
        binding.tvMedium.setTextColor(ContextCompat.getColor(requireContext(),R.color.main_color))

        textview.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.main_color))
        textview.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))

    }

}