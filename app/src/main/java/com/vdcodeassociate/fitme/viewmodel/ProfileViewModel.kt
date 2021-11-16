package com.vdcodeassociate.fitme.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.constants.Constants
import com.vdcodeassociate.fitme.model.profilemodel.UserDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
): ViewModel() {

    var userData = MutableLiveData<UserDetails>()

    init {
        getSharedPreferences()
    }

    private fun getSharedPreferences(){
        val sharedPreferences = getSharedPrefData()
        userData.postValue(sharedPreferences)
    }

    // get data from shared pref
    private fun getSharedPrefData(): UserDetails {
        val name = sharedPreferences.getString(Constants.KEY_NAME, "")
        val age = sharedPreferences.getInt(Constants.KEY_AGE, 18)
        val gender = sharedPreferences.getString(Constants.KEY_GENDER, "")
        val weight = sharedPreferences.getFloat(Constants.KEY_WEIGHT, 80f)
        val height = sharedPreferences.getFloat(Constants.KEY_HEIGHT, 80f)
        val image = sharedPreferences.getInt(Constants.KEY_IMAGE, R.drawable.profile_other_image)
        return UserDetails(name!!, age, gender!!, weight, height, image)
    }

    // save / update data in shared pref
    fun updateSharedPrefData(user: UserDetails){
        sharedPreferences.edit()
            .putString(Constants.KEY_NAME, user.name)
            .putInt(Constants.KEY_AGE, user.age)
            .putString(Constants.KEY_GENDER, user.gender)
            .putFloat(Constants.KEY_WEIGHT, user.weight)
            .putFloat(Constants.KEY_HEIGHT, user.height)
            .putInt(Constants.KEY_IMAGE, user.image)
            .putBoolean(Constants.KEY_FIRST_TIME_TOGGLE, false)
            .apply()
    }

}