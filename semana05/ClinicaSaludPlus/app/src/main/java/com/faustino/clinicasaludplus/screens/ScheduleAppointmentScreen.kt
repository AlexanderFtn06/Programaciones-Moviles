package com.faustino.clinicasaludplus.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.faustino.clinicasaludplus.data.Doctor
import com.faustino.clinicasaludplus.navigation.Screen

private val fechas = listOf("Jue 26", "Vie 27", "Sáb 28")
private val horas = listOf("5:00", "10:30", "3:00")

@Composable
fun ScheduleAppointmentScreen(navController: NavController, doctor: Doctor) {

    var selectedFecha by remember { mutableStateOf(fechas[1]) }
    var selectedHora by remember { mutableStateOf(horas[1]) }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("Agendar cita con ${doctor.name}", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(20.dp))

        Text("Selecciona fecha", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            fechas.forEach { fecha ->
                FilterChip(
                    selected = selectedFecha == fecha,
                    onClick = { selectedFecha = fecha },
                    label = { Text(fecha) }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text("Selecciona hora", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            horas.forEach { hora ->
                FilterChip(
                    selected = selectedHora == hora,
                    onClick = { selectedHora = hora },
                    label = { Text(hora) }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                navController.navigate(
                    Screen.Confirmation.createRoute(doctor.id, selectedFecha, selectedHora)
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Confirmar cita")
        }
    }
}