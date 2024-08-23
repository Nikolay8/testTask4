package com.fly.testtask4.ui.screens

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fly.testtask4.R
import com.fly.testtask4.ui.TestTaskViewModel
import com.fly.testtask4.ui.theme.TestTask4Theme
import kotlinx.coroutines.delay


@Composable
fun LaunchScreen(
    onNextButtonClicked: () -> Unit, onInternetError: () -> Unit, viewModel: TestTaskViewModel
) {
    val localContext = LocalContext.current

    // Go next in 1 sec
    LaunchedEffect(key1 = Unit) {
        delay(1000)
        if (viewModel.checkInternetConnection(localContext)) {
            onNextButtonClicked.invoke()
        } else {
            onInternetError.invoke()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.yellow_main_color))
            .clickable {
                onNextButtonClicked.invoke()
            },
    ) {
        Image(
            painter = painterResource(
                id = R.drawable.ic_logo
            ),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 70.dp)
                .align(Alignment.CenterHorizontally),
            alignment = Alignment.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LaunchScreenPrev() {
    TestTask4Theme {
        LaunchScreen(onNextButtonClicked = {

        }, onInternetError = {

        }, viewModel = TestTaskViewModel(application = Application()))
    }
}