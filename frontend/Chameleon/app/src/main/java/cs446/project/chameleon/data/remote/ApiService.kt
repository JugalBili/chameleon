package cs446.project.chameleon.data.remote

import cs446.project.chameleon.data.model.Favorite
import cs446.project.chameleon.data.model.History
import cs446.project.chameleon.data.model.ImageResponse
import cs446.project.chameleon.data.model.LoginRequest
import cs446.project.chameleon.data.model.LoginResponse
import cs446.project.chameleon.data.model.Paint
import cs446.project.chameleon.data.model.RegisterRequest
import cs446.project.chameleon.data.model.Token
import cs446.project.chameleon.data.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    // user endpoints
    @GET("user/login-token")
    suspend fun getLoginToken(@Header("Authorization") token: String): User

    @POST("user/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @POST("user/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Token


    // image endpoints
    @GET("image/{image_hash}")
    suspend fun getImage(@Header("Authorization") token: String, @Path("image_hash") imageHash: String): ResponseBody

    @GET("image/list/{hash}")
    suspend fun getImageList(@Header("Authorization") token: String, @Path("hash") hash: String): ImageResponse

    @Multipart
    @POST("image")
    suspend fun postImage(@Header("Authorization") token: String,
                          @Part file: MultipartBody.Part,
                          @Part("colors") colors: RequestBody
    ):ImageResponse


    // history endpoints
    @GET("history")
    suspend fun getHistory(@Header("Authorization") token: String): History


    // favorite endpoints
    @GET("favorite")
    suspend fun getFavorite(@Header("Authorization") token: String): List<Favorite>

    @POST("favorite")
    suspend fun postFavorite(@Header("Authorization") token: String, @Body favorite: Favorite): Unit

    @DELETE("favorite/{paint_id}")
    suspend fun deleteFavorite(@Header("Authorization") token: String, @Path("paint_id") paintId: String): Unit


    // gallery endpoints


}