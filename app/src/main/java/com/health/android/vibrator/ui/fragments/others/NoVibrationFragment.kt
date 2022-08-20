package com.health.android.vibrator.ui.fragments.others

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.fragment.app.Fragment
import com.health.android.vibrator.R
import com.health.android.vibrator.databinding.FragmentNovibrationsBinding
import com.health.android.vibrator.databinding.FragmentSettingsBinding

class NoVibrationFragment : Fragment(R.layout.fragment_novibrations) {

    lateinit var binding : FragmentNovibrationsBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentNovibrationsBinding.bind(view)

        setUI(view)

    }

    @SuppressLint("SetTextI18n")
    private fun setUI(view: View) {

        binding.ibBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

     val text = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Html.fromHtml("Please check <b>Vibration Enabled</b> in your device Settings.\n <br/>How to check ? <br/>Go to <b>Settings</b> <br/>" +
                    "Select <b>Sounds</b> and switch on <b>Vibrator on Silent</b> or <b>Vibrator on Ring</b> <br/><br/>If it not works, then check <b>Your device Vibration Motor</br>, In some cases it may be damaged.",Html.FROM_HTML_MODE_LEGACY);
        } else {
            Html.fromHtml("Please check <b>Vibration Enabled</b> in your device Settings.\n <br/>How to check ? <br/>Go to <b>Settings</b> <br/>" +
                    "Select <b>Sounds</b> and switch on <b>Vibrator on Silent</b> or <b>Vibrator on Ring</b> <br/><br/>If it not works, then check <b>Your device Vibration Motor</br>, In some cases it may be damaged.");
        }

        binding.tvNovibrationAnswe.text = text
    }
}