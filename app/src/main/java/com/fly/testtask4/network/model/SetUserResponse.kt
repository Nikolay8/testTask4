package com.fly.testtask4.network.model


import com.google.gson.annotations.SerializedName

/** Response for "setUser" Api */
data class SetUserResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)