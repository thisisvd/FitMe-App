package com.vdcodeassociate.fitme.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.databinding.ItemRunBinding
import com.vdcodeassociate.fitme.room.runs.Run
import com.vdcodeassociate.fitme.utils.Utils
import java.text.SimpleDateFormat
import kotlin.math.roundToInt

class RunAdapter: RecyclerView.Adapter<RunAdapter.RunViewHolder>() {

    inner class RunViewHolder(val binding: ItemRunBinding) : RecyclerView.ViewHolder(binding.root)

    // differ callback
    val diffCallBack = object : DiffUtil.ItemCallback<Run>(){
        override fun areItemsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    // differ
    val differ = AsyncListDiffer(this,diffCallBack)

    fun submitList(list: List<Run>) = differ.submitList(list)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunViewHolder {
        val binding = ItemRunBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RunViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RunViewHolder, position: Int) {
        val run = differ.currentList[position]

        holder.binding.apply {
            Glide.with(root)
                .load(run.img)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(ivRunImage)

            tvDate.text = Utils().DateFormatRuns(run,true)

            val avgSpeed = "${run.avgSpeedInKMH} km/h"
            tvAvgSpeed.text = avgSpeed

            val distanceInKm = "${run.distanceInMeters / 1000f} km"
            tvDistance.text = distanceInKm

            tvTime.text = Utils().getTimeInWords(run.timeInMillis)

            val caloriesBurned = "${run.caloriesBurned} kcal"
            tvCalories.text = caloriesBurned

            // top time run
            var format = SimpleDateFormat("hh:mm a")
            var startTime = format.format(run.timestamp)
            runCurrentTime.text = startTime

            runTimeStats.text = Utils().timeRunName(run.timestamp)

            tvSteps.text = ((run.distanceInMeters/1000f) * 1312).roundToInt().toString()

            runWeatherStatus.text = run.weatherStatus
            runWeatherCelcius.text = "${run.weatherCelsius} \u00B0C"
            runWindSpeed.text = "${run.windSpeed} km/h"

            if(run.isStepGoalCheck){
                tvStep.setImageResource(R.drawable.check_icons8)
            }else {
                tvStep.setImageResource(R.drawable.cancel_not_icons8)
            }

            if(run.isDistGoalCheck){
                tvDist.setImageResource(R.drawable.check_icons8)
            }else {
                tvDist.setImageResource(R.drawable.cancel_not_icons8)
            }

            removeRun.setOnClickListener {
                onItemClickListener?.let { it(run) }
            }

//            root.setOnClickListener {
//                onItemClickListener?.let { it(run) }
//            }

        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    // On click listener
    private var onItemClickListener: ((Run) -> Unit)? = null

    fun setOnItemClickListener(listener: (Run) -> Unit) {
        onItemClickListener = listener
    }

}