package com.fly.testtask4.network.model


import com.fly.testtask4.data.Position

/** Response for "positions" Api */
data class PositionsResponse(
    val positions: List<Position>,
    val success: Boolean
)