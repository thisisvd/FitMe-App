package com.vdcodeassociate.fitme.hilt

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import androidx.viewbinding.BuildConfig
import com.vdcodeassociate.fitme.constants.Constants
import com.vdcodeassociate.fitme.constants.Constants.BASE_URL
import com.vdcodeassociate.fitme.constants.Constants.KEY_FIRST_TIME_TOGGLE
import com.vdcodeassociate.fitme.constants.Constants.KEY_NAME
import com.vdcodeassociate.fitme.constants.Constants.KEY_WEIGHT
import com.vdcodeassociate.fitme.constants.Constants.RUNNING_DATABASE_NAME
import com.vdcodeassociate.fitme.constants.Constants.SHARED_PREFERENCES_NAME
import com.vdcodeassociate.fitme.restapi.newsapi.NewsAPIHelper
import com.vdcodeassociate.fitme.restapi.newsapi.NewsAPIHelperImpl
import com.vdcodeassociate.fitme.restapi.newsapi.NewsAPIInterface
import com.vdcodeassociate.fitme.room.RunDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

// AppModule object will provide required instances as a dependency
// to other classes and maintain singleton behavior
@Module     // A Module object wrt dagger implementation
@InstallIn(SingletonComponent::class)  // Available for complete application lifecycle
object AppModule {

    // @Singleton” and “@Provides” on them. These annotations tells it that
    // only single instance of that particular class will be available throughout
    // the application and will be passed whenever and wherever needed as a dependency.

    @Singleton
    @Provides
    fun provideRunningDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        RunDatabase::class.java,
        RUNNING_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideRunDao(db: RunDatabase) = db.getRunDao()

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext app: Context) =
        app.getSharedPreferences(SHARED_PREFERENCES_NAME,MODE_PRIVATE)

    @Singleton
    @Provides
    fun providesName(sharedPreference: SharedPreferences) = sharedPreference.getString((KEY_NAME),"") ?: ""

    @Singleton
    @Provides
    fun providesWeight(sharedPreference: SharedPreferences) = sharedPreference.getFloat((KEY_WEIGHT),80f)

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
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Provides
    fun provideApiInterface(retrofit: Retrofit) = retrofit.create(NewsAPIInterface::class.java)

    @Provides
    @Singleton
    fun provideApiHelper(apiHelper: NewsAPIHelperImpl): NewsAPIHelper = apiHelper

}