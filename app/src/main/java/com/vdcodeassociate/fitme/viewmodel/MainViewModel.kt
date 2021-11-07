package com.vdcodeassociate.fitme.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vdcodeassociate.fitme.room.Run
import com.vdcodeassociate.fitme.utils.SortsEnum
import com.vdcodeassociate.fitme.viewmodel.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val repository: MainRepository
): ViewModel(){

    var sortType = SortsEnum.DATE

    val runs = MediatorLiveData<List<Run>>()

    private val runSortedByDate = repository.getAllRunSortedByDate()
    private val runSortedByDistance = repository.getAllRunSortedByDistance()
    private val runSortedByTimeInMillis = repository.getAllRunSortedByTimeInMillis()
    private val runSortedByAvgSpeed = repository.getAllRunSortedByAvgSpeed()
    private val runSortedByCaloriesBurned = repository.getAllRunSortedByCaloriesBurned()

    init {
        runs.addSource(runSortedByDate) { result ->
            if(sortType == SortsEnum.DATE) {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runSortedByAvgSpeed) { result ->
            if(sortType == SortsEnum.AVG_SPEED) {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runSortedByCaloriesBurned) { result ->
            if(sortType == SortsEnum.CALORIES_BURNED) {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runSortedByDistance) { result ->
            if(sortType == SortsEnum.DISTANCE) {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runSortedByTimeInMillis) { result ->
            if(sortType == SortsEnum.TIME) {
                result?.let { runs.value = it }
            }
        }
    }

    fun sortRuns(sortType: SortsEnum) = when(sortType) {
        SortsEnum.DATE -> runSortedByDate.value?.let { runs.value = it }
        SortsEnum.TIME -> runSortedByTimeInMillis.value?.let { runs.value = it }
        SortsEnum.AVG_SPEED -> runSortedByAvgSpeed.value?.let { runs.value = it }
        SortsEnum.DISTANCE -> runSortedByDistance.value?.let { runs.value = it }
        SortsEnum.CALORIES_BURNED -> runSortedByCaloriesBurned.value?.let { runs.value = it }
    }.also {
        this.sortType = sortType
    }

    // insert new run
    fun insertRun(run : Run) = viewModelScope.launch {
        repository.insertRun(run)
    }

    // delete a run
    fun deleteRun(run: Run) = viewModelScope.launch {
        repository.deleteRun(run)
    }

}