package com.example.fint.components

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun EmbeddedGoogleMap() {
    AndroidView(factory = { context ->
        WebView(context).apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true // Needed for Google Maps Embed
            loadUrl(
                "https://www.google.com/maps/embed/v1/place?key=AIzaSyChyo1ZYe6YU3eJ4xNuwqf1tyvoYt_9oB8=Faulu+School,Nairobi"
            )
        }
    })
}
