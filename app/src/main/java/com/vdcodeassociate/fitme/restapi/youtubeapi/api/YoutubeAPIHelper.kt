package com.vdcodeassociate.fitme.restapi.youtubeapi.api

import com.digitalinclined.edugate.models.youtubemodel.YoutubeResponse
import retrofit2.Response

interface YoutubeAPIHelper {

    suspend fun getYoutubeSearchQuery(query: String): Response<YoutubeResponse>

}