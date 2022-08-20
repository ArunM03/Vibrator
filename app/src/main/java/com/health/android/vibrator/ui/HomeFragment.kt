package com.health.android.vibrator.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.health.android.vibrator.R
import com.health.android.vibrator.databinding.FragmentHomeBinding
import com.health.android.vibrator.adapter.ViewPagerAdapter


class HomeFragment : Fragment(R.layout.fragment_home) {

    lateinit var binding : FragmentHomeBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)

        setUI(view)

    }

    private fun setUI(view: View) {

        val adapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = adapter

        binding.viewPager.setUserInputEnabled(false)

    }

    fun swipe(pos : Int){
        binding.viewPager.currentItem = pos
    }


}