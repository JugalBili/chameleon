package cs446.project.chameleon.data.model

data class LoginRequest (
    val email: String,
    val password: String // make sure this is hashed
)
