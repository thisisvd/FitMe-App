package com.vdcodeassociate.runningtrackerapp.ui.Fragments

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.adapters.RunAdapter
import com.vdcodeassociate.fitme.constants.Constants.REQUEST_CODE_LOCATION_PERMISSION
import com.vdcodeassociate.fitme.databinding.FragmentRunBinding
import com.vdcodeassociate.fitme.room.Run
import com.vdcodeassociate.fitme.utils.SortsEnum
import com.vdcodeassociate.fitme.utils.TrackingUtility
import com.vdcodeassociate.fitme.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class RunFragment : Fragment(R.layout.fragment_run), EasyPermissions.PermissionCallbacks{

    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: FragmentRunBinding

    private lateinit var runAdapter: RunAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRunBinding.bind(view)

        requestPermission()

        setupRecyclerView()

        when(viewModel.sortType) {
            SortsEnum.DATE -> binding.spFilter.setSelection(0)
            SortsEnum.TIME -> binding.spFilter.setSelection(1)
            SortsEnum.DISTANCE -> binding.spFilter.setSelection(2)
            SortsEnum.AVG_SPEED -> binding.spFilter.setSelection(3)
            SortsEnum.CALORIES_BURNED -> binding.spFilter.setSelection(4)
        }

        binding.spFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                when(pos) {
                    0 -> viewModel.sortRuns(SortsEnum.DATE)
                    1 -> viewModel.sortRuns(SortsEnum.TIME)
                    2 -> viewModel.sortRuns(SortsEnum.DISTANCE)
                    3 -> viewModel.sortRuns(SortsEnum.AVG_SPEED)
                    4 -> viewModel.sortRuns(SortsEnum.CALORIES_BURNED)
                }
            }
        }

        viewModel.runs.observe(viewLifecycleOwner, Observer {
            runAdapter.submitList(it)
        })

        binding.fab.setOnClickListener{
            findNavController().navigate(R.id.action_runFragment_to_trackingFragment)
        }

        runAdapter.setOnItemClickListener {
            onSNACK(view,it)
        }

    }

    // Recycler view setup
    private fun setupRecyclerView(){
        runAdapter = RunAdapter()
        binding.apply {
            rvRuns.apply {
                adapter = runAdapter
                layoutManager = LinearLayoutManager(activity)
            }
        }
    }

    private fun requestPermission(){
        if(TrackingUtility.hasLocationPermissions(requireContext())){
            return
        }
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permissions to use this app.",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
            AppSettingsDialog.Builder(this).build().show()
        }else {
            requestPermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this)
    }

    private fun onSNACK(view: View, run: Run){
        viewModel.deleteRun(run)
        runAdapter.notifyDataSetChanged()
        Snackbar.make(
            view.findViewById(R.id.runFragmentID),
            "Deleted a run from recent runs!",
            Snackbar.LENGTH_LONG).setAction(
            "UNDO"
        ){
            viewModel.insertRun(run)
            runAdapter.notifyDataSetChanged()
        }.show()

    }

}