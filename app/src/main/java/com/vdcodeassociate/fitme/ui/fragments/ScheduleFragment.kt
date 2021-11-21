package com.vdcodeassociate.fitme.ui.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.adapters.RunAdapter
import com.vdcodeassociate.fitme.adapters.ScheduleAdapter
import com.vdcodeassociate.fitme.databinding.*
import com.vdcodeassociate.fitme.room.schedules.Schedule
import com.vdcodeassociate.fitme.ui.fragments.notification.SchedulerNotificationDialog
import com.vdcodeassociate.fitme.viewmodel.MainViewModel
import com.vdcodeassociate.fitme.viewmodel.ScheduleViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ScheduleFragment : Fragment(R.layout.fragment_schedule) {

    // viewModel
    private val viewModel: ScheduleViewModel by viewModels()

    // viewBinding
    private lateinit var binding: FragmentScheduleBinding

    // recycler view adapter
    private lateinit var scheduleAdapter: ScheduleAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentScheduleBinding.bind(view)

        // recycler view init
        setupRecyclerView()

        // init swapping delete schedule
        deleteSchedule(view)

        viewModel.resetSchedules()

        scheduleAdapter.setOnItemClickListener {
            viewModel.deleteSchedule(it)
        }

        binding.apply {

            // viewModelObservers
            viewModelObservers()

            // setOnClickListener
            fab.setOnClickListener {// dialog Schedule
                var dialog = SchedulerNotificationDialog()
                dialog.show(parentFragmentManager, "Schedule Dialog")
                dialog.setOnItemClickListener {
//                avatar.setImageResource(it)
                }
            }

            // handle onBack pressed
            back.setOnClickListener {
                requireActivity().onBackPressed()
            }

        }

    }

    // viewModel Observers
    private fun viewModelObservers() {

        // schedule viewModel observe
        viewModel.getScheduledRuns().observe(viewLifecycleOwner, Observer {
            scheduleAdapter.submitList(it)
            scheduleAdapter.notifyDataSetChanged()
        })

    }

    // Swiping to delete functionality for delete schedule's
    private fun deleteSchedule(view: View){
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val schedule = scheduleAdapter.differ.currentList[position]
                viewModel.deleteSchedule(schedule)
                Snackbar.make(view,"Successfully deleted article!", Snackbar.LENGTH_SHORT).apply {
                    setAction("Undo"){
                        viewModel.insertSchedule(schedule)
                        scheduleAdapter.notifyDataSetChanged()
                    }
                }.setActionTextColor(Color.parseColor("#FED32C")).show()
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.recyclerView)
        }

    }

    // setting up recycler view
    private fun setupRecyclerView(){
        scheduleAdapter = ScheduleAdapter()
        binding.apply {
            recyclerView.apply {
                adapter = scheduleAdapter
                layoutManager = LinearLayoutManager(activity)
            }
        }
    }


}