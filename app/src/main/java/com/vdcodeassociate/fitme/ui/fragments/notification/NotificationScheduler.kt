package com.vdcodeassociate.fitme.ui.fragments.notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.viewmodel.repository.MainRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

const val notificationID = 1
const val channelID = "channelID"
const val messageExtra = "messageExtra"
const val messageBroadcastId = "messageBroadcastId"

@AndroidEntryPoint
class NotificationScheduler : HiltBroadcastReceiver() {

    @Inject
    lateinit var repo: MainRepository

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        val notification = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.run_icons8)
            .setContentTitle("Run Scheduled!")
            .setContentText(intent.getStringExtra(messageExtra))
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationID, notification)

        // remove from db
        CoroutineScope(Dispatchers.IO).launch {
            val value = intent.getIntExtra(messageBroadcastId, 0).plus(1)
            repo.deleteScheduledById(value)
        }
    }
}