package com.example.mapsappogg.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.mapsappogg.screens.extras_screens.AddMarkerBottomSheet
import com.example.mapsappogg.models.Marker
import com.example.mapsappogg.viewmodel.MyViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_RED
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(myViewModel: MyViewModel, navController: NavController) {
    val currentLocation: LatLng by myViewModel.selectedLocation.observeAsState(LatLng(0.0, 0.0))
    val selectedMarker by myViewModel.selectedMarker.observeAsState(null)
    val mapType by myViewModel.selectedMapType.observeAsState(MapType.TERRAIN)
    val trafficEnabled by myViewModel.trafficEnabled.observeAsState(false)

    val cameraPositionState =
        rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(currentLocation, 6f)
        }
    LaunchedEffect(currentLocation) {
        cameraPositionState.position = CameraPosition.fromLatLngZoom(currentLocation, 15f)
    }
    val showBottomSheet by myViewModel.bottomSheet.observeAsState(false)

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = {
                myViewModel.changeCurrentLocation(it)
                cameraPositionState.position = CameraPosition.fromLatLngZoom(currentLocation, 15f)
            },
            onMapLongClick = {
                myViewModel.changeCurrentLocation(it)
                cameraPositionState.position = CameraPosition.fromLatLngZoom(currentLocation, 15f)
                myViewModel.showBottomSheet()
            },
            properties = MapProperties(
                mapType = mapType,
                isMyLocationEnabled = true,
                isTrafficEnabled = trafficEnabled
            )
        ) {
            val myMarkers by myViewModel.listOfMarkers.observeAsState()
            myViewModel.getSavedMarkers()
            myMarkers!!.forEach {
                Marker(
                    state = MarkerState(position = it.position),
                    title = it.title,
                    snippet = it.snippet,
                    icon = BitmapDescriptorFactory.defaultMarker(it.color),
                    onInfoWindowLongClick = { marker ->
                        myViewModel.deleteMarker(it)
                        if (it.photo != null && it.photo != "NULL") myViewModel.removeImage(it.photo!!)
                    }
                )
            }
        }
    }
    if (showBottomSheet) {
        myViewModel.selectMarker(
            Marker(
                null, null, currentLocation,
                "", "", HUE_RED, null
            )
        )
        AddMarkerBottomSheet(
            myViewModel,
            navController,
            selectedMarker!!
        )
    }
}