package com.vdcodeassociate.fitme.hilt

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import com.vdcodeassociate.fitme.BuildConfig.GOOGLE_PLATFORM_API_KEY
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : Application() {

//    Here it is defined that, we are going to use Hilt to implement
//    Dagger by adding @HiltAndroidApp annotation

    override fun onCreate() {
        super.onCreate()

        // update MAP_API_KEY
        updateApiKeyInManifest()
    }

    // update MAP_API_KEY in manifest meta-data file in runtime
    private fun updateApiKeyInManifest() {
        try {
            // Access the meta-data in the AndroidManifest
            val appInfo: ApplicationInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
            val metaData: Bundle = appInfo.metaData
            metaData.putString("com.google.android.geo.API_KEY", GOOGLE_PLATFORM_API_KEY)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }
}