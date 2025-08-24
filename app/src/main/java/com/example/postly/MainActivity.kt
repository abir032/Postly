package com.example.postly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.postly.Presentation.ViewModel.LoginViewModel
import com.example.postly.Presentation.ViewModel.RegisterViewModel
import com.example.postly.VIEW.screen.auth.WelcomeScreen
import com.example.postly.ui.theme.PostlyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PostlyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WelcomeScreen(
                        onNavigateToLogin = { navigateToLogin() },
                        onNavigateToRegister = { navigateToRegister() }
                    )
                }
            }
        }
    }

    private fun navigateToLogin() {
        // You'll implement navigation later
        // For now, just show a toast or log
        println("Navigate to Login screen")
    }

    private fun navigateToRegister() {
        // You'll implement navigation later
        // For now, just show a toast or log
        println("Navigate to Register screen")
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    PostlyTheme {
        WelcomeScreen(
            onNavigateToLogin = { println("Login clicked") },
            onNavigateToRegister = { println("Register clicked") }
        )
    }
}