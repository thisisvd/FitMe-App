package com.digitalinclined.edugate.models.youtubemodel

import java.io.Serializable

data class High(
    val height: Int,
    val url: String,
    val width: Int
) : Serializable