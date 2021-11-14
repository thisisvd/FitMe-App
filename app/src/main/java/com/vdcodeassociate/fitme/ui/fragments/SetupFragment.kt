package com.vdcodeassociate.runningtrackerapp.ui.Fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textview.MaterialTextView
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.constants.Constants.KEY_FIRST_TIME_TOGGLE
import com.vdcodeassociate.fitme.constants.Constants.KEY_NAME
import com.vdcodeassociate.fitme.constants.Constants.KEY_WEIGHT
import com.vdcodeassociate.fitme.databinding.FragmentSetupBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.prefs.AbstractPreferences
import javax.inject.Inject

@AndroidEntryPoint
class SetupFragment : Fragment(R.layout.fragment_setup){

    private lateinit var binding: FragmentSetupBinding

    @Inject
    lateinit var sharedPreferences: SharedPreferences

//    @set:Inject    // to inject primitives
//    var isAppFirstOpen = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSetupBinding.bind(view)

        binding.apply {

            gender.apply {
                setAdapter(ArrayAdapter(requireContext() , android.R.layout.simple_dropdown_item_1line , listOf("Male", "Female" , "Others")))
            }

            continueButton.setOnClickListener {
                Toast.makeText(requireContext(),isTextEmpty().toString(),Toast.LENGTH_SHORT).show()
            }

        }

//        if(!isAppFirstOpen){
//            val navOptions = NavOptions.Builder()
//                .setPopUpTo(R.id.setupFragment,true)
//                .build()
//            findNavController().navigate(R.id.action_setupFragment_to_runFragment,savedInstanceState,navOptions)
//        }

//        binding.tvContinue.setOnClickListener {
//            val success = writeDataToSharedPreference()
//            if (success) {
//                findNavController().navigate(R.id.action_setupFragment_to_runFragment)
//            }else {
//                Snackbar.make(requireView(), "Please enter all the fields", Snackbar.LENGTH_SHORT).show()
//            }
//        }
    }

    private fun allLayoutNull(){
        binding.apply {
            nameLayout.error = null
            ageLayout.error = null
            genderLayout.error = null
            weightLayout.error = null
            heightLayout.error = null
        }
    }

    private fun isTextEmpty(): Boolean {
        var result = true
        allLayoutNull()

        binding.apply {
            if (name.text!!.isEmpty()) {
                nameLayout!!.error = "Name can't be empty!"
                result = false
            }

            if (age.text!!.isEmpty()) {
                ageLayout!!.error = "Age can't be empty!"
                result = false
            }

            if (gender.text!!.isEmpty()) {
                genderLayout!!.error = "Gender can't be empty!"
                result = false
            }

            if (weight.text!!.isEmpty()) {
                weightLayout!!.error = "Weight can't be empty!"
                result = false
            }

            if (height.text!!.isEmpty()) {
                heightLayout!!.error = "Height can't be empty!"
                result = false
            }

        }

        return result
    }

    // Save user data to Shared Preference
    private fun writeDataToSharedPreference(): Boolean {
//        val name = binding.etName.text.toString()
//        val weight = binding.etWeight.text.toString()
//        if(name.isEmpty() || weight.isEmpty()){
//            return false
//        }
//
//        // all data set into shared preferences
//        sharedPreferences.edit()
//            .putString(KEY_NAME,name)
//            .putFloat(KEY_WEIGHT,weight.toFloat())
//            .putBoolean(KEY_FIRST_TIME_TOGGLE,false)
//            .apply()
//
//        val toolbarText = "Let's go, $name!"
////        requireActivity().findViewById<TextView>(R.id.tvToolbarTitle).text = toolbarText
        return true

    }

}