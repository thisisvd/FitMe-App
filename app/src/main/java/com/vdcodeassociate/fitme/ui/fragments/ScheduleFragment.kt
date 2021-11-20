package com.vdcodeassociate.fitme.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.databinding.FragmentArticlesBinding
import com.vdcodeassociate.fitme.databinding.FragmentScheduleBinding

class ScheduleFragment : Fragment(R.layout.fragment_schedule) {

    // viewBinding
    private lateinit var binding: FragmentScheduleBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentScheduleBinding.bind(view)

        binding.apply {

            // handle onBack pressed
            back.setOnClickListener {
                requireActivity().onBackPressed()
            }

        }

    }

}