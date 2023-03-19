package com.vdcodeassociate.fitme.room.runs

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RunDao {

    // insert a new run
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRun(run: Run)

    // delete a new run
    @Delete
    suspend fun deleteRun(run: Run)

    @Query("SELECT COUNT() from running_table")
    fun getItemCount(): LiveData<Int>

    // sort a run by timestamp
    @Query("SELECT * FROM running_table ORDER BY timestamp DESC")
    fun getAllRunSortedByDate(): LiveData<List<Run>>

    // sort a run by timeInMillis
    @Query("SELECT * FROM RUNNING_TABLE ORDER BY timeInMillis DESC")
    fun getAllRunSortedByTimeInMillis(): LiveData<List<Run>>

    // sort a run by caloriesBurned
    @Query("SELECT * FROM RUNNING_TABLE ORDER BY caloriesBurned DESC")
    fun getAllRunSortedByCaloriesBurned(): LiveData<List<Run>>

    // sort a run by avgSpeedInKMH
    @Query("SELECT * FROM RUNNING_TABLE ORDER BY avgSpeedInKMH DESC")
    fun getAllRunSortedByAvgSpeed(): LiveData<List<Run>>

    // sort a run by distanceInMeters
    @Query("SELECT * FROM RUNNING_TABLE ORDER BY distanceInMeters DESC")
    fun getAllRunSortedByDistance(): LiveData<List<Run>>

    // total of a run by timeInMillis
    @Query("SELECT SUM(timeInMillis) FROM running_table")
    fun getTotalTimeInMillis(): LiveData<Long>

    // total of a run by caloriesBurned
    @Query("SELECT SUM(caloriesBurned) FROM running_table")
    fun getTotalCaloriesBurned(): LiveData<Int>

    // total of a run by avgSpeedInKMH
    @Query("SELECT SUM(avgSpeedInKMH) FROM running_table")
    fun getTotalAvgSpeedInKMH(): LiveData<Float>

    // total of a run by distanceInMeters
    @Query("SELECT SUM(distanceInMeters) FROM running_table")
    fun getTotalDistanceInMeters(): LiveData<Int>

    // get last Item from db
    @Query("SELECT * FROM running_table ORDER BY timestamp DESC LIMIT 1")
    fun getLastItem(): LiveData<Run>

    // get total calories for last week
    @Query("SELECT COUNT() FROM running_table")
    fun getLastWeekItems(): LiveData<Int>

}