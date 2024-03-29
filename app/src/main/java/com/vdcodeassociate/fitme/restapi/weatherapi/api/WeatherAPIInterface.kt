package com.vdcodeassociate.fitme.restapi.weatherapi.api

import com.vdcodeassociate.fitme.BuildConfig
import com.vdcodeassociate.fitme.restapi.weatherapi.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPIInterface {

    // weather update
    @GET("current.json")
    suspend fun getWeather(
        @Query("q")
        searchLatLng: String,
        @Query("aqi")
        aqi: String = "no",
        @Query("key")
        apiKey: String = BuildConfig.WEATHER_API_KEY
    ): Response<WeatherResponse>

}