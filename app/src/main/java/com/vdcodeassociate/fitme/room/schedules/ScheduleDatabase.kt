package com.vdcodeassociate.fitme.room.schedules

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Schedule::class],
    version = 1,
    exportSchema = false
)
abstract class ScheduleDatabase: RoomDatabase() {

    abstract fun scheduleDao(): ScheduleDao

}