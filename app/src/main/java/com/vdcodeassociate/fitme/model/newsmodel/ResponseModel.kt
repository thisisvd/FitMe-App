package com.vdcodeassociate.newsheadlines.kotlin.model

data class ResponseModel(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)