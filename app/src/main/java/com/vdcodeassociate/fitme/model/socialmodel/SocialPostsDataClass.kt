package com.vdcodeassociate.fitme.model.socialmodel

data class SocialPostsDataClass(
    val imageUrl: String? = null,
    val likedPeoplesId: ArrayList<String>? = null,
    val postId: String? = null,
    val socialDescription: String? = null,
    val timestamp: String? = null,
    val ownerUserId: String? = null,
    val ownerUserImage: String? = null,
    val ownerUserName: String? = null,
)
