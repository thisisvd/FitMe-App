package com.vdcodeassociate.fitme.restapi.weatherapi.api

import com.vdcodeassociate.fitme.restapi.weatherapi.model.WeatherResponse
import retrofit2.Response

interface WeatherAPIHelper {

    suspend fun getWeatherUpdate(query: String): Response<WeatherResponse>

}