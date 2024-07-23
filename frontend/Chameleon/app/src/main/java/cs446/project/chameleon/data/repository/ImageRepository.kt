package cs446.project.chameleon.data.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.gson.Gson
import cs446.project.chameleon.Constants.BASE_URL
import cs446.project.chameleon.data.model.Color
import cs446.project.chameleon.data.model.ImageResponse
import cs446.project.chameleon.data.remote.ApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

class ImageRepository {
    private val apiService: ApiService

    init {
        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }


    // TODO test the bitmaps in-app!!
    suspend fun getImageBitmap(authToken:String, imageHash: String): Bitmap {
        val responseBody = apiService.getImage("Bearer $authToken", imageHash)

        return BitmapFactory.decodeStream(responseBody.byteStream())
    }

    suspend fun getImageList(authToken:String, hash: String): ImageResponse {
        return apiService.getImageList("Bearer $authToken", hash)
    }

    suspend fun postImage(authToken: String, bitmapFile: File, colors: List<Color>): ImageResponse {
        // file
        val file = bitmapFile
        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("file", file.name, requestFile)

        // color
        val gson = Gson()
        val colorsJson = gson.toJson(colors)
        val colorsRequestBody = colorsJson.toRequestBody("text/plain".toMediaTypeOrNull())

        println(filePart)
        println(colorsRequestBody)
        return apiService.postImage("Bearer $authToken", filePart, colorsRequestBody)
    }

}
