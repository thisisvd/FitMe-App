package com.vdcodeassociate.runningtrackerapp.ui.Fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.constants.Constants
import com.vdcodeassociate.fitme.constants.Constants.ACTION_PAUSE_SERVICE
import com.vdcodeassociate.fitme.constants.Constants.ACTION_START_OR_RESUME_SERVICE
import com.vdcodeassociate.fitme.constants.Constants.ACTION_STOP_SERVICE
import com.vdcodeassociate.fitme.constants.Constants.KEY_DISTANCE_GOAL
import com.vdcodeassociate.fitme.constants.Constants.KEY_HEART_POINTS
import com.vdcodeassociate.fitme.constants.Constants.KEY_STEP_GOAL
import com.vdcodeassociate.fitme.constants.Constants.MAP_ZOOM
import com.vdcodeassociate.fitme.constants.Constants.POLYLINE_COLOR
import com.vdcodeassociate.fitme.constants.Constants.POLYLINE_WIDTH
import com.vdcodeassociate.fitme.databinding.FragmentTrackingBinding
import com.vdcodeassociate.fitme.room.runs.Run
import com.vdcodeassociate.fitme.services.Polyline
import com.vdcodeassociate.fitme.services.TrackingService
import com.vdcodeassociate.fitme.ui.fragments.Dialog
import com.vdcodeassociate.fitme.utils.TrackingUtility
import com.vdcodeassociate.fitme.viewmodel.MainViewModel
import java.lang.Math.round
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt

const val CANCEL_TRACKING_DIALOG_TAG = "Cancel Dialog"

@AndroidEntryPoint
class TrackingFragment : Fragment(R.layout.fragment_tracking) {

    // ViewModel
    private val viewModel: MainViewModel by viewModels()

    //Binding
    private lateinit var binding: FragmentTrackingBinding

    // map
    private var map: GoogleMap? = null

    // shared pref
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    // tracking booleans & poly-lines
    private var isTracking = false
    private var pathPoints = mutableListOf<Polyline>()

    // Time Format
    private var currentTimeInMillis = 0L

    // injecting primitive type
    @set:Inject
    private var weight = 80f

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTrackingBinding.bind(view)
        binding.mapView.onCreate(savedInstanceState)

        binding.apply {

            // toolbar name
            var name = sharedPreferences.getString(Constants.KEY_NAME, "User")!!.split(" ")
            tvLetsGo.text = "let's go, ${name[0]}!"

            // Making new updates
            btnToggleRun.setOnClickListener {
//            sendCommandToService(Constants.ACTION_START_OR_RESUME_SERVICE)
                toggleRun()
            }

            // cancel run dialog things
            if (savedInstanceState != null) {
                val dialog = parentFragmentManager.findFragmentByTag(
                    CANCEL_TRACKING_DIALOG_TAG
                ) as Dialog?
                dialog?.setListener {
                    stopRun()
                }
            }

            // Finish a run
            btnFinishRun.setOnClickListener {
//                zoomToSeeMap()
                saveDataToRoom()
            }

            // map sync and poly lines
            mapView.getMapAsync {
                map = it
                addAllPolyline()
            }

            // on back pressed button
            back.setOnClickListener {
                requireActivity().onBackPressed()
            }

            // on cancelling the run
            cancel.setOnClickListener {
                showCancelDialog()
            }

        }

        // viewModel Observers
        subscribeToObservers()

    }

    // Observers init
    private fun subscribeToObservers() {
        TrackingService.isTracking.observe(viewLifecycleOwner, Observer {
            updateTracking(it)
        })

        TrackingService.pathPoints.observe(viewLifecycleOwner, Observer {
            pathPoints = it
            addLatestPolyline() // connect 2 latest polyline
            moveCamera() // move camera
        })

        TrackingService.timeRunsInMillis.observe(viewLifecycleOwner, Observer {
            currentTimeInMillis = it
            val formattedTime = TrackingUtility.getFormattedStopWatchTime(currentTimeInMillis, true)
            binding.tvTimer.text = formattedTime
        })
    }

    // toggle run button services
    private fun toggleRun() {
        if (isTracking) {
            binding.cancel.visibility = View.VISIBLE
            sendCommandToService(ACTION_PAUSE_SERVICE)
        } else {
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
        }
    }

    // Update Tracking with ui
    private fun updateTracking(isTracking: Boolean) {
        this.isTracking = isTracking

        binding.apply {
            if (!isTracking && currentTimeInMillis > 0L) {
                btnToggleRun.text = "Resume"
                btnToggleRun.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_play_arrow_24
                    ),
                    null
                )
                btnFinishRun.visibility = View.VISIBLE
                cancel.visibility = View.VISIBLE
            } else if (isTracking) {
                btnToggleRun.text = "Pause"
                btnToggleRun.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_pause_black_24dp),
                    null
                )
                cancel.visibility = View.GONE
                btnFinishRun.visibility = View.GONE
            }
        }
    }

    // Moving camera to users position
    private fun moveCamera() {
        if (pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    MAP_ZOOM
                )
            )
        }
    }

    private fun zoomToSeeMap() {
        val bounce = LatLngBounds.Builder()
        for (polyline in pathPoints) {
            for (pos in polyline) {
                bounce.include(pos)
            }
        }

        map?.moveCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounce.build(),
                binding.mapView.width,
                binding.mapView.height,
                (binding.mapView.height * 0.05).toInt()
            )
        )
    }

    private fun saveDataToRoom() {
        map?.snapshot { bitmap ->
            var distanceInMeters = 0
            for (polyline in pathPoints) {
                distanceInMeters += TrackingUtility.calculatePolylineLength(polyline).toInt()
            }

            val avgSpeed =
                round((distanceInMeters / 1000f) / (currentTimeInMillis / 1000f / 60 / 60) * 10) / 10f
            val dateTimeStamp = Calendar.getInstance().timeInMillis
            val caloriesBurned = ((distanceInMeters / 1000f) * weight).toInt()

            // calculate steps goals
            var stepGoal =
                ((distanceInMeters / 1000f) * 1312).roundToInt() >= sharedPreferences.getInt(
                    KEY_STEP_GOAL,
                    1000
                )
            var distanceGoal =
                ((distanceInMeters / 1000f)) >= sharedPreferences.getFloat(KEY_DISTANCE_GOAL, 1.0f)

            // adding value to run Model (Dataclass) -
            val run = Run(
                bitmap,
                dateTimeStamp,
                avgSpeed,
                distanceInMeters,
                currentTimeInMillis,
                caloriesBurned,
                18, 8.09f, "Clear",
                stepGoal, distanceGoal
            )

            // cal. and save heart point
            calculateNSaveHeartPts(stepGoal, distanceGoal)

            // adding run in viewModel
            viewModel.insertRun(run)

            Snackbar.make(
                requireActivity().findViewById(R.id.main_root_view),
                "Run saved successfully",
                Snackbar.LENGTH_LONG
            ).show()

            stopRun()

        }
    }

    // calculate HeartPts
    private fun calculateNSaveHeartPts(stepGoal: Boolean, distGoal: Boolean) {
        var heartPoint = if (stepGoal && distGoal) {
            2
        } else {
            0
        }

        val oldHeartPoint = sharedPreferences.getInt(KEY_HEART_POINTS, 0)
        heartPoint += oldHeartPoint
        sharedPreferences.edit().putInt(KEY_HEART_POINTS, heartPoint).apply()

    }

    // All polyline re-added
    private fun addAllPolyline() {
        for (polyline in pathPoints) {
            val polylineOption = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .addAll(polyline)
            map?.addPolyline(polylineOption)
        }
    }

    // Draw a polyline
    private fun addLatestPolyline() {
        if (pathPoints.isNotEmpty() && pathPoints.last().size > 1) {
            val preLastLatLng = pathPoints.last()[pathPoints.last().size - 2]
            val lasLatLng = pathPoints.last().last()
            val polylineOption = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .add(preLastLatLng)
                .add(lasLatLng)
            map?.addPolyline(polylineOption)
        }
    }

    private fun sendCommandToService(action: String) =
        Intent(requireContext(), TrackingService::class.java).also {
            it.action = action
            requireContext().startService(it)
        }

    private fun showCancelDialog() {
        Dialog().apply {
            setListener {
                stopRun()
            }
        }.show(parentFragmentManager, CANCEL_TRACKING_DIALOG_TAG)
    }

    private fun stopRun() {
        binding.tvTimer.text = "00:00:00:00"
        sendCommandToService(ACTION_STOP_SERVICE)
        findNavController().navigate(R.id.action_trackingFragment_to_runFragment)
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }
}