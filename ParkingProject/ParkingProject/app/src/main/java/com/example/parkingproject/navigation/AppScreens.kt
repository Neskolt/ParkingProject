package com.example.parkingproject.navigation

sealed class AppScreens(val route: String) {
    object HomeList : AppScreens("home_list")
    object AddScreen : AppScreens("add_screen")
    object ConfigurationScreen : AppScreens("configuration_screen")
    object VehicleDetails : AppScreens("vehicle_details/{patente}") {
        fun createRoute(patente: String) = "vehicle_details/$patente"
    }
}