package com.vdcodeassociate.fitme.ui.fragments.notification

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.constants.Constants.KEY_BROADCASTID
import com.vdcodeassociate.fitme.databinding.SchedulerDialogBinding
import com.vdcodeassociate.fitme.room.schedules.Schedule
import com.vdcodeassociate.fitme.viewmodel.ScheduleViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.properties.Delegates

@AndroidEntryPoint
class SchedulerNotificationDialog : DialogFragment() {

    // viewBinding
    private lateinit var binding: SchedulerDialogBinding

    // Inject shared preferences
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    // viewModel
    private val viewModel: ScheduleViewModel by viewModels()

    // Broadcasting ID
    private var broadcastID by Delegates.notNull<Int>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SchedulerDialogBinding.bind(view)

        // init broadcastID
        broadcastID = sharedPreferences.getInt(KEY_BROADCASTID, 1)

        // creating notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        binding.apply {

            // set min date in picker
            datePicker.minDate = System.currentTimeMillis()

            // cancel dialog button
            cancelBtn.setOnClickListener {
                dialog!!.cancel()
            }

            scheduleBtn.setOnClickListener {
                if (!isTextEmpty()) {
                    scheduleNotification()
                    dialog!!.dismiss()
                }
            }

        }

    }

    // viewModel Observers
    private fun saveDataToRoom(schedule: Schedule) {
        viewModel.insertSchedule(schedule)
    }

    // check for null
    private fun isTextEmpty(): Boolean {
        var result = false

        binding.apply {

            if (title.text.toString().isEmpty()) {
                titleLayout.error = "*Required"
                result = true
            }

        }

        return result
    }

    // schedule a notification
    private fun scheduleNotification() {
        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(requireActivity(), NotificationScheduler::class.java)
        val title = binding.title.text.toString()
        intent.putExtra(messageExtra, title)
        intent.putExtra(messageBroadcastId, broadcastID)

        val pendingIntent = PendingIntent.getBroadcast(
            requireActivity(),
            broadcastID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // get time from time method
        val time = getTime()

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )

        // updating the broadcastID
        updateBroadcastID()

        // new schedule instance
        val schedule = Schedule(broadcastID, title, time, 0f, 0)

        // save data in room
        saveDataToRoom(schedule)
    }

    // updating the broad cast ID in shared pref
    private fun updateBroadcastID() {
        broadcastID++
        sharedPreferences.edit().putInt(KEY_BROADCASTID, broadcastID).apply()
    }

    // get Time from date picker
    private fun getTime(): Long {
        val minute = binding.timePicker.minute
        val hour = binding.timePicker.hour
        val day = binding.datePicker.dayOfMonth
        val month = binding.datePicker.month
        val year = binding.datePicker.year

        val calender = Calendar.getInstance()
        calender.set(year, month, day, hour, minute)
        return calender.timeInMillis
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val name = "Notification Channel"
        val desc = "A Description of the channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        val notificationManager =
            requireActivity().getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.scheduler_dialog, container, false)
    }

    // On click listener
    private var onItemClickListener: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

}