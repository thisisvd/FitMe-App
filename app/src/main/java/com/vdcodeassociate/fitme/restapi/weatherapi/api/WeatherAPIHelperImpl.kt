package com.vdcodeassociate.fitme.restapi.weatherapi.api

import com.vdcodeassociate.fitme.restapi.weatherapi.model.WeatherResponse
import retrofit2.Response
import javax.inject.Inject

class WeatherAPIHelperImpl @Inject constructor(
    private val weatherAPIInterface: WeatherAPIInterface
): WeatherAPIHelper {
    override suspend fun getWeatherUpdate(query: String): Response<WeatherResponse> =
        weatherAPIInterface.getWeather(query)

}