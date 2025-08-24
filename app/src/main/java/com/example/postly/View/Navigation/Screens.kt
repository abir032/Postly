package com.example.postly.View.Navigation

import com.example.postly.Utils.Constants

sealed class Screens(val route: String) {
    object Welcome : Screens(Constants.SCREEN_WELCOME)
    object Login : Screens(Constants.SCREEN_LOGIN)
    object Register : Screens(Constants.SCREEN_REGISTER)
    object Posts : Screens(Constants.SCREEN_POSTS)
    object Favorites : Screens(Constants.SCREEN_FAVORITES)
    object Profile : Screens(Constants.SCREEN_FAVORITES)
}