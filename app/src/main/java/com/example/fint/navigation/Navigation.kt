package com.example.fint.navigation

import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.fint.model.AuthViewModel
import com.example.fint.screens.About
import com.example.fint.screens.Exam
import com.example.fint.screens.Gallery
import com.example.fint.screens.Home
import com.example.fint.screens.LoginScreen
import com.example.fint.screens.ForgotPasswordScreen
import com.example.fint.screens.RegisterScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()

    // Observe authentication state
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    val loginState by authViewModel.loginState.collectAsState()

    LaunchedEffect(loginState) {
        if (loginState is AuthViewModel.AuthState.Success) {
            navController.navigate(AppRoutes.HOME) {
                popUpTo("auth") { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) AppRoutes.HOME else AppRoutes.LOGIN
    ) {
        // Auth Navigation Graph
        authNavGraph(navController)

        // App Navigation Graph
        appNavGraph(navController)
    }
}

// Auth Navigation Graph
fun NavGraphBuilder.authNavGraph(navController: NavController) {
    navigation(
        startDestination = AppRoutes.LOGIN,
        route = "auth"
    ) {
        composable(AppRoutes.LOGIN) {
            LoginScreen(
                navController = navController,
                onForgotPassword = {
                    navController.navigate(AppRoutes.FORGOT_PASSWORD)
                }
            )
        }

        composable(AppRoutes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(
                navController = navController,
            )
        }

        composable(AppRoutes.REGISTER) {
            RegisterScreen(
                navController = navController,
            )
        }
    }
}

// App Navigation Graph
fun NavGraphBuilder.appNavGraph(navController: NavController) {
    navigation(
        startDestination = AppRoutes.HOME,
        route = "app"
    ) {
        composable(AppRoutes.HOME) {
            Home(
                navController,
                onNavigateToExams = {
                    navController.navigate(AppRoutes.EXAMS)
                },
            )
        }

        composable(AppRoutes.ABOUT) {
            About(
                navController,
                scrollState = rememberScrollState(),
                viewModel = hiltViewModel()
            )
        }

        composable(AppRoutes.GALLERY) {
            Gallery(
            )
        }

        composable(AppRoutes.EXAMS) {
            Exam(
                navController = navController,
                authViewModel = hiltViewModel(),
                examViewModel = hiltViewModel()
            )
        }
    }
}
