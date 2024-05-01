package com.example.mapsappogg.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mapsappogg.R
import com.example.mapsappogg.models.UsersSettings
import com.example.mapsappogg.routes.Routes
import com.example.mapsappogg.viewmodel.MyViewModel

@Composable
fun RegisterScreen(myViewModel: MyViewModel, navController: NavController) {
    val context = LocalContext.current

    var checked by remember { mutableStateOf(false) }
    val userPrefs = UsersSettings(context)
    val goToNext by myViewModel.goToNext.observeAsState(false)
    val loggedUser by myViewModel.loggedUser.observeAsState("")

    val userEnterError by myViewModel.userEnterError.observeAsState(false)

    var userEmail: String by remember { mutableStateOf("") }
    var password: String by remember { mutableStateOf("") }
    var password2: String by remember { mutableStateOf("") }

    val processing by myViewModel.isProcessing.observeAsState(false)
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (processing) {
            Image(
                painterResource(id = R.drawable.logoregister),
                "REGISTER LOGO",
                Modifier
                    .fillMaxSize(0.3f)
                    .padding(0.dp)
            )
            if (userEnterError) Text(
                "FAILED TO REGISTER.\n" +
                        "YOUR PASSWORD MUST BE BETWEEN 6 - 20 CHARACTERS .\n" +
                        "PLEASE TRY AGAIN!.",
                color = Color.Red, modifier = Modifier.padding(horizontal = 25.dp)
            )
            TextField(
                value = userEmail,
                onValueChange = { if (userEmail.length < 75) userEmail = it },
                placeholder = { Text(text = "ENTER EMAIL") },
                modifier = Modifier
                    .padding(3.dp)
                    .fillMaxWidth(0.7f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            )
            TextFieldWithVisibility(
                password,
                placeholder = "ENTER PASSWORD",
                enterInput = { if (password.length < 20) password = it }
            )
            TextFieldWithVisibility(
                password2,
                placeholder = "REPEAT PASSWORD",
                enterInput = { if (password2.length < 20) password2 = it }
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // -------------------------------------------------------- GUARDAR USUARI I CONTRASENYA ---------------------------------------------------------------------
                Text("REMEMBER ME", fontSize = 20.sp)
                Checkbox(
                    checked = checked,
                    onCheckedChange = { checked = !checked }
                )
            }
            Button(
                onClick = {
                    myViewModel.register(userEmail, password)
                },
                modifier = Modifier.fillMaxWidth(0.3f),
                shape = RoundedCornerShape(20),
                enabled = (userEmail.isNotEmpty() && password.isNotEmpty() && password == password2)
            ) {
                Text(text = "REGISTER")
            }
            Button(
                onClick = {
                    myViewModel.setUserEnterError(false)
                    navController.navigate(Routes.LoginScreen.route)
                },
                modifier = Modifier.fillMaxWidth(0.3f),
                shape = RoundedCornerShape(20),
            ) {
                Text(text = "LOGIN")
            }
        } else {
            CircularProgressIndicator(
                modifier = Modifier.size(100.dp)
            )
        }
        LaunchedEffect(goToNext) {
            if (goToNext) {
                myViewModel.setUserEnterError(false)
                Toast.makeText(context, "WELCOME, ${loggedUser ?: "GUEST USER!"}", Toast.LENGTH_SHORT)
                    .show()
                userPrefs.saveUserData(userEmail, password)
                navController.navigate(Routes.MyDrawer.route)
                myViewModel.setUserEnterError(false)
            }
        }
    }
}