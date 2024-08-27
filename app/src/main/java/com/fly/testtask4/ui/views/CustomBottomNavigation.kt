package com.fly.testtask4.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
import com.fly.testtask4.R
import com.fly.testtask4.ui.theme.TestTask4Theme

/** Bottom navigation with two btns: "Users", "Sign up" */
@Composable
fun CustomBottomNavigation(
    onUsersClick: () -> Unit,
    onSignUpClick: () -> Unit
) {

    var usersSelected by remember {
        mutableStateOf(false)
    }
    var signUpSelected by remember {
        mutableStateOf(false)
    }

    var usersColor = colorResource(id = R.color.grey_color_text)
    var signUpColor = colorResource(id = R.color.grey_color_text)

    if (usersSelected) {
        usersColor = colorResource(id = R.color.blue_second_color)
    }
    if (signUpSelected) {
        signUpColor = colorResource(id = R.color.blue_second_color)
    }

    androidx.compose.material.BottomNavigation(
        modifier = Modifier, elevation = 0.dp
    ) {
        BottomNavigationItem(icon = {
                Row {
                    Icon(
                        painter = painterResource(
                            id = R.drawable.ic_users
                        ),
                        contentDescription = null,
                        modifier = Modifier.wrapContentWidth(),
                        tint = usersColor
                    )

                    Text(
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(start = 10.dp),
                        text = stringResource(id = R.string.users),
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            fontFamily = FontFamily(Font(R.font.nunito_sans_regular)),
                            fontWeight = FontWeight(600),

                            color = usersColor,
                        )
                    )
                }
            },
            selected = false,
            modifier = Modifier.background(color = colorResource(id = R.color.grey_color)),
            onClick = {
                usersSelected = true
                signUpSelected = false
                onUsersClick.invoke()
            })
        BottomNavigationItem(icon = {
                Row {
                    Icon(
                        painter = painterResource(
                            id = R.drawable.ic_sign_up
                        ),
                        contentDescription = null,
                        modifier = Modifier.wrapContentWidth(),
                        tint = signUpColor
                    )

                    Text(
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(start = 10.dp),
                        text = stringResource(id = R.string.sign_up),
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            fontFamily = FontFamily(Font(R.font.nunito_sans_regular)),
                            fontWeight = FontWeight(600),
                            color = signUpColor,
                        )
                    )
                }
            },
            selected = true,
            modifier = Modifier.background(color = colorResource(id = R.color.grey_color)),
            onClick = {
                usersSelected = false
                signUpSelected = true
                onSignUpClick.invoke()
            })
    }
}

@Preview(showBackground = true)
@Composable
fun UsersEmptyScreenPrev() {
    TestTask4Theme {
        CustomBottomNavigation(
            onUsersClick = {},
            onSignUpClick = {}
        )
    }
}
