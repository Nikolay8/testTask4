package com.fly.testtask4.network

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.fly.testtask4.network.api.ApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * ApiManager is responsible for configuring and providing an instance of the [ApiService].
 * This class sets up the OkHttpClient with appropriate timeouts and integrates the ChuckerInterceptor
 * for debugging network requests.
 */
class ApiManager {
    companion object {
        /** The base URL for the API endpoints. */
        const val HTTPS_API_URL =
            "https://frontend-test-assignment-api.abz.agency/api/v1/"

        /** The timeout duration in seconds for network requests. */
        private const val HTTP_TIMEOUT: Long = 60 // SECONDS
    }

    /**
     * Provides an instance of [ApiService] configured with an OkHttpClient and Retrofit.
     *
     * @param context The [Context] required to initialize the ChuckerInterceptor.
     * @return A configured [ApiService] instance.
     */
    fun provideApiService(context: Context): ApiService {
        // Configure OkHttpClient with timeouts and ChuckerInterceptor for network logging
        val okhttpClient = OkHttpClient.Builder()
            .addInterceptor(ChuckerInterceptor(context))
            .connectTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS).build()

        // Configure Retrofit with the base URL and Gson converter
        val retrofit = Retrofit.Builder()
            .baseUrl(HTTPS_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okhttpClient).build()

        // Create and return the ApiService instance
        return retrofit.create(ApiService::class.java)
    }
}