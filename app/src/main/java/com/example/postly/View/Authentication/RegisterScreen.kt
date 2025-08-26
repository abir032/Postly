

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
import com.example.postly.ViewModel.RegisterViewModel
import com.example.postly.Utils.AppText
import com.example.postly.Utils.ValidationUtils.getColor
import com.example.postly.Utils.ValidationUtils.getDescription


@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToLogin: () -> Unit,
) {
    val registerViewModel: RegisterViewModel = hiltViewModel()

    val uiState by registerViewModel.registerState.collectAsStateWithLifecycle()
    val email by registerViewModel.email.collectAsStateWithLifecycle()
    val password by registerViewModel.password.collectAsStateWithLifecycle()
    val confirmPassword by registerViewModel.confirmPassword.collectAsStateWithLifecycle()
    val isPasswordVisible by registerViewModel.isPasswordVisible.collectAsStateWithLifecycle()
    val isConfirmPasswordVisible by registerViewModel.isConfirmPasswordVisible.collectAsStateWithLifecycle()
    val fieldErrors by registerViewModel.fieldErrors.collectAsStateWithLifecycle()
    val passwordStrength by registerViewModel.passwordStrength.collectAsStateWithLifecycle()
    val passwordRequirements by registerViewModel.passwordRequirements.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(uiState) {
        when (uiState) {
            is Result.Success -> {
                Toast.makeText(
                    context,
                    AppText.REGISTRATION_SUCCESS,
                    Toast.LENGTH_SHORT).show()
               onRegisterSuccess()
            }
            is Result.Loading -> {

            }
            is Result.Error -> {
                val error = (uiState as Result.Error).error
                Log.d("tag", "Error" + error.userMessage)
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
            Text(
                text = AppText.REGISTRATION_TITLE,
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
                onValueChange = registerViewModel::onEmailChanged,
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
                onValueChange = registerViewModel::onPasswordChanged,
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (isPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    IconButton(onClick = { registerViewModel.togglePasswordVisibility() }) {
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
                    Column {
                        fieldErrors["password"]?.let { error ->
                            Text(
                                text = error,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                        if (password.isNotEmpty()) {
                            Text(
                                text = passwordStrength.getDescription(),
                                color = passwordStrength.getColor(),
                                fontSize = 12.sp
                            )
                        }
                    }
                },
                singleLine = true
            )

            if (password.isNotEmpty()) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)) {
                    passwordRequirements.forEach { (requirement, met) ->
                        Text(
                            text = "• $requirement",
                            color = if (met) Color.Green else Color.Gray,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = registerViewModel::onConfirmPasswordChanged,
                label = { Text("Confirm Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (isConfirmPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    IconButton(onClick = { registerViewModel.toggleConfirmPasswordVisibility() }) {
                        Icon(
                            imageVector = if (isConfirmPasswordVisible) {
                                Icons.Default.Visibility
                            } else {
                                Icons.Default.VisibilityOff
                            },
                            contentDescription = if (isConfirmPasswordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                isError = fieldErrors.containsKey("confirmPassword"),
                supportingText = {
                    fieldErrors["confirmPassword"]?.let { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            GlassButton(
                onClick = { registerViewModel.register() },
                text = "Register",
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
                text = AppText.REGISTRATION_ALREADY_HAVE_ACCOUNT,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            GlassButton(
                onClick = onNavigateToLogin,
                text = "Login",
                modifier = Modifier.fillMaxWidth(),
                colors = listOf(
                    Color(0xFFF3E5F5).copy(alpha = 0.3f),
                    Color(0xFFE1BEE7).copy(alpha = 0.2f),
                    Color(0xFFCE93D8).copy(alpha = 0.1f)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

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