package com.vdcodeassociate.runningtrackerapp.ui.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.databinding.FragmentSetupBinding

class SetupFragment : Fragment(R.layout.fragment_setup){

    private lateinit var binding: FragmentSetupBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSetupBinding.inflate(inflater,container,false)
        return binding.root
    }

}