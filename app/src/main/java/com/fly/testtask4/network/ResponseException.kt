package com.fly.testtask4.network

import android.util.Log
import com.google.gson.JsonParseException
import retrofit2.HttpException
import java.io.IOException

class ResponseException(
    cause: Throwable
) : Exception(cause.message, cause) {

    val code = (cause as? HttpException)?.code()
    override val message: String
        get() {
            return getFallbackErrorMessage()
        }

    private fun getFallbackErrorMessage() = when (cause) {
        is IOException -> { // UnknownHostException, SocketTimeoutException...
            "No internet connection"
        }

        else -> {
            "Unknown error"
        }
    }
}

fun ResponseException.log(tag: String, message: String) {
    when (cause) {
        is IOException -> Log.e(tag, "$message (network error)", cause as IOException)
        is HttpException -> Log.e(tag, "$message (Http error)", cause as HttpException)
        is JsonParseException ->
            Log.e(tag, "$message (parsing error)", cause as JsonParseException)

        else -> {
            cause?.let { Log.e(tag, "$message (unknown error)", it) }
                ?: kotlin.run { Log.e(tag, "$message (unknown error)") }
        }
    }
}
