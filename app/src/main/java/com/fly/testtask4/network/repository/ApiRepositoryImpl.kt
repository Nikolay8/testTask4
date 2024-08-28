package com.fly.testtask4.network.repository

import android.content.Context
import android.net.Uri
import com.fly.testtask4.data.UserModel
import com.fly.testtask4.network.RestAPIClient
import com.fly.testtask4.network.Result
import com.fly.testtask4.network.api.ApiService
import com.fly.testtask4.network.model.GetTokenResponse
import com.fly.testtask4.network.model.GetUsersResponse
import com.fly.testtask4.network.model.PositionsResponse
import com.fly.testtask4.network.model.SetUserResponse
import com.fly.testtask4.util.InputStreamConverter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class ApiRepositoryImpl(
    private val apiService: ApiService
) : ApiRepository {
    override suspend fun getUsers(page: Int, count: Int): Result<GetUsersResponse> {
        return RestAPIClient.callAPI("getUsers") {
            apiService.getUsers(page = page, count = count)
        }
    }

    override suspend fun setUser(
        context: Context, token: String, userModel: UserModel
    ): Result<SetUserResponse> {

        // Use InputStreamConverter for get the photo body
        val photoJPEGBody =
            InputStreamConverter().convertToJPEG(context = context, Uri.parse(userModel.photo))

        return RestAPIClient.callAPI("setUser") {
            apiService.setUser(
                token = token,
                name = userModel.name.toRequestBody("text/plain".toMediaTypeOrNull()),
                email = userModel.email.toRequestBody("text/plain".toMediaTypeOrNull()),
                phone = userModel.phone.toRequestBody("text/plain".toMediaTypeOrNull()),
                positionId = userModel.positionId.toString()
                    .toRequestBody("text/plain".toMediaTypeOrNull()),
                photo = photoJPEGBody
            )
        }
    }

    override suspend fun getPositions(): Result<PositionsResponse> {
        return RestAPIClient.callAPI("positions") {
            apiService.getPositions()
        }
    }

    override suspend fun getToken(): Result<GetTokenResponse> {
        return RestAPIClient.callAPI("token") {
            apiService.getToken()
        }
    }
}
