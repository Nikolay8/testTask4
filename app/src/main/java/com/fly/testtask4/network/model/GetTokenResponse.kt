package com.fly.testtask4.network.model


import com.google.gson.annotations.SerializedName

/**
 * Data class representing the response received when requesting a token.
 */
data class GetTokenResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("token")
    val token: String
)
