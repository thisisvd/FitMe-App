package com.vdcodeassociate.fitme.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.vdcodeassociate.fitme.databinding.ScheduleItemsBinding
import com.vdcodeassociate.fitme.room.schedules.Schedule
import com.vdcodeassociate.fitme.utils.Utils
import java.sql.Timestamp
import java.text.SimpleDateFormat

class ScheduleAdapter : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    // inner class
    inner class ScheduleViewHolder(val binding: ScheduleItemsBinding) :
        RecyclerView.ViewHolder(binding.root)

    // differ callback
    val differCallBack = object : DiffUtil.ItemCallback<Schedule>() {

        override fun areItemsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
            return false
        }

    }

    // differ
    val differ = AsyncListDiffer(this, differCallBack)

    fun submitList(list: List<Schedule>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val binding =
            ScheduleItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScheduleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val schedule = differ.currentList[position]

        holder.binding.apply {

            title.text = schedule.title

            date.text = Utils().DateFormat(Timestamp(System.currentTimeMillis()).toString())

            var format = SimpleDateFormat("hh:mm a")
            var startTime = format.format(schedule.timeStamp)
            time.text = startTime

            goalSteps.text = schedule.goalStep.toString()

            goalDist.text = schedule.goalDistance.toString()

            cancel.setOnClickListener {
                onItemClickListener?.let { it(schedule) }
            }

        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    // On click listener
    private var onItemClickListener: ((Schedule) -> Unit)? = null

    fun setOnItemClickListener(listener: (Schedule) -> Unit) {
        onItemClickListener = listener
    }
}