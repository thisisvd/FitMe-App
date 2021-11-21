package com.vdcodeassociate.fitme.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.databinding.FragmentSplashBinding
import com.vdcodeassociate.fitme.databinding.RoundImageViewBinding

class SplashFragment : Fragment() {

    // view binding
    private lateinit var binding: FragmentSplashBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSplashBinding.bind(view)



    }

}