package com.vdcodeassociate.fitme.ui.fragments.notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.vdcodeassociate.fitme.R

const val notificationID = 1
const val channelID = "channelID"
const val messageExtra = "messageExtra"

class NotificationScheduler: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val notification = NotificationCompat.Builder(context!!, channelID)
            .setSmallIcon(R.drawable.run_icons8)
            .setContentTitle("Run Scheduled!")
            .setContentText(intent?.getStringExtra(messageExtra))
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationID,notification)

    }

}