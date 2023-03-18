package com.vdcodeassociate.fitme.hilt

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import androidx.viewbinding.BuildConfig
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.constants.Constants.BASE_URL
import com.vdcodeassociate.fitme.constants.Constants.KEY_AGE
import com.vdcodeassociate.fitme.constants.Constants.KEY_DISTANCE_GOAL
import com.vdcodeassociate.fitme.constants.Constants.KEY_FIRST_TIME_TOGGLE
import com.vdcodeassociate.fitme.constants.Constants.KEY_GENDER
import com.vdcodeassociate.fitme.constants.Constants.KEY_HEART_POINTS
import com.vdcodeassociate.fitme.constants.Constants.KEY_HEIGHT
import com.vdcodeassociate.fitme.constants.Constants.KEY_IMAGE
import com.vdcodeassociate.fitme.constants.Constants.KEY_NAME
import com.vdcodeassociate.fitme.constants.Constants.KEY_STEP_GOAL
import com.vdcodeassociate.fitme.constants.Constants.KEY_WEIGHT
import com.vdcodeassociate.fitme.constants.Constants.RUNNING_DATABASE_NAME
import com.vdcodeassociate.fitme.constants.Constants.SCHEDULE_DATABASE_NAME
import com.vdcodeassociate.fitme.constants.Constants.SHARED_PREFERENCES_NAME
import com.vdcodeassociate.fitme.constants.Constants.WEATHER_URL
import com.vdcodeassociate.fitme.constants.Constants.YOUTUBE_API_URL
import com.vdcodeassociate.fitme.restapi.newsapi.NewsAPIHelper
import com.vdcodeassociate.fitme.restapi.newsapi.NewsAPIHelperImpl
import com.vdcodeassociate.fitme.restapi.newsapi.NewsAPIInterface
import com.vdcodeassociate.fitme.restapi.weatherapi.api.WeatherAPIHelper
import com.vdcodeassociate.fitme.restapi.weatherapi.api.WeatherAPIHelperImpl
import com.vdcodeassociate.fitme.restapi.weatherapi.api.WeatherAPIInterface
import com.vdcodeassociate.fitme.restapi.youtubeapi.api.YoutubeAPIHelper
import com.vdcodeassociate.fitme.restapi.youtubeapi.api.YoutubeAPIHelperImpl
import com.vdcodeassociate.fitme.restapi.youtubeapi.api.YoutubeAPIInterface
import com.vdcodeassociate.fitme.room.runs.RunDatabase
import com.vdcodeassociate.fitme.room.schedules.ScheduleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

// AppModule object will provide required instances as a dependency
// to other classes and maintain singleton behavior
@Module     // A Module object wrt dagger implementation
@InstallIn(SingletonComponent::class)  // Available for complete application lifecycle
object AppModule {

    // @Singleton” and “@Provides” on them. These annotations tells it that
    // only single instance of that particular class will be available throughout
    // the application and will be passed whenever and wherever needed as a dependency.

    // Runs Room database
    @Singleton
    @Provides
    fun provideRunningDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        RunDatabase::class.java,
        RUNNING_DATABASE_NAME
    ).fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideRunDao(db: RunDatabase) = db.getRunDao()

    // Runs Schedule database
    @Singleton
    @Provides
    fun provideScheduleDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        ScheduleDatabase::class.java,
        SCHEDULE_DATABASE_NAME
    ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideScheduleDao(scheduleDatabase: ScheduleDatabase) = scheduleDatabase.scheduleDao()

    // Shared Preferences Database
    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext app: Context) =
        app.getSharedPreferences(SHARED_PREFERENCES_NAME,MODE_PRIVATE)

    @Singleton
    @Provides
    fun providesName(sharedPreference: SharedPreferences) = sharedPreference.getString((KEY_NAME),"") ?: ""

    @Singleton
    @Provides
    fun providesAge(sharedPreference: SharedPreferences) = sharedPreference.getInt((KEY_AGE),18)

    @Singleton
    @Provides
    fun providesGender(sharedPreference: SharedPreferences) = sharedPreference.getString((KEY_GENDER),"") ?: ""

    @Singleton
    @Provides
    fun providesHeight(sharedPreference: SharedPreferences) = sharedPreference.getFloat((KEY_HEIGHT),173f)

    @Singleton
    @Provides
    fun providesWeight(sharedPreference: SharedPreferences) = sharedPreference.getFloat((KEY_WEIGHT),80f)

    @Singleton
    @Provides
    fun providesImage(sharedPreference: SharedPreferences) = sharedPreference.getInt((KEY_IMAGE), R.drawable.question_mark5)

    @Singleton
    @Provides
    fun providesStepsGoal(sharedPreference: SharedPreferences) = sharedPreference.getInt((KEY_STEP_GOAL), 0)

    @Singleton
    @Provides
    fun providesDistanceGoal(sharedPreference: SharedPreferences) = sharedPreference.getFloat((KEY_DISTANCE_GOAL), 0f)

    @Singleton
    @Provides
    fun providesHeartPoints(sharedPreference: SharedPreferences) = sharedPreference.getInt((KEY_HEART_POINTS), 0)

    @Singleton
    @Provides
    fun providesFirstTimeToggle(sharedPreference: SharedPreferences) = sharedPreference.getBoolean((KEY_FIRST_TIME_TOGGLE),true)

    // News element Singleton Provides
    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG){
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }else{
        OkHttpClient
            .Builder()
            .build()
    }

    @Singleton
    @Provides
    @Named("News")
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Provides
    fun provideApiInterface(@Named("News") retrofit: Retrofit) = retrofit.create(NewsAPIInterface::class.java)

    @Provides
    @Singleton
    fun provideApiHelper(apiHelper: NewsAPIHelperImpl): NewsAPIHelper = apiHelper

    // weather update
    @Singleton
    @Provides
    @Named("Weather")
    fun provideRetrofitWeather(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(WEATHER_URL)
        .client(okHttpClient)
        .build()

    @Provides
    fun provideApiInterfaceWeather(@Named("Weather") retrofit: Retrofit) = retrofit.create(WeatherAPIInterface::class.java)

    @Provides
    @Singleton
    fun provideApiHelperWeather(apiHelper: WeatherAPIHelperImpl): WeatherAPIHelper = apiHelper

    // youtube search video query
    @Singleton
    @Provides
    @Named("YoutubeAPI")
    fun provideRetrofitYoutubeVideos(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(YOUTUBE_API_URL)
        .client(okHttpClient)
        .build()

    @Provides
    fun provideApiInterfaceYoutubeVideos(@Named("YoutubeAPI") retrofit: Retrofit) = retrofit.create(YoutubeAPIInterface::class.java)

    @Provides
    @Singleton
    fun provideApiHelperYoutubeVideos(apiHelper: YoutubeAPIHelperImpl): YoutubeAPIHelper = apiHelper

}