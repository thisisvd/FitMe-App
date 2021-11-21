package com.vdcodeassociate.fitme.room.schedules

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedule_table")
data class Schedule(
    val broadcastID: Int,
    val title: String = "",
    val timeStamp: Long = 0L,
    val goalDistance: Float = 1f,
    val goalStep: Int = 500
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
