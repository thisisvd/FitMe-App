package com.vdcodeassociate.fitme.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
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










@AndroidEntryPoint
class HomeFragment: Fragment(R.layout.fragment_home) {

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

        // pieChart
        val pieChart = binding.homePieChart
        pieChart.addPieSlice(PieModel("Distance", 15F, Color.parseColor("#E75344")))
        pieChart.addPieSlice(PieModel("Time", 25F, Color.parseColor("#436DDD")))
        pieChart.addPieSlice(PieModel("Calories", 35F, Color.parseColor("#F8BD5B")))
        pieChart.addPieSlice(PieModel("Speed", 9F, Color.parseColor("#2BCD73")))
        pieChart.startAnimation()

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