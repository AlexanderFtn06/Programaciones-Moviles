package com.faustino.clinicasaludplus.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.faustino.clinicasaludplus.data.Appointment
import com.faustino.clinicasaludplus.data.AppointmentsStore

@Composable
fun MyAppointmentsScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Mis citas", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        if (AppointmentsStore.appointments.isEmpty()) {
            Text(
                "Aún no tienes citas agendadas.",
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(AppointmentsStore.appointments) { appointment ->
                    AppointmentCard(appointment)
                }
            }
        }
    }
}

@Composable
private fun AppointmentCard(appointment: Appointment) {
    val isConfirmada = appointment.estado == "Confirmada"
    Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(appointment.doctorName, style = MaterialTheme.typography.titleSmall)
            Text(
                "${appointment.fecha}, ${appointment.hora}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))
            AssistChip(
                onClick = { },
                label = { Text(appointment.estado) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (isConfirmada)
                        Color(0xFFDFF5E3) else MaterialTheme.colorScheme.surfaceVariant,
                    labelColor = if (isConfirmada)
                        Color(0xFF2E7D32) else MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}