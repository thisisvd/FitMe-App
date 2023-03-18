package com.vdcodeassociate.fitme.viewmodel.repository

import com.vdcodeassociate.fitme.restapi.newsapi.NewsAPIHelper
import com.vdcodeassociate.fitme.restapi.weatherapi.api.WeatherAPIHelper
import com.vdcodeassociate.fitme.restapi.youtubeapi.api.YoutubeAPIHelper
import com.vdcodeassociate.fitme.room.runs.Run
import com.vdcodeassociate.fitme.room.runs.RunDao
import com.vdcodeassociate.fitme.room.schedules.Schedule
import com.vdcodeassociate.fitme.room.schedules.ScheduleDao
import javax.inject.Inject

// Job of main repo is to collect data from all data sources (i.e, room or retrofit etc...)
class MainRepository @Inject constructor(
    val runDao: RunDao,
    private val newsAPIHelper: NewsAPIHelper,
    private val weatherAPIHelper: WeatherAPIHelper,
    private val youtubeAPIHelper: YoutubeAPIHelper,
    private val scheduleDao: ScheduleDao
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

    // get youtube search query videos
    suspend fun getYoutubeSearchQuery(query: String) = youtubeAPIHelper.getYoutubeSearchQuery(query)

    // Schedule Room Database
    // insert data in schedule db
    suspend fun insertScheduledRuns(schedule: Schedule) = scheduleDao.insertScheduleRun(schedule)

    // delete data in schedule db
    suspend fun deleteScheduledRuns(schedule: Schedule) = scheduleDao.deleteScheduleRun(schedule)

    // get data from schedule db
    fun getScheduledRuns() = scheduleDao.getScheduledRuns()

    // get last scheduled run
    fun getLastScheduledItem() = scheduleDao.getLastScheduledItem()

}
