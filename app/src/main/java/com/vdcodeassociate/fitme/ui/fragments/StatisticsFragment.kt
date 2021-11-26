package com.vdcodeassociate.runningtrackerapp.ui.Fragments

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.databinding.FragmentStatisticsBinding
import com.vdcodeassociate.fitme.room.runs.Run
import com.vdcodeassociate.fitme.utils.TrackingUtility
import com.vdcodeassociate.fitme.utils.Utils
import com.vdcodeassociate.fitme.viewmodel.StatisticsViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.eazegraph.lib.models.BarModel
import java.lang.Math.round

import com.vdcodeassociate.fitme.viewmodel.MainViewModel
import org.eazegraph.lib.models.StackedBarModel

@AndroidEntryPoint
class StatisticsFragment : Fragment(R.layout.fragment_statistics){

    // viewModels
    private val viewModelStatistics: StatisticsViewModel by viewModels()
    private val viewModelRuns: MainViewModel by viewModels()

    // view binding
    private lateinit var binding: FragmentStatisticsBinding

    // total items counts for all stats
    private var totalItemCount = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentStatisticsBinding.bind(view)

        // viewModel observers
        viewModelObservers()

    }

    // viewModel Observers
    private fun viewModelObservers() {

        // List view model setup
        viewModelStatistics.apply {
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
                    val totalDistanceString = "$totalDistance km"
                    binding.tvTotalDistance.text = totalDistanceString
                }
            })
            totalAvgSpeed.observe(viewLifecycleOwner, Observer {
                it?.let {
                    val avgSpeed = round((it / this@StatisticsFragment.totalItemCount) * 10f) / 10f
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

    // bar graph sorted list last recent
    private fun submitSortedList(runs: List<Run>) {

        val mStackedBarChart = binding.stackedbarchart

        for(run in runs){
            val model = StackedBarModel(Utils().DateFormatRuns(run,false))
            model.addBar(BarModel((run.distanceInMeters.toFloat() / 1000f), Color.parseColor("#EE7D72")))
            model.addBar(BarModel(run.avgSpeedInKMH, Color.parseColor("#2ECC71")))
            model.addBar(BarModel((run.caloriesBurned.toFloat() / 10f), Color.parseColor("#F9BE59")))
            mStackedBarChart.addBar(model)
        }

        mStackedBarChart.animationTime = 1000
        mStackedBarChart.startAnimation()

    }

}