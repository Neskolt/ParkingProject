package com.example.parkingproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.parkingproject.navigation.Router
import com.example.parkingproject.ui.theme.ParkingProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ParkingProjectTheme {
                //Tal como se explico en clases podriamos haber colocado todas las screens aqui
                //pero se implemento una estructura mas organizada donde la funcion Router controllara la navegacion
                Router()
            }
        }
    }
}
