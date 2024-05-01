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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mapsappogg.R
import com.example.mapsappogg.models.UsersSettings
import com.example.mapsappogg.routes.Routes
import com.example.mapsappogg.viewmodel.MyViewModel

@Composable
fun LoginScreen(myViewModel: MyViewModel, navController: NavHostController) {
    val context = LocalContext.current

    val goToNext by myViewModel.goToNext.observeAsState(false)
    val loggedUser by myViewModel.loggedUser.observeAsState("")

    var userEmail: String by remember { mutableStateOf("") }
    var password: String by remember { mutableStateOf("") }

    var checked by remember { mutableStateOf(false) }
    val userPrefs = UsersSettings(context)
    val storedUserData = userPrefs.getUserData.collectAsState(initial = emptyList())

    var autoLogin: Boolean by remember { mutableStateOf(false) }
    autoLogin = (storedUserData.value.isNotEmpty() && storedUserData.value[0] != "" && storedUserData.value[1] != "")

    println("AUTOLOIGN: $autoLogin")
    println("STORED USER DATA: ${if (storedUserData.value.isNotEmpty()) "${storedUserData.value}" else "false"}")

    val userEnterError by myViewModel.userEnterError.observeAsState(false)
    if (autoLogin) {
        myViewModel.login(storedUserData.value[0], storedUserData.value[1])
        userEmail = storedUserData.value[0]
        password = storedUserData.value[1]
    }
    val processing by myViewModel.isProcessing.observeAsState(false)
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (processing) {
            Image(
                painterResource(id = R.drawable.logologin),
                "LOGIN LOGO",
                Modifier.fillMaxSize(0.3f)
            )
            if (userEnterError) Text("FAILED TO LOGIN! PLEASE TRY AGAIN!.",
                color = Color.Red, modifier = Modifier.padding(35.dp))
            TextField(
                value = userEmail,
                onValueChange = { if (userEmail.length < 75) userEmail = it },
                placeholder = { Text(text = "ENTER EMAIL") },
                modifier = Modifier.padding(3.dp).fillMaxWidth(0.7f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            )
            TextFieldWithVisibility(
                password,
                placeholder = "ENTER PASSWORD",
                enterInput = { if (password.length < 20) password = it }
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // ------------------------------------------------------------ SAVE EMAIL AND PASSWD -------------------------------------------------------------------------
                Text("REMEMBER ME", fontSize = 20.sp)
                Checkbox(
                    checked = checked,
                    onCheckedChange =  { checked = !checked }
                )
            }
            Button(
                onClick = {
                    myViewModel.login(userEmail, password)
                },
                modifier = Modifier.fillMaxWidth(0.3f),
                shape = RoundedCornerShape(20),
                enabled = (userEmail.isNotEmpty() && password.isNotEmpty())
            ) {
                Text(text = "LOGIN")
            }
            Button(
                onClick = {
                    myViewModel.setUserEnterError(false)
                    navController.navigate(Routes.RegisterScreen.route)
                },
                modifier = Modifier.fillMaxWidth(0.3f),
                shape = RoundedCornerShape(20),
            ) {
                Text(text = "REGISTER")
            }
        } else {
            CircularProgressIndicator(
                modifier = Modifier.size(100.dp)
            )
        }
        LaunchedEffect(goToNext) {
            if (goToNext) {
                if (checked) userPrefs.saveUserData(userEmail, password)
                myViewModel.setUserEnterError(false)
                Toast.makeText(context, "WELCOME, ${loggedUser ?: "GUEST USER!"}", Toast.LENGTH_SHORT).show()
                navController.navigate(Routes.MyDrawer.route)
            }
        }
    }
}

@Composable
fun TextFieldWithVisibility(
    userInput: String,
    placeholder: String,
    enterInput: (String) -> Unit
) {
    var input by remember { mutableStateOf(userInput) }
    var showInput by remember { mutableStateOf(false) }
    TextField(
        value = input,
        onValueChange =
        {
            input = it
            enterInput(input)
        },
        visualTransformation =  if (showInput) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        placeholder = { Text(text = placeholder) },
        modifier = Modifier.padding(3.dp).fillMaxWidth(0.7f),
        trailingIcon = {
            if (showInput) {
                IconButton(onClick = { showInput = false }) {
                    Icon(
                        imageVector = Icons.Filled.Visibility,
                        contentDescription = "HIDE PASSWD"
                    )
                }
            } else {
                IconButton(
                    onClick = { showInput = true }) {
                    Icon(
                        imageVector = Icons.Filled.VisibilityOff,
                        contentDescription = "HIDE PASSWD"
                    )
                }
            }
        }
    )
}