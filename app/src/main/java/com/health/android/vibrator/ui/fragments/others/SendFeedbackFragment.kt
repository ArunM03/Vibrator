package com.health.android.vibrator.ui.fragments.others

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.health.android.vibrator.R
import com.health.android.vibrator.data.FeedbackData
import com.health.android.vibrator.databinding.FragmentSendfeedbackBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class SendFeedbackFragment : Fragment(R.layout.fragment_sendfeedback) {

    lateinit var binding : FragmentSendfeedbackBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSendfeedbackBinding.bind(view)

        setUI(view)

    }

    private fun setUI(view: View) {

        binding.fabSubmit.setOnClickListener {

            val feedback = binding.edFeedback.text.toString()

            if(feedback.isEmpty()){
                Toast.makeText(requireContext(), "Please enter feedback", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.progressbar.visibility = View.VISIBLE

            CoroutineScope(Dispatchers.IO).launch {

                FirebaseFirestore.getInstance().collection("vibratorappfeedbacks").document().set(FeedbackData(feedback,Calendar.getInstance().time)).addOnSuccessListener {
                    Toast.makeText(requireContext(), "Feedback submitted successfully! Thanks for your feedback", Toast.LENGTH_LONG).show()
                    requireActivity().onBackPressed()
                }.addOnFailureListener {
                    Toast.makeText(requireContext(), "Something went wrong ${it.message}", Toast.LENGTH_LONG).show()
                }

            }

        }

    }

}