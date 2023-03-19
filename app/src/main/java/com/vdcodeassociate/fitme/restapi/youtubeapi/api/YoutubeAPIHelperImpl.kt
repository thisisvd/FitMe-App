package com.vdcodeassociate.fitme.restapi.youtubeapi.api

import com.digitalinclined.edugate.models.youtubemodel.YoutubeResponse
import retrofit2.Response
import javax.inject.Inject

//only one instance of ApiService throughout the application lifecycle for any number of network calls
class YoutubeAPIHelperImpl @Inject constructor(
    private val apiInterface: YoutubeAPIInterface
) : YoutubeAPIHelper {

    override suspend fun getYoutubeSearchQuery(query: String): Response<YoutubeResponse> =
        apiInterface.getYoutubeSearchQuery(query)

}