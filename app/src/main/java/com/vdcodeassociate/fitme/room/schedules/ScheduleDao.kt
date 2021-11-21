package com.vdcodeassociate.fitme.room.schedules

import androidx.lifecycle.LiveData
import androidx.room.*
import com.vdcodeassociate.fitme.room.runs.Run

@Dao
interface ScheduleDao {

    // insert a new run
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScheduleRun(schedule: Schedule)

    // delete a run
    @Delete
    suspend fun deleteScheduleRun(schedule: Schedule)

    // sort schedules with timestamp
    @Query("SELECT * FROM schedule_table ORDER BY timeStamp ASC")
    fun getScheduledRuns(): LiveData<List<Schedule>>

    // get last Item from db
    @Query("SELECT * FROM schedule_table ORDER BY timeStamp ASC LIMIT 1")
    fun getLastScheduledItem(): LiveData<Schedule>


}