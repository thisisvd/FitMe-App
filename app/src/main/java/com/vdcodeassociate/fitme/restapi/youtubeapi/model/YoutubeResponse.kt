package com.digitalinclined.edugate.models.youtubemodel

data class YoutubeResponse(
    val etag: String,
    val items: List<Item>,
    val kind: String,
    val nextPageToken: String,
    val pageInfo: PageInfo,
    val regionCode: String
)