package com.example.parkingproject.components

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.parkingproject.navigation.AppScreens


@Composable
// funcion que posteriormente sera llama desde todas las pantallas para implementar la barra de navegacion
fun NavigationButomBar(navController:NavController){
    NavigationBar {
        NavigationBarItem(
            label={
                Text(text="Inicio Vehiculos")
            },
            selected = false ,
            onClick = {
                navController.navigate(AppScreens.HomeList.route)
            },
            icon = {

            })
        NavigationBarItem(
            label={
                Text(text="Ingreso")
            },
            selected = false ,
            onClick = {
                navController.navigate(AppScreens.AddScreen.route)
            },
            icon = {

            })
        NavigationBarItem(
            label={
                Text(text="Configuracion")
            },
            selected = false ,
            onClick = {
                navController.navigate(AppScreens.ConfigurationScreen.route)
            },
            icon = {

            })
    }


}
