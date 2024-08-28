package com.fly.testtask4.data

import android.net.Uri


/**
 * Data class that represents the current UI state
 */
data class TestTaskUiState(

    /** Available installed apps on device */
    val usersList: List<UserModel> = listOf(),

    /** Error in user name */
    val isNameError: Boolean = false,

    /** Error in user email */
    val isEmailError: Boolean = false,

    /** Error in user phone */
    val isPhoneError: Boolean = false,

    /** Error in user photo */
    val isPhotoError: Boolean = false,

    /** List of user positions from server */
    val positions: List<Position> = listOf(),

    /** User name */
    val name: String = "",

    /** User email */
    val email: String = "",

    /** User phone */
    val phone: String = "",

    /** User position */
    val position: Position = Position(id = 0, name = ""),

    /** User photo for upload */
    val photoUri: String? = null
    )
