package com.vdcodeassociate.runningtrackerapp.ui.Fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.constants.Constants
import com.vdcodeassociate.fitme.constants.Constants.KEY_AGE
import com.vdcodeassociate.fitme.constants.Constants.KEY_DISTANCE_GOAL
import com.vdcodeassociate.fitme.constants.Constants.KEY_GENDER
import com.vdcodeassociate.fitme.constants.Constants.KEY_HEIGHT
import com.vdcodeassociate.fitme.constants.Constants.KEY_IMAGE
import com.vdcodeassociate.fitme.constants.Constants.KEY_NAME
import com.vdcodeassociate.fitme.constants.Constants.KEY_STEP_GOAL
import com.vdcodeassociate.fitme.constants.Constants.KEY_WEIGHT
import com.vdcodeassociate.fitme.databinding.FragmentProfileBinding
import com.vdcodeassociate.fitme.viewmodel.StatisticsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.properties.Delegates

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile){

    // viewBinding
    private lateinit var binding: FragmentProfileBinding

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    // viewModel
    private val viewModelStatistics: StatisticsViewModel by viewModels()

    // some init
    private var pStepsGoal by Delegates.notNull<Int>()
    private var pDistanceGoal by Delegates.notNull<Float>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        // handling onBack pressed
//        activity?.supportFragmentManager?.popBackStack()

        viewModelObservers()

        loadFieldsFromSharedPreferences()

        binding.apply {

            profileStepsGoal.doAfterTextChanged {
                if(pStepsGoal.toString() != it.toString()) {
                    saveChanges.visibility = View.VISIBLE
                } else {
                    saveChanges.visibility = View.GONE
                }
            }

            profileDistanceGoal.doAfterTextChanged {
                if(pDistanceGoal.toString() != it.toString()) {
                    saveChanges.visibility = View.VISIBLE
                } else {
                    saveChanges.visibility = View.GONE
                }
            }

            saveChanges.setOnClickListener {
                updateStepDistGoal()
            }

        }

    }

    private fun updateStepDistGoal() {
        if (!isTextEmpty()) {
            binding.apply {
                sharedPreferences.edit()
                    .putInt(KEY_STEP_GOAL,profileStepsGoal!!.text.toString().toInt())
                    .putFloat(KEY_DISTANCE_GOAL,profileDistanceGoal!!.text.toString().toFloat())
                    .apply()
                loadFieldsFromSharedPreferences()
            }
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

    private fun isTextEmpty(): Boolean {
        var result = false
        allLayoutNull()

        binding.apply {

            if (profileStepsGoal.text!!.isEmpty()) {
                profileStepsGoalLayout!!.error = "Steps Goals can't be empty!"
                result = true
            }

            if (profileDistanceGoal.text!!.isEmpty()) {
                profileDistanceGoalLayout!!.error = "Distance Goals can't be empty!"
                result = true
            }

        }

        return result
    }

    private fun allLayoutNull() {
        binding.apply {
            profileStepsGoalLayout.error = null
            profileDistanceGoalLayout.error = null
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

    private fun loadFieldsFromSharedPreferences() {
        val pName = sharedPreferences.getString(KEY_NAME, "")
        val pAge = sharedPreferences.getInt(KEY_AGE, 18)
        val pGender = sharedPreferences.getString(KEY_GENDER, "")
        val pWeight = sharedPreferences.getFloat(KEY_WEIGHT, 80f)
        val pHeight = sharedPreferences.getFloat(KEY_HEIGHT, 80f)
        val pImage = sharedPreferences.getInt(KEY_IMAGE, R.drawable.question_mark5)
        pStepsGoal = sharedPreferences.getInt(KEY_STEP_GOAL,1000)
        pDistanceGoal = sharedPreferences.getFloat(KEY_DISTANCE_GOAL,1.0f)
        binding.apply {
            profileName.text = pName
            profileGender.text = pGender
            profileAge.text = pAge.toString()
            profileWeight.text = pWeight.toString()
            profileHeight.text = pHeight.toString()
            navProfileImage.setImageResource(pImage)
            profileStepsGoal.setText(pStepsGoal.toString())
            profileDistanceGoal.setText(pDistanceGoal.toString())
        }
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