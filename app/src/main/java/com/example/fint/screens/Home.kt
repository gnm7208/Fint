package com.example.fint.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.fint.AuthViewModel
import com.example.fint.R
import com.example.fint.ui.theme.Fonti


@Composable
fun Home(
    navController: NavController,
         onNavigateToExams: () -> Unit,
     authViewModel: AuthViewModel = hiltViewModel()) {


    Box(modifier= Modifier.fillMaxSize()){
        Image(painter = painterResource(id = R.drawable.index),
            contentDescription = "background image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize())
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = buildAnnotatedString {
            append("Welcome to")
            withStyle(
                SpanStyle(
                    color = Color.Yellow,
                    fontFamily = Fonti,

                    fontSize = 45.sp
                )
            ){
                append("Faulu")
            }


        },
            fontSize = 40.sp,
            fontWeight = FontWeight.SemiBold)

        Spacer(modifier = Modifier.height(32.dp))


        Button(
            onClick = onNavigateToExams,
            modifier = Modifier.width(200.dp)
        ) {
            Text("View Exam Results")
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
            modifier = Modifier.width(200.dp)
        ) {
            Text("Log Out", color = Color.White)
        }



    }


}
@Preview(showBackground = true)
@Composable
fun HomePreview(){
    Home(
        navController = rememberNavController(),
        onNavigateToExams = {}
    )

}