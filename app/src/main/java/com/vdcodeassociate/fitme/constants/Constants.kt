package com.vdcodeassociate.fitme.constants

import android.graphics.Color

object Constants {

    // URL
    const val WEATHER_URL = "http://api.weatherapi.com/"
    const val WEATHER_API_KEY = "002e410db6ef45eaae3174227210511"

    // Database Constant
    const val RUNNING_DATABASE_NAME = "running_db"

    // Location Permission Constant
    const val REQUEST_CODE_LOCATION_PERMISSION = 0

    // Tracking Service Constants
    const val ACTION_START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE"

    const val ACTION_PAUSE_SERVICE = "ACTION_PAUSE_SERVICE"

    const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"

    const val ACTION_SHOW_TRACKING_FRAGMENT = "ACTION_SHOW_TRACKING_FRAGMENT"

    // Location Update Constant
    const val LOCATION_UPDATE_INTERVAL = 5000L
    const val FASTEST_LOCATION_INTERVAL = 2000L

    // Polyline
    const val POLYLINE_COLOR = Color.RED
    const val POLYLINE_WIDTH = 8f

    // Map Zoom
    const val MAP_ZOOM = 15f

    // TIMER_UPDATE_INTERVAL
    const val TIMER_UPDATE_INTERVAL = 50L

    // Notifications Constants
    const val NOTIFICATION_CHANNEL_ID = "tracking_channel"

    const val NOTIFICATION_CHANNEL_NAME = "Tracking"

    const val NOTIFICATION_ID = 1

}