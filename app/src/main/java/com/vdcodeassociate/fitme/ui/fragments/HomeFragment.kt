package com.vdcodeassociate.fitme.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import app.futured.donut.DonutSection
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.databinding.FragmentHomeBinding
import com.vdcodeassociate.fitme.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import java.sql.Timestamp

import org.eazegraph.lib.models.BarModel

import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.vdcodeassociate.fitme.ui.MainActivity
import com.vdcodeassociate.fitme.utils.Resource
import com.vdcodeassociate.fitme.viewmodel.HomeViewModel
import com.vdcodeassociate.fitme.viewmodel.MainViewModel
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import devlight.io.library.ArcProgressStackView
import java.lang.Math.round
import kotlin.math.roundToInt
import org.eazegraph.lib.models.ValueLinePoint

import org.eazegraph.lib.models.ValueLineSeries
import im.dacer.androidcharts.LineView

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    // TAG
    private val TAG = "HomeFragment"

    // viewModel
//    private val viewModel: HomeViewModel by viewModels()
    lateinit var viewModel: HomeViewModel
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

        // Home viewModel Implementation from activity
        viewModel = (activity as MainActivity).viewModel

        binding.homeDate.apply {
            text = Utils().DateFormat(Timestamp(System.currentTimeMillis()).toString())
        }

        // init viewModel
        viewModelsObservers()

        setUpDonutGraph(10f,20f,15f)

        binding.apply {
            homeStats.setOnClickListener {
                findNavController().navigate(R.id.statisticsFragment)
            }

            homeLastRunLayout.setOnClickListener {

            }

            // fitness articles -
            homeNewsButton1.setOnClickListener {
                findNavController().navigate(R.id.newsFragment)
            }

            homeNewsButton2.setOnClickListener {
                val bundle = bundleOf("amount" to 2)
                findNavController().navigate(R.id.newsFragment,bundle)
            }

            homeNewsButton3.setOnClickListener {
                val bundle = bundleOf("amount" to 1)
                findNavController().navigate(R.id.newsFragment,bundle)
            }

        }

        // barChart
//        val barChart = binding.homeBarChart
//        barChart.addBar(BarModel("Sun", 0.0f, -0xa9480f))
//        barChart.addBar(BarModel("Mon", 0.0f, -0xa9480f))
//        barChart.addBar(BarModel("Tue", 2f, -0xa9480f))
//        barChart.addBar(BarModel("Wed", 2.7f, -0xa9480f))
//        barChart.addBar(BarModel("Thu", 1f, -0xa9480f))
//        barChart.addBar(BarModel("Fri", 0f, -0xa9480f))
//        barChart.addBar(BarModel("Sat", 2f, -0xa9480f))
//        barChart.startAnimation()

    }

    // viewModel observers
    private fun viewModelsObservers() {

        viewModel.apply {

            // weather viewModel Observer
            getWeatherUpdate.observe(viewLifecycleOwner, Observer { response ->
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
                                            .apply(
                                                RequestOptions.diskCacheStrategyOf(
                                                    DiskCacheStrategy.ALL
                                                )
                                            )
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

            // weekly runs viewModel
            sortedWeeklyStats.observe(viewLifecycleOwner, Observer { stats ->

                binding.homeCalories.text = "${stats.calories} kcal"
                binding.homeDist.text = "${stats.distance} km"
                binding.homeSteps.text = "${stats.steps}"
                binding.homeWeeksHeartPts.text = "${stats.heartPoints}"

            })
        }

        // last run viewModel Observer
        viewModelRuns.lastRun.observe(viewLifecycleOwner, Observer { run ->
            binding.apply {
                if (run == null) {
                    homeLastRunLayout.visibility = View.GONE
                    donutView.visibility = View.GONE
                    homeNoLastRun.visibility = View.VISIBLE
                } else {
                    homeNoLastRun.visibility = View.GONE
                    binding.homeLastCalories.text = "${run.caloriesBurned} kcal"
                    binding.homeLastDist.text = "${run.distanceInMeters / 1000f} km"
                    binding.homeLastTime.text = Utils().getTimeInWords(run.timeInMillis)
                    binding.homeLastRunDate.text =
                        SimpleDateFormat("EEE, dd MMM", Locale.getDefault()).format(run.timestamp)
                    binding.homeLastSpeed.text = "${run.avgSpeedInKMH} km/h"
                    Glide.with(requireView())
                        .load(run.img)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(binding.homeLastRunImage)

//                setUpLastRun(77f, 80f)

                }
            }

        })

    }

    // set Up Spark Graph
    private fun setUpSparkGraph(calories: List<Int>, distance: List<Int>) {

    }

//    private fun setUpLastRun(distance: Float, calories: Float) {
//        val models: ArrayList<ArcProgressStackView.Model> = ArrayList()
//        models.add(
//            ArcProgressStackView.Model(
//                "Distance",
//                distance,
//                Color.parseColor("#E9EAEA"),
//                Color.parseColor("#EE7D72")
//            )
//        )
//        models.add(
//            ArcProgressStackView.Model(
//                "Calories",
//                calories,
//                Color.parseColor("#E9EAEA"),
//                Color.parseColor("#F9BE59")
//            )
//        )
//        binding.arcGraph.models = models
//        binding.arcGraph.animate()
//
//    }

    // view last run donut graph
    private fun setUpDonutGraph(steps: Float, distance: Float, total: Float) {
        val steps = DonutSection(
            name = "steps",
            color = Color.parseColor("#61D893"),
            amount = steps
        )

        val distance = DonutSection(
            name = "distance",
            color = Color.parseColor("#E95B4D"),
            amount = distance
        )

        val total = DonutSection(
            name = "calories",
            color = Color.parseColor("#F0F0F0"),
            amount = total
        )

        binding.donutView.animationDurationMs = 2000
        binding.donutView.submitData(listOf(steps, distance, total))

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
        when (item.itemId) {
            R.id.profileFragmentIcon -> {
                Toast.makeText(requireContext(), "Profile Icon", Toast.LENGTH_SHORT).show()
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