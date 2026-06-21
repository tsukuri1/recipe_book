package ru.course.recipemanager.foundation.remote

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

data class AuthResponse(
    val accessToken: String,
    val tokenType: String = "Bearer",
    val role: String = "USER",
    val name: String = "Пользователь"
)
