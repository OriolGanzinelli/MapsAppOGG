package com.example.mapsappogg.models

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mapsappogg.routes.Routes
import com.example.mapsappogg.screens.LauncherScreen
import com.example.mapsappogg.screens.LoginScreen
import com.example.mapsappogg.screens.RegisterScreen
import com.example.mapsappogg.viewmodel.MyViewModel

@Composable
fun CreateAPP(myViewModel: MyViewModel) {
    val mainNavController = rememberNavController()

    NavHost(
        navController = mainNavController,
        startDestination = Routes.LauncherScreen.route
    ) {
        composable(Routes.LauncherScreen.route) {
            LauncherScreen(mainNavController)
        }
        composable(Routes.LoginScreen.route) {
            LoginScreen(myViewModel, mainNavController)
        }
        composable(Routes.RegisterScreen.route) {
            RegisterScreen(myViewModel, mainNavController)
        }
        composable(Routes.MyDrawer.route) {
            MyDrawer(myViewModel, mainNavController)
        }
        composable(Routes.CreateAPP.route) {
            CreateAPP(myViewModel)
        }
    }
}