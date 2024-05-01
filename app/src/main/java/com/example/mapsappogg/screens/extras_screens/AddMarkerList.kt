package com.example.mapsappogg.screens.extras_screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.mapsappogg.R
import com.example.mapsappogg.models.Marker
import com.example.mapsappogg.routes.Routes
import com.example.mapsappogg.viewmodel.MyViewModel
import com.google.android.gms.maps.model.LatLng

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun AddMarkerBottomSheet(
    myViewModel: MyViewModel,
    navController: NavController,
    selectedMarker: Marker,
    fileName: String? = null
) {
    val markerReady by myViewModel.markerReady.observeAsState(false)

    val userId by myViewModel.userId.observeAsState(null)
    var newMarkerTitle: String by remember { mutableStateOf(selectedMarker.title) }
    var newMarkerSnippet: String by remember { mutableStateOf(selectedMarker.snippet) }
    var newMarkerLat: Double by remember { mutableStateOf(selectedMarker.position.latitude) }
    var newMarkerLong: Double by remember { mutableStateOf(selectedMarker.position.longitude) }
    var newMarkerColor: Float by remember { mutableStateOf(selectedMarker.color) }
    val newMarkerPhoto by myViewModel.selectedImage.observeAsState(null)
    val imageUrl by myViewModel.imageUrl.observeAsState(null)

    ModalBottomSheet(onDismissRequest = {
        myViewModel.selectMarker(null)
        myViewModel.hideBottomSheet()
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .padding(15.dp),
                contentAlignment = Alignment.Center
            ) {
                if (newMarkerPhoto.toString() != "null") {
                    GlideImage(
                        model = newMarkerPhoto.toString(),
                        contentDescription = "STORAGE IMAGE",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                }
                IconButton(onClick = {
                    myViewModel.hideBottomSheet()
                    navController.navigate(Routes.CameraScreen.route)
                }
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        "TAKE A PHOTO",
                        tint = Color.LightGray
                    )
                }
            }
            Text(text = fileName ?: "TAKE PHOTO OR SELECT IMAGE")
            TextField(
                value = newMarkerTitle,
                onValueChange = {
                    newMarkerTitle = it
                    myViewModel.selectMarker(
                        Marker(
                            userId,
                            selectedMarker.markerId,
                            LatLng(newMarkerLat, newMarkerLong),
                            newMarkerTitle,
                            newMarkerSnippet,
                            newMarkerColor,
                            newMarkerPhoto.toString()
                        )
                    )
                },
                placeholder = { Text(text = "TITLE") },
                modifier = Modifier
                    .padding(3.dp)
                    .fillMaxWidth(0.8f)
            )
            TextField(
                value = newMarkerSnippet,
                onValueChange = {
                    newMarkerSnippet = it
                    myViewModel.selectMarker(
                        Marker(
                            userId,
                            selectedMarker.markerId,
                            LatLng(newMarkerLat, newMarkerLong),
                            newMarkerTitle,
                            newMarkerSnippet,
                            newMarkerColor,
                            newMarkerPhoto.toString()
                        )
                    )
                },
                placeholder = { Text(text = "DESCRIPTION") },
                modifier = Modifier
                    .padding(3.dp)
                    .fillMaxWidth(0.8f),
            )
            TextField(
                value = "$newMarkerLat",
                onValueChange = { newMarkerLat = it.toDouble() },
                placeholder = { Text(text = "LATITUDE") },
                modifier = Modifier
                    .padding(3.dp)
                    .fillMaxWidth(0.8f),
                enabled = false
            )
            TextField(
                value = "$newMarkerLong",
                onValueChange = { newMarkerLong = it.toDouble() },
                placeholder = { Text(text = "LENGHT") },
                modifier = Modifier
                    .padding(3.dp)
                    .fillMaxWidth(0.8f),
                enabled = false
            )

            // ------------------------------------------------------------------ GUARDAR MARCADOR -----------------------------------------------------------------------------
            Button(
                onClick = {
                    myViewModel.uploadImage(
                        newMarkerPhoto,
                        fileName ?: "${System.currentTimeMillis()}",
                        selectedMarker.photo
                    )
                },
                shape = RoundedCornerShape(10),
                enabled = true,
                modifier = Modifier.padding(15.dp)
            ) {
                Text("SAVE MARKER")
            }

            if (markerReady) {
                // -------------------------------------------------------------- GUARDAR NOU MARCADOR -------------------------------------------------------------------------
                if (selectedMarker.markerId == null) {
                    myViewModel.saveMarker(
                        Marker(
                            userId, null,
                            LatLng(newMarkerLat, newMarkerLong),
                            if (!newMarkerTitle.isBlank()) newMarkerTitle else "MARKER WITHOUT NAME",
                            if (!newMarkerSnippet.isBlank()) newMarkerSnippet else "NO DESCRIPTION",
                            newMarkerColor, imageUrl.toString()
                        )
                    )
                // -------------------------------------------------------------------- EDITAR MARCADOR -------------------------------------------------------------------------
                } else {
                    myViewModel.editMarker(
                        Marker(
                            userId,
                            selectedMarker.markerId,
                            LatLng(newMarkerLat, newMarkerLong),
                            if (!newMarkerTitle.isBlank()) newMarkerTitle else "MARKER WITHOUT NAMEr",
                            if (!newMarkerSnippet.isBlank()) newMarkerSnippet else "NNO DESCRIPTION",
                            newMarkerColor, imageUrl.toString()
                        )
                    )
                }
                // -------------------------------------------------------------------- RESTAURAR VALORS -------------------------------------------------------------------------
                myViewModel.changeCurrentLocation(selectedMarker.position)
                myViewModel.hideBottomSheet()
                myViewModel.getSavedMarkers()
                navController.navigate(Routes.MapScreen.route)
                myViewModel.selectMarker(null)
                myViewModel.selectImage(null)
                myViewModel.selectImageUrl(null)
                myViewModel.confirmMarkerReady(false)
            }
        }
    }
}