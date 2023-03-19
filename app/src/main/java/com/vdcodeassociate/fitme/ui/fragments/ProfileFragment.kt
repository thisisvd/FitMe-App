package com.vdcodeassociate.runningtrackerapp.ui.Fragments

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.constants.Constants.KEY_AGE
import com.vdcodeassociate.fitme.constants.Constants.KEY_DISTANCE_GOAL
import com.vdcodeassociate.fitme.constants.Constants.KEY_GENDER
import com.vdcodeassociate.fitme.constants.Constants.KEY_HEART_POINTS
import com.vdcodeassociate.fitme.constants.Constants.KEY_HEIGHT
import com.vdcodeassociate.fitme.constants.Constants.KEY_IMAGE
import com.vdcodeassociate.fitme.constants.Constants.KEY_NAME
import com.vdcodeassociate.fitme.constants.Constants.KEY_STEP_GOAL
import com.vdcodeassociate.fitme.constants.Constants.KEY_WEIGHT
import com.vdcodeassociate.fitme.databinding.FragmentProfileBinding
import com.vdcodeassociate.fitme.room.runs.Run
import com.vdcodeassociate.fitme.utils.TrackingUtility
import com.vdcodeassociate.fitme.utils.Utils
import com.vdcodeassociate.fitme.viewmodel.MainViewModel
import com.vdcodeassociate.fitme.viewmodel.StatisticsViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.eazegraph.lib.models.BarModel
import org.eazegraph.lib.models.StackedBarModel
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlin.properties.Delegates

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    // TAG
    private val TAG = "ProfileFragment"

    // viewBinding
    private lateinit var binding: FragmentProfileBinding

    // shared pref
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    // viewModel
    private val viewModelStatistics: StatisticsViewModel by viewModels()
    private val viewModelRuns: MainViewModel by viewModels()

    // some init
    private var pStepsGoal by Delegates.notNull<Int>()
    private var pDistanceGoal by Delegates.notNull<Float>()

    // total items counts for all stats
    private var totalItemCountVar = 0

    // enable owned menu by the fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        // viewModel observers
        viewModelObservers()

        // load user data from shared pref.
        loadFieldsFromSharedPreferences()

        binding.apply {

            // Text Watcher for Steps Goal
            profileStepsGoal.doAfterTextChanged {
                if (pStepsGoal.toString() != it.toString()) {
                    saveChanges.visibility = View.VISIBLE
                } else {
                    saveChanges.visibility = View.GONE
                }
            }

            // Text Watcher for Distance Goal
            profileDistanceGoal.doAfterTextChanged {
                if (pDistanceGoal.toString() != it.toString()) {
                    saveChanges.visibility = View.VISIBLE
                } else {
                    saveChanges.visibility = View.GONE
                }
            }

            // update Steps / Distance goals
            saveChanges.setOnClickListener {
                updateStepDistGoal()
            }

        }

    }

    // Update Steps / Distance goals
    private fun updateStepDistGoal() {
        if (!isTextEmpty()) {
            binding.apply {
                sharedPreferences.edit()
                    .putInt(KEY_STEP_GOAL, profileStepsGoal!!.text.toString().toInt())
                    .putFloat(KEY_DISTANCE_GOAL, profileDistanceGoal!!.text.toString().toFloat())
                    .apply()
                loadFieldsFromSharedPreferences()
            }
        }
    }

    // viewModel observers
    private fun viewModelObservers() {

        binding.apply {

            viewModelStatistics.apply {
                totalCaloriesBurned.observe(viewLifecycleOwner, Observer {
                    if (it != null) {
                        profileCalories.text = "$it kcal"
                    } else {
                        profileCalories.text = "0 kcal"
                    }
                })
            }

            // list view model setup
            viewModelStatistics.apply {
                totalItemCount.observe(viewLifecycleOwner, Observer {
                    it?.let {
                        totalItemCountVar = it
                    }
                })
                totalTimeRun.observe(viewLifecycleOwner, Observer {
                    it?.let {
                        val totalTimeRun = TrackingUtility.getFormattedStopWatchTime(it)
                        binding.tvTotalTime.text = totalTimeRun
                    }
                })
                totalDistance.observe(viewLifecycleOwner, Observer {
                    it?.let {
                        val km = it / 1000f
                        val totalDistance = (km * 10f).roundToInt() / 10f
                        val totalDistanceString = "$totalDistance km"
                        binding.tvTotalDistance.text = totalDistanceString
                    }
                })
                totalAvgSpeed.observe(viewLifecycleOwner, Observer {
                    it?.let {
                        val avgSpeed = ((it / totalItemCountVar) * 10f).roundToInt() / 10f
                        val avgSpeedString = "$avgSpeed km/h"
                        binding.tvAverageSpeed.text = avgSpeedString
                    }
                })
                totalCaloriesBurned.observe(viewLifecycleOwner, Observer {
                    it?.let {
                        val totalCalories = "$it Kcal"
                        binding.tvTotalCalories.text = totalCalories
                    }
                })
            }

            // bar graph viewModel setups
            viewModelRuns.runs.observe(viewLifecycleOwner, Observer {
                submitSortedList(it)
            })
        }
    }

    // bar graph sorted list last recent
    private fun submitSortedList(runs: List<Run>) {

        val mStackedBarChart = binding.stackedbarchart

        for (run in runs) {
            val model = StackedBarModel(Utils().DateFormatRuns(run, false))
            model.addBar(
                BarModel(
                    (run.distanceInMeters.toFloat() / 1000f),
                    Color.parseColor("#EE7D72")
                )
            )
            model.addBar(BarModel(run.avgSpeedInKMH, Color.parseColor("#2ECC71")))
            model.addBar(
                BarModel(
                    (run.caloriesBurned.toFloat() / 10f),
                    Color.parseColor("#F9BE59")
                )
            )
            mStackedBarChart.addBar(model)
        }

        mStackedBarChart.animationTime = 1000
        mStackedBarChart.startAnimation()
    }

    // check for empty texts
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

    // reset all layouts to null
    private fun allLayoutNull() {
        binding.apply {
            profileStepsGoalLayout.error = null
            profileDistanceGoalLayout.error = null
        }
    }

    // setting up menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    // load user data from shared prefs.
    private fun loadFieldsFromSharedPreferences() {
        val pName = sharedPreferences.getString(KEY_NAME, "")
        val pAge = sharedPreferences.getInt(KEY_AGE, 18)
        val pGender = sharedPreferences.getString(KEY_GENDER, "")
        val pWeight = sharedPreferences.getFloat(KEY_WEIGHT, 80f)
        val pHeight = sharedPreferences.getFloat(KEY_HEIGHT, 80f)
        val pImage = sharedPreferences.getInt(KEY_IMAGE, R.drawable.profile_other_image)
        val heartPoints = sharedPreferences.getInt(KEY_HEART_POINTS, 0)
        pStepsGoal = sharedPreferences.getInt(KEY_STEP_GOAL, 1000)
        pDistanceGoal = sharedPreferences.getFloat(KEY_DISTANCE_GOAL, 1.0f)
        binding.apply {
            profileName.text = pName
            profileGender.text = pGender
            profileAge.text = "$pAge years"
            profileWeight.text = pWeight.toString()
            profileHeight.text = pHeight.toString()
            profileStepsGoal.setText(pStepsGoal.toString())
            profileDistanceGoal.setText(pDistanceGoal.toString())
            profileHeartPts.text = heartPoints.toString()
            navProfileImage.setImageResource(pImage)
        }
    }

}