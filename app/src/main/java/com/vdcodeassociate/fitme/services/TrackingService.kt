package com.vdcodeassociate.fitme.services

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.constants.Constants.ACTION_PAUSE_SERVICE
import com.vdcodeassociate.fitme.constants.Constants.ACTION_SHOW_TRACKING_FRAGMENT
import com.vdcodeassociate.fitme.constants.Constants.ACTION_START_OR_RESUME_SERVICE
import com.vdcodeassociate.fitme.constants.Constants.ACTION_STOP_SERVICE
import com.vdcodeassociate.fitme.constants.Constants.FASTEST_LOCATION_INTERVAL
import com.vdcodeassociate.fitme.constants.Constants.LOCATION_UPDATE_INTERVAL
import com.vdcodeassociate.fitme.constants.Constants.NOTIFICATION_CHANNEL_ID
import com.vdcodeassociate.fitme.constants.Constants.NOTIFICATION_CHANNEL_NAME
import com.vdcodeassociate.fitme.constants.Constants.NOTIFICATION_ID
import com.vdcodeassociate.fitme.constants.Constants.TIMER_UPDATE_INTERVAL
import com.vdcodeassociate.fitme.ui.MainActivity
import com.vdcodeassociate.fitme.utils.TrackingUtility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias Polyline = MutableList<LatLng>
typealias Polylines = MutableList<Polyline>

@AndroidEntryPoint
class TrackingService: LifecycleService() {

    // TAG
    private var TAG = "TRACKING_SERVICE"

    private var isFirstRun = true

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @Inject
    lateinit var baseNotificationBuilder: NotificationCompat.Builder

    lateinit var currentNotificationBuilder: NotificationCompat.Builder

    // livedata for current time runs in sec (for notification)
    private val timeRunsInSec = MutableLiveData<Long>()

    // timer is on/off
    private var isTimeEnable = false
    // total time from beginning (start) to end (pause/stop)
    private var lapTime = 0L
    // total time of run (whole)
    private var timeRun = 0L
    // time at the start of run
    private var timeStart = 0L
    //
    private var lastSecondTimestamp = 0L

    companion object{
        // livedata for current time runs in Milli-sec (for accurate fragment ui changes)
        val timeRunsInMillis = MutableLiveData<Long>()

        var isTracking = MutableLiveData<Boolean>()
        var pathPoints = MutableLiveData<Polylines>()

    }

    private fun postInitValues(){
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
        timeRunsInSec.postValue(0L)
        timeRunsInMillis.postValue(0L)
    }

    override fun onCreate() {
        super.onCreate()
        postInitValues()
        currentNotificationBuilder = baseNotificationBuilder
        fusedLocationProviderClient = FusedLocationProviderClient(this)

        isTracking.observe(this, Observer {
            updateLocationTracking(it)
            updateNotificationTrackingState(it)
        })
    }

    // starting, resume, stop
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            when(it.action){
                ACTION_START_OR_RESUME_SERVICE -> {
                    if(isFirstRun){
                        startForegroundService()
                        isFirstRun = false
                    }else {
                        Log.d("$TAG -> ", "RESUME_SERVICE")
                        startTimer()
                    }
                }
                ACTION_PAUSE_SERVICE -> {
                    Log.d("$TAG -> ","ACTION_PAUSE_SERVICE")
                    pauseService()
                }
                ACTION_STOP_SERVICE -> {
                    Log.d("$TAG -> ","ACTION_STOP_SERVICE")
                }
                else -> {
                    //nothing
                }

            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    // pausing the service
    private fun pauseService(){
        isTracking.postValue(false)
        isTimeEnable = false
    }

    private fun updateNotificationTrackingState(isTracking: Boolean) {
        val notificationActionText = if(isTracking) "Pause" else "Resume"
        val pendingIntent = if(isTracking) {
            val pauseIntent = Intent(this,TrackingService::class.java).apply {
                action = ACTION_PAUSE_SERVICE
            }
            PendingIntent.getService(this,1,pauseIntent, FLAG_UPDATE_CURRENT)
        }else {
            val resumeIntent = Intent(this, TrackingService::class.java).apply {
                action = ACTION_START_OR_RESUME_SERVICE
            }
            PendingIntent.getService(this,1,resumeIntent, FLAG_UPDATE_CURRENT)
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        currentNotificationBuilder.javaClass.getDeclaredField("mActions").apply {
            isAccessible = true
            set(currentNotificationBuilder, ArrayList<NotificationCompat.Action>())
        }

        currentNotificationBuilder = baseNotificationBuilder
            .addAction(R.drawable.ic_pause_black_24dp,notificationActionText,pendingIntent)
        notificationManager.notify(NOTIFICATION_ID,currentNotificationBuilder.build())

    }

    // Start Timer method
    private fun startTimer(){
        addEmptyPolyline()
        isTracking.postValue(true)
        timeStart = System.currentTimeMillis()
        isTimeEnable = true
        // track current time in coroutine
        CoroutineScope(Dispatchers.Main).launch {
            while (isTracking.value!!){
                // time difference between now and timeStart
                lapTime = System.currentTimeMillis() - timeStart
                // post the new lapTime
                timeRunsInMillis.postValue(timeRun + lapTime)
                if(timeRunsInMillis.value!! >= lastSecondTimestamp + 1000L) {
                    timeRunsInSec.postValue(timeRunsInSec.value!! + 1)
                    lastSecondTimestamp += 1000L
                }
                delay(TIMER_UPDATE_INTERVAL)
            }
            timeRun += lapTime
        }
    }

    private fun addPathPoints(location: Location?) {
        location?.let {
            val pos = LatLng(location.latitude,location.longitude)
            pathPoints.value?.apply {
                last().add(pos)
                pathPoints.postValue(this)
            }
        }
    }

    private fun updateLocationTracking(isTracking: Boolean) {
        if(isTracking){
            if(TrackingUtility.hasLocationPermissions(this)){
                val request = LocationRequest().apply {
                    interval = LOCATION_UPDATE_INTERVAL
                    fastestInterval = FASTEST_LOCATION_INTERVAL
                    priority = PRIORITY_HIGH_ACCURACY
                }
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                fusedLocationProviderClient.requestLocationUpdates(
                    request,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
        }else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    // location callback
    val locationCallback = object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult?) {
            super.onLocationResult(p0)
            if(isTracking.value!!){
                p0?.locations?.let { locations ->
                    for(location in locations){
                        addPathPoints(location)
                        Log.d(TAG,"New Location : ${location.latitude}, ${location.longitude}")
                    }

                }
            }
        }
    }

    private fun addEmptyPolyline() = pathPoints.value?.apply {
        add(mutableListOf())
        pathPoints.postValue(this)
    } ?: pathPoints.postValue(mutableListOf(mutableListOf()))

    @RequiresApi(Build.VERSION_CODES.ECLAIR)
    private fun startForegroundService() {
        startTimer()
        // need or create empty polyline
        addEmptyPolyline()

        isTracking.postValue(true)

        // using notification manager
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // create notification channel
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        // notification builder to construct actual notification with foreground service and a pending
        // intent which leads the user from notification to tracking activity
        startForeground(NOTIFICATION_ID,baseNotificationBuilder.build())

        timeRunsInSec.observe(this, Observer {
            val notification = currentNotificationBuilder
                .setContentText(TrackingUtility.getFormattedStopWatchTime(it*1000L))
            notificationManager.notify(NOTIFICATION_ID,notification.build())
        })

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager){
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

}