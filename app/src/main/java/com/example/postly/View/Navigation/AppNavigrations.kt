package com.example.postly.View.Navigation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.postly.View.Authentication.LoginScreen
import com.example.postly.View.Authentication.RegisterScreen
import com.example.postly.View.Authentication.WelcomeScreen
import com.example.postly.View.Posts.FavoriteScreen
import com.example.postly.View.Posts.PostScreen
import com.example.postly.ViewModel.PostViewModel

@Composable
fun AppNavigations(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screens.Welcome.route,
        modifier = modifier
    ) {
        composable(Screens.Welcome.route) {
            WelcomeScreen(
                onNavigateToLogin = { navController.navigate(Screens.Login.route) },
                onNavigateToRegister = { navController.navigate(Screens.Register.route) }
            )
        }

        // Login Screen
        composable(Screens.Login.route) {
            LoginScreen(
                onLoginSuccess = { navController.navigate(Screens.Posts.route) },
                onNavigateToRegister = { navController.navigate(Screens.Register.route) },
                onNavigateBack = { navController.navigate(Screens.Welcome.route) }
            )
        }

        // Register Screen
        composable(Screens.Register.route) {
            RegisterScreen(
                onRegisterSuccess = { navController.navigate(Screens.Welcome.route) },
                onNavigateToLogin = { navController.navigate(Screens.Login.route) },
                onNavigateBack = { navController.navigate(Screens.Welcome.route) }
            )
        }

        // Posts Screen
        composable(Screens.Posts.route) {
            PostScreen(
                onNavigateToFavorites = { navController.navigate(Screens.Favorites.route) },
                onNavigateToProfile = { navController.navigate(Screens.Profile.route) },
                onLogout = {
                    navController.popBackStack(Screens.Welcome.route, inclusive = false)
                }
            )
        }

        composable(Screens.Favorites.route) {
            val parentEntry = remember(navController.currentBackStackEntry) {
                navController.getBackStackEntry(Screens.Posts.route)
            }
            val postViewModel: PostViewModel = hiltViewModel(parentEntry)
            FavoriteScreen (
                onNavigateBack = { navController.popBackStack() },
                viewModel = postViewModel
            )
        }
    }
}
