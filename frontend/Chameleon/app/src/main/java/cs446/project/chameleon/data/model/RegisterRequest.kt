package cs446.project.chameleon.data.model

data class RegisterRequest (
    val email: String,
    val password: String,
    val firstname: String,
    val lastname: String
)