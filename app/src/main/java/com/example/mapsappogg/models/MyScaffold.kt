package com.example.mapsappogg.models


import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mapsappogg.routes.Routes
import com.example.mapsappogg.screens.CameraScreen
import com.example.mapsappogg.screens.GalleryScreen
import com.example.mapsappogg.screens.extras_screens.Geolocation
import com.example.mapsappogg.screens.MapScreen
import com.example.mapsappogg.screens.extras_screens.MarkerList
import com.example.mapsappogg.screens.TakePhotoScreen
import com.example.mapsappogg.viewmodel.MyViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission", "UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MyScaffold(myViewModel: MyViewModel, navController: NavController, state: DrawerState) {
    val locationPermissionState =
        rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)
    LaunchedEffect(Unit) {
        locationPermissionState.launchPermissionRequest()
    }
    Scaffold(
        topBar = { MyTopAppBar(myViewModel, state) }
    ) { paddingValues ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.Transparent),
            contentAlignment = Alignment.BottomStart
        ) {
            NavHost(
                navController = navController as NavHostController,
                startDestination = Routes.Geolocation.route
            ) {
                composable(Routes.Geolocation.route) {
                    Geolocation(
                        navController,
                        myViewModel
                    )
                }
                composable(Routes.MapScreen.route) {
                    MapScreen(
                        myViewModel,
                        navController
                    )
                }
                composable(Routes.CameraScreen.route) {
                    CameraScreen(
                        myViewModel,
                        navController
                    )
                }
                composable(Routes.MarkerList.route) {
                    MarkerList(
                        myViewModel,
                        navController
                    )
                }
                composable(Routes.TakePhotoScreen.route) {
                    TakePhotoScreen(
                        navController,
                        myViewModel
                    )
                }
                composable(Routes.GalleryScreen.route) {
                    GalleryScreen(
                        navController,
                        myViewModel
                    )
                }
            }
            Button(
                onClick = {
                    navController.navigate(Routes.CameraScreen.route)
                },
                modifier = Modifier
                    .size(75.dp)
                    .padding(10.dp)
                    .alpha(0.7f),
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp),
                border = BorderStroke(2.dp, Color.White),
            ) {
                Icon(Icons.Default.Add, contentDescription = "ADD MARKER.")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(myViewModel: MyViewModel, state: DrawerState) {
    val scope = rememberCoroutineScope()
    TopAppBar(
        title = { Text("MAPS APP OGG") },
        colors = TopAppBarDefaults.topAppBarColors(),
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    state.open()
                }
            }) {
                Icon(imageVector = Icons.Filled.Menu, contentDescription = "MAPS MENU")
            }
        }
    )
}