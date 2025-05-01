package com.example.fint

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fint.navigation.AppRoutes
import com.example.fint.navigation.BottomNavItem
import com.example.fint.navigation.appNavGraph
import com.example.fint.navigation.authNavGraph
import com.example.fint.screens.ForgotPasswordScreen
import androidx.compose.runtime.getValue
import com.example.fint.model.AuthViewModel

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    val bottomNavRoutes = setOf(
        AppRoutes.HOME,
        AppRoutes.ABOUT,
        AppRoutes.GALLERY
    )


    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val showBottomNav = isLoggedIn && currentRoute in bottomNavRoutes


    LaunchedEffect(isLoggedIn) {
        if (!isLoggedIn) {
            navController.navigate("auth") {
                popUpTo("app") { inclusive = true }
            }
        }
    }

    Scaffold(
        bottomBar = {
            if (showBottomNav) {
                AppBottomNavigation(navController = navController)
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = if (isLoggedIn) "app" else "auth",
            modifier = Modifier.padding(padding)
        ) {
            composable(AppRoutes.FORGOT_PASSWORD) {
                ForgotPasswordScreen(navController = navController)
            }
            authNavGraph(navController)
            appNavGraph(navController)
        }
    }
}

@Composable
fun AppBottomNavigation(navController: NavController) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    BottomNavigation {
        BottomNavItem.items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(item.icon, contentDescription = null) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        // Pop up to the start destination to avoid back stack buildup
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of same destination
                        launchSingleTop = true
                        // Restore state when reselecting item
                        restoreState = true
                    }
                }
            )
        }
    }
}

fun NavController.currentRoute(): String? {
    return currentBackStackEntry?.destination?.route
}