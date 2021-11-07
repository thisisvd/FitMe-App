package com.vdcodeassociate.fitme.hilt

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication: Application() {

//    Here it is defined that, we are going to use Hilt to implement
//    Dagger by adding @HiltAndroidApp annotation

}