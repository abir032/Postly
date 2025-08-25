package com.example.postly.Model.DataModels

import com.example.postly.Utils.AppError

//User data model
data class User(
    val id: Int = 0,
    val email: String,
    val createdAt: Long,
    val lastLogin: Long?
)

sealed class Result<out T> {
    object Idle: Result<Nothing>()
    object Loading : Result<Nothing>()
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val error: AppError) : Result<Nothing>()
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

//Post data
data class Post (
    val id: Int = 0,
    val title: String,
    val body: String,
    val isFavorite: Boolean,
)
