package com.example.mapsappogg.routes

sealed class Routes(val route: String) {
    object LauncherScreen : Routes("LaunchScreen - Screen1")
    object CameraScreen : Routes("CameraScreen - Screen2")
    object TakePhotoScreen : Routes("PhotoScreen - Screen3")
    object RegisterScreen : Routes("RegisterScreen - Screen4")
    object MapScreen : Routes("MapScreen - Screen5")
    object Geolocation : Routes("Geolocation - Screen6")
    object MyDrawer : Routes("MyDrawer - Screen7")
    object LoginScreen : Routes("loginScreen - Screen8")
    object CreateAPP : Routes("CreateAPP - Screen9")
    object GalleryScreen : Routes("GalleryScreen - Screen10")
    object MarkerList : Routes("MarkerList - Screen11")
}