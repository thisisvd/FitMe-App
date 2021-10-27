package com.vdcodeassociate.runningtrackerapp.ui.Fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.databinding.FragmentTrackingBinding
import com.vdcodeassociate.fitme.viewmodel.MainViewModel

@AndroidEntryPoint
class TrackingFragment : Fragment(R.layout.fragment_tracking){

    // ViewModel
    private val viewModel: MainViewModel by viewModels()

    //Binding
    private lateinit var binding: FragmentTrackingBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTrackingBinding.bind(view)

    }

}