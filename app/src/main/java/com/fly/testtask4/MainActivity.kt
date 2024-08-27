package com.fly.testtask4

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.fly.testtask4.network.ApiManager
import com.fly.testtask4.network.repository.ApiRepositoryImpl
import com.fly.testtask4.ui.TestTaskApp
import com.fly.testtask4.ui.TestTaskViewModel
import com.fly.testtask4.ui.theme.TestTask4Theme
import com.github.dhaval2404.imagepicker.ImagePicker

class MainActivity : ComponentActivity() {

    private lateinit var testTaskViewModel: TestTaskViewModel

    companion object {
        const val MAX_IMAGE_SIZE_KB = 5120
        const val IMAGE_SIZE = 1080
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_TestTask4)
        super.onCreate(savedInstanceState)

        testTaskViewModel = TestTaskViewModel(application)

        setContent {
            TestTask4Theme {
                TestTaskApp(
                    viewModel = testTaskViewModel,
                    apiRepository = ApiRepositoryImpl(
                        apiService = ApiManager().provideApiService(
                            context = this
                        )
                    ),
                    uploadPhotoClick = {
                        imagePickerImp()
                    }
                )
            }
        }
    }

    /** Setup imagePicker */
    private fun imagePickerImp() {
        ImagePicker.with(this)
            .compress(MAX_IMAGE_SIZE_KB) //Final image size will be less than 5 MB
            .maxResultSize(
                width = IMAGE_SIZE,
                height = IMAGE_SIZE
            )  //Final image resolution will be less than 1080 x 1080
            .createIntent { intent ->
                startForProfileImageResult.launch(intent)
            }
    }

    /** Result for picture/photo picker */
    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == RESULT_OK) {
                val fileUri = data?.data!!
                testTaskViewModel.setSignUpUserPhotoUri(fileUri)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, getString(R.string.task_cancelled), Toast.LENGTH_SHORT).show()
            }
        }
}
