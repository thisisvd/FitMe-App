package com.vdcodeassociate.fitme.ui.fragments

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.constants.Constants
import com.vdcodeassociate.fitme.constants.Constants.AVATAR_ID
import com.vdcodeassociate.fitme.constants.Constants.KEY_IMAGE
import com.vdcodeassociate.fitme.databinding.ChooseAvatarDialogBinding
import com.vdcodeassociate.fitme.databinding.FragmentEditProfileBinding
import com.vdcodeassociate.fitme.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EditProfileFragment: Fragment(R.layout.fragment_edit_profile) {

    // TAG
    private val TAG = "EditProfileFragment"

    // binding
    private lateinit var binding: FragmentEditProfileBinding

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    // viewModel
    private val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEditProfileBinding.bind(view)

        // load files
//        loadFieldsFromSharedPreferences()

        // viewModel
        viewModelObservers()

        binding.apply {

            avatar.setOnClickListener{
                AvatarDialog().show(parentFragmentManager,"Avatar Fragment")
            }

            gender.apply {
                setAdapter(
                    ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        listOf("Male", "Female", "Others")
                    )
                )
            }

            saveButton.setOnClickListener {
                if (!isTextEmpty()) {
                    val success = applyChangesToSharedPreference()
                    if (success) {
                        findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)
                    } else {
                        Log.i(TAG, "Error at saving data to share preferences.")
                    }
                } else {
                    Log.i(TAG, "Error at Text Not Empty.")
                    Snackbar.make(
                        requireView(),
                        "Error occurred, Please try after some time!",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }

            back.setOnClickListener {
                activity?.onBackPressed()
            }

        }
    }

    // viewModel Observers
    private fun viewModelObservers(){

        binding.apply {

            viewModel.apply {

                userData.observe(viewLifecycleOwner, Observer { user ->

                    name.setText(user.name)
                    gender.setText(user.gender)
                    age.setText(user.age.toString())
                    weight.setText(user.weight.toString())
                    height.setText(user.height.toString())
                    avatar.setImageResource(user.image)

                })

            }

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

        }

        return result
    }

    private fun applyChangesToSharedPreference() : Boolean {
        binding.apply {

            // all data set into shared preferences
            sharedPreferences.edit()
                .putString(Constants.KEY_NAME, name.text.toString())
                .putInt(Constants.KEY_AGE, age.text.toString().toInt())
                .putString(Constants.KEY_GENDER, gender.text.toString())
                .putFloat(Constants.KEY_WEIGHT, weight.text.toString().toFloat())
                .putFloat(Constants.KEY_HEIGHT, height.text.toString().toFloat())
                .putInt(KEY_IMAGE, AVATAR_ID)
                .putBoolean(Constants.KEY_FIRST_TIME_TOGGLE, false)
                .apply()

        }

//        val toolbarText = "Let's go, $name!"
//        requireActivity().findViewById<TextView>(R.id.tvToolbarTitle).text = toolbarText
        return true
    }

    private fun loadFieldsFromSharedPreferences() {
        val pName = sharedPreferences.getString(Constants.KEY_NAME, "")
        val pAge = sharedPreferences.getInt(Constants.KEY_AGE, 18)
        val pGender = sharedPreferences.getString(Constants.KEY_GENDER, "")
        val pWeight = sharedPreferences.getFloat(Constants.KEY_WEIGHT, 80f)
        val pHeight = sharedPreferences.getFloat(Constants.KEY_HEIGHT, 80f)
        val pImage = sharedPreferences.getInt(Constants.KEY_IMAGE, R.drawable.profile_other_image)
        binding.apply {
            name.setText(pName)
            gender.setText(pGender)
            age.setText(pAge.toString())
            weight.setText(pWeight.toString())
            height.setText(pHeight.toString())
            avatar.setImageResource(pImage)
        }
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

}