package com.fly.testtask4.network.api

import com.fly.testtask4.network.model.GetUsersResponse
import com.fly.testtask4.network.model.SetUserRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

/** Interface for Api calls */
interface ApiService {

    @GET("users?")
    suspend fun getUsers(
        @Query("page") page: Int,
        @Query("count") count: Int
    ): Response<GetUsersResponse>

    @POST("users?")
    suspend fun setUser(
        @Header("access_token") token: String,
        @Body body: SetUserRequest
    ): Response<ResponseBody>

}