package com.fly.testtask4.ui.screens

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.fly.testtask4.R
import com.fly.testtask4.ui.TestTaskViewModel
import com.fly.testtask4.ui.theme.TestTask4Theme


/** Screen to show error in addition of new user */
@Composable
fun SignUpErrorScreen(
    onBackPressed: () -> Unit, viewModel: TestTaskViewModel
) {
    val localContext = LocalContext.current

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.white))
            .verticalScroll(rememberScrollState())
    ) {

        val (image, text, button, closeIcon) = createRefs()

        Image(
            painter = painterResource(
                id = R.drawable.ic_error
            ), contentDescription = null, modifier = Modifier
                .size(200.dp)
                .constrainAs(image) {
                    bottom.linkTo(text.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }, alignment = Alignment.Center
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 30.dp)
                .constrainAs(text) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            text = stringResource(id = R.string.email_already_register),
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.nunito_sans_regular)),
                fontWeight = FontWeight(400),
                lineHeight = 24.sp,
                color = colorResource(id = R.color.black),
            )
        )

        Image(
            painter = painterResource(
                id = R.drawable.ic_close
            ), contentDescription = null, modifier = Modifier
                .padding(end = 30.dp, top = 30.dp)
                .size(25.dp)
                .clickable {
                    onBackPressed.invoke()
                }
                .constrainAs(closeIcon) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }, alignment = Alignment.Center
        )

        Button(
            modifier = Modifier
                .padding(top = 30.dp)
                .height(50.dp)
                .padding(horizontal = 22.dp)
                .wrapContentWidth()
                .constrainAs(button) {
                    top.linkTo(text.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            onClick = {
                // Check internet connection
                if (viewModel.checkInternetConnection(localContext)) {
                    // Go to next screen if internet connection available
                    onBackPressed.invoke()
                }
            },
            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.yellow_main_color)),
            shape = RoundedCornerShape(30.dp),
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 20.dp),
                text = stringResource(id = R.string.try_agin),
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
fun SignUpErrorScreenPrev() {
    TestTask4Theme {
        SignUpErrorScreen(
            onBackPressed = {

            }, viewModel = TestTaskViewModel(application = Application())
        )
    }
}
