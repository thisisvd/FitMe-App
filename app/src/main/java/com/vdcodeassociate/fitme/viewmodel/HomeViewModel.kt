package com.vdcodeassociate.fitme.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vdcodeassociate.fitme.restapi.weatherapi.model.WeatherResponse
import com.vdcodeassociate.fitme.utils.Resource
import com.vdcodeassociate.fitme.viewmodel.repository.MainRepository
import com.vdcodeassociate.newsheadlines.kotlin.model.ResponseModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val repository: MainRepository
): ViewModel() {

    private val weatherUpdate = MutableLiveData<Resource<WeatherResponse>>()

    val getWeatherUpdate: LiveData<Resource<WeatherResponse>> get() = weatherUpdate

    init {
        getWeatherUpdate("Jabalpur")
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


}