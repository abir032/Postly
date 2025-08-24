package com.example.postly.Model.Types

//User data model
data class User(
    val id: Int = 0,
    val email: String,
    val createdAt: Long,
    val lastLogin: Long?
)

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: User) : AuthState()
    data class Error(val error: AppError) : AuthState()  // Use AppError instead of String
}

//LoginRequestModel
data class LoginRequest(
    val email: String,
    val password: String
)

//Signup request model
data class RegisterRequest(
    val email: String,
    val password: String,
    val confirmPassword: String
)