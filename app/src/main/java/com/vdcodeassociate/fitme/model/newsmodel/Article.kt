package com.vdcodeassociate.newsheadlines.kotlin.model

import java.io.Serializable

data class Article(
    val id: Int? = null,
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String
) : Serializable