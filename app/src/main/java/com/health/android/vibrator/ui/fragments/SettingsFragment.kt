package com.health.android.vibrator.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.health.android.vibrator.R
import com.health.android.vibrator.databinding.FragmentHomeBinding
import com.health.android.vibrator.databinding.FragmentSettingsBinding
import com.health.android.vibrator.databinding.FragmentVibrationBinding

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    lateinit var binding : FragmentSettingsBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSettingsBinding.bind(view)

        setUI(view)

    }

    private fun setUI(view: View) {



    }

}