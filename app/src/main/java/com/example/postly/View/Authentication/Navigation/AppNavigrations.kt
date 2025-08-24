package com.example.postly.View.Authentication.Navigation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.postly.VIEW.screen.auth.RegisterScreen
import com.example.postly.View.Authentication.WelcomeScreen

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

//        // Login Screen
//        composable(Screens.Login.route) {
////            LoginScreen(
////                onLoginSuccess = { navController.navigate(Screens.Posts.route) },
////                onNavigateToRegister = { navController.navigate(Screens.Register.route) },
////                onNavigateBack = { navController.popBackStack() }
////            )
//        }
//
        // Register Screen
        composable(Screens.Register.route) {
            RegisterScreen(
                onRegisterSuccess = { navController.navigate(Screens.Welcome.route) },
                onNavigateToLogin = { navController.navigate(Screens.Login.route) },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Posts Screen (Main feed after login)
        composable(Screens.Posts.route) {
//            PostsScreen(
//                onNavigateToFavorites = { navController.navigate(Screens.Favorites.route) },
//                onNavigateToProfile = { navController.navigate(Screens.Profile.route) },
//                onLogout = {
//                    // Clear back stack and go to welcome screen
//                    navController.popBackStack(Screens.Welcome.route, inclusive = false)
//                }
//            )
        }

        // Favorites Screen
        composable(Screens.Favorites.route) {
//            FavoritesScreen(
//                onNavigateBack = { navController.popBackStack() }
//            )
        }
//
//        // Profile Screen
        composable(Screens.Profile.route) {
//            ProfileScreen(
//                onNavigateBack = { navController.popBackStack() },
//                onLogout = {
//                    navController.popBackStack(Screens.Welcome.route, inclusive = false)
//                }
//            )
        }
    }
}
