package cs446.project.chameleon.data.repository

import cs446.project.chameleon.Constants.BASE_URL
import cs446.project.chameleon.data.model.Favorite
import cs446.project.chameleon.data.remote.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FavoriteRepository {
    private val apiService: ApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    suspend fun getFavorites(authToken: String): List<Favorite> {
        val favoriteResponse = apiService.getFavorite("Bearer $authToken")
        return favoriteResponse.favorites
    }

    suspend fun postFavorite(authToken: String, favorite: Favorite): Unit {
        return apiService.postFavorite("Bearer $authToken", favorite)
    }

    suspend fun deleteFavorite(authToken: String, paintId: String): Unit {
        apiService.deleteFavorite("Bearer $authToken", paintId)
    }

}
