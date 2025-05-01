package com.example.fint.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fint.model.AuthViewModel
import com.example.fint.components.Header
import com.example.fint.navigation.AppRoutes

fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
}
@Composable
fun RegisterScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var grade by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val registerState by authViewModel.registerState.collectAsState()
    val isLoading = registerState is AuthViewModel.AuthState.Loading
    val errorMessage = (registerState as? AuthViewModel.AuthState.Error)?.message

    LaunchedEffect(registerState) {
        if (registerState is AuthViewModel.AuthState.Success) {
            navController.navigate(AppRoutes.LOGIN) {
                popUpTo("auth") { inclusive = true }
            }
        }
    }

    Header()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = grade,
            onValueChange = { grade = it },
            label = { Text("Grade") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                val description = if (passwordVisible) "Hide password" else "Show password"
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = icon, contentDescription = description)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                val description = if (passwordVisible) "Hide password" else "Show password"
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = icon, contentDescription = description)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val trimmedEmail = email.trim()
                if (
                    name.isNotBlank() &&
                    grade.isNotBlank() &&
                    isValidEmail(trimmedEmail) &&
                    password == confirmPassword &&
                    password.isNotBlank()
                ) {
                    authViewModel.register(name, trimmedEmail, password, grade, context)
                } else {
                    Toast.makeText(context, "Please enter valid data", Toast.LENGTH_SHORT).show()
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
                Text("Register")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = {
            navController.navigate(AppRoutes.LOGIN)
        }) {
            Text("Already have an account? Login here.")
        }
    }
}











//@Composable
//fun RegisterScreen(
//    navController: NavController,
//    authViewModel: AuthViewModel = hiltViewModel()
//) {
//    // Local state to capture user inputs
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var confirmPassword by remember { mutableStateOf("") }
//    var name by remember { mutableStateOf("") }
//    val context = LocalContext.current
//
//
//    // State for loading and error handling
//    val registerState by authViewModel.registerState.collectAsState()
//    val isLoading = registerState is AuthViewModel.AuthState.Loading
//    val errorMessage = (registerState as? AuthViewModel.AuthState.Error)?.message
//    var passwordVisible by remember { mutableStateOf(false) }
//    var grade by remember { mutableStateOf("") }
//    var emailError by remember { mutableStateOf("") }
//    var passwordError by remember { mutableStateOf("") }
//    var nameError by remember { mutableStateOf("") }
//
//    // Handle successful registration and navigate to login screen
//    LaunchedEffect(registerState) {
//        if (registerState is AuthViewModel.AuthState.Success) {
//            navController.navigate(AppRoutes.LOGIN) {
//                popUpTo("auth") { inclusive = true }
//            }
//        }
//    }
//    Header()
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center
//    ) {
//
//        // Name field
//        OutlinedTextField(
//            value = name,
//            onValueChange = { name = it },
//            label = { Text("Name") },
//            modifier = Modifier.fillMaxWidth(),
//            isError = errorMessage != null
//        )
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        // Email field
//        OutlinedTextField(
//            value = email,
//            onValueChange = {
//                email = it
//                emailError = ""
//            },
//            label = { Text("Email") },
//            singleLine = true,
//            modifier = Modifier.fillMaxWidth(),
//            isError = emailError.isNotEmpty()
//        )
//        if (emailError.isNotEmpty()) {
//            Text(
//                text = emailError,
//                color = MaterialTheme.colorScheme.error,
//                style = MaterialTheme.typography.labelSmall,
//                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
//            )
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(
//            value = grade,
//            onValueChange = { grade = it },
//            label = { Text("Grade") },
//            modifier = Modifier.fillMaxWidth(),
//            isError = errorMessage != null
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//
//        // Password field
//        OutlinedTextField(
//            value = password,
//            onValueChange = { password = it },
//            label = { Text("Password") },
//            singleLine = true,
//            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
//            trailingIcon = {
//                val icon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
//                val description = if (passwordVisible) "Hide password" else "Show password"
//                IconButton(onClick = { passwordVisible = !passwordVisible }) {
//                    Icon(imageVector = icon, contentDescription = description)
//                }
//            },
//            modifier = Modifier.fillMaxWidth(),
//            isError = errorMessage != null
//        )
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        // Confirm Password field
//        OutlinedTextField(
//            value = confirmPassword,
//            onValueChange = { confirmPassword = it },
//            label = { Text("Confirm Password") },
//            singleLine = true,
//            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
//            trailingIcon = {
//                val icon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
//                val description = if (passwordVisible) "Hide password" else "Show password"
//                IconButton(onClick = { passwordVisible = !passwordVisible }) {
//                    Icon(imageVector = icon, contentDescription = description)
//                }
//            },
//            modifier = Modifier.fillMaxWidth(),
//            isError = errorMessage != null
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//
//        // Register Button
//        Button(
//            onClick = {
//                val isEmailValid = isValidEmail(email)
//                val isPasswordConfirmed = password == confirmPassword
//
//                emailError = if (!isEmailValid) "Invalid email address" else ""
//                passwordError = if (!isPasswordConfirmed) "Passwords do not match" else ""
//                nameError = if (name.isBlank()) "Name cannot be empty" else ""
//
//                if (isEmailValid && isPasswordConfirmed && name.isNotBlank()) {
//                    authViewModel.register(email.trim(), password, name, grade, context)
//                }
//            },
//            modifier = Modifier.fillMaxWidth(),
//            enabled = !isLoading
//        ) {
//            if (isLoading) {
//                CircularProgressIndicator(
//                    modifier = Modifier.size(20.dp),
//                    color = MaterialTheme.colorScheme.onPrimary
//                )
//            } else {
//                Text("Register")
//            }
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        // Error Message
//        errorMessage?.let {
//            Text(
//                text = it,
//                color = MaterialTheme.colorScheme.error,
//                modifier = Modifier.padding(top = 16.dp)
//            )
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Already have an account? Login link
//        TextButton(onClick = {
//            navController.navigate(AppRoutes.LOGIN)
//        }) {
//            Text("Already have an account? Login here.")
//        }
//    }
//}

