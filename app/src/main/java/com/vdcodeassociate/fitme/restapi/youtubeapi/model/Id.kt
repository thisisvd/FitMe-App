package com.digitalinclined.edugate.models.youtubemodel

import java.io.Serializable

data class Id(
    val kind: String,
    val videoId: String
) : Serializable