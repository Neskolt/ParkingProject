package com.example.parkingproject.screens

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.parkingproject.components.NavigationButomBar
import com.example.parkingproject.navigation.AppScreens
import com.example.parkingproject.data.Patente
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeList(navController: NavController) {

    // SharedPreferences, variables una variable para la query y ademas la lista
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("PatentePrefence", Context.MODE_PRIVATE)
    val gson = Gson()
    val listJson = sharedPreferences.getString("patentes", "[]")
    val listType = object : TypeToken<MutableList<Patente>>() {}.type
    val patenteList: MutableList<Patente> = gson.fromJson(listJson, listType)
    var searchQuery by remember { mutableStateOf("") }
    val filteredPatenteList by remember(searchQuery, patenteList) {
        derivedStateOf {
            patenteList.filter {
                it.patente.contains(searchQuery, ignoreCase = true)
            }
        }
    }
    val plazasDisponibles = remember {
        val configPreferences = context.getSharedPreferences("ConfigurationPrefence", Context.MODE_PRIVATE)
        getSavedData(configPreferences, "plazasDisponibles").toIntOrNull() ?: 12
    }
    val plazasLibres = plazasDisponibles - filteredPatenteList.size

    //uso del scaffold para poder usar la estrucutura base y no encontrar problemas con la barra de notificaciones
    //y los botones del dispositivo
    Scaffold(
        bottomBar = {
            NavigationButomBar(navController)
        },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {

                Text(
                    text = "DISPONIBLES: $plazasLibres",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Barra de busqueda que usa una query, la query usa los valores de la lista filteredPatenteList
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Buscar patente") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = plazasLibres > 0
                )

                // valida plazas disponibles
                if (plazasLibres <= 0) {
                    Text(
                        text = "No hay más plazas disponibles.",
                        color = androidx.compose.ui.graphics.Color.Red,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                LazyColumn {
                    items(filteredPatenteList) { item ->
                        Row(
                            modifier = Modifier
                                .clickable {
                                    navController.navigate(AppScreens.VehicleDetails.createRoute(item.patente))
                                }
                                .padding(8.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = item.patente,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = item.horaIngreso,
                                modifier = Modifier.weight(1f),
                                textAlign = androidx.compose.ui.text.style.TextAlign.End
                            )
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
