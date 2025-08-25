package com.example.postly.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postly.Model.DataModels.Result
import com.example.postly.Model.DataModels.LoginRequest
import com.example.postly.Utils.ValidationUtils
import com.example.postly.Model.Repository.Repositories.UserRepositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<Result>(Result.Idle)
    val loginState: StateFlow<Result> = _loginState.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _isPasswordVisible = MutableStateFlow(false)
    val isPasswordVisible: StateFlow<Boolean> = _isPasswordVisible.asStateFlow()



    private val _fieldErrors = MutableStateFlow<Map<String, String>>(emptyMap())
    val fieldErrors: StateFlow<Map<String, String>> = _fieldErrors.asStateFlow()

    fun onEmailChanged(newEmail: String) {
        _email.value = newEmail
        validateEmailField()
    }

    fun onPasswordChanged(newPassword: String) {
        _password.value = newPassword
        validatePasswordField()
    }

    fun togglePasswordVisibility() {
        _isPasswordVisible.value = !_isPasswordVisible.value
    }

    fun login() {
        viewModelScope.launch {
            if (!isFormValid()) {
                return@launch
            }
            val validationResult = ValidationUtils.validateLogin(
                _email.value,
                _password.value
            )

            if (validationResult is ValidationUtils.ValidationResult.Error) {
                _loginState.value = Result.Error(validationResult.appError)
                return@launch
            }

            _loginState.value = Result.Loading

            val request = LoginRequest(
                email = _email.value,
                password = _password.value
            )

            _loginState.value = userRepository.login(request)
        }
    }

    fun clearError() {
        if (_loginState.value is Result.Error) {
            _loginState.value = Result.Idle
        }
        _fieldErrors.value = emptyMap()
    }

    fun resetForm() {
        _email.value = ""
        _password.value = ""
        _fieldErrors.value = emptyMap()
        _loginState.value = Result.Idle
    }

    // Individual field validation methods
    private fun validateEmailField() {
        val result = ValidationUtils.validateEmail(_email.value)
        updateFieldError("email", result)
    }

    private fun validatePasswordField() {
        val result = ValidationUtils.validateLoginPassword(_password.value)
        updateFieldError("password", result)
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

    fun isFormValid(): Boolean {
        return _fieldErrors.value.isEmpty() &&
                _email.value.isNotBlank() &&
                _password.value.isNotBlank()
    }

    // For logging and analytics
    fun getErrorDetails(): Pair<String, String>? {
        return if (_loginState.value is Result.Error) {
            val error = (_loginState.value as Result.Error).error
            Pair(error.code, error.userMessage)
        } else {
            null
        }
    }
}