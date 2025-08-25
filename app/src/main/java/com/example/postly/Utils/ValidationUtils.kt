// Utils/ValidationUtils.kt
package com.example.postly.Utils

import android.util.Patterns
import androidx.compose.ui.graphics.Color

object ValidationUtils {

    fun isValidEmail(email: String): Boolean {
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

    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isBlank() -> ValidationResult.Error(
                AppError.EmptyFields,
                "Password cannot be empty"
            )
            password.length < 8 -> ValidationResult.Error(
                AppError.CustomError(
                    code = "VAL_004",
                    userMessage = "Password must be at least 8 characters",
                    debugMessage = "Password too short: ${password.length} characters"
                ),
                "Password must be at least 8 characters"
            )
            !password.any { it.isUpperCase() } -> ValidationResult.Error(
                AppError.CustomError(
                    code = "VAL_010",
                    userMessage = "Password must contain at least one uppercase letter",
                    debugMessage = "Password missing uppercase letter"
                ),
                "Must contain at least one uppercase letter"
            )
            !password.any { it.isLowerCase() } -> ValidationResult.Error(
                AppError.CustomError(
                    code = "VAL_011",
                    userMessage = "Password must contain at least one lowercase letter",
                    debugMessage = "Password missing lowercase letter"
                ),
                "Must contain at least one lowercase letter"
            )
            checkPasswordStrength(password) == PasswordStrength.WEAK -> ValidationResult.Error(
                AppError.CustomError(
                    code = "VAL_009",
                    userMessage = "Password is too weak. Include numbers and special characters for better security",
                    debugMessage = "Password strength: WEAK"
                ),
                "Password is too weak"
            )
            else -> ValidationResult.Valid
        }
    }

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

    fun checkPasswordStrength(password: String): PasswordStrength {
        val strengthScore = calculatePasswordStrengthScore(password)
        return when {
            strengthScore < 2 -> PasswordStrength.WEAK
            strengthScore < 4 -> PasswordStrength.MEDIUM
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


    fun getPasswordRequirements(password: String): Map<String, Boolean> {
        return mapOf(
            "At least 8 characters" to (password.length >= 8),
            "Contains a number" to password.any { it.isDigit() },
            "Contains uppercase letter" to password.any { it.isUpperCase() },
            "Contains lowercase letter" to password.any { it.isLowerCase() },
            "Contains special character" to password.any { !it.isLetterOrDigit() }
        )
    }

    sealed class ValidationResult {
        object Valid : ValidationResult()
        data class Error(val appError: AppError, val fieldMessage: String) : ValidationResult()
    }

    enum class PasswordStrength {
        WEAK, MEDIUM, STRONG
    }

    fun PasswordStrength.getDescription(): String {
        return when (this) {
            PasswordStrength.WEAK -> "Weak password"
            PasswordStrength.MEDIUM -> "Medium strength"
            PasswordStrength.STRONG -> "Strong password"
        }
    }

    fun PasswordStrength.getColor(): Color {
        return when (this) {
            PasswordStrength.WEAK -> Color(0xFFFF5252) // Red
            PasswordStrength.MEDIUM -> Color(0xFFFFA000) // Orange/Amber
            PasswordStrength.STRONG -> Color(0xFF4CAF50) // Green
        }
    }

    // Login-specific password validation (might be less strict than registration)
    fun validateLoginPassword(password: String): ValidationResult {
        return when {
            password.isBlank() -> ValidationResult.Error(
                AppError.EmptyFields,
                "Password cannot be empty"
            )
            else -> ValidationResult.Valid
        }
    }

    // Comprehensive login validation
    fun validateLogin(
        email: String,
        password: String
    ): ValidationResult {
        val emailResult = validateEmail(email)
        if (emailResult is ValidationResult.Error) return emailResult

        val passwordResult = validateLoginPassword(password)
        if (passwordResult is ValidationResult.Error) return passwordResult

        return ValidationResult.Valid
    }
}