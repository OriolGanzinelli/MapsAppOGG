package com.example.mapsappogg.screens.extras_screens

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight

@Composable
fun PermissionsLocation() {
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "PERMISSION REQUIRED", fontWeight = FontWeight.Bold)
        Text(text = "APP NEED ACCESS TO THE CAMERA TO TAKE PHOTOS")
        Button(onClick = {
            openAppSettings(context as Activity)
        }) {
            Text(text = "ACCEPT")
        }
    }
}