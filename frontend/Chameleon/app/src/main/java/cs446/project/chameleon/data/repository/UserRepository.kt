package cs446.project.chameleon.data.repository

import cs446.project.chameleon.Constants.BASE_URL
import cs446.project.chameleon.data.model.LoginRequest
import cs446.project.chameleon.data.model.LoginResponse
import cs446.project.chameleon.data.model.RegisterRequest
import cs446.project.chameleon.data.model.Token
import cs446.project.chameleon.data.model.User
import cs446.project.chameleon.data.remote.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class UserRepository {
    private val apiService: ApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }


    // MAKE SURE THE VIEWMODEL DOES CORRECT ERROR HANDLING FOR ALL OF THESE - SEE UserRepositoryTest FOR SOME ERROR EXAMPLES
    suspend fun getUser(authToken: String): User {
        return apiService.getLoginToken("Bearer $authToken")
    }

    suspend fun login(email: String, password: String): LoginResponse {
        val loginRequest = LoginRequest(email, password)
        return apiService.login(loginRequest)
    }

    suspend fun register(email: String, password: String, firstname: String, lastname: String): Token {
        // hash the password ?
        val registerRequest = RegisterRequest(email, password, firstname, lastname)
        return apiService.register(registerRequest)
    }

}
