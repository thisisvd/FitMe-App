package com.vdcodeassociate.fitme.restapi.newsapi

import com.vdcodeassociate.newsheadlines.kotlin.model.ResponseModel
import retrofit2.Response
import javax.inject.Inject

//only one instance of ApiService throughout the application lifecycle for any number of network calls
class NewsAPIHelperImpl @Inject constructor(
    private val apiInterface: NewsAPIInterface
): NewsAPIHelper {

    override suspend fun getLatestNews(): Response<ResponseModel> =
        apiInterface.getBreakingNews()

}