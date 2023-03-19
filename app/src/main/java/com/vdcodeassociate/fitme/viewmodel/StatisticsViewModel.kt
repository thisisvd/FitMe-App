package com.vdcodeassociate.fitme.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.vdcodeassociate.fitme.room.runs.Run
import com.vdcodeassociate.fitme.utils.Utils
import com.vdcodeassociate.fitme.viewmodel.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    val repository: MainRepository
) : ViewModel() {

    var totalItemCount = repository.getTotalItemCount()
    val totalTimeRun = repository.getTotalTimeInMillis()
    val totalDistance = repository.getTotalDistanceInMeters()
    val totalCaloriesBurned = repository.getTotalCaloriesBurned()
    val totalAvgSpeed = repository.getTotalAvgSpeedInKMH()

    val runSortedByDate = repository.getAllRunSortedByDate()

    // Total Heart Points n Calories
    val heartPoints = MediatorLiveData<Int>()

    init {
        calTotalHealthPoints()
    }

    private fun calTotalHealthPoints() {
        var heartPts = 0

        // cal heart points
        heartPoints.addSource(runSortedByDate) { runs ->
            for (run in runs) {
                heartPts += calculateHeartPts(run)
            }

            heartPoints.value = heartPts

        }

    }

    // calculate HeartPts
    private fun calculateHeartPts(run: Run): Int {
        return if ((((run.distanceInMeters / 1000f) * 1312) / Utils().getMinutes(run.timeInMillis)).toInt() <= 125) {
            2
        } else {
            0
        }
    }
}