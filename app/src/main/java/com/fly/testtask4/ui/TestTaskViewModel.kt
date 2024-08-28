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
import com.fly.testtask4.network.ResponseException
import com.fly.testtask4.network.Result
import com.fly.testtask4.network.model.GetUsersResponse
import com.fly.testtask4.network.model.PositionsResponse
import com.fly.testtask4.network.model.SetUserResponse
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

    /**
     * Initiates the sign-up process for a new user by first retrieving an authorization token and then sending
     * the user data to the server.
     *
     * This function works asynchronously using `viewModelScope` and `launch` to handle the process:
     * 1. It first retrieves an authorization token using the `apiRepository.getToken()` function.
     * 2. If the token retrieval is successful, the function proceeds to send the user data (`signUpUser`)
     *    to the server using the `apiRepository.setUser()` function.
     * 3. The result of the user sign-up request is posted to `responseMutableLiveData`.
     * 4. In case of an error during token retrieval, the error is posted to `responseMutableLiveData`.
     *
     * @param context The context used for making network requests.
     * @param responseMutableLiveData A `MutableLiveData` object used to post the result of the sign-up process,
     *                                either success or error.
     * @param signUpUser The user data (`UserModel`) that needs to be signed up.
     */
    fun signUpNewUserRequests(
        context: Context, responseMutableLiveData: MutableLiveData<Result<SetUserResponse>>,
        signUpUser: UserModel
    ) {
        viewModelScope.launch {
            when (val tokenResponse = apiRepository.getToken()) {
                is Result.Success -> {
                    if (tokenResponse.data.success) {
                        val token = tokenResponse.data.token

                        signUpApi(
                            context = context,
                            token = token,
                            responseMutableLiveData = responseMutableLiveData,
                            signUpUser = signUpUser
                        )
                    }
                }

                is Result.Error -> {
                    responseMutableLiveData.value =
                        Result.Error(exception = ResponseException(cause = Exception()))
                }

                else -> {

                }
            }
        }
    }

    /** Process signUpUser API */
    private fun signUpApi(
        context: Context,
        token: String,
        responseMutableLiveData: MutableLiveData<Result<SetUserResponse>>,
        signUpUser: UserModel
    ) {
        viewModelScope.launch {
            when (val signUpResponse = apiRepository.setUser(
                context = context,
                token = token,
                userModel = signUpUser
            )) {
                // Success case
                is Result.Success -> {
                    if (signUpResponse.data.success) {
                        responseMutableLiveData.value = signUpResponse
                    } else {
                        responseMutableLiveData.value =
                            Result.Error(exception = ResponseException(cause = Exception()))
                    }
                }

                // Error case
                else -> {
                    responseMutableLiveData.value =
                        Result.Error(exception = ResponseException(cause = Exception()))
                }
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
    fun setSignUpUserPhotoUri(photo: Uri) {
        _uiState.update { currentState ->
            currentState.copy(photoUri = photo.toString())
        }
    }

    /** Validate all fields that required to sign up new user
     *  If all validate passed will return true*/
    fun validateInsertedData(): Boolean {
        // Validate name
        val isNameError = uiState.value.name.isEmpty()

        // Validate email
        val emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        val isEmailError =
            uiState.value.email.isEmpty() || !uiState.value.email.matches(emailPattern.toRegex())

        // Validate phone
        val isPhoneError =
            uiState.value.phone.isEmpty()
                    || uiState.value.phone.length != 10
                    || !uiState.value.phone.startsWith("0")

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

        return !(isNameError || isEmailError || isPhoneError || isPhotoError)
    }
}
