# Project Name 
TestTask4 

## Overview
Test project to display the interaction of Compose UI, Retrofit, ImagePicker.
A list of downloaded users is displayed with the possibility of adding new ones with identification fields and a user image

## Configuration Options

### `Base API Endpoints`
- **Type:** `String`
- **Default:** `https://frontend-test-assignment-api.abz.agency/api/v1/`
- *Description:* Base API endpoint for all requests

### `API token`
- **Type:** `String`
- **Default:** `String value from server`
- *Description:* APi request for get token for adding new user

### `API positions`
- **Type:** `String`
- **Default:** `String value from server`
- *Description:* APi request for get all available positions for new users

### `API @GET user`
- **Type:** `String`
- **Default:** `String value from server`
- *Description:* APi requests  for get all exist users from server

### `API @POST user`
- **Type:** `String`
- **Default:** `String value from server`
- *Description:* APi requests  for add new user to server

### `MAX_IMAGE_SIZE_KB`
- **Type:** `Int`
- **Default:** `5120`
- *Description:* Compress Image so that max image size can be below specified size, KB

### `IMAGE_SIZE`
- **Type:** `Int`
- **Default:** `500`
- *Description:* Max Width and Height of final image, Px

### `HTTP_TIMEOUT`
- **Type:** `Long`
- **Default:** `60`
- *Description:* The timeout duration in seconds for network requests, Sec.

### `App Version`
- **Type:** `Int`
- **Default:** `26`
- *Description:* Min support SDK version

## Dependencies and any external libraries

### `libs.androidx.lifecycle.viewmodel.compose`
- *Description:* For create and use viewModel

### `libs.androidx.constraintlayout.compose`
- *Description:* Using constraintlayout in Compose

### `libs.retrofit`
### `libs.converter.gson`
- *Description:* For APi request using Retrofit with GSON converter

### `libs.library`
### `libs.library.no.op`
- *Description:* For debugging API requests using Chucker

### `libs.coil.compose`
- *Description:* Coil for load the image

### `libs.imagepicker`
- *Description:* For use image picker from camera or gallery

## Troubleshooting tips and common issues.

### Issue: Application crashes on startup
- **Solution:** 
   1. Ensure all dependencies are correctly installed by running `./gradlew clean build`.
   2. Check if the correct API keys are provided in your configuration files.
   3. Review the logcat output for specific error messages.

### Issue: Slow performance during animations
- **Solution:**
   1. Verify that the animations are not being processed on the main thread.
   2. Consider optimizing your layout by reducing nested views.
   3. Use the Android Profiler to identify any performance bottlenecks.

### Issue: Unable to connect to the API
- **Solution:**
   1. Confirm that the `apiBaseUrl` is correctly set and accessible.
   2. Check network connectivity on the device or emulator.
   3. Review API keys and ensure they are correctly configured.

### Issue: Gradle build fails
- **Solution:**
   1. Try clearing the Gradle cache using `./gradlew clean` and then rebuild the project.
   2. Ensure all required SDKs are installed by opening the SDK Manager in Android Studio.
   3. Check for any version mismatches between dependencies and update them as needed.

### Issue: Missing resources error
- **Solution:**
   1. Ensure that all resource files (e.g., images, strings) are correctly placed in the appropriate resource directories.
   2. Check the file naming conventions, as Android is case-sensitive.
   3. Rebuild the project using `./gradlew assembleDebug` to regenerate the resource indexes.

### Issue: App not running on older Android versions
- **Solution:**
   1. Check the `minSdkVersion` in your `build.gradle` file and ensure it supports the target device.
   2. Review any deprecated APIs or features that may not be available on older Android versions.
   3. Test the application on an emulator running the target Android version to replicate the issue.

## Pacakge structure
- **data:** `Consist from global Models files`
- **network:** `Work with API requests`
- - **api:** `ApiService class for all requests`
- - **model:** `Models class for network`
- - **repository:** `Repository for network work process`
- **ui:** `all ui classes`
- - - *screens:** `All compose screens will be there`
- - - *theme:** `App theme`
- - - *views:** `Group of views for use in compose screens`
- **util:** `Collect all utils class for help in app work`


