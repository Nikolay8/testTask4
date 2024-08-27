package com.fly.testtask4.network.repository

import com.fly.testtask4.data.UserModel
import com.fly.testtask4.network.RestAPIClient
import com.fly.testtask4.network.Result
import com.fly.testtask4.network.api.ApiService
import com.fly.testtask4.network.model.GetUsersResponse
import com.fly.testtask4.network.model.PositionsResponse
import com.fly.testtask4.network.model.SetUserRequest
import okhttp3.ResponseBody

class ApiRepositoryImpl(
    private val apiService: ApiService
) : ApiRepository {
    override suspend fun getUsers(page: Int, count: Int): Result<GetUsersResponse> {
        return RestAPIClient.callAPI("getUsers") {
            apiService.getUsers(page = page, count = count)
        }
    }

    override suspend fun setUser(userModel: UserModel): Result<ResponseBody> {
        return RestAPIClient.callAPI("setUser") {
            apiService.setUser(
                token = "",
                SetUserRequest(userModel = userModel)
            )
        }
    }

    override suspend fun getPositions(): Result<PositionsResponse> {
        return RestAPIClient.callAPI("positions") {
            apiService.getPositions()
        }
    }
}
