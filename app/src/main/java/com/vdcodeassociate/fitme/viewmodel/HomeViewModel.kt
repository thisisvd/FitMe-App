package com.vdcodeassociate.fitme.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.vdcodeassociate.fitme.restapi.weatherapi.model.WeatherResponse
import com.vdcodeassociate.fitme.room.Run
import com.vdcodeassociate.fitme.utils.Resource
import com.vdcodeassociate.fitme.viewmodel.repository.MainRepository
import com.vdcodeassociate.newsheadlines.kotlin.model.ResponseModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class HomeViewModel @Inject constructor(
    val repository: MainRepository
): ViewModel() {

    private val weatherUpdate = MutableLiveData<Resource<WeatherResponse>>()

    val getWeatherUpdate: LiveData<Resource<WeatherResponse>> get() = weatherUpdate

    // room weekly stats
    private val runSortedByDate = repository.getAllRunSortedByDate()

    // mediator live Data for sorted runs
    var sortedWeeklyRuns = MediatorLiveData<List<Run>>()

    init {
        getWeatherUpdate("Jabalpur")
        sortWeeklyDate()
    }

    private fun getWeatherUpdate(query: String) = viewModelScope.launch {
        weatherUpdate.postValue(Resource.Loading())
        val response = repository.getWeatherUpdate(query)
        weatherUpdate.postValue(handleWeatherUpdateResponse(response))
    }

    // handle Response for getWeatherUpdate()
    private fun handleWeatherUpdateResponse(response: Response<WeatherResponse>) : Resource<WeatherResponse> {
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    private fun sortWeeklyDate(){
        var tempList = mutableListOf<Run>()
        // show week stats
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -6)
        val sixDaysAgo = cal.timeInMillis
        sortedWeeklyRuns.addSource(runSortedByDate){ runs ->
            for(run in runs){
                if(run.timestamp < sixDaysAgo){
                    Log.i("STAMPFUCK","${run.timestamp} < $sixDaysAgo = ${run.timestamp < sixDaysAgo}")
                    tempList.add(run)
                }
            }

            tempList.let {
                sortedWeeklyRuns.value = it
            }

        }
    }

}