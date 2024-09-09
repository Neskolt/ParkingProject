package com.example.parkingproject.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.parkingproject.screens.AddScreen
import com.example.parkingproject.screens.ConfigurationScreen
import com.example.parkingproject.screens.HomeList
import com.example.parkingproject.screens.VehicleDetails

@Composable
fun Router() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppScreens.HomeList.route) {
        composable(AppScreens.AddScreen.route) {
            AddScreen(navController)
        }
        composable(AppScreens.ConfigurationScreen.route) {
            ConfigurationScreen(navController)
        }
        composable(AppScreens.HomeList.route) {
            HomeList(navController)
        }
        composable(
            route = AppScreens.VehicleDetails.route,
            arguments = listOf(navArgument("patente") { type = NavType.StringType })
        ) { backStackEntry ->
            val patente = backStackEntry.arguments?.getString("patente") ?: ""
            VehicleDetails(navController, patente)
        }
    }
}