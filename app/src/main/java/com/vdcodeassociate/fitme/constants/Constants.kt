package com.vdcodeassociate.fitme.constants

import android.graphics.Color

object Constants {

        // Weather URL
        const val WEATHER_URL = "http://api.weatherapi.com/v1/"
        const val WEATHER_API_KEY = "002e410db6ef45eaae3174227210511"

        // News URL
        const val API_KEY = "e6a7a646f7404e96a430f16b307c60e5"
        const val BASE_URL = "https://newsapi.org/"

        // Database Constant
        const val RUNNING_DATABASE_NAME = "running_db"

        // Shared Preferences
        const val SHARED_PREFERENCES_NAME = "Shared_preferences"
        const val KEY_FIRST_TIME_TOGGLE = "KEY_FIRST_TIME_TOGGLE"
        const val KEY_WEIGHT = "KEY_WEIGHT"
        const val KEY_NAME = "KEY_NAME"

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