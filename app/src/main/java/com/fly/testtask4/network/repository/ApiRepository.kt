package com.fly.testtask4.network.repository


import com.fly.testtask4.data.UserModel
import com.fly.testtask4.network.Result
import com.fly.testtask4.network.model.GetUsersResponse
import okhttp3.ResponseBody

/**
 *  Repository for Api requests
 */
interface ApiRepository {

    /** Get list for all users */
    suspend fun getUsers(page: Int, count: Int): Result<GetUsersResponse>

    /** Register new user */
    suspend fun setUser(userModel: UserModel): Result<ResponseBody>

}
