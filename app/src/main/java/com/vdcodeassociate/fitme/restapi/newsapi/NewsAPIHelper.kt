package com.vdcodeassociate.fitme.restapi.newsapi

import com.vdcodeassociate.newsheadlines.kotlin.model.ResponseModel
import retrofit2.Response

interface NewsAPIHelper {

    suspend fun getLatestNews(): Response<ResponseModel>

}