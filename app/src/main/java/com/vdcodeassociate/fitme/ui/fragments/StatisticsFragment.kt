package com.vdcodeassociate.runningtrackerapp.ui.Fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.databinding.FragmentSettingsBinding
import com.vdcodeassociate.fitme.databinding.FragmentStatisticsBinding
import com.vdcodeassociate.fitme.utils.TrackingUtility
import com.vdcodeassociate.fitme.viewmodel.StatisticsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Math.round
import kotlin.properties.Delegates

@AndroidEntryPoint
class StatisticsFragment : Fragment(R.layout.fragment_statistics){

    private val viewModel: StatisticsViewModel by viewModels()

    private lateinit var binding: FragmentStatisticsBinding

    private var totalItemCount = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentStatisticsBinding.bind(view)

        subscribeToObservers()

    }

    private fun subscribeToObservers() {

        viewModel.apply {
            totalItemCount.observe(viewLifecycleOwner, Observer {
                it?.let {
                    this@StatisticsFragment.totalItemCount = it
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
                    val totalDistance = round(km * 10f) / 10f
                    val totalDistanceString = "${totalDistance}km"
                    binding.tvTotalDistance.text = totalDistanceString
                }
            })
            totalAvgSpeed.observe(viewLifecycleOwner, Observer {
                it?.let {
                    val avgSpeed = round((it / this@StatisticsFragment.totalItemCount) * 10f) / 10f
                    val avgSpeedString = "${avgSpeed}km/h"
                    binding.tvAverageSpeed.text = avgSpeedString
                }
            })
            totalCaloriesBurned.observe(viewLifecycleOwner, Observer {
                it?.let {
                    val totalCalories = "${it}Kcal"
                    binding.tvTotalCalories.text = totalCalories
                }
            })

        }

    }

}