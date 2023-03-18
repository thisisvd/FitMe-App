package com.vdcodeassociate.fitme.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.digitalinclined.edugate.models.youtubemodel.Item
import com.vdcodeassociate.fitme.model.homemodel.WeekStatsHome
import com.vdcodeassociate.fitme.restapi.weatherapi.model.WeatherResponse
import com.vdcodeassociate.fitme.room.runs.Run
import com.vdcodeassociate.fitme.utils.Resource
import com.vdcodeassociate.fitme.utils.Utils
import com.vdcodeassociate.fitme.viewmodel.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class HomeViewModel @Inject constructor(
    val repository: MainRepository
): ViewModel() {

    // weather private value
    private val weatherUpdate = MutableLiveData<Resource<WeatherResponse>>()

    // save data for observers
    val getWeatherUpdate: LiveData<Resource<WeatherResponse>> get() = weatherUpdate

    // room weekly stats
    private val runSortedByDate = repository.getAllRunSortedByDate()

    // mediator live Data for sorted runs
    var sortedWeeklyStats = MediatorLiveData<WeekStatsHome>()

    // banner details
    var getYoutubeSearchResult: MutableLiveData<Resource<List<Item>>> = MutableLiveData()

    // init
    init {
        sortWeeklyDate()
    }

    // get youtube videos result
    fun getYoutubeResult(query: String) = viewModelScope.launch {
        getYoutubeSearchResult.postValue(Resource.Loading())

        try {
            // sending request
            val response = repository.getYoutubeSearchQuery(query)

            if (response.isSuccessful) {
                response.body().let { response ->
                    if (response != null && response.items.isNotEmpty()) {
                        getYoutubeSearchResult.postValue(Resource.Success(response.items))
                    }
                }

            } else {
                getYoutubeSearchResult.postValue(Resource.Error(response.message().toString()))
            }

        } catch (e: Exception) {
            getYoutubeSearchResult.postValue(Resource.Error(e.message.toString()))
            e.printStackTrace()
        }
    }

    // getting data from repos
    fun getWeatherUpdate(query: String) = viewModelScope.launch {
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

    // selecting run's happened in week
    private fun sortWeeklyDate(){
        var tempList = mutableListOf<Run>()
        var calories = 0
        var distance = 0F
        var steps = 0
        var heartPoints = 0

        // calender set up 6 days ago value in timestamp
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -6)
        val sixDaysAgo = cal.timeInMillis

        // show week stats
        sortedWeeklyStats.addSource(runSortedByDate) { runs ->
            for (run in runs) {
                if (run.timestamp > sixDaysAgo) {
                    tempList.add(run)
                    calories += run.caloriesBurned
                    distance += run.distanceInMeters
                    heartPoints += calculateHeartPts(run)
                }
            }

            distance /= 1000f
            steps = (distance * 1312).roundToInt()

            sortedWeeklyStats.value = WeekStatsHome(calories, distance, steps, heartPoints)

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
