package com.fly.testtask4.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.InputStream

/** Converter for inputStream images */
class InputStreamConverter {

    /**
     * Converts an image from a given URI to JPEG format and creates a `MultipartBody.Part` for HTTP requests.
     *
     * This function takes an image URI, reads the image as a PNG (or any other format), converts it to JPEG format,
     * and packages it into a `MultipartBody.Part` for sending in an HTTP multipart request.
     *
     * @param context The context used to access the content resolver.
     * @param imageUri The URI of the image to be converted to JPEG.
     * @return A `MultipartBody.Part` containing the image data in JPEG format, ready to be sent in a multipart request.
     *
     * @throws IOException if there is an error while reading the image or closing the output stream.
     */
    fun convertToJPEG(context: Context, imageUri: Uri): MultipartBody.Part {

        val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)

        // Convert InputStream to Bitmap
        val bitmap = BitmapFactory.decodeStream(inputStream)

        // Create a ByteArrayOutputStream to save JPEG
        val outputStream = ByteArrayOutputStream()

        // Compress the image in JPEG and write it to the ByteArrayOutputStream
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

        // Get the image bytes in JPEG format
        val jpegBytes = outputStream.toByteArray()

        // Create a RequestBody to send via Multipart
        val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), jpegBytes)

        // Create MultipartBody.Part to send
        val photoBody = MultipartBody.Part.createFormData(
            "photo", "photo.jpg", requestFile
        )

        // Close the streams
        outputStream.close()

        return photoBody
    }
}
