package com.example.mapsappogg.screens.extras_screens

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.location.Location
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.navigation.NavController
import com.example.mapsappogg.routes.MainActivity
import com.example.mapsappogg.routes.Routes
import com.example.mapsappogg.viewmodel.MyViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

@Composable
@SuppressLint("MissingPermission")
fun Geolocation(navController: NavController, myViewModel: MyViewModel) {
    val context = LocalContext.current
    val isLocationPermissionGranted by myViewModel.locationPermissionGranted.observeAsState(false)
    val shouldShowPermissionRationale by myViewModel.shouldShowLocationPermissionRationale.observeAsState(
        false
    )
    val permissionDenied by myViewModel.locationPermissionDenied.observeAsState(false)
    val locationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                myViewModel.setLocationPermissionGranted(true)
            } else {
                myViewModel.setShouldLocationPermissionRationale(
                    shouldShowRequestPermissionRationale(
                        context as Activity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                )
                if (!shouldShowPermissionRationale) {
                    Log.i("MAPSCREEN", "CAN'T ASK PERMISSION.")
                    myViewModel.setShowLocationPermissionDenied(true)
                }
            }
        }
    )
    Log.i("Boolean", "$isLocationPermissionGranted")
    if (!isLocationPermissionGranted) {
        SideEffect { // ---------------------------------------------------------- EVITAR ERROR JAVA LANG -----------------------------------------------------------------------
            locationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    } else {
        var deviceLatLng by remember { mutableStateOf(LatLng(0.0, 0.0)) }
        val fusedLocationProviderClient =
            remember { LocationServices.getFusedLocationProviderClient(context) }
        var lastKnownLocation by remember { mutableStateOf<Location?>(null) }
        val locationResult = fusedLocationProviderClient.getCurrentLocation(100, null)
        locationResult.addOnCompleteListener(context as MainActivity) { task ->
            if (task.isSuccessful) {
                lastKnownLocation = task.result
                deviceLatLng = LatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude)
                myViewModel.changeCurrentLocation(deviceLatLng)
            } else {
                Log.e("Error", "Exception %s", task.exception)
            }
        }
        navController.navigate(Routes.MapScreen.route)
    }
    if (permissionDenied)
        PermissionsLocation()
}