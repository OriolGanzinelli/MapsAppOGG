package com.example.mapsappogg.models

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mapsappogg.routes.Routes
import com.example.mapsappogg.viewmodel.MyViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDrawer(myViewModel: MyViewModel, mainNavController: NavController) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val state: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val loggedUser by myViewModel.loggedUser.observeAsState("")

    val mapList: List<String> = listOf("NORMAL", "SATELLITE", "TERRAIN", "HYBRID")
    var mapText by remember { mutableStateOf("TERRAIN") }
    val traffic by myViewModel.trafficEnabled.observeAsState(false)
    var expanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val usersSettings = UsersSettings(context)

    ModalNavigationDrawer(
        drawerState = state,
        gesturesEnabled = false,
        drawerContent = {
            ModalDrawerSheet {
                IconButton(onClick = {
                    scope.launch {
                        state.close()
                    }
                }) {
                    Icon(imageVector = Icons.Filled.Menu, contentDescription = "MENU")
                }
                NavigationDrawerItem(
                    label = { Text("SEE MAP") },
                    selected = false,
                    onClick = {
                        scope.launch {
                            state.close()
                        }
                        navController.navigate(Routes.Geolocation.route)
                    }
                )
                NavigationDrawerItem(
                    label = { Text("MARKERS LIST") },
                    selected = false,
                    onClick = {
                        scope.launch {
                            state.close()
                        }
                        navController.navigate(Routes.MarkerList.route)
                    }
                )
                Divider()
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        Modifier
                            .padding(10.dp)
                            .fillMaxWidth(0.5f)
                    ) {
                        OutlinedTextField(
                            value = mapText,
                            onValueChange = {},
                            enabled = false,
                            readOnly = true,
                            label = {
                                Text(
                                    text = "TYPE OF MAP", color = Color.Black,
                                    fontSize = 15.sp
                                )
                            },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = expanded
                                )
                            },
                            modifier = Modifier.clickable { expanded = true },
                            colors = TextFieldDefaults.colors()
                        )
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            mapList.forEach { option ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = option,
                                            fontSize = 25.sp,
                                            color = Color.Black
                                        )
                                    },
                                    onClick = {
                                        expanded = false
                                        myViewModel.changeMapType(mapList.indexOf(option))
                                        mapText = option
                                    }
                                )
                            }
                        }
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text("TRAFFIC ROADS", modifier = Modifier.padding(15.dp))
                    Checkbox(
                        checked = traffic,
                        onCheckedChange = { myViewModel.enableTraffic() }
                    )
                }
                Divider()
                Text(
                    text = loggedUser ?: "GUEST USER",
                    modifier = Modifier
                        .padding(10.dp)
                )
                if (loggedUser != null) {
                    Button(
                        onClick = {
                            scope.launch {
                                state.close()
                            }
                            myViewModel.logout()
                            CoroutineScope(Dispatchers.IO).launch {
                                usersSettings.deleteUserData()
                            }
                            mainNavController.navigate(Routes.CreateAPP.route)
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.4f)
                            .padding(horizontal = 10.dp),
                        shape = RoundedCornerShape(10),
                    ) {
                        Text(text = "LOGOUT")
                    }
                } else {
                    Button(
                        onClick = {
                            mainNavController.navigate(Routes.CreateAPP.route)
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.4f)
                            .padding(horizontal = 10.dp),
                        shape = RoundedCornerShape(10),
                    ) {
                        Text(text = "LOGIN")
                    }
                }

            }
        })
    {
        MyScaffold(myViewModel, navController, state)
    }
}