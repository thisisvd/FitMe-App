package com.vdcodeassociate.runningtrackerapp.ui.Fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.databinding.FragmentProfileBinding
import com.vdcodeassociate.fitme.viewmodel.StatisticsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile){

    // viewBinding
    private lateinit var binding: FragmentProfileBinding

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    // viewModel
    private val viewModelStatistics: StatisticsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        viewModelObservers()

        binding.apply {



        }

    }

    private fun viewModelObservers(){

        binding.apply {
            viewModelStatistics.apply {
                totalCaloriesBurned.observe(viewLifecycleOwner, Observer {
                    profileCalories.text = "$it kcal"
                })
                heartPoints.observe(viewLifecycleOwner, Observer {
                    profileHeartPts.text = it.toString()
                })
            }
        }
    }

    // invite Friend
    private fun inviteFriend(){
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        val shareBody = "Download this app - FitMe 2021"
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
        startActivity(sharingIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        binding = FragmentProfileBinding.bind(view)
//
//        loadFieldsFromSharedPreferences()
//
//        binding.btnApplyChanges.setOnClickListener {
//            val success = applyChangesToSharedPreference()
//            if (success) {
//                Snackbar.make(requireView(), "Saved Changes", Snackbar.LENGTH_SHORT).show()
//            }else {
//                Snackbar.make(requireView(), "Please fill out all the fields", Snackbar.LENGTH_SHORT).show()
//            }
//        }
//
//    }
//
//    private fun loadFieldsFromSharedPreferences(){
//        val name = sharedPreferences.getString(KEY_NAME,"")
//        val weight = sharedPreferences.getFloat(KEY_WEIGHT,80f)
//        binding.etName.setText(name)
//        binding.etWeight.setText(weight.toString())
//    }
//
//    private fun applyChangesToSharedPreference() : Boolean {
//        val name = binding.etName.text.toString()
//        val weight = binding.etWeight.text.toString()
//
//        if(name.isEmpty() || weight.isEmpty()){
//            return false
//        }
//
//        sharedPreferences.edit().putString(KEY_NAME,name).putFloat(KEY_WEIGHT,weight.toFloat()).apply()
//
//        return true
//    }

}