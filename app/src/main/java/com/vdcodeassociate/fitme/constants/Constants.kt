package com.vdcodeassociate.fitme.constants

import android.graphics.Color
import com.vdcodeassociate.fitme.R

object Constants {

    // Changeable avatar id
    var AVATAR_ID = R.drawable.question_mark5

    // Weather URL
    const val WEATHER_URL = "http://api.weatherapi.com/v1/"

    // News URL
    const val BASE_URL = "https://newsapi.org/"

    // Youtube API URL
    const val YOUTUBE_API_URL = "https://www.googleapis.com/youtube/v3/"

    // Database Constant
    const val RUNNING_DATABASE_NAME = "running_db"
    const val SCHEDULE_DATABASE_NAME = "schedule_db"

    // Shared Preferences
    const val SHARED_PREFERENCES_NAME = "Shared_preferences"
    const val KEY_FIRST_TIME_TOGGLE = "KEY_FIRST_TIME_TOGGLE"
    const val KEY_WEIGHT = "KEY_WEIGHT"
    const val KEY_NAME = "KEY_NAME"
    const val KEY_HEIGHT = "KEY_HEIGHT"
    const val KEY_AGE = "KEY_AGE"
    const val KEY_GENDER = "KEY_GENDER"
    const val KEY_IMAGE = "KEY_IMAGE"
    const val KEY_STEP_GOAL = "KEY_STEP_GOAL"
    const val KEY_DISTANCE_GOAL = "KEY_DISTANCE_GOAL"
    const val KEY_HEART_POINTS = "KEY_HEART_POINTS"
    const val KEY_BROADCASTID = "KEY_BROADCASTID"

    // Location Permission Constant
    const val REQUEST_CODE_LOCATION_PERMISSION = 0
    const val PERMISSION_LOCATION_REQUEST_CODE = 1

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

    // advt. count
    var MAIN_AD_COUNT = 0

}