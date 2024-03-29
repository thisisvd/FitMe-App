package com.vdcodeassociate.fitme.ui.fragments

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import app.futured.donut.DonutSection
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.databinding.FragmentHomeBinding
import com.vdcodeassociate.fitme.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import java.sql.Timestamp
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.vdcodeassociate.fitme.constants.Constants
import com.vdcodeassociate.fitme.constants.Constants.AVATAR_ID
import com.vdcodeassociate.fitme.constants.Constants.KEY_HEART_POINTS
import com.vdcodeassociate.fitme.ui.MainActivity
import com.vdcodeassociate.fitme.utils.Resource
import com.vdcodeassociate.fitme.viewmodel.HomeViewModel
import com.vdcodeassociate.fitme.viewmodel.MainViewModel
import com.vdcodeassociate.fitme.viewmodel.ScheduleViewModel
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    // TAG
    private val TAG = "HomeFragment"

    // viewModel
    lateinit var viewModel: HomeViewModel
    private val viewModelRuns: MainViewModel by viewModels()
    private val viewModelSchedule: ScheduleViewModel by viewModels()

    // view binding
    private lateinit var binding: FragmentHomeBinding

    // Injected Shared preferences
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    // enable the options menu in activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        // Home viewModel Implementation from activity
        viewModel = (activity as MainActivity).viewModel

        // GPS Enable
        (activity as MainActivity).enableGPS()

        // init viewModel
        viewModelsObservers()

        // rough setup donut graph
        setUpDonutGraph(10f, 20f, 15f)

        binding.apply {

            // topmost home date setup
            homeDate.text = Utils().DateFormat(Timestamp(System.currentTimeMillis()).toString())

            // top most name setup
            var name = sharedPreferences.getString(Constants.KEY_NAME, "User")!!.split(" ")
            homeUserName.text = name[0]

            // Heart point injection
            homeWeeksHeartPts.text = sharedPreferences.getInt(KEY_HEART_POINTS, 0).toString()

            // Last run to run fragment
            homeLastRunLayout.setOnClickListener {
                (activity as MainActivity)!!.navigateToFragment(R.id.runFragment)
            }

            // Fitness Articles Buttons -
            homeNewsButton1.setOnClickListener {
                findNavController().navigate(R.id.newsFragment)
            }
            homeNewsButton2.setOnClickListener {
                val bundle = bundleOf("amount" to 2)
                findNavController().navigate(R.id.newsFragment, bundle)
            }
            homeNewsButton3.setOnClickListener {
                val bundle = bundleOf("amount" to 1)
                findNavController().navigate(R.id.newsFragment, bundle)
            }

            // schedules open
            viewAllSchedules.setOnClickListener {
                findNavController().navigate(R.id.scheduleFragment)
            }
        }
    }

    // viewModel observers
    private fun viewModelsObservers() {

        viewModel.apply {

            // weather viewModel Observer
            getWeatherUpdate.observe(viewLifecycleOwner, Observer { response ->
                when (response) {
                    is Resource.Success -> {
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
//                                        (activity as MainActivity).stopLocationUpdate()
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
                    }
                }
            })

            // weekly runs viewModel
            sortedWeeklyStats.observe(viewLifecycleOwner, Observer { stats ->

                binding.homeCalories.text = "${stats.calories} kcal"
                binding.homeDist.text = "${stats.distance} km"
                binding.homeSteps.text = "${stats.steps}"

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

        viewModelSchedule.getLastScheduledItem().observe(viewLifecycleOwner, Observer { schedule ->

            binding.apply {

                if (schedule != null) {
                    title.text = schedule.title

                    date.text = Utils().DateFormat(Timestamp(System.currentTimeMillis()).toString())

                    var format = SimpleDateFormat("hh:mm a")
                    var startTime = format.format(schedule.timeStamp)
                    time.text = startTime

                    goalSteps.text = schedule.goalStep.toString()

                    goalDist.text = schedule.goalDistance.toString()

                } else {
                    noScheduleRuns.visibility = View.VISIBLE
                    scheduleRuns.visibility = View.GONE
                }
            }

        })

    }

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
        // load user image
        rootView.findViewById<ImageView>(R.id.nav_profile_image).setImageResource(AVATAR_ID)
        rootView.setOnClickListener {
            onOptionsItemSelected(alertMenuItem)
        }
        super.onPrepareOptionsMenu(menu)
    }

    // option selector for Circle layout profile menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profileFragmentIcon -> {
                (activity as MainActivity)!!.navigateToFragment(R.id.profileFragment)
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