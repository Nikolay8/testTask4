package com.fly.testtask4.ui

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fly.testtask4.R
import com.fly.testtask4.network.repository.ApiRepository
import com.fly.testtask4.ui.screens.InternetErrorScreen
import com.fly.testtask4.ui.screens.LaunchScreen
import com.fly.testtask4.ui.screens.SignUpErrorScreen
import com.fly.testtask4.ui.screens.SignUpSuccessScreen
import com.fly.testtask4.ui.screens.UsersScreen
import com.fly.testtask4.ui.theme.TestTask4Theme

/**
 * enum values that represent the screens in the app
 */
enum class AppScreen {
    LaunchScreen, InternetErrorScreen, UsersScreen, SignUpSuccessScreen, SignUpErrorScreen
}

@Composable
fun TestTaskApp(
    activity: Activity,
    viewModel: TestTaskViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
    apiRepository: ApiRepository,
    uploadPhotoClick: () -> Unit
) {

    LaunchedEffect(key1 = Unit) {
        viewModel.setApiRepository(apiRepository)
    }

    Surface(
        color = colorResource(id = R.color.white),
        modifier = Modifier.fillMaxSize(),
    ) {
        TestTask4Theme {
            NavHost(
                navController = navController,
                startDestination = AppScreen.LaunchScreen.name,
                modifier = Modifier
                    .fillMaxSize()
            ) {

                // Launch screen
                composable(route = AppScreen.LaunchScreen.name) {
                    LaunchScreen(onNextButtonClicked = {
                        navController.navigate(AppScreen.UsersScreen.name)
                    }, onInternetError = {
                        navController.navigate(AppScreen.InternetErrorScreen.name)
                    }, viewModel = viewModel)
                }


                // InternetError screen
                composable(route = AppScreen.InternetErrorScreen.name, enterTransition = {
                    enterTransition()
                }, exitTransition = {
                    exitTransition()
                }) {
                    InternetErrorScreen(onNextButtonClicked = {
                        navController.navigate(AppScreen.UsersScreen.name)
                    }, viewModel = viewModel)
                }

                // Users screen
                composable(route = AppScreen.UsersScreen.name, enterTransition = {
                    enterTransition()
                }, exitTransition = {
                    exitTransition()
                }) {
                    // Disable back press
                    BackHandler(true) {
                        activity.finish()
                    }

                    UsersScreen(
                        onUsersClick = {
                            navController.navigate(AppScreen.InternetErrorScreen.name)
                        },
                        onSignUpClick = {
                            navController.navigate(AppScreen.UsersScreen.name)
                        },
                        uploadPhotoClick = {
                            uploadPhotoClick.invoke()
                        },
                        onSuccessSingUp = {
                            navController.navigate(AppScreen.SignUpSuccessScreen.name)
                        },
                        onErrorSingUp = {
                            navController.navigate(AppScreen.SignUpErrorScreen.name)
                        },
                        viewModel = viewModel
                    )
                }

                // Success sign up screen
                composable(route = AppScreen.SignUpSuccessScreen.name, enterTransition = {
                    enterTransition()
                }, exitTransition = {
                    exitTransition()
                }) {
                    BackHandler(true) {
                        navController.navigate(AppScreen.LaunchScreen.name) {
                            popUpTo(AppScreen.LaunchScreen.name) { inclusive = true }
                        }
                    }

                    SignUpSuccessScreen(
                        onNextButtonClicked = {
                            navController.navigate(AppScreen.LaunchScreen.name) {
                                popUpTo(AppScreen.LaunchScreen.name) { inclusive = true }
                            }
                        },
                        viewModel = viewModel
                    )
                }

                // Error sign up screen
                composable(route = AppScreen.SignUpErrorScreen.name, enterTransition = {
                    enterTransition()
                }, exitTransition = {
                    exitTransition()
                }) {
                    BackHandler(true) {
                        navController.popBackStack()
                    }

                    SignUpErrorScreen(
                        onBackPressed = {
                            navController.popBackStack()
                        },
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

/** Enter transition animation for navigation */

val enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
    fadeIn(
        animationSpec = tween(
            300, easing = LinearEasing
        )
    ) + slideIntoContainer(
        animationSpec = tween(300, easing = EaseIn),
        towards = AnimatedContentTransitionScope.SlideDirection.Start
    )
}

/** Exit transition animation for navigation */

val exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
    fadeOut(
        animationSpec = tween(
            300, easing = LinearEasing
        )
    ) + slideOutOfContainer(
        animationSpec = tween(300, easing = EaseOut),
        towards = AnimatedContentTransitionScope.SlideDirection.End
    )
}