package com.fly.testtask4.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fly.testtask4.data.Position
import com.fly.testtask4.data.TestTaskUiState
import com.fly.testtask4.data.UserModel
import com.fly.testtask4.network.Result
import com.fly.testtask4.network.model.GetUsersResponse
import com.fly.testtask4.network.model.PositionsResponse
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

    /** Get user positions from server */
    fun getPositions(
        responseMutableLiveData: MutableLiveData<Result<PositionsResponse>>
    ) {
        viewModelScope.launch {
            apiRepository.getPositions().let {
                responseMutableLiveData.value = it
            }
        }
    }

    /** Set list of positions to uiState */
    fun setPositionsList(positions: List<Position>) {
        _uiState.update { currentState ->
            currentState.copy(positions = positions)
        }
    }

    /** Set user name to uiState */
    fun setSignUpUserName(name: String) {
        _uiState.update { currentState ->
            currentState.copy(name = name)
        }
    }

    /** Set user email to uiState */
    fun setSignUpUserEmail(email: String) {
        _uiState.update { currentState ->
            currentState.copy(email = email)
        }
    }

    /** Set user phone to uiState */
    fun setSignUpUserPhone(phone: String) {
        _uiState.update { currentState ->
            currentState.copy(phone = phone)
        }
    }

    /** Set user selected position to uiState */
    fun setSignUpUserPosition(position: Position) {
        _uiState.update { currentState ->
            currentState.copy(position = position)
        }
    }

    /** Set user photo to uiState */
    fun setSignUpUserPhotoUri(phone: Uri) {
        _uiState.update { currentState ->
            currentState.copy(photoUri = phone)
        }
    }

    /** Validate all fields that required to sign up new user */
    fun validateInsertedData() {
        // Validate name
        val isNameError = uiState.value.name.isEmpty()

        // Validate email
        val emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        val isEmailError =
            uiState.value.email.isEmpty() || !uiState.value.email.matches(emailPattern.toRegex())

        // Validate phone
        val phonePattern = """d{10}"""
        val isPhoneError =
            uiState.value.phone.isEmpty() || uiState.value.phone.length != 10

        // Validate photo
        val isPhotoError = uiState.value.photoUri == null

        _uiState.update { currentState ->
            currentState.copy(
                isNameError = isNameError,
                isEmailError = isEmailError,
                isPhoneError = isPhoneError,
                isPhotoError = isPhotoError
            )
        }
    }
}