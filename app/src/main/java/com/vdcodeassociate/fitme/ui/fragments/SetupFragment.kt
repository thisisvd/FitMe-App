package com.vdcodeassociate.runningtrackerapp.ui.Fragments

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textview.MaterialTextView
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.constants.Constants
import com.vdcodeassociate.fitme.constants.Constants.AVATAR_ID
import com.vdcodeassociate.fitme.constants.Constants.KEY_AGE
import com.vdcodeassociate.fitme.constants.Constants.KEY_BROADCASTID
import com.vdcodeassociate.fitme.constants.Constants.KEY_DISTANCE_GOAL
import com.vdcodeassociate.fitme.constants.Constants.KEY_FIRST_TIME_TOGGLE
import com.vdcodeassociate.fitme.constants.Constants.KEY_GENDER
import com.vdcodeassociate.fitme.constants.Constants.KEY_HEART_POINTS
import com.vdcodeassociate.fitme.constants.Constants.KEY_HEIGHT
import com.vdcodeassociate.fitme.constants.Constants.KEY_IMAGE
import com.vdcodeassociate.fitme.constants.Constants.KEY_NAME
import com.vdcodeassociate.fitme.constants.Constants.KEY_STEP_GOAL
import com.vdcodeassociate.fitme.constants.Constants.KEY_WEIGHT
import com.vdcodeassociate.fitme.databinding.FragmentSetupBinding
import com.vdcodeassociate.fitme.ui.fragments.AvatarDialog
import dagger.hilt.android.AndroidEntryPoint
import java.util.prefs.AbstractPreferences
import javax.inject.Inject

@AndroidEntryPoint
class SetupFragment : Fragment(R.layout.fragment_setup){

    // TAG
    private val TAG = "SetupFragment"

    // viewBinding
    private lateinit var binding: FragmentSetupBinding

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @set:Inject    // to inject primitives
    var isAppFirstOpen = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSetupBinding.bind(view)

        binding.apply {

            avatar.setOnClickListener{
                var dialog = AvatarDialog()
                dialog.show(parentFragmentManager,"Avatar Fragment")
                dialog.setOnItemClickListener {
                    avatar.setImageResource(it)
                }
            }

            gender.apply {
                setAdapter(ArrayAdapter(requireContext() , android.R.layout.simple_dropdown_item_1line , listOf("Male", "Female" , "Others")))
            }

            continueButton.setOnClickListener {
                if(!isTextEmpty()){
                    val success = writeDataToSharedPreference()
                    if (success) {
                        findNavController().navigate(R.id.action_setupFragment_to_permissionRequiredFragment)
                    } else {
                        Log.i(TAG,"Error at saving data to share preferences.")
                    }
                }else {
                    Log.i(TAG,"Error at Text Not Empty.")
                }
            }

        }

        if(!isAppFirstOpen){
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.setupFragment,true)
                .build()
            findNavController().navigate(R.id.action_setupFragment_to_permissionRequiredFragment,savedInstanceState,navOptions)
        }

    }

    private fun allLayoutNull(){
        binding.apply {
            nameLayout.error = null
            ageLayout.error = null
            genderLayout.error = null
            weightLayout.error = null
            heightLayout.error = null
            avatar.borderColor = Color.parseColor("#FF000000")
            changeAvatarTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_normal_text))
        }
    }

    private fun isTextEmpty(): Boolean {
        var result = false
        allLayoutNull()

        binding.apply {
            if (name.text!!.isEmpty()) {
                nameLayout!!.error = "Name can't be empty!"
                result = true
            }

            when {
                age.text!!.isEmpty() -> {
                    ageLayout!!.error = "Age can't be empty!"
                    result = true
                }
                Integer.parseInt(age.text.toString()) <= 5 -> {
                    ageLayout!!.error = "Age should be greater than 5 years!"
                    result = true
                }
                Integer.parseInt(age.text.toString()) > 100 -> {
                    ageLayout!!.error = "Age should be smaller than 100 years!"
                    result = true
                }
            }

            if (gender.text!!.isEmpty()) {
                genderLayout!!.error = "Gender can't be empty!"
                result = true
            }

            if (weight.text!!.isEmpty()) {
                weightLayout!!.error = "Weight can't be empty!"
                result = true
            }else if(weight.text!!.toString().toFloat() > 300){
                weightLayout!!.error = "Enter a valid Weight < 300 kg!"
                result = true
            }

            if (height.text!!.isEmpty()) {
                heightLayout!!.error = "Height can't be empty!"
                result = true
            }else if(height.text!!.toString().toFloat() > 250){
                heightLayout!!.error = "Enter a valid Height < 250 cm"
                result = true
            }

            if(AVATAR_ID == R.drawable.question_mark5){
                avatar.borderColor = Color.parseColor("#B1001A")
                changeAvatarTextView.setTextColor(Color.parseColor("#B1001A"))
                result = true
            }

        }

        return result
    }

    // Save user data to Shared Preference
    private fun writeDataToSharedPreference(): Boolean {

        binding.apply {

            // all data set into shared preferences
            sharedPreferences.edit()
                .putString(KEY_NAME, name.text.toString())
                .putInt(KEY_AGE, age.text.toString().toInt())
                .putString(KEY_GENDER, gender.text.toString())
                .putFloat(KEY_WEIGHT, weight.text.toString().toFloat())
                .putFloat(KEY_HEIGHT, height.text.toString().toFloat())
                .putInt(KEY_IMAGE, AVATAR_ID)
                .putBoolean(KEY_FIRST_TIME_TOGGLE, false)
                .putInt(KEY_STEP_GOAL,1000)
                .putFloat(KEY_DISTANCE_GOAL,1.0f)
                .putInt(KEY_HEART_POINTS,0)
                .putInt(KEY_BROADCASTID,0)
                .apply()

        }

//        val toolbarText = "Let's go, $name!"
//        requireActivity().findViewById<TextView>(R.id.tvToolbarTitle).text = toolbarText
        return true

    }

}