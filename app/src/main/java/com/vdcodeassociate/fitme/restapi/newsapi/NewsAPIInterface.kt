package com.vdcodeassociate.fitme.restapi.newsapi

import com.vdcodeassociate.fitme.BuildConfig
import com.vdcodeassociate.newsheadlines.kotlin.model.ResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPIInterface {

    // latest news
    @GET("v2/everything")
    suspend fun getBreakingNews(
        @Query("q")
        query: String = "diet",
        @Query("page")
        pageNumber: Int = 1,
        @Query("language")
        language: String = "en",
        @Query("apiKey")
        apiKey: String = BuildConfig.ARTICLES_API_KEY
    ): Response<ResponseModel>

}