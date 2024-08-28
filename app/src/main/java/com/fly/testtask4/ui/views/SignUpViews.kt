package com.fly.testtask4.ui.views

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import com.fly.testtask4.R
import com.fly.testtask4.data.UserModel
import com.fly.testtask4.network.Result
import com.fly.testtask4.network.model.PositionsResponse
import com.fly.testtask4.network.model.SetUserResponse
import com.fly.testtask4.ui.TestTaskViewModel
import com.fly.testtask4.ui.theme.TestTask4Theme
import com.fly.testtask4.util.NanpVisualTransformation

/** Views that will shown for signup flow */
@Composable
fun SignUpViews(
    viewModel: TestTaskViewModel,
    uploadPhotoAction: () -> Unit,
    onSuccessSingUp: () -> Unit,
    onErrorSingUp: () -> Unit
) {
    // Get the current LifecycleOwner
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    // Collect the UI state from the ViewModel
    val uiState by viewModel.uiState.collectAsState()

    var phoneNumber by rememberSaveable { mutableStateOf("") }
    val numericRegex = Regex("[^0-9]")
    val defaultErrorString = stringResource(id = R.string.default_error)

    LaunchedEffect(key1 = Unit) {
        // MutableLiveData for get all positions for show in RadioButtons views
        val responseMutableLiveData = MutableLiveData<Result<PositionsResponse>>()

        responseMutableLiveData.observe(lifecycleOwner) { positionResponse ->
            when (positionResponse) {
                is Result.Success -> {
                    val data = positionResponse.data
                    if (data.success) {
                        viewModel.setPositionsList(data.positions)
                    }
                }

                is Result.Error -> {
                    Toast.makeText(context, defaultErrorString, Toast.LENGTH_LONG).show()
                }

                else -> {
                    // Other cases are not handled explicitly
                }
            }
        }
        viewModel.getPositions(responseMutableLiveData)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.white))
            .verticalScroll(rememberScrollState())
    ) {

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(id = R.color.yellow_main_color))
                .padding(vertical = 10.dp)
                .padding(horizontal = 16.dp),
            text = stringResource(id = R.string.working_with_post_request),
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 20.sp,
                lineHeight = 24.sp,
                fontFamily = FontFamily(Font(R.font.nunito_sans_regular)),
                fontWeight = FontWeight(400),
                color = colorResource(id = R.color.black),
            )
        )

        // Name text field
        OutlinedTextField(
            colors = OutlinedTextFieldDefaults.colors(
                focusedLabelColor = colorResource(id = R.color.grey_border_color),
                focusedBorderColor = colorResource(id = R.color.grey_border_color),
                unfocusedBorderColor = colorResource(id = R.color.grey_border_color),
                focusedTextColor = colorResource(id = R.color.grey_color_text),
            ),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 20.dp)
                .fillMaxWidth(),
            supportingText = {
                if (uiState.isNameError) {
                    Text(stringResource(id = R.string.required_field))
                }
            },
            value = uiState.name,
            isError = uiState.isNameError,
            onValueChange = { name -> viewModel.setSignUpUserName(name) },
            label = { Text(stringResource(id = R.string.your_name)) },
        )

        // Email text field
        OutlinedTextField(
            colors = OutlinedTextFieldDefaults.colors(
                focusedLabelColor = colorResource(id = R.color.grey_border_color),
                focusedBorderColor = colorResource(id = R.color.grey_border_color),
                unfocusedBorderColor = colorResource(id = R.color.grey_border_color),
                focusedTextColor = colorResource(id = R.color.grey_color_text),
            ),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 10.dp)
                .fillMaxWidth(),
            supportingText = {
                if (uiState.isEmailError) {
                    Text(stringResource(id = R.string.email_error))
                }
            },
            value = uiState.email,
            isError = uiState.isEmailError,
            onValueChange = { email -> viewModel.setSignUpUserEmail(email) },
            label = { Text(stringResource(id = R.string.email)) },
        )

        // Phone text field
        OutlinedTextField(
            colors = OutlinedTextFieldDefaults.colors(
                focusedLabelColor = colorResource(id = R.color.grey_border_color),
                focusedBorderColor = colorResource(id = R.color.grey_border_color),
                unfocusedBorderColor = colorResource(id = R.color.grey_border_color),
                focusedTextColor = colorResource(id = R.color.grey_color_text),
            ),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 10.dp)
                .fillMaxWidth(),
            supportingText = {
                if (uiState.isPhoneError) {
                    Text(stringResource(id = R.string.phone_label))
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            value = uiState.phone,
            isError = uiState.isPhoneError,
            visualTransformation = NanpVisualTransformation(),
            onValueChange = { phone ->
                // Remove non-numeric characters.
                val stripped = numericRegex.replace(phone, "")
                phoneNumber = if (stripped.length >= 10) {
                    stripped.substring(0..9)
                } else {
                    stripped
                }
                viewModel.setSignUpUserPhone(phoneNumber)
            },
            label = { Text(stringResource(id = R.string.phone)) },
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .padding(horizontal = 16.dp),
            text = stringResource(id = R.string.select_your_position),
            textAlign = TextAlign.Left,
            style = TextStyle(
                fontSize = 18.sp,
                lineHeight = 24.sp,
                fontFamily = FontFamily(Font(R.font.nunito_sans_regular)),
                fontWeight = FontWeight(400),
                color = colorResource(id = R.color.black),
            )
        )

        val positionList = uiState.positions

        if (positionList.isNotEmpty()) {
            val (selectedOption, onOptionSelected) = remember { mutableStateOf(positionList[0]) }

            // Position radio buttons
            positionList.forEach { position ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .selectable(selected = (position == selectedOption), onClick = {
                            onOptionSelected(position)
                            viewModel.setSignUpUserPosition(position)
                        })
                        .padding(horizontal = 16.dp)
                ) {
                    RadioButton(colors = RadioButtonColors(
                        selectedColor = colorResource(id = R.color.blue_second_color),
                        unselectedColor = colorResource(id = R.color.blue_second_color),
                        disabledSelectedColor = colorResource(id = R.color.blue_second_color),
                        disabledUnselectedColor = colorResource(id = R.color.blue_second_color)
                    ),
                        selected = (position == selectedOption),
                        onClick = {
                            onOptionSelected(position)
                            viewModel.setSignUpUserPosition(position)
                        })
                    Text(
                        text = position.name,
                        style = MaterialTheme.typography.body1.merge(),
                        modifier = Modifier
                            .padding(start = 10.dp, top = 10.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth()
        ) {
            // Photo text field
            OutlinedTextField(modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 10.dp)
                .fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedLabelColor = colorResource(id = R.color.grey_border_color),
                    focusedBorderColor = colorResource(id = R.color.grey_border_color),
                    unfocusedBorderColor = colorResource(id = R.color.grey_border_color),
                    focusedTextColor = colorResource(id = R.color.grey_color_text),
                ),
                supportingText = {
                    if (uiState.isPhotoError) {
                        Text(stringResource(id = R.string.photo_error))
                    }
                },
                value = if (uiState.photoUri != null) uiState.photoUri.toString() else "",
                onValueChange = {},
                isError = uiState.isPhotoError,
                readOnly = true,
                trailingIcon = {
                    Text(
                        text = stringResource(id = R.string.upload),
                        color = colorResource(id = R.color.blue_second_color),
                        modifier = Modifier.padding(end = 20.dp)
                    )

                },
                label = {
                    Row {
                        Text(stringResource(id = R.string.upload_your_photo))
                    }
                })

            Box(modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .clickable { uploadPhotoAction.invoke() })
        }

        Button(
            modifier = Modifier
                .padding(top = 30.dp, bottom = 30.dp)
                .height(50.dp)
                .padding(horizontal = 22.dp)
                .wrapContentWidth()
                .align(Alignment.CenterHorizontally),
            onClick = {
                if (viewModel.validateInsertedData()) {
                    // MutableLiveData for get token
                    val responseMutableLiveData = MutableLiveData<Result<SetUserResponse>>()

                    val photoUri = uiState.photoUri ?: return@Button

                    // Create model for request
                    val signUpUserModel = UserModel(
                        id = 0,
                        name = uiState.name,
                        email = uiState.email,
                        phone = "38" + uiState.phone,
                        positionId = uiState.position.id,
                        position = uiState.position.name,
                        photo = photoUri,
                        registrationTimestamp = 0
                    )

                    // Handling sign up flows
                    responseMutableLiveData.observe(lifecycleOwner) { response ->
                        when (response) {
                            is Result.Success -> {
                                onSuccessSingUp.invoke() // Success
                            }

                            else -> {
                                onErrorSingUp.invoke() // Error
                            }
                        }
                    }

                    // Call get token request
                    viewModel.signUpNewUserRequests(
                        context = context,
                        responseMutableLiveData,
                        signUpUser = signUpUserModel
                    )
                }
            },
            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.yellow_main_color)),
            shape = RoundedCornerShape(30.dp),
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 20.dp),
                text = stringResource(id = R.string.sign_up),
                style = TextStyle(
                    fontSize = 18.sp,
                    lineHeight = 24.sp,
                    fontFamily = FontFamily(Font(R.font.nunito_sans_regular)),
                    fontWeight = FontWeight(700),
                    color = colorResource(id = R.color.black),
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpViewsPrev() {
    TestTask4Theme {
        SignUpViews(viewModel = TestTaskViewModel(application = Application()),
            uploadPhotoAction = {},
            onSuccessSingUp = {},
            onErrorSingUp = {})
    }
}
