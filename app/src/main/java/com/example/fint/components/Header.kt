package com.example.fint.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Header() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .background(Color.Blue),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Faulu International",
            color = Color.Yellow,
            fontFamily = com.example.fint.ui.theme.Fonti,
            fontSize = 25.sp,
            textAlign = TextAlign.Center,
        )
    }
}
