package com.example.postly.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postly.Model.Types.AppError
import com.example.postly.Model.Types.AuthState
import com.example.postly.Model.Types.LoginRequest
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

    private val _loginState = MutableStateFlow<AuthState>(AuthState.Idle)
    val loginState: StateFlow<AuthState> = _loginState.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    fun onEmailChanged(newEmail: String) {
        _email.value = newEmail
    }

    fun onPasswordChanged(newPassword: String) {
        _password.value = newPassword
    }

    fun login() {
        viewModelScope.launch {
            _loginState.value = AuthState.Loading

            // Input validation with proper error codes
            when {
                _email.value.isBlank() || _password.value.isBlank() -> {
                    _loginState.value = AuthState.Error(AppError.EmptyFields)
                    return@launch
                }

                !ValidationUtils.isValidEmail(_email.value) -> {
                    _loginState.value = AuthState.Error(AppError.InvalidEmailFormat)
                    return@launch
                }
            }

            val request = LoginRequest(
                email = _email.value,
                password = _password.value
            )

            _loginState.value = userRepository.login(request)
            Log.d("!10003", "Login status" + _loginState.value)
        }
    }

    fun clearError() {
        if (_loginState.value is AuthState.Error) {
            _loginState.value = AuthState.Idle
        }
    }

    // For logging and analytics
    fun getErrorDetails(errorState: AuthState.Error): Pair<String, String> {
        val error = errorState.error
        return Pair(error.code, error.userMessage)
    }

    // For debugging
    fun getDebugInfo(errorState: AuthState.Error): String {
        val error = errorState.error
        return "Code: ${error.code}, Debug: ${error.debugMessage ?: "No debug info"}"
    }
}