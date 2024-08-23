package com.fly.testtask4.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fly.testtask4.R

/** Views that will shown if no users loaded */
@Composable
fun EmptyUsersListViews(
    onUsersClick: () -> Unit, onSignUpClick: () -> Unit
) {
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

        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Column {
                Image(
                    painter = painterResource(
                        id = R.drawable.ic_no_users
                    ),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    alignment = Alignment.Center
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 30.dp),
                    text = stringResource(id = R.string.there_are_no_users_yet),
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 20.sp,
                        lineHeight = 24.sp,
                        fontFamily = FontFamily(Font(R.font.nunito_sans_regular)),
                        fontWeight = FontWeight(400),

                        color = colorResource(id = R.color.black),
                    )
                )
            }
        }

        CustomBottomNavigation(onUsersClick = onUsersClick, onSignUpClick = onSignUpClick)
    }
}
