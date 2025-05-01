package com.example.fint.screens

import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.fint.model.AuthViewModel
import com.example.fint.ExamResult
import com.example.fint.model.ExamViewModel
import com.example.fint.R
import com.example.fint.components.Header


@Composable
fun Exam(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(),
    examViewModel: ExamViewModel = hiltViewModel()
) {
    // Observe logged-in user data
    val userData by authViewModel.userData.collectAsState()

    // Observe exam results and loading state
    val examResults by examViewModel.examResults.collectAsState()
    val isLoading by examViewModel.isLoading.collectAsState()
    val context = LocalContext.current


    val getContentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            authViewModel.uploadProfileImage(it, context)
        }
    }

    val isPhotoPickerAvailable = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    val pickMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let {
            authViewModel.uploadProfileImage(it, context)
        }
    }


    // Trigger fetch only once per user
    LaunchedEffect(Unit) {
        authViewModel.fetchUserData()
    }

    // When userData becomes available, fetch exam results
    LaunchedEffect(userData?.userId) {
        userData?.userId?.let {
            examViewModel.fetchExamResults(it)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Header()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Show profile image
            userData?.let { user ->
                if (user.profileImageUrl.isNotEmpty()) {
                    val painter = rememberAsyncImagePainter(
                        model = user.profileImageUrl,
                        placeholder = painterResource(R.drawable.default_profile),
                        error = painterResource(R.drawable.default_profile),
                        fallback = painterResource(R.drawable.default_profile)
                    )

                    Image(
                        painter = painter,
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        pickMediaLauncher.launch(
                            PickVisualMediaRequest.Builder()
                                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                .build()
                        )
                    } else {
                        getContentLauncher.launch("image/*")
                    }
                }) {
                    Text("Change Profile Picture")
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Welcome, ${user.name}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                } else {
                    if (examResults.isNotEmpty()) {
                        LazyColumn {
                            items(examResults) { result ->
                                ExamResultCard(result)
                            }
                        }
                    } else {
                        Text(
                            "No exam results available.",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        authViewModel.logout()
                        navController.navigate("auth") {
                            popUpTo("app") { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Log Out", color = Color.White)
                }
            }
        }


    }
}

@Composable
fun ExamResultCard(examResult: ExamResult) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Subject: ${examResult.subject}",
                fontWeight = FontWeight.SemiBold
            )
            Text("Score: ${examResult.score}")
            Text("Grade: ${examResult.grade}")
        }
    }
}

//Button(
//                onClick = {
//                    if (isPhotoPickerAvailable) {
//                        pickMediaLauncher.launch(
//                            PickVisualMediaRequest.Builder()
//                                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly)
//                                .build()
//                        )
//                    } else {
//                        getContentLauncher.launch("image/*")
//                    }
//                },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text("Change Profile Picture")
//            }