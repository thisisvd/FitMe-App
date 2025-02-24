package com.vdcodeassociate.fitme.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.vdcodeassociate.fitme.BuildConfig
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.databinding.FragmentAboutUsBinding
import com.vdcodeassociate.fitme.ui.MainActivity
import java.util.Calendar

class AboutUsFragment : Fragment() {

    // view binding
    private var _binding: FragmentAboutUsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutUsBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            toolbar.text = "About us"

            // app version name
            appVersionName.text = "Version (${BuildConfig.VERSION_NAME}) - ${
                Calendar.getInstance().get(
                    Calendar.YEAR)}"

            // copy right text
            copyrightText.text = "Copyright © ${Calendar.getInstance().get(Calendar.YEAR)}  ${getString(
                R.string.app_name)}. All rights reserved."

            aboutUsShareOurApp.setOnClickListener {
                (requireActivity() as MainActivity).inviteFriend()
            }

            // onBack pressed
            back.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}