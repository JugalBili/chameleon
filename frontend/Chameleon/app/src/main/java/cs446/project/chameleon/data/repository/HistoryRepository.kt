package cs446.project.chameleon.data.repository

import cs446.project.chameleon.Constants.BASE_URL
import cs446.project.chameleon.data.model.History
import cs446.project.chameleon.data.remote.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HistoryRepository {
    private val apiService: ApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    suspend fun getHistory(authToken: String): History {
        println(authToken)
        val historyResponse = apiService.getHistory("Bearer $authToken")
        println(historyResponse)
        return historyResponse
    }

}
