package com.example.parkingproject.screens

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.parkingproject.components.NavigationButomBar
import com.example.parkingproject.data.Patente
import com.example.parkingproject.navigation.AppScreens
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun AddScreen(navController: NavController) {

    // SharedPreferences y variables
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("PatentePrefence", Context.MODE_PRIVATE)
    var text by remember { mutableStateOf("") }
    val configPreferences = context.getSharedPreferences("ConfigurationPrefence", Context.MODE_PRIVATE)
    val plazasDisponibles = remember {
        getSavedData(configPreferences, "plazasDisponibles").toIntOrNull() ?: 12
    }

    val gson = Gson()
    val listJson = sharedPreferences.getString("patentes", "[]")
    val listType = object : TypeToken<MutableList<Patente>>() {}.type
    val patenteList: MutableList<Patente> = gson.fromJson(listJson, listType)

    // plazas libres
    val plazasLibres = plazasDisponibles - patenteList.size

    //uso del scaffold para poder usar la estrucutura base y no encontrar problemas con la barra de notificaciones
    //y los botones del dispositivo
    Scaffold(
        bottomBar = {
            NavigationButomBar(navController)
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .width(300.dp)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Patente", style = MaterialTheme.typography.headlineLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = text,
                        onValueChange = { newValue -> text = newValue },
                        label = { Text("RF-PF-21") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    if (plazasLibres <= 0) {
                        Text(
                            text = "No hay plazas disponibles.",
                            color = androidx.compose.ui.graphics.Color.Red
                        )
                    } else {
                        Button(
                            onClick = {
                                if (text.isNotEmpty()) {
                                    val patente = text
                                    val currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

                                    // instancia nueva para la Patente
                                    val newPatente =
                                        Patente(patente = patente, horaIngreso = currentTime)
                                    patenteList.add(newPatente)

                                    val updatedListJson = gson.toJson(patenteList)
                                    with(sharedPreferences.edit()) {
                                        putString("patentes", updatedListJson)
                                        apply()
                                    }
                                    // aqui deberia haber un pop pero queria usar el parametro, por lo mismo tambien esta
                                    // modificado en las appscreens, en el proyecto el flujo se uso de 2 maneras
                                    // tambien se uso la clase Patente para usarlo como objeto
                                    navController.navigate("${AppScreens.HomeList.route}?customParam=${patente}")
                                    text = ""
                                }
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text(text = "Guardar Ingreso")
                        }
                    }
                }
            }
        }
    )
}

private fun getSavedData(sharedPreferences: SharedPreferences, key: String): String {
    return sharedPreferences.getString(key, "") ?: ""
}
