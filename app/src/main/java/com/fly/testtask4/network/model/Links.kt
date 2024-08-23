package com.fly.testtask4.network.model

import com.google.gson.annotations.SerializedName

/**  Data clas for GetUsersResponse */
data class Links(
    @SerializedName("next_url")
    val nextUrl: String,
    @SerializedName("prev_url")
    val prevUrl: Any
)
