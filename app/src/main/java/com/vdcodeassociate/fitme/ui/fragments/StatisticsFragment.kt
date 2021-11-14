package com.vdcodeassociate.runningtrackerapp.ui.Fragments

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.databinding.FragmentStatisticsBinding
import com.vdcodeassociate.fitme.room.Run
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

    private lateinit var binding: FragmentStatisticsBinding

    private var totalItemCount = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentStatisticsBinding.bind(view)

        viewModelObservers()

//        graphChartObserver()
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

    // bar chart
    private fun graphChartObserver(){
        val mStackedBarChart = binding.stackedbarchart

        val s1 = StackedBarModel("12.4")

        // Hard color - Red = EE7D72, Blue = 2ECC71, Green = F9BE59, Yellow = 416DDF
        // Lite color - Red = FCDCD8, Blue = CAE6F9, Green = E0F7EA, Yellow = FFF3E2
        s1.addBar(BarModel(2.3f, Color.parseColor("#EE7D72")))
        s1.addBar(BarModel(2.3f, Color.parseColor("#2ECC71")))
        s1.addBar(BarModel(2.3f, Color.parseColor("#F9BE59")))
        s1.addBar(BarModel(2.3f, Color.parseColor("#416DDF")))

        val s2 = StackedBarModel("13.4")
        s2.addBar(BarModel(1.1f, -0x9c3450))
        s2.addBar(BarModel(2.7f, -0xa9480f))
        s2.addBar(BarModel(0.7f, -0x325981))

        val s3 = StackedBarModel("14.4")

        s3.addBar(BarModel(2.3f, -0x9c3450))
        s3.addBar(BarModel(2f, -0xa9480f))
        s3.addBar(BarModel(3.3f, -0x325981))

        val s4 = StackedBarModel("15.4")
        s4.addBar(BarModel(1f, -0x9c3450))
        s4.addBar(BarModel(4.2f, -0xa9480f))
        s4.addBar(BarModel(2.1f, -0x325981))

        mStackedBarChart.addBar(s1)
        mStackedBarChart.addBar(s2)
        mStackedBarChart.addBar(s3)
        mStackedBarChart.addBar(s4)

        mStackedBarChart.animationTime = 1000
        mStackedBarChart.startAnimation()

    }

}