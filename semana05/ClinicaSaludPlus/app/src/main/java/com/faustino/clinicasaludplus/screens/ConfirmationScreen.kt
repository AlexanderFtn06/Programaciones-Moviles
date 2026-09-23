package com.faustino.clinicasaludplus.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.faustino.clinicasaludplus.data.Appointment
import com.faustino.clinicasaludplus.data.AppointmentsStore
import com.faustino.clinicasaludplus.data.Doctor
import com.faustino.clinicasaludplus.navigation.Screen

@Composable
fun ConfirmationScreen(navController: NavController, doctor: Doctor, fecha: String, hora: String) {

    LaunchedEffect(Unit) {
        AppointmentsStore.addAppointment(
            Appointment(
                doctorName = doctor.name,
                fecha = fecha,
                hora = hora,
                estado = "Confirmada"
            )
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF4CAF50),
            modifier = Modifier.size(72.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("¡Cita agendada!", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(4.dp))
        Text(doctor.name, style = MaterialTheme.typography.bodyMedium)
        Text("$fecha, $hora", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(32.dp))
        OutlinedButton(
            onClick = {
                navController.navigate(Screen.MyAppointments.route) {
                    popUpTo(Screen.Home.route)
                }
            }
        ) {
            Text("Ver mis citas")
        }
    }
}
