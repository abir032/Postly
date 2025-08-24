// Utils/ValidationUtils.kt
package com.example.postly.Utils

import android.util.Patterns
import com.example.postly.Model.Types.AppError

object ValidationUtils {

    fun isValidEmail(email: String) : Boolean {
       return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Email validation
    fun validateEmail(email: String): ValidationResult {
        return when {
            email.isBlank() -> ValidationResult.Error(
                AppError.EmptyFields,
                "Email cannot be empty"
            )
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> ValidationResult.Error(
                AppError.InvalidEmailFormat,
                "Invalid email format"
            )
            else -> ValidationResult.Valid
        }
    }

    // Password validation
    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isBlank() -> ValidationResult.Error(
                AppError.EmptyFields,
                "Password cannot be empty"
            )
            password.length < 6 -> ValidationResult.Error(
                AppError.CustomError(
                    code = "VAL_004",
                    userMessage = "Password must be at least 6 characters",
                    debugMessage = "Password too short: ${password.length} characters"
                ),
                "Password too short"
            )
            else -> ValidationResult.Valid
        }
    }

    // Confirm password validation
    fun validateConfirmPassword(password: String, confirmPassword: String): ValidationResult {
        return when {
            confirmPassword.isBlank() -> ValidationResult.Error(
                AppError.EmptyFields,
                "Confirm password cannot be empty"
            )
            password != confirmPassword -> ValidationResult.Error(
                AppError.PasswordMismatch,
                "Passwords do not match"
            )
            else -> ValidationResult.Valid
        }
    }

    // Comprehensive registration validation
    fun validateRegistration(
        email: String,
        password: String,
        confirmPassword: String
    ): ValidationResult {
        val emailResult = validateEmail(email)
        if (emailResult is ValidationResult.Error) return emailResult

        val passwordResult = validatePassword(password)
        if (passwordResult is ValidationResult.Error) return passwordResult

        val confirmPasswordResult = validateConfirmPassword(password, confirmPassword)
        if (confirmPasswordResult is ValidationResult.Error) return confirmPasswordResult

        return ValidationResult.Valid
    }

    // Password strength checker (optional)
    fun checkPasswordStrength(password: String): PasswordStrength {
        val strengthScore = calculatePasswordStrengthScore(password)
        return when {
            strengthScore < 3 -> PasswordStrength.WEAK
            strengthScore < 5 -> PasswordStrength.MEDIUM
            else -> PasswordStrength.STRONG
        }
    }

    private fun calculatePasswordStrengthScore(password: String): Int {
        var score = 0
        if (password.length >= 8) score++
        if (password.any { it.isDigit() }) score++
        if (password.any { it.isUpperCase() }) score++
        if (password.any { it.isLowerCase() }) score++
        if (password.any { !it.isLetterOrDigit() }) score++
        return score
    }

    // Sealed class for validation results
    sealed class ValidationResult {
        object Valid : ValidationResult()
        data class Error(val appError: AppError, val fieldMessage: String) : ValidationResult()
    }

    // Password strength enum
    enum class PasswordStrength {
        WEAK, MEDIUM, STRONG
    }

    // Extension function to get user-friendly strength message
    fun PasswordStrength.getDescription(): String {
        return when (this) {
            PasswordStrength.WEAK -> "Weak password"
            PasswordStrength.MEDIUM -> "Medium strength"
            PasswordStrength.STRONG -> "Strong password"
        }
    }
}