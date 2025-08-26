
package com.example.postly.Utils

sealed class AppError(
    open val code: String,
    open val userMessage: String,
    open val debugMessage: String? = null
) {
    object UserNotFound : AppError(
        code = "AUTH_001",
        userMessage = "User account not found. Please check your email or register.",
        debugMessage = "No user found with provided email"
    )

    object InvalidPassword : AppError(
        code = "AUTH_002",
        userMessage = "Incorrect password. Please try again.",
        debugMessage = "Password mismatch for existing user"
    )

    object EmailAlreadyRegistered : AppError(
        code = "AUTH_003",
        userMessage = "This email is already registered. Please login instead.",
        debugMessage = "User attempted to register with existing email"
    )

    object DatabaseConnectionError : AppError(
        code = "DB_001",
        userMessage = "Database error. Please try again.",
        debugMessage = "Failed to connect to database"
    )

    object UserCreationFailed : AppError(
        code = "DB_002",
        userMessage = "Failed to create account. Please try again.",
        debugMessage = "User entity insertion failed"
    )

    object EmptyFields : AppError(
        code = "VAL_001",
        userMessage = "Please fill in all required fields.",
        debugMessage = "Required fields are empty"
    )

    object InvalidEmailFormat : AppError(
        code = "VAL_002",
        userMessage = "Please enter a valid email address.",
        debugMessage = "Email format validation failed"
    )

    object PasswordMismatch : AppError(
        code = "VAL_003",
        userMessage = "Passwords do not match.",
        debugMessage = "Password and confirm password don't match"
    )

    object NetworkUnavailable : AppError(
        code = "NET_001",
        userMessage = "No internet connection. Please check your network.",
        debugMessage = "Network connectivity issue"
    )

    object UnknownError : AppError(
        code = "GEN_001",
        userMessage = "An unexpected error occurred. Please try again.",
        debugMessage = "Unknown error occurred"
    )

    data class CustomError(
        override val code: String,
        override val userMessage: String,
        override val debugMessage: String? = null
    ) : AppError(code, userMessage, debugMessage)

    companion object {
        fun fromCode(code: String): AppError {
            return when (code) {
                "AUTH_001" -> UserNotFound
                "AUTH_002" -> InvalidPassword
                "AUTH_003" -> EmailAlreadyRegistered
                "DB_001" -> DatabaseConnectionError
                "DB_002" -> UserCreationFailed
                "VAL_001" -> EmptyFields
                "VAL_002" -> InvalidEmailFormat
                "VAL_003" -> PasswordMismatch
                "NET_001" -> NetworkUnavailable
                else -> UnknownError
            }
        }
    }
}