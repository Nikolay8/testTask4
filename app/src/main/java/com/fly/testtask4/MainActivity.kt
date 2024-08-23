package com.fly.testtask4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.fly.testtask4.network.ApiManager
import com.fly.testtask4.network.repository.ApiRepositoryImpl
import com.fly.testtask4.ui.TestTaskApp
import com.fly.testtask4.ui.theme.TestTask4Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TestTask4Theme {
                TestTaskApp(
                    apiRepository = ApiRepositoryImpl(
                        apiService = ApiManager().provideApiService(
                            context = this
                        )
                    ),
                    finishApp = {

                    }
                )
            }
        }
    }
}
