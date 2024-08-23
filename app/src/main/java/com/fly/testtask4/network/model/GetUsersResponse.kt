package com.fly.testtask4.network.model


import com.fly.testtask4.data.UserModel
import com.google.gson.annotations.SerializedName

/** Response for "getUser" Api */
data class GetUsersResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("links")
    val links: Links,
    @SerializedName("page")
    val page: Int,
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_users")
    val totalUsers: Int,
    @SerializedName("users")
    val users: List<UserModel>
)
