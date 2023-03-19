package com.vdcodeassociate.fitme.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vdcodeassociate.fitme.room.schedules.Schedule
import com.vdcodeassociate.fitme.viewmodel.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel  // injection to view model internally
class ScheduleViewModel @Inject constructor(
    val repository: MainRepository
) : ViewModel() {

    private val privateSortedData = MediatorLiveData<Schedule>()

    // insert a schedule
    fun insertSchedule(schedule: Schedule) = viewModelScope.launch {
        repository.insertScheduledRuns(schedule)
    }

    // insert a schedule
    fun deleteSchedule(schedule: Schedule) = viewModelScope.launch {
        repository.deleteScheduledRuns(schedule)
    }

    // get scheduled runs
    fun getScheduledRuns() = repository.getScheduledRuns()

    // get last scheduled
    fun getLastScheduledItem() = repository.getLastScheduledItem()

}