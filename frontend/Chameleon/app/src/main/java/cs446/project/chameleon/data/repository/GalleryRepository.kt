package cs446.project.chameleon.data.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.gson.Gson
import cs446.project.chameleon.Constants.BASE_URL
import cs446.project.chameleon.data.model.CreateReviewRequest
import cs446.project.chameleon.data.model.ReviewImage
import cs446.project.chameleon.data.model.Review
import cs446.project.chameleon.data.remote.ApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class GalleryRepository {
    private val apiService: ApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    suspend fun getUserReview(authToken: String, paintId: String): Review {
        return apiService.getReview("Bearer $authToken", paintId)
    }

    suspend fun getAllReviews(authToken: String, paintId: String): List<Review> {
        val allReviewResponse = apiService.getAllReviews("Bearer $authToken", paintId)
        return allReviewResponse.reviews
    }

    suspend fun getImage(authToken: String, paintId: String, imageHash:String): Bitmap {
        val reviewImage = ReviewImage(paintId, imageHash)
        val responseBody = apiService.galleryGetImage("Bearer $authToken", reviewImage)
        return BitmapFactory.decodeStream(responseBody.byteStream())
    }

    suspend fun createReview(authToken: String, bitmapFile: File, paintId: String, review: String): Unit {
        val file = bitmapFile
        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("file", file.name, requestFile)

        // review object
        val createReviewRequest = CreateReviewRequest(paintId, review)
        val gson = Gson()
        val createReviewRequestJson = gson.toJson(createReviewRequest)
        val createReviewRequestBody = createReviewRequestJson.toRequestBody("text/plain".toMediaTypeOrNull())

        apiService.createReview("Bearer $authToken", filePart, createReviewRequestBody)
    }

    suspend fun createImagelessReview(authToken: String, paintId: String, review: String) {
        val createReviewRequest = CreateReviewRequest(paintId, review)
        val gson = Gson()
        val createReviewRequestJson = gson.toJson(createReviewRequest)
        val createReviewRequestBody = createReviewRequestJson.toRequestBody("text/plain".toMediaTypeOrNull())

        apiService.createImagelessReview("Bearer $authToken", createReviewRequestBody)
    }
}
