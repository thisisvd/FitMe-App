package com.vdcodeassociate.fitme.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.databinding.FragmentPermissionRequiredBinding
import com.vdcodeassociate.fitme.utils.Permissions
import com.vdcodeassociate.fitme.utils.Permissions.hasLocationPermission
import com.vdcodeassociate.fitme.utils.Permissions.requestLocationPermission
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class PermissionRequiredFragment: Fragment(R.layout.fragment_permission_required), EasyPermissions.PermissionCallbacks {

    // viewBinding
    private lateinit var binding: FragmentPermissionRequiredBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPermissionRequiredBinding.bind(view)

        requestPermission()

        binding.continueButton.setOnClickListener {
            if(hasLocationPermission(requireContext())){
                findNavController().navigate(R.id.action_permissionRequiredFragment_to_homeFragment2)
            }else {
                requestLocationPermission(this)
            }
        }

    }

    private fun requestPermission() {
        if(hasLocationPermission(requireContext())){
            findNavController().navigate(R.id.action_permissionRequiredFragment_to_homeFragment2)
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

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(requireActivity(),perms)){
//            AppSettingsDialog.Builder(requireActivity()).build().show()
            findNavController().navigate(R.id.action_permissionRequiredFragment_to_homeFragment2)
        }else {
            requestLocationPermission(this)
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        Toast.makeText(requireContext(),"Location Permission Denied!",Toast.LENGTH_SHORT).show()
        AppSettingsDialog.Builder(requireActivity()).build().show()
    }

}