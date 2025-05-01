package com.example.fint.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fint.model.AuthViewModel
import com.example.fint.components.Header
import kotlinx.coroutines.launch

@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    val resetState by viewModel.passwordResetState.collectAsState()
    val isLoading = resetState is AuthViewModel.AuthState.Loading
    val errorMessage = (resetState as? AuthViewModel.AuthState.Error)?.message
    val coroutineScope = rememberCoroutineScope()

    // Handle reset state
    LaunchedEffect(resetState) {
        when (resetState) {
            is AuthViewModel.AuthState.Success -> {
                // Handle success
                navController.popBackStack()
                viewModel.resetAuthState()
            }
            is AuthViewModel.AuthState.Error -> {
                // Show error
                viewModel.resetAuthState()
            }
            else -> {}
        }
    }
    Header()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {

        // Email input field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            isError = errorMessage != null
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Error message
        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Reset Password Button
        Button(
            onClick = {
                if (email.isNotBlank()) {
                    coroutineScope.launch {
                        viewModel.sendPasswordResetEmail(email)
                    }
                } else {
                    // Optionally handle email validation
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Reset Password")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Back to Login link
        TextButton(onClick = {
            navController.popBackStack()
        }) {
            Text("Back to Login")
        }
    }
}