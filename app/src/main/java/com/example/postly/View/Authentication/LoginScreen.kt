package com.example.postly.View.Authentication

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.postly.Model.DataModels.Result
import com.example.postly.Utils.AppText
import com.example.postly.ViewModel.LoginViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val loginViewModel: LoginViewModel = hiltViewModel()
    val context = LocalContext.current

    val uiState by loginViewModel.loginState.collectAsStateWithLifecycle()
    val email by loginViewModel.email.collectAsStateWithLifecycle()
    val password by loginViewModel.password.collectAsStateWithLifecycle()
    val isPasswordVisible by loginViewModel.isPasswordVisible.collectAsStateWithLifecycle()
    val fieldErrors by loginViewModel.fieldErrors.collectAsStateWithLifecycle()

    LaunchedEffect(uiState) {
        when (uiState) {
            is  Result.Success -> {
                Toast.makeText(
                    context,
                    AppText.LOGIN_SUCCESS,
                    Toast.LENGTH_SHORT
                ).show()
                onLoginSuccess()
            }
            is Result.Loading -> {
            }
            is Result.Error -> {
                val error = (uiState as Result.Error).error
                Log.d("LoginScreen", "Error: ${error.userMessage}")
                Toast.makeText(context, error.userMessage, Toast.LENGTH_SHORT).show()
            }
            else -> {
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title
            Text(
                text = AppText.LOGIN_TITLE,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                ),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 40.dp)
            )

            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = loginViewModel::onEmailChanged,
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                isError = fieldErrors.containsKey("email"),
                supportingText = {
                    fieldErrors["email"]?.let { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = loginViewModel::onPasswordChanged,
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (isPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    IconButton(onClick = { loginViewModel.togglePasswordVisibility() }) {
                        Icon(
                            imageVector = if (isPasswordVisible) {
                                Icons.Default.Visibility
                            } else {
                                Icons.Default.VisibilityOff
                            },
                            contentDescription = if (isPasswordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                isError = fieldErrors.containsKey("password"),
                supportingText = {
                    fieldErrors["password"]?.let { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Login Button
            GlassButton(
                onClick = { loginViewModel.login() },
                text = "Login",
                modifier = Modifier.fillMaxWidth(),
                height = 56.dp,
                colors = listOf(
                    Color(0xFFE3F2FD).copy(alpha = 0.3f),  // Light blue
                    Color(0xFFBBDEFB).copy(alpha = 0.2f),  // Medium blue
                    Color(0xFF90CAF9).copy(alpha = 0.1f)   // Dark blue
                )
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = AppText.LOGIN_NO_ACCOUNT,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Register button
            GlassButton(
                onClick = onNavigateToRegister,
                text = "Register",
                modifier = Modifier.fillMaxWidth(),
                colors = listOf(
                    Color(0xFFF3E5F5).copy(alpha = 0.3f),
                    Color(0xFFE1BEE7).copy(alpha = 0.2f),
                    Color(0xFFCE93D8).copy(alpha = 0.1f)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Back button
            Button(
                onClick = onNavigateBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text(
                    text = "Back to Welcome",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}