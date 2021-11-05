package com.vdcodeassociate.runningtrackerapp.ui.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.constants.Constants
import com.vdcodeassociate.fitme.constants.Constants.ACTION_PAUSE_SERVICE
import com.vdcodeassociate.fitme.constants.Constants.ACTION_START_OR_RESUME_SERVICE
import com.vdcodeassociate.fitme.constants.Constants.ACTION_STOP_SERVICE
import com.vdcodeassociate.fitme.constants.Constants.MAP_ZOOM
import com.vdcodeassociate.fitme.constants.Constants.POLYLINE_COLOR
import com.vdcodeassociate.fitme.constants.Constants.POLYLINE_WIDTH
import com.vdcodeassociate.fitme.databinding.FragmentTrackingBinding
import com.vdcodeassociate.fitme.services.Polyline
import com.vdcodeassociate.fitme.services.Polylines
import com.vdcodeassociate.fitme.services.TrackingService
import com.vdcodeassociate.fitme.utils.TrackingUtility
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

    // Time Format
    private var currentTimeInmillis = 0L

    // menu
    private var menu: Menu? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTrackingBinding.bind(view)
        binding.mapView.onCreate(savedInstanceState)

        // Making new updates
        binding.btnToggleRun.setOnClickListener {
//            sendCommandToService(Constants.ACTION_START_OR_RESUME_SERVICE)
            toggleRun()
        }

        binding.mapView.getMapAsync {
            map = it
            addAllPolyline()
        }

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
            currentTimeInmillis = it
            val formattedTime = TrackingUtility.getFormattedStopWatchTime(currentTimeInmillis,true)
            binding.tvTimer.text = formattedTime
        })
    }

    // toggle run button services
    private fun toggleRun(){
        if(isTracking){
            menu?.getItem(0)?.isVisible = true
            sendCommandToService(ACTION_PAUSE_SERVICE)
        }else {
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
        }
    }

    // Update Tracking with ui
    private fun updateTracking(isTracking: Boolean) {
        this.isTracking = isTracking
        if(!isTracking){
            binding.btnToggleRun.text = "Start"
            binding.btnFinishRun.visibility = View.VISIBLE
        }else {
            binding.btnToggleRun.text = "Stop"
            menu?.getItem(0)?.isVisible = true
            binding.btnFinishRun.visibility = View.GONE
        }
    }

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.tracking_menu, menu)
        this.menu = menu
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        if(currentTimeInmillis > 0){
            this.menu?.getItem(0)?.isVisible = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.cancelRun -> {
                showCancelDialog()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showCancelDialog(){
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Cancel the Run?")
            .setMessage("Are you sure to cancel the current run?")
            .setIcon(R.drawable.ic_delete)
            .setPositiveButton("Yes") { _,_ ->
                stopRun()
            }
            .setNegativeButton("No") { dialogInterface,_ ->
                dialogInterface.cancel()
            }
            .create()
        dialog.show()
    }

    private fun stopRun(){
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