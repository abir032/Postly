// Presentation/ViewModel/RegisterViewModel.kt
package com.example.postly.Presentation.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postly.Model.Types.AppError
import com.example.postly.Model.Types.AuthState
import com.example.postly.Model.Types.RegisterRequest
import com.example.postly.Utils.ValidationUtils
import com.example.postly.Model.Repository.Repositories.UserRepositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _registerState = MutableStateFlow<AuthState>(AuthState.Idle)
    val registerState: StateFlow<AuthState> = _registerState.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword

    private val _isPasswordVisible = MutableStateFlow(false)
    val isPasswordVisible: StateFlow<Boolean> = _isPasswordVisible.asStateFlow()

    private val _isConfirmPasswordVisible = MutableStateFlow(false)
    val isConfirmPasswordVisible: StateFlow<Boolean> = _isConfirmPasswordVisible.asStateFlow()

    private val _passwordStrength = MutableStateFlow(ValidationUtils.PasswordStrength.WEAK)
    val passwordStrength: StateFlow<ValidationUtils.PasswordStrength> = _passwordStrength.asStateFlow()

    private val _fieldErrors = MutableStateFlow<Map<String, String>>(emptyMap())
    val fieldErrors: StateFlow<Map<String, String>> = _fieldErrors.asStateFlow()

    private val _passwordRequirements = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val passwordRequirements: StateFlow<Map<String, Boolean>> = _passwordRequirements.asStateFlow()

    fun onEmailChanged(newEmail: String) {
        _email.value = newEmail
        validateEmailField()
    }

    fun onPasswordChanged(newPassword: String) {
        _password.value = newPassword
        _passwordStrength.value = ValidationUtils.checkPasswordStrength(newPassword)
        _passwordRequirements.value = ValidationUtils.getPasswordRequirements(newPassword)
        validatePasswordField()
        validateConfirmPasswordField()
    }

    fun isFormValid(): Boolean {
        return _fieldErrors.value.isEmpty() &&
                _email.value.isNotBlank() &&
                _password.value.isNotBlank() &&
                _confirmPassword.value.isNotBlank() &&
                areAllPasswordRequirementsMet()
    }

    // Add helper function to check if all password requirements are met
    fun areAllPasswordRequirementsMet(): Boolean {
        return _passwordRequirements.value.values.all { it }
    }

    fun onConfirmPasswordChanged(newConfirmPassword: String) {
        _confirmPassword.value = newConfirmPassword
        validateConfirmPasswordField()
    }

    fun togglePasswordVisibility() {
        _isPasswordVisible.value = !_isPasswordVisible.value
    }

    fun toggleConfirmPasswordVisibility() {
        _isConfirmPasswordVisible.value = !_isConfirmPasswordVisible.value
    }

    fun register() {
        viewModelScope.launch {
            // Validate all fields first
            if(!isFormValid()) {
                return@launch
            }
            val validationResult = ValidationUtils.validateRegistration(
                _email.value,
                _password.value,
                _confirmPassword.value
            )

            if (validationResult is ValidationUtils.ValidationResult.Error) {
                _registerState.value = AuthState.Error(validationResult.appError)
                return@launch
            }

            _registerState.value = AuthState.Loading

            val request = RegisterRequest(
                email = _email.value,
                password = _password.value,
                confirmPassword = _confirmPassword.value
            )

            _registerState.value = userRepository.register(request)
        }
    }


    fun clearError() {
        if (_registerState.value is AuthState.Error) {
            _registerState.value = AuthState.Idle
        }
        _fieldErrors.value = emptyMap()
    }

    fun resetForm() {
        _email.value = ""
        _password.value = ""
        _confirmPassword.value = ""
        _passwordStrength.value = ValidationUtils.PasswordStrength.WEAK
        _fieldErrors.value = emptyMap()
        _registerState.value = AuthState.Idle
    }

    // Individual field validation methods
    private fun validateEmailField() {
        val result = ValidationUtils.validateEmail(_email.value)
        updateFieldError("email", result)
    }

    private fun validatePasswordField() {
        val result = ValidationUtils.validatePassword(_password.value)
        updateFieldError("password", result)
    }

    private fun validateConfirmPasswordField() {
        val result = ValidationUtils.validateConfirmPassword(_password.value, _confirmPassword.value)
        updateFieldError("confirmPassword", result)
    }

    private fun updateFieldError(fieldName: String, result: ValidationUtils.ValidationResult) {
        val currentErrors = _fieldErrors.value.toMutableMap()

        if (result is ValidationUtils.ValidationResult.Error) {
            currentErrors[fieldName] = result.fieldMessage
        } else {
            currentErrors.remove(fieldName)
        }

        _fieldErrors.value = currentErrors
    }

    // For logging and analytics
    fun getErrorDetails(): Pair<String, String>? {
        return if (_registerState.value is AuthState.Error) {
            val error = (_registerState.value as AuthState.Error).error
            Pair(error.code, error.userMessage)
        } else {
            null
        }
    }
}