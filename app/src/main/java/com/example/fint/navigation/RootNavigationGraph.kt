package com.example.fint.navigation

import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.fint.AuthViewModel
import com.example.fint.ContactViewModel
import com.example.fint.screens.About
import com.example.fint.screens.Gallery
import com.example.fint.screens.Home

object AppRoutes {
    // Auth routes
    const val LOGIN = "login"
    const val FORGOT_PASSWORD = "forgot"
    const val REGISTER = "register"

    const val AUTH = "auth"
    const val APP = "app"


    // App routes
    const val HOME = "home"
    const val ABOUT = "about"
    const val GALLERY = "gallery"
    const val EXAMS = "exams"
}

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    object Home : BottomNavItem(
        route = AppRoutes.HOME,
        icon = Icons.Default.Home,
        label = "Home"
    )

    object About : BottomNavItem(
        route = AppRoutes.ABOUT,
        icon = Icons.Default.Info,
        label = "About"
    )

    object Gallery : BottomNavItem(
        route = AppRoutes.GALLERY,
        icon = Icons.Default.Image,
        label = "Gallery"
    )

    companion object {
        val items = listOf(Home, About, Gallery)
    }
}

object Graph{
    const val ROOT = "root_graph"
    const val AUTHENTICATION = "auth_graph"
    const val MAIN_SCREEN = "main_screen_graph"
}

