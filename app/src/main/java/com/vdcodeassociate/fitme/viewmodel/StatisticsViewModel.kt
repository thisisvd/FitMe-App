package com.vdcodeassociate.fitme.viewmodel

import androidx.lifecycle.ViewModel
import com.vdcodeassociate.fitme.viewmodel.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    val repository: MainRepository
): ViewModel(){

    val totalItemCount = repository.getTotalItemCount()
    val totalTimeRun = repository.getTotalTimeInMillis()
    val totalDistance = repository.getTotalDistanceInMeters()
    val totalCaloriesBurned = repository.getTotalCaloriesBurned()
    val totalAvgSpeed = repository.getTotalAvgSpeedInKMH()

    val runSortedByDate = repository.getAllRunSortedByDate()

}