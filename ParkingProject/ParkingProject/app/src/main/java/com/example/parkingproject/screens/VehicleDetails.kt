package com.example.parkingproject.screens

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.isPopupLayout
import androidx.navigation.NavController
import com.example.parkingproject.components.NavigationButomBar
import com.example.parkingproject.data.Patente
import com.example.parkingproject.navigation.AppScreens
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun VehicleDetails(navController: NavController, patente: String) {

    // SharedPreferences, variables y uso de la implementacion Gson para poder usar la lista
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("ConfigurationPrefence", Context.MODE_PRIVATE)
    val valorPorMinuto = remember { getSavedData(sharedPreferences, "valorPorMinuto").toFloatOrNull() ?: 0f }
    val patentePreferences = context.getSharedPreferences("PatentePrefence", Context.MODE_PRIVATE)
    val gson = Gson()
    val listJson = patentePreferences.getString("patentes", "[]")
    val listType = object : TypeToken<MutableList<Patente>>() {}.type
    val patenteList: MutableList<Patente> = gson.fromJson(listJson, listType)
    val patenteSeleccionada = patenteList.find { it.patente == patente }
    val horaIngreso = patenteSeleccionada?.horaIngreso

    // Validacion
    if (patenteSeleccionada == null || horaIngreso.isNullOrEmpty()) {
        Text("Error: Patente no encontrada o sin hora de ingreso")
    } else {
        // Define los formatos para poder mostrarlos bien en la lista
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val horaIngresoDateTime = try {
            LocalDateTime.parse(horaIngreso, formatter)
        } catch (e: Exception) {
            null
        }

        // Try catch para asegurar que la aplicacion no se caiga
        if (horaIngresoDateTime == null) {
            Text("Error: Formato de fecha inválido")
        } else {
            val currentTime = LocalDateTime.now()
            val minutosEstadia = Duration.between(horaIngresoDateTime, currentTime).toMinutes().toInt()

            val valorAPagar = minutosEstadia * valorPorMinuto

            //uso del scaffold para poder usar la estrucutura base y no encontrar problemas con la barra de notificaciones
            //y los botones del dispositivo
            Scaffold(
                bottomBar = {
                    NavigationButomBar(navController)
                },
                content = { paddingValues ->
                    Column(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = patente,
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.height(32.dp))

                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top
                        ) {
                            Text(text = "RESUMEN", style = MaterialTheme.typography.headlineMedium)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = "Hora de Ingreso: $horaIngreso")
                            Text(text = "Minutos de estadía: $minutosEstadia")
                            Text(text = "Valor del minuto: $valorPorMinuto")

                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = "VALOR A PAGAR", style = MaterialTheme.typography.headlineMedium)
                            Text(text = "$valorAPagar")

                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    // Uso del SharedPrefeces.edit para modificar los contenidos de la lista y se uso un pop
                                    // para poder hacer buen uso de la navegacion
                                    patenteList.remove(patenteSeleccionada)
                                    val updatedListJson = gson.toJson(patenteList)
                                    with(patentePreferences.edit()) {
                                        putString("patentes", updatedListJson)
                                        apply()
                                    }
                                    navController.popBackStack()
                                }
                            ) {
                                Text(text = "Marcar salida")
                            }
                        }
                    }
                }
            )
        }
    }
}

private fun getSavedData(sharedPreferences: SharedPreferences, key: String): String {
    return sharedPreferences.getString(key, "") ?: ""
}
