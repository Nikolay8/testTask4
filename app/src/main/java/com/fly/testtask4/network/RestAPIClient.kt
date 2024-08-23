package com.fly.testtask4.network

import android.util.Log
import com.google.gson.JsonParseException
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import retrofit2.Response

/**
 * Generic REST API Client that can understand various types of API errors and can handle them all
 * at one place and just return the [Result] wrapper indicating Success or Error states with all the
 * information about the error/exception of the successful response
 */
object RestAPIClient {

    const val TAG = "RestApiClient"

    private const val EMPTY_RESPONSE = 204

    const val CODE_REQUEST_ABORTED_BY_USER = 1
    const val CODE_REQUEST_ENCOUNTERED_EXCEPTION = 2
    const val MESSAGE_REQUEST_ENCOUNTERED_EXCEPTION = "Request encountered an exception"

    /**
     * Calls the API and converts the response to [Result] while also performing the generic error
     * handling plus logging
     *
     * apiIdentifier is just for logging, use any name that helps identify what API is being called
     */
    suspend fun <T> callAPI(apiIdentifier: String, apiCall: suspend () -> Response<T>): Result<T> {
        var responseCode = 0
        return try {
            val response = apiCall()
            responseCode = response.code()
            if (response.isSuccessful) {
                val data = response.body()
                if (data == null || responseCode == EMPTY_RESPONSE) {
                    Log.i(
                        TAG,
                        "[$apiIdentifier] API succeeded with Empty Response - code=$responseCode"
                    )
                    Result.SuccessEmptyResponse
                } else {
                    Log.i(TAG, "[$apiIdentifier] API succeeded with code=$responseCode")
                    Result.Success(data)
                }
            } else if (responseCode == CODE_REQUEST_ABORTED_BY_USER) {
                Log.i(TAG, "[$apiIdentifier] API aborted by user - code=$responseCode")
                Result.Aborted
            } else {
                // Error is always wrapped into a [ResponseException]
                Result.Error(
                    ResponseException(
                        cause = if (responseCode == CODE_REQUEST_ENCOUNTERED_EXCEPTION) Exception(
                            response.message() ?: MESSAGE_REQUEST_ENCOUNTERED_EXCEPTION
                        )
                        else HttpException(response)
                    ).apply { log(TAG, "[$apiIdentifier] API failed with code=$responseCode") })
            }
        } catch (throwable: Throwable) {
            if (throwable is JsonParseException) {
                Log.i(TAG, "[$apiIdentifier] API failed with JsonParseException")
            }

            Result.Error(ResponseException(cause = throwable).apply {
                if (throwable is CancellationException) {
                    Log.e(TAG, "[$apiIdentifier] API canceled")
                } else {
                    // This will report the error in Rollbar
                    log(TAG, "[$apiIdentifier] API failed with code=$responseCode")
                }
            })
        }
    }
}
