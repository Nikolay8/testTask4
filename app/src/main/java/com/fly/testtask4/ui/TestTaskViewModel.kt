package com.fly.testtask4.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fly.testtask4.data.TestTaskUiState
import com.fly.testtask4.data.UserModel
import com.fly.testtask4.network.Result
import com.fly.testtask4.network.model.GetUsersResponse
import com.fly.testtask4.network.repository.ApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TestTaskViewModel(application: Application) : AndroidViewModel(application = application) {

    private val _uiState = MutableStateFlow(TestTaskUiState())
    val uiState: StateFlow<TestTaskUiState> = _uiState.asStateFlow()

    private lateinit var apiRepository: ApiRepository


    companion object {
        const val COUNT_ON_PAGE = 10
    }

    /** Checking internet connection */
    fun checkInternetConnection(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    /** Set instance of ApiRepository */
    fun setApiRepository(apiRepository: ApiRepository) {
        this.apiRepository = apiRepository
    }

    /** Set users list to uiState */
    fun setUserList(userList: List<UserModel>) {
        _uiState.update { currentState ->
            val currentList = currentState.usersList.toMutableList()
            currentList.addAll(userList)
            currentState.copy(usersList = currentList)
        }
    }

    /** Get users list by API request */
    fun getUserList(
        responseMutableLiveData: MutableLiveData<Result<GetUsersResponse>>, page: Int
    ) {
        viewModelScope.launch {
            apiRepository.getUsers(page, COUNT_ON_PAGE).let {
                responseMutableLiveData.value = it
            }
        }
    }
}