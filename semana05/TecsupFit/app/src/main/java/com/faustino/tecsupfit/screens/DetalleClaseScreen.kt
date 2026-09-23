package com.faustino.tecsupfit.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.faustino.tecsupfit.navigation.Screen
import com.faustino.tecsupfit.repository.ReservasRepository

@Composable
fun DetalleClaseScreen(navController: NavController, claseId: Int) {

    // Recupera la clase elegida usando el id que llegó por navegación
    val clase = ReservasRepository.obtenerClasePorId(claseId)

    if (clase == null) {
        Text("Clase no encontrada", modifier = Modifier.padding(16.dp))
        return
    }

    // Turno seleccionado (se comporta como RadioButton: una sola opción posible)
    var turnoSeleccionado by remember { mutableStateOf(clase.horariosDisponibles.first()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = clase.nombre, style = MaterialTheme.typography.headlineSmall)
        Text(text = "Instructor: ${clase.instructor}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Cupos disponibles: ${clase.cuposDisponibles}", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(12.dp))
        Text(text = clase.descripcion, style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Elige un turno", style = MaterialTheme.typography.titleMedium)

        // Selección de opción única (horario/cupo): chips que funcionan como RadioButton
        Column(modifier = Modifier.padding(top = 8.dp)) {
            clase.horariosDisponibles.forEach { turno ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = turno == turnoSeleccionado,
                            onClick = { turnoSeleccionado = turno }
                        )
                        .padding(vertical = 4.dp)
                ) {
                    RadioButton(
                        selected = turno == turnoSeleccionado,
                        onClick = { turnoSeleccionado = turno }
                    )
                    Text(text = turno)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                ReservasRepository.agregarReserva(clase)
                navController.navigate(Screen.Confirmacion.createRoute(clase.id))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reservar cupo")
        }
    }
}