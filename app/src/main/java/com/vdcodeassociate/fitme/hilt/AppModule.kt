package com.vdcodeassociate.fitme.hilt

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import com.vdcodeassociate.fitme.constants.Constants.KEY_FIRST_TIME_TOGGLE
import com.vdcodeassociate.fitme.constants.Constants.KEY_NAME
import com.vdcodeassociate.fitme.constants.Constants.KEY_WEIGHT
import com.vdcodeassociate.fitme.constants.Constants.RUNNING_DATABASE_NAME
import com.vdcodeassociate.fitme.constants.Constants.SHARED_PREFERENCES_NAME
import com.vdcodeassociate.fitme.room.RunDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

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

}