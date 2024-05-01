package com.example.mapsappogg.screens.extras_screens

import android.Manifest
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.mapsappogg.R
import com.example.mapsappogg.models.MySearchBar
import com.example.mapsappogg.routes.Routes
import com.example.mapsappogg.viewmodel.MyViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun MarkerList(myViewModel: MyViewModel, navController: NavController) {
    val myMarkers by myViewModel.listOfMarkers.observeAsState()

    val searchText by myViewModel.searchText.observeAsState("")
    val showBottomSheet by myViewModel.bottomSheet.observeAsState(false)
    val selectedMarker by myViewModel.selectedMarker.observeAsState(null)

    if (showBottomSheet) {
        AddMarkerBottomSheet(
            myViewModel,
            navController,
            selectedMarker!!
        )
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MySearchBar(myViewModel)
        if (!myMarkers!!.isEmpty()) {
            LazyColumn(
                Modifier.fillMaxSize()
            ) {
                items(myMarkers!!.reversed().filter {
                    it.title.contains(searchText ?: "", ignoreCase = true)
                }.toMutableList()) {
                    Box {
                        ElevatedCard(
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 6.dp
                            ),
                            shape = RoundedCornerShape(5),
                            modifier = Modifier
                                .padding(vertical = 2.dp, horizontal = 7.dp)
                                .fillMaxWidth()
                                .fillMaxHeight(0.3f)
                                .clickable {
                                    myViewModel.changeCurrentLocation(it.position)
                                    navController.navigate(Routes.MapScreen.route)
                                }
                        ) {
                            val cameraPermissionState =
                                rememberPermissionState(permission = Manifest.permission.CAMERA)
                            LaunchedEffect(Unit) {
                                cameraPermissionState.launchPermissionRequest()
                            }
                            Row(
                                Modifier
                                    .fillMaxSize()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                if (it.photo != null && it.photo != "null") {
                                    GlideImage(
                                        model = it.photo,
                                        contentDescription = "IMAGE FROM STORAGE",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.size(60.dp)
                                    )
                                } else {
                                    Image(
                                        painterResource(id = R.drawable.logomaps),
                                        "LOGO MAP",
                                        Modifier.size(60.dp)
                                    )
                                }
                                //}
                                Column(
                                    modifier = Modifier.fillMaxWidth(0.5f)
                                ) {
                                    Text(it.title, fontWeight = FontWeight.Bold)
                                    Text(it.snippet)
                                }
                                IconButton(
                                    onClick = {
                                        myViewModel.selectMarker(it)
                                        myViewModel.selectImage(it.photo?.toUri())
                                        myViewModel.showBottomSheet()
                                    }) {
                                    Icon(
                                        imageVector = Icons.Default.Create,
                                        contentDescription = "EDIT MARKER"
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        myViewModel.deleteMarker(it)
                                        if (it.photo != null && it.photo != "NULL") myViewModel.removeImage(
                                            it.photo!!
                                        )
                                        myViewModel.selectMarker(null)
                                        myViewModel.getSavedMarkers()
                                    }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "DELETE MARKER"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } else {
            Text(
                text = "NO MARKERS YET",
                fontSize = 20.sp,
                modifier = Modifier.padding(20.dp)
            )
        }
    }
}