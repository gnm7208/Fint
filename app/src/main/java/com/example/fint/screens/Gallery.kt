package com.example.fint.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fint.R
import com.example.fint.components.Header
import com.example.fint.ui.theme.FintTheme

@Composable
fun Gallery(
) {
    val imageList = listOf(
        R.drawable.bb to "Building Image",
        R.drawable.bus to "Bus Image",
        R.drawable.dining to "Dining Area Image",
        R.drawable.hallway to "Hallway Image",
        R.drawable.inauma to "Inauma Image",
        R.drawable.learning to "Learning Area Image",
        R.drawable.library to "Library Image",
        R.drawable.lockers to "Lockers Image",
        R.drawable.nini to "Nini Image",
        R.drawable.trophy to "Trophy Image"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Header()
        }
        items(imageList) { pair ->
            val (imageRes, description) = pair
            ImageBox(imageRes, description)
        }
    }
}

@Composable
fun ImageBox(imageRes: Int, contentDescription: String) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(Color.LightGray)
            .height(250.dp)
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )


    }
}
@Preview(showBackground = true)
@Composable
fun GalleryPreview(){
    FintTheme {
        Gallery()
    }

}