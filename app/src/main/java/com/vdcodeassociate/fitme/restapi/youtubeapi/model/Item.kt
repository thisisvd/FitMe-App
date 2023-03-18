package com.digitalinclined.edugate.models.youtubemodel

import java.io.Serializable

data class Item(
    val etag: String,
    val id: Id,
    val kind: String,
    val snippet: Snippet
) : Serializable