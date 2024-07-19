package cs446.project.chameleon.data.model

import com.google.gson.annotations.SerializedName

data class Token (
    val token: String,
    @SerializedName("token_type") val tokenType: String
)
