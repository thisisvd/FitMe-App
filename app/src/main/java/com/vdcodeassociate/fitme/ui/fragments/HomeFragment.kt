package com.vdcodeassociate.fitme.ui.fragments

import android.graphics.Color
import android.graphics.drawable.Animatable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import app.futured.donut.DonutSection
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.databinding.FragmentHomeBinding
import com.vdcodeassociate.fitme.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings
import java.sql.Timestamp
import org.eazegraph.lib.models.ValueLinePoint

import org.eazegraph.lib.models.ValueLineSeries
import org.eazegraph.lib.models.PieModel
import org.eazegraph.lib.models.BarModel
import android.view.animation.DecelerateInterpolator

import android.view.animation.AlphaAnimation

import android.view.animation.Animation
import android.view.animation.AnimationSet
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.vdcodeassociate.fitme.utils.Resource
import com.vdcodeassociate.fitme.viewmodel.HomeViewModel
import com.vdcodeassociate.fitme.viewmodel.MainViewModel
import java.net.URL


@AndroidEntryPoint
class HomeFragment: Fragment(R.layout.fragment_home) {

    //
    private val TAG = "HomeFragment"

    // viewModel
    private val viewModel: HomeViewModel by viewModels()
    private val viewModelRuns: MainViewModel by viewModels()

    // viewBinding
    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        binding.homeDate.apply {
            text = Utils().DateFormat(Timestamp(System.currentTimeMillis()).toString())
        }

        // init viewModel
        viewModelsObservers()

        // init change fragments
        openOtherFragments()

//        // pieChart
//        val pieChart = binding.homePieChart
//        pieChart.addPieSlice(PieModel("Distance", 15F, Color.parseColor("#E75344")))
//        pieChart.addPieSlice(PieModel("Time", 25F, Color.parseColor("#436DDD")))
//        pieChart.addPieSlice(PieModel("Calories", 35F, Color.parseColor("#F8BD5B")))
//        pieChart.addPieSlice(PieModel("Speed", 9F, Color.parseColor("#2BCD73")))
//        pieChart.startAnimation()

        // barChart
        val barChart = binding.homeBarChart
        barChart.addBar(BarModel("Sun",0.0f, -0xa9480f))
        barChart.addBar(BarModel("Mon",0.0f, -0xa9480f))
        barChart.addBar(BarModel("Tue",2f, -0xa9480f))
        barChart.addBar(BarModel("Wed",2.7f, -0xa9480f))
        barChart.addBar(BarModel("Thu",1f, -0xa9480f))
        barChart.addBar(BarModel("Fri",0f, -0xa9480f))
        barChart.addBar(BarModel("Sat",2f, -0xa9480f))
        barChart.startAnimation()

        val donut_view = binding.donutView
        val section1 = DonutSection(
            name = "section_1",
            color = Color.parseColor("#416DDF"),
            amount = 10f
        )

        val section2 = DonutSection(
            name = "section_2",
            color = Color.parseColor("#E95B4D"),
            amount = 5f
        )

        val section3 = DonutSection(
            name = "section_3",
            color = Color.parseColor("#F9BE59"),
            amount = 15f
        )

        donut_view.cap = 30f
        donut_view.animationDurationMs = 2000
        donut_view.submitData(listOf(section1, section2, section3))

        val sparkLine = binding.sparkLine
        sparkLine.markerBorderColor = Color.parseColor("#fed32c")
        sparkLine.sparkLineThickness = 4f
        var aList = ArrayList<Int>()
        aList.add(2)
        aList.add(4)
        aList.add(2)
        aList.add(5)
        aList.add(1)
        sparkLine.setData(aList)

    }

    // viewModel observers
    private fun viewModelsObservers(){

        viewModel.getWeatherUpdate.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
//                    binding.progress.visibility = View.GONE
//                    binding.recyclerView.visibility = View.VISIBLE
                    response.data?.let { weatherResponse ->
                        binding.apply {
                            weatherResponse.apply {
                                current.apply {
                                    weatherCelcius.text = temp_c.toString()
                                    weatherStatus.text = condition.text
                                    weatherCelciusFeelsLike.text = feelslike_c.toString()
                                    weatherHumidity.text = humidity.toString()
                                    weatherWind.text = wind_mph.toString()
                                    weatherPressure.text = pressure_mb.toString()
                                    Glide.with(root)
                                        .load(URL("https:${condition.icon}"))
                                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                                        .transition(DrawableTransitionOptions.withCrossFade())
                                        .into(weatherIcon)
                                }
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Log.e(TAG, "AN error occurred : $message")
                        binding.apply {
                            weatherNumbers.visibility = View.GONE
                            weatherCondition.visibility = View.GONE
                            weatherNoLocation.visibility = View.VISIBLE
                        }
                    }
                }
                is Resource.Loading -> {
//                    binding.progress.visibility = View.VISIBLE
//                    binding.recyclerView.visibility = View.GONE
                }
            }
        })

        viewModelRuns.lastRun.observe(viewLifecycleOwner, Observer {
            binding.homeLastCalories.text = "${it[0].caloriesBurned} kcal"
            binding.homeLastDist.text = "${it[0].distanceInMeters} km"
            binding.homeLastTime.text = "${it[0].timeInMillis}"
            binding.homeLastRunDate.text = "${it[0].timestamp}"
            Glide.with(requireView())
                .load(it[0].img)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.homeLastRunImage)
        })

    }

    // open other fragments
    private fun openOtherFragments(){
        binding.apply {

            homeStats1.setOnClickListener {
                changeFragment()
            }

            homeStats2.setOnClickListener {
                changeFragment()
            }

            homeStats3.setOnClickListener {
                changeFragment()
            }

        }
    }

    // change fragment
    private fun changeFragment(){
        findNavController().navigate(R.id.statisticsFragment)
    }

    // onPrepareOptionsMenu for Circle layout profile menu
    override fun onPrepareOptionsMenu(menu: Menu) {
        val alertMenuItem = menu!!.findItem(R.id.profileFragmentIcon)
        val rootView = alertMenuItem.actionView as FrameLayout
        rootView.setOnClickListener {
            onOptionsItemSelected(alertMenuItem)
        }
        super.onPrepareOptionsMenu(menu)
    }

    // option selector for Circle layout profile menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.profileFragmentIcon -> {
                Toast.makeText(requireContext(),"Profile Icon", Toast.LENGTH_SHORT).show()
                true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // calling own menu for this fragment
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.nav_drawer_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

}