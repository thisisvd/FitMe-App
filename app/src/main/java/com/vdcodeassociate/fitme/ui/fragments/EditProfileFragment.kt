package com.vdcodeassociate.fitme.ui.fragments

import android.app.AlertDialog
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.constants.Constants
import com.vdcodeassociate.fitme.constants.Constants.AVATAR_ID
import com.vdcodeassociate.fitme.constants.Constants.KEY_IMAGE
import com.vdcodeassociate.fitme.databinding.FragmentEditProfileBinding
import com.vdcodeassociate.fitme.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EditProfileFragment: Fragment(R.layout.fragment_edit_profile) {

    // TAG
    private val TAG = "EditProfileFragment"

    // binding
    private lateinit var binding: FragmentEditProfileBinding

    // injecting shared prefs.
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEditProfileBinding.bind(view)

        // load data from shared preferences
        loadFieldsFromSharedPreferences()

        binding.apply {

            // avatar call back from AvatarDialog
            avatar.setOnClickListener{
                var dialog = AvatarDialog()
                dialog.show(parentFragmentManager,"Avatar Fragment")
                dialog.setOnItemClickListener {
                    avatar.setImageResource(it)
                }
            }

            // Select gender from AutoCompleteTextView
            gender.apply {
                setAdapter(
                    ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        listOf("Male", "Female", "Others")
                    )
                )
            }

            // Save button listener
            saveButton.setOnClickListener {
                if (!isTextEmpty()) {
                    val success = applyChangesToSharedPreference()
                    if (success) {
                        (activity as MainActivity).setUpHeader()  // callback to update navigation drawer header
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

            // onBack handle
            back.setOnClickListener {
                activity?.onBackPressed()
            }

        }
    }

    // check for empty texts
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

    // Update user data in shared prefs.
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

        return true
    }

    // load data from shared pref.
    private fun loadFieldsFromSharedPreferences() {
        val pName = sharedPreferences.getString(Constants.KEY_NAME, "")
        val pAge = sharedPreferences.getInt(Constants.KEY_AGE, 18)
        val pGender = sharedPreferences.getString(Constants.KEY_GENDER, "")
        val pWeight = sharedPreferences.getFloat(Constants.KEY_WEIGHT, 80f)
        val pHeight = sharedPreferences.getFloat(Constants.KEY_HEIGHT, 80f)
        val pImage = sharedPreferences.getInt(KEY_IMAGE, R.drawable.question_mark5)
        AVATAR_ID = pImage
        binding.apply {
            name.setText(pName)
            gender.setText(pGender)
            age.setText(pAge.toString())
            weight.setText(pWeight.toString())
            height.setText(pHeight.toString())
            avatar.setImageResource(AVATAR_ID)
        }
    }

    // reset null layouts
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

}