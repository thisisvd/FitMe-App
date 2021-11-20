package com.vdcodeassociate.fitme.room

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "running_table")
data class Run(
    var img: Bitmap? = null,
    var timestamp: Long = 0L,
    var avgSpeedInKMH: Float = 0f,
    var distanceInMeters: Int = 0,
    var timeInMillis: Long = 0L,
    var caloriesBurned: Int = 0,
    var weatherCelsius: Int = 0,
    var windSpeed: Float = 0f,
    var weatherStatus: String = "",
    var isStepGoalCheck: Boolean = false,
    var isDistGoalCheck: Boolean = false,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
