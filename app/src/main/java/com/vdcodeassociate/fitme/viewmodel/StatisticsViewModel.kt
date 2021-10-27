package com.vdcodeassociate.fitme.viewmodel

import androidx.lifecycle.ViewModel
import com.vdcodeassociate.fitme.viewmodel.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    val repository: MainRepository
): ViewModel(){



}