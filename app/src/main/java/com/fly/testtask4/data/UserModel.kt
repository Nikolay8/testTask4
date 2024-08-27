package com.fly.testtask4.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Data class representing a user model.
 *
 * @property id Unique identifier for the user.
 * @property name Full name of the user.
 * @property email Email address of the user.
 * @property phone Phone number of the user.
 * @property position The user's position in the company.
 * @property positionId Unique identifier for the user's position.
 * @property registrationTimestamp Unix timestamp representing the user's registration date.
 * @property photo URL to the user's profile photo.
 */
@Parcelize
data class UserModel(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String,
    val position: String,
    @SerializedName("position_id")
    val positionId: Int,
    @SerializedName("registration_timestamp")
    val registrationTimestamp: Int,
    val photo: String
) : Parcelable