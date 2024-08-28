package com.fly.testtask4.ui.screens

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import coil.compose.rememberAsyncImagePainter
import com.fly.testtask4.R
import com.fly.testtask4.data.TestTaskUiState
import com.fly.testtask4.data.UserModel
import com.fly.testtask4.network.Result
import com.fly.testtask4.network.model.GetUsersResponse
import com.fly.testtask4.ui.TestTaskViewModel
import com.fly.testtask4.ui.theme.TestTask4Theme
import com.fly.testtask4.ui.views.CustomBottomNavigation
import com.fly.testtask4.ui.views.EmptyUsersListViews
import com.fly.testtask4.ui.views.SignUpViews


/**
 * Composable function that displays the Users screen.
 *
 * This screen handles user interactions and displays a list of users with pagination.
 * It also observes the scroll state of the list to trigger loading more data when the user
 * reaches the bottom of the list.
 *
 * @param onNextButtonClicked Callback invoked when the next button is clicked.
 * @param onUsersClick Callback invoked when a user item is clicked.
 * @param onSignUpClick Callback invoked when the sign-up button is clicked.
 * @param onSuccessSingUp Callback invoked when an success sign up triggered.
 * @param onErrorSingUp Callback invoked when an error sign up triggered.
 * @param viewModel The ViewModel that provides the UI state and handles business logic for this screen.
 */
@Composable
fun UsersScreen(
    onUsersClick: () -> Unit,
    onSignUpClick: () -> Unit,
    uploadPhotoClick: () -> Unit, onSuccessSingUp: () -> Unit, onErrorSingUp: () -> Unit,
    viewModel: TestTaskViewModel
) {

    // Get the current LifecycleOwner
    val lifecycleOwner = LocalLifecycleOwner.current
    // Collect the UI state from the ViewModel
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val defaultErrorString = stringResource(id = R.string.default_error)

    var isShowSignUpScreen by remember {
        mutableStateOf(false)
    }

    // State to keep track of the current page for pagination
    var page by remember {
        mutableIntStateOf(1)
    }

    // State for managing the scroll position of the LazyList
    val listState = rememberLazyListState()
    // State to indicate whether data is currently being loaded
    var loading by remember { mutableStateOf(false) }

    // State to determine if this is the first time the screen is opened
    var firstOpen by remember {
        mutableStateOf(true)
    }

    // State to track if there are more pages to load
    var notLastPage by remember {
        mutableStateOf(true)
    }


    // observe list scrolling
    val reachedBottom: Boolean by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem?.index != 0 && lastVisibleItem?.index == listState.layoutInfo.totalItemsCount - 1
        }
    }

    LaunchedEffect(reachedBottom) {
        // MutableLiveData for holding the result of the user list request
        val responseMutableLiveData = MutableLiveData<Result<GetUsersResponse>>()
        // Observing the LiveData for changes and handling the response
        responseMutableLiveData.observe(lifecycleOwner) { statusResponse ->
            loading = false
            when (statusResponse) {
                is Result.Success -> {
                    val data = statusResponse.data
                    val totalPages = data.totalPages

                    if (data.success) {
                        // Increment page if there are more pages, otherwise mark as last page
                        if (page + 1 <= totalPages) {
                            page += 1
                        } else {
                            notLastPage = false
                        }
                        viewModel.setUserList(statusResponse.data.users)
                    }
                }

                else -> {
                    Toast.makeText(context, defaultErrorString, Toast.LENGTH_LONG).show()
                }
            }
        }

        // Trigger data loading if the user has reached the bottom of the list or if it's the first open
        if (reachedBottom && notLastPage || firstOpen) {
            firstOpen = false
            loading = true
            viewModel.getUserList(
                responseMutableLiveData = responseMutableLiveData, page = page
            )
        }
    }

    Column {
        Box(modifier = Modifier.weight(1f)) {

            if (isShowSignUpScreen) {
                SignUpViews(viewModel = viewModel, uploadPhotoAction = {
                    uploadPhotoClick.invoke()
                }, onSuccessSingUp = {
                    onSuccessSingUp.invoke()
                }, onErrorSingUp = {
                    onErrorSingUp.invoke()
                })
            } else {
                // Based on uiState show empty user list views or lazyList with users info
                uiState.usersList.let {
                    if (it.isNotEmpty()) {
                        UserLazyList(
                            loading = loading,
                            listState = listState,
                            uiState = uiState,
                            onUsersClick = onUsersClick,
                            onSignUpClick = onSignUpClick
                        )
                    } else {
                        EmptyUsersListViews()
                    }
                }
            }
        }

        Box {
            CustomBottomNavigation(onUsersClick = {
                isShowSignUpScreen = false
            }, onSignUpClick = {
                isShowSignUpScreen = true
            })
        }
    }
}

/** Users lazyList */
@Composable
fun UserLazyList(
    loading: Boolean,
    listState: LazyListState,
    uiState: TestTaskUiState,
    onUsersClick: () -> Unit,
    onSignUpClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.white))
    ) {

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(id = R.color.yellow_main_color))
                .padding(vertical = 10.dp)
                .padding(horizontal = 16.dp),
            text = stringResource(id = R.string.working_with_get_request),
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 20.sp,
                lineHeight = 24.sp,
                fontFamily = FontFamily(Font(R.font.nunito_sans_regular)),
                fontWeight = FontWeight(400),
                color = colorResource(id = R.color.black),
            )
        )

        Box(
            Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            LazyColumn(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(color = colorResource(id = R.color.white)),
                state = listState,
            ) {
                items(count = uiState.usersList.size, key = {
                    uiState.usersList[it]
                }, itemContent = { index ->
                    val appData = uiState.usersList[index]
                    UserItem(item = appData)
                })
            }

            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .height(50.dp)
                        .padding(top = 10.dp, bottom = 10.dp)
                        .align(Alignment.BottomCenter),
                    color = colorResource(id = R.color.white),
                    trackColor = colorResource(id = R.color.blue_second_color),
                )
            }
        }
    }
}

/** User item */
@Composable
fun UserItem(item: UserModel) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(start = 22.dp, top = 20.dp, bottom = 12.dp)) {
            item.photo.let {

                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(30.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 16.dp)
            ) {

                Text(
                    modifier = Modifier.wrapContentHeight(),
                    text = item.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontSize = 18.sp,
                        lineHeight = 24.sp,
                        fontFamily = FontFamily(Font(R.font.nunito_sans_regular)),
                        fontWeight = FontWeight(400),
                        color = colorResource(id = R.color.black),
                    )
                )

                Text(
                    modifier = Modifier.wrapContentHeight(),
                    text = item.position,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontFamily = FontFamily(Font(R.font.nunito_sans_regular)),
                        fontWeight = FontWeight(400),
                        color = colorResource(id = R.color.grey_color_text),
                    )
                )

                Text(
                    modifier = Modifier.wrapContentHeight(),
                    text = item.email,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontFamily = FontFamily(Font(R.font.nunito_sans_regular)),
                        fontWeight = FontWeight(400),
                        color = colorResource(id = R.color.black),
                    )
                )

                Text(
                    modifier = Modifier.wrapContentHeight(),
                    text = item.phone,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontFamily = FontFamily(Font(R.font.nunito_sans_regular)),
                        fontWeight = FontWeight(400),
                        color = colorResource(id = R.color.black),
                    )
                )

            }
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 85.dp, top = 20.dp, end = 16.dp),
            color = colorResource(id = R.color.grey_divider)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UsersEmptyScreenPrev() {
    TestTask4Theme {

        val viewModel = TestTaskViewModel(application = Application())

        val mockUserList = listOf(
            UserModel(
                id = 30,
                name = "test name",
                email = "test@email.com",
                phone = "+38090606060",
                positionId = 15515151,
                registrationTimestamp = 1537777441,
                photo = "",
                position = "Manager"
            ), UserModel(
                id = 30,
                name = "test name1",
                email = "test1@email.com",
                phone = "+38090606060",
                positionId = 15515151,
                registrationTimestamp = 1537777441,
                photo = "",
                position = "Manager"
            )
        )

        viewModel.setUserList(mockUserList)

        UsersScreen(
            onUsersClick = {},
            onSignUpClick = {}, uploadPhotoClick = {}, onSuccessSingUp = {}, onErrorSingUp = {},
            viewModel = viewModel
        )
    }
}
