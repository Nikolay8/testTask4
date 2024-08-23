package com.fly.testtask4.data


/**
 * Data class that represents the current UI state
 */
data class TestTaskUiState(

    /** Available installed apps on device */
    val usersList: List<UserModel> = listOf(),

    )
