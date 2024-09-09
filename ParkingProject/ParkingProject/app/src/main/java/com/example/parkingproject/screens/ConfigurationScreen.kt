package com.example.parkingproject.screens

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.parkingproject.components.NavigationButomBar

@Composable
fun ConfigurationScreen(navController: NavController) {

    // SharedPreferences y variables
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("ConfigurationPrefence", Context.MODE_PRIVATE)
    var valorPorMinuto by remember { mutableStateOf("") }
    var plazasDisponibles by remember { mutableStateOf("") }

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
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                // Título
                Text(
                    text = "CONFIGURACIÓN",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "Valor por minuto",
                    fontWeight = FontWeight.Bold
                )
                TextField(
                    value = valorPorMinuto,
                    onValueChange = { newValue ->
                        valorPorMinuto = newValue
                    },
                    label = { Text("Ejemplo: 100") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions.Default
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Plazas disponibles
                Text(
                    text = "Plazas disponibles",
                    fontWeight = FontWeight.Bold
                )
                TextField(
                    value = plazasDisponibles,
                    onValueChange = { newValue ->
                        plazasDisponibles = newValue
                    },
                    label = { Text("Ejemplo: 12") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions.Default
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        saveData(sharedPreferences, "valorPorMinuto", valorPorMinuto)
                        saveData(sharedPreferences, "plazasDisponibles", plazasDisponibles)
                        // se implemento el pop que se pidio
                        navController.popBackStack()
                    },
                    modifier = Modifier.align(Alignment.End),
                    shape = RoundedCornerShape(0.dp)
                ) {
                    Text(text = "Guardar Ingreso")
                }
            }
        }
    )
}


private fun saveData(sharedPreferences: SharedPreferences, key: String, value: String) {
    with(sharedPreferences.edit()) {
        putString(key, value)
        apply()
    }
}
