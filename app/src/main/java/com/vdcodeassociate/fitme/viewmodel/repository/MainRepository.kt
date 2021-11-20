package com.vdcodeassociate.fitme.viewmodel.repository

import com.vdcodeassociate.fitme.restapi.newsapi.NewsAPIHelper
import com.vdcodeassociate.fitme.restapi.weatherapi.api.WeatherAPIHelper
import com.vdcodeassociate.fitme.room.Run
import com.vdcodeassociate.fitme.room.RunDao
import javax.inject.Inject

// Job of main repo is to collect data from all data sources (i.e, room or retrofit etc...)
class MainRepository @Inject constructor(
    val runDao: RunDao,
    private val newsAPIHelper: NewsAPIHelper,
    private val weatherAPIHelper: WeatherAPIHelper
) {

    // Runs Functions
    suspend fun insertRun(run: Run) = runDao.insertRun(run)

    suspend fun deleteRun(run: Run) = runDao.deleteRun(run)

    fun getAllRunSortedByDate() = runDao.getAllRunSortedByDate()

    fun getAllRunSortedByDistance() = runDao.getAllRunSortedByDistance()

    fun getAllRunSortedByTimeInMillis() = runDao.getAllRunSortedByTimeInMillis()

    fun getAllRunSortedByAvgSpeed() = runDao.getAllRunSortedByAvgSpeed()

    fun getAllRunSortedByCaloriesBurned() = runDao.getAllRunSortedByCaloriesBurned()

    fun getTotalTimeInMillis() = runDao.getTotalTimeInMillis()

    fun getTotalCaloriesBurned() = runDao.getTotalCaloriesBurned()

    fun getTotalAvgSpeedInKMH() = runDao.getTotalAvgSpeedInKMH()

    fun getTotalDistanceInMeters() = runDao.getTotalDistanceInMeters()

    fun getTotalItemCount() = runDao.getItemCount()

    // last run Item
    fun getLastItem() = runDao.getLastItem()

    // News functions
    suspend fun getLatestNews(query: String) = newsAPIHelper.getLatestNews(query)

    // Current Weather functions -
    suspend fun getWeatherUpdate(query: String) = weatherAPIHelper.getWeatherUpdate(query)

}
