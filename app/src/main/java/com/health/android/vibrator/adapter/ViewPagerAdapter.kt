package com.health.android.vibrator.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.health.android.vibrator.ui.fragments.PatternFragment
import com.health.android.vibrator.ui.fragments.SettingsFragment
import com.health.android.vibrator.ui.fragments.VibrationFragment



class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2


    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> VibrationFragment()
            1 -> PatternFragment()
            else -> VibrationFragment()
        }
    }

}