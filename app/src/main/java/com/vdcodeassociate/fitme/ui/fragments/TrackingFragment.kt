package com.vdcodeassociate.runningtrackerapp.ui.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.PolylineOptions
import dagger.hilt.android.AndroidEntryPoint
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.constants.Constants
import com.vdcodeassociate.fitme.constants.Constants.MAP_ZOOM
import com.vdcodeassociate.fitme.constants.Constants.POLYLINE_COLOR
import com.vdcodeassociate.fitme.constants.Constants.POLYLINE_WIDTH
import com.vdcodeassociate.fitme.databinding.FragmentTrackingBinding
import com.vdcodeassociate.fitme.services.Polyline
import com.vdcodeassociate.fitme.services.Polylines
import com.vdcodeassociate.fitme.services.TrackingService
import com.vdcodeassociate.fitme.viewmodel.MainViewModel

@AndroidEntryPoint
class TrackingFragment : Fragment(R.layout.fragment_tracking){

    // ViewModel
    private val viewModel: MainViewModel by viewModels()

    //Binding
    private lateinit var binding: FragmentTrackingBinding

    // map
    private var map: GoogleMap? = null

    //
    private var isTracking = false
    private var pathPoints = mutableListOf<Polyline>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTrackingBinding.bind(view)
        binding.mapView.onCreate(savedInstanceState)

        // Making new updates
        binding.btnToggleRun.setOnClickListener {
            sendCommandToService(Constants.ACTION_START_OR_RESUME_SERVICE)
        }

        binding.mapView.getMapAsync {
            map = it
        }

    }

    // Update Tracking with ui


    // Moving camera to users position
    private fun moveCamera(){
        if(pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()){
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    MAP_ZOOM
                )
            )
        }
    }

    // All polyline re-added
    private fun addAllPolyline(){
        for(polyline in pathPoints){
            val polylineOption = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .addAll(polyline)
            map?.addPolyline(polylineOption)
        }
    }

    // Draw a polyline
    private fun addLatestPolyline(){
        if(pathPoints.isNotEmpty() && pathPoints.last().size > 1){
            val preLastLatLng = pathPoints.last()[pathPoints.last().size-2]
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
        Intent(requireContext(),TrackingService::class.java).also {
            it.action = action
            requireContext().startService(it)
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