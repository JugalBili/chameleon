package cs446.project.chameleon.data.repository

import cs446.project.chameleon.Constants.BASE_URL
import cs446.project.chameleon.data.model.Favorite
import cs446.project.chameleon.data.model.History
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
        return apiService.getFavorite("Bearer $authToken")
    }

    suspend fun postFavorite(authToken: String, favorite: Favorite): Unit {
        apiService.postFavorite(authToken, favorite)
    }

    suspend fun deleteFavorite(authToken: String, paintId: String): Unit {
        apiService.deleteFavorite(authToken, paintId)
    }

}