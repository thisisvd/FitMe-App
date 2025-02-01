package com.vdcodeassociate.fitme.ui.fragments

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.adapters.VideosListAdapter
import com.vdcodeassociate.fitme.databinding.FragmentVideoListBinding
import com.vdcodeassociate.fitme.ui.MainActivity
import com.vdcodeassociate.fitme.utils.Resource
import com.vdcodeassociate.fitme.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VideoListFragment : Fragment() {

    // TAG
    private val TAG = "VideoListFragment"

    // view binding
    private var _binding: FragmentVideoListBinding? = null
    private val binding get() = _binding!!

    // view model
    private lateinit var viewModel: HomeViewModel

    // adapters
    private lateinit var recyclerAdapter: VideosListAdapter

    // alert progress dialog
    private lateinit var dialog: Dialog

    // tab value vars
    private var tabValueMaps = mapOf(
        0 to "Workout and Exercise",
        1 to "Exercise",
        2 to "Gym Workout",
        3 to "Cardio",
        4 to "Diet plans",
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVideoListBinding.inflate(inflater, container, false)

        // Home viewModel Implementation from activity
        viewModel = (activity as MainActivity).viewModel

        // init Loading Dialog
        dialog = Dialog(requireContext())
        dialog.apply {
            setContentView(R.layout.custom_dialog_layout)
            setCancelable(false)
            if (window != null) {
                window!!.setBackgroundDrawable(ColorDrawable(0))
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            // tab init
            tabLayoutSetUp()

            // set up recycler view
            setupRecyclerView()

            // view model observer
            viewModelObserver()

            // api search call
            viewModel.getYoutubeResult(tabValueMaps[0]!!)

        }
    }

    // tab layout setup
    private fun tabLayoutSetUp() {
        binding.apply {

            videoTabLayout.apply {
                // adding news tabs
                addTab(newTab().setText("WORKOUT"))
                addTab(newTab().setText("EXERCISE"))
                addTab(newTab().setText("GYM"))
                addTab(newTab().setText("CARDIO"))
                addTab(newTab().setText("DIET"))

                // on tab select listener
                addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        viewModel.getYoutubeResult(tabValueMaps[tab!!.position]!!)
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {
                        // Do not implement anything
                    }

                    override fun onTabReselected(tab: TabLayout.Tab?) {
                        // Do not do anything
                    }
                })
            }
        }
    }

    // view model observer
    private fun viewModelObserver() {
        binding.apply {

            // observe search youtube api
            viewModel.getYoutubeSearchResult.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Success -> {
                        response.data?.let { videosList ->
                            if (videosList.isNotEmpty()) {
                                recyclerAdapter.differ.submitList(videosList)
                                dialog.dismiss()
                            }
                        }
                    }
                    is Resource.Error -> {
                        response.message?.let { message ->
                            Log.e(TAG, "An error occurred : $message")
                            Snackbar.make(
                                binding.root,
                                "Error occurred! Please try again after some time.",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                        dialog.dismiss()
                    }
                    is Resource.Loading -> {
                        Log.d(TAG, "Youtube search api Loading!")
                        dialog.show()
                    }
                }
            }

        }
    }

    // recycler view setup
    private fun setupRecyclerView() {
        recyclerAdapter = VideosListAdapter()
        binding.apply {
            recyclerView.apply {
                adapter = recyclerAdapter
                layoutManager = LinearLayoutManager(activity)
            }
        }
        // on click listener
        recyclerAdapter.apply {
            setOnItemClickListener {
                val bundle = bundleOf(
                    "videoItem" to it
                )
                findNavController().navigate(R.id.action_videoListFragment_to_videoFragment, bundle)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
