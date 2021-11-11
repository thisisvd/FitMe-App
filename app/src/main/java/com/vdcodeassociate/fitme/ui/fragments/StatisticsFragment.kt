package com.vdcodeassociate.runningtrackerapp.ui.Fragments

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.databinding.FragmentStatisticsBinding
import com.vdcodeassociate.fitme.utils.TrackingUtility
import com.vdcodeassociate.fitme.viewmodel.StatisticsViewModel
import dagger.hilt.android.AndroidEntryPoint
import im.dacer.androidcharts.BarView
import im.dacer.androidcharts.LineView
import org.eazegraph.lib.models.BarModel
import java.lang.Math.round
import com.github.mikephil.charting.components.AxisBase

import com.github.mikephil.charting.components.XAxis




@AndroidEntryPoint
class StatisticsFragment : Fragment(R.layout.fragment_statistics){

    private val viewModel: StatisticsViewModel by viewModels()

    private lateinit var binding: FragmentStatisticsBinding

    private var totalItemCount = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentStatisticsBinding.bind(view)

        viewModelObservers()

    }


    private fun viewModelObservers() {

//        viewModel.apply {
//            totalItemCount.observe(viewLifecycleOwner, Observer {
//                it?.let {
//                    this@StatisticsFragment.totalItemCount = it
//                }
//            })
//            totalTimeRun.observe(viewLifecycleOwner, Observer {
//                it?.let {
//                    val totalTimeRun = TrackingUtility.getFormattedStopWatchTime(it)
//                    binding.tvTotalTime.text = totalTimeRun
//                }
//            })
//            totalDistance.observe(viewLifecycleOwner, Observer {
//                it?.let {
//                    val km = it / 1000f
//                    val totalDistance = round(km * 10f) / 10f
//                    val totalDistanceString = "${totalDistance}km"
//                    binding.tvTotalDistance.text = totalDistanceString
//                }
//            })
//            totalAvgSpeed.observe(viewLifecycleOwner, Observer {
//                it?.let {
//                    val avgSpeed = round((it / this@StatisticsFragment.totalItemCount) * 10f) / 10f
//                    val avgSpeedString = "${avgSpeed}km/h"
//                    binding.tvAverageSpeed.text = avgSpeedString
//                }
//            })
//            totalCaloriesBurned.observe(viewLifecycleOwner, Observer {
//                it?.let {
//                    val totalCalories = "${it}Kcal"
//                    binding.tvTotalCalories.text = totalCalories
//                }
//            })
//
//        }

    }

}