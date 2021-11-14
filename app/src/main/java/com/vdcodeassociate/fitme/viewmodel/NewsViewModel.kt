package com.vdcodeassociate.fitme.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vdcodeassociate.fitme.utils.Resource
import com.vdcodeassociate.fitme.viewmodel.repository.MainRepository
import com.vdcodeassociate.newsheadlines.kotlin.model.ResponseModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: MainRepository
): ViewModel() {

    var isDataAdded = false

    private val latestNews = MutableLiveData<Resource<ResponseModel>>()

    val getLatestNews: LiveData<Resource<ResponseModel>> get() = latestNews

    init {
        getLatestNews("fitness")
    }

    private fun getLatestNews(query: String) = viewModelScope.launch {
        latestNews.postValue(Resource.Loading())
        val response = repository.getLatestNews(query)
        latestNews.postValue(handleLatestNewsResponse(response))
        isDataAdded = true
    }

    // handle Response for LatestNews()
    private fun handleLatestNewsResponse(response: Response<ResponseModel>) : Resource<ResponseModel>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun tabLatestNews(tabValue: Int){
        // local variable
        var query = "fitness tips"

        // select query wrt to selected tab
        when(tabValue) {
            1 -> query = "diet"
            2 -> query = "meditation"
        }

        getLatestNews(query)
    }

}