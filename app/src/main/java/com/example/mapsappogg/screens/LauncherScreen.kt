package com.example.mapsappogg.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.mapsappogg.R
import com.example.mapsappogg.routes.Routes
import kotlinx.coroutines.delay

@Composable
fun LauncherScreen(navController: NavController) {
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1750),
        label = ""
    )
    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(1500)
        navController.popBackStack()
        navController.navigate(Routes.LoginScreen.route)
    }
    Launcher(alphaAnim.value)
}

@Composable
fun Launcher(alphaAnim: Float) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logomaps),
                modifier = Modifier.fillMaxSize(0.2f),
                contentDescription = "LOGO",
                alpha = alphaAnim
            )
        }
    }
}