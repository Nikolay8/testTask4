package com.fly.testtask4.network.model

import com.fly.testtask4.data.UserModel
import com.squareup.moshi.JsonClass

/**
 * Request body for set new user
 */
@JsonClass(generateAdapter = true)
data class SetUserRequest(
    val userModel: UserModel
)
