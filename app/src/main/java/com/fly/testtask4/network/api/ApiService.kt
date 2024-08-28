package com.fly.testtask4.network.api

import com.fly.testtask4.network.model.GetTokenResponse
import com.fly.testtask4.network.model.GetUsersResponse
import com.fly.testtask4.network.model.PositionsResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

/** Interface for Api calls */
interface ApiService {

    @GET("users")
    suspend fun getUsers(
        @Query("page") page: Int,
        @Query("count") count: Int
    ): Response<GetUsersResponse>

    @Multipart
    @POST("users")
    suspend fun setUser(
        @Header("token") token: String,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("position_id") positionId: RequestBody,
        @Part photo: MultipartBody.Part
    ): Response<ResponseBody>

    @GET("positions")
    suspend fun getPositions(): Response<PositionsResponse>

    @GET("token")
    suspend fun getToken(): Response<GetTokenResponse>
}