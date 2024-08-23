package com.fly.testtask4.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.BottomNavigationItem
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
    androidx.compose.material.BottomNavigation(
        elevation = 0.dp,
        contentColor = colorResource(id = R.color.yellow_main_color)
    ) {
        BottomNavigationItem(
            icon = {
                Row {
                    Image(
                        painter = painterResource(
                            id = R.drawable.ic_users
                        ), contentDescription = null, modifier = Modifier
                            .wrapContentWidth(), alignment = Alignment.Center
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

                            color = colorResource(id = R.color.blue_second_color),
                        )
                    )
                }
            },
            selected = false,
            modifier = Modifier.background(color = colorResource(id = R.color.grey_color)),
            onClick = {
                onUsersClick.invoke()
            }
        )
        BottomNavigationItem(
            icon = {
                Row {
                    Image(
                        painter = painterResource(
                            id = R.drawable.ic_sign_up
                        ), contentDescription = null, modifier = Modifier
                            .wrapContentWidth(), alignment = Alignment.Center
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

                            color = colorResource(id = R.color.grey_color_text),
                        )
                    )
                }
            },
            selected = false,
            modifier = Modifier.background(color = colorResource(id = R.color.grey_color)),
            onClick = {
                onSignUpClick.invoke()
            }
        )
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