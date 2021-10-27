package com.vdcodeassociate.fitme.viewmodel.repository

import com.vdcodeassociate.fitme.room.Run
import com.vdcodeassociate.fitme.room.RunDao
import javax.inject.Inject

// Job of main repo is to collect data from all data sources (i.e, room or retrofit etc...)
class MainRepository @Inject constructor(
    val runDao: RunDao
) {

    suspend fun insertRun(run: Run) = runDao.insertRun(run)

    suspend fun deleteRun(run: Run) = runDao.deleteRun(run)

    fun getAllRunSortedByDate(run: Run) = runDao.getAllRunSortedByDate()

    fun getAllRunSortedByDistance(run: Run) = runDao.getAllRunSortedByDistance()

    fun getAllRunSortedByTimeInMillis(run: Run) = runDao.getAllRunSortedByTimeInMillis()

    fun getAllRunSortedByAvgSpeed(run: Run) = runDao.getAllRunSortedByAvgSpeed()

    fun getAllRunSortedByCaloriesBurned(run: Run) = runDao.getAllRunSortedByCaloriesBurned()

    fun getTotalTimeInMillis() = runDao.getTotalTimeInMillis()

    fun getTotalCaloriesBurned() = runDao.getTotalCaloriesBurned()

    fun getTotalAvgSpeedInKMH() = runDao.getTotalAvgSpeedInKMH()

    fun getTotalDistanceInMeters() = runDao.getTotalDistanceInMeters()

}
