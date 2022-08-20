package com.health.android.vibrator.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.health.android.vibrator.ui.fragments.PatternFragment
import com.health.android.vibrator.ui.fragments.SettingsFragment
import com.health.android.vibrator.ui.fragments.VibrationFragment
import com.health.android.vibrator.ui.fragments.pattern.SoftFragment


class PatternViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3


    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> SoftFragment("Soft")
            1 -> SoftFragment("Medium")
            2 -> SoftFragment("Intensive")
            else -> VibrationFragment()
        }
    }

}