package com.example.postly.Model.DataModels

import com.example.postly.Utils.AppError

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

data class LoginRequest(
    val email: String,
    val password: String
)


data class RegisterRequest(
    val email: String,
    val password: String,
    val confirmPassword: String
)


data class Post (
    val id: Int = 0,
    val title: String,
    val body: String,
    var isFavorite: Boolean,
)
