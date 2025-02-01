package com.vdcodeassociate.fitme.restapi.youtubeapi.api

import com.digitalinclined.edugate.models.youtubemodel.YoutubeResponse
import com.vdcodeassociate.fitme.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeAPIInterface {

    // youtube videos api search
    @GET("search")
    suspend fun getYoutubeSearchQuery(
        @Query("q")
        q: String,
        @Query("regionCode")
        regionCode: String = "IN",
        @Query("type")
        type: String = "video",
        @Query("key")
        key: String = BuildConfig.GOOGLE_PLATFORM_API_KEY,
        @Query("maxResults")
        maxResults: Int = 30,
        @Query("part")
        part: String = "snippet"
    ): Response<YoutubeResponse>

}