package com.faustino.clinicasaludplus.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.faustino.clinicasaludplus.data.Appointment
import com.faustino.clinicasaludplus.data.AppointmentsStore
import kotlinx.coroutines.launch

@Composable
fun MyAppointmentsScreen() {

    var appointmentToCancel by remember { mutableStateOf<Appointment?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp)
        ) {
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
                        AppointmentCard(
                            appointment = appointment,
                            onCancelClick = { appointmentToCancel = appointment }
                        )
                    }
                }
            }
        }
    }
    appointmentToCancel?.let { appointment ->
        AlertDialog(
            onDismissRequest = { appointmentToCancel = null },
            title = { Text("Cancelar cita") },
            text = {
                Text(
                    "¿Estás seguro de que deseas cancelar tu cita con " +
                            "${appointment.doctorName}? Esta acción no se puede deshacer."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        AppointmentsStore.cancelAppointment(appointment.id)
                        appointmentToCancel = null
                        scope.launch {
                            snackbarHostState.showSnackbar("Cita cancelada correctamente")
                        }
                    }
                ) {
                    Text("Sí, cancelar", color = Color(0xFFD32F2F))
                }
            },
            dismissButton = {
                TextButton(onClick = { appointmentToCancel = null }) {
                    Text("No, mantener")
                }
            }
        )
    }
}

@Composable
private fun AppointmentCard(appointment: Appointment,onCancelClick: () -> Unit) {
    val isConfirmada = appointment.estado == "Confirmada"
    Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(appointment.doctorName, style = MaterialTheme.typography.titleSmall)
                    Text(
                        "${appointment.fecha}, ${appointment.hora}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (isConfirmada) {
                    TextButton(onClick = onCancelClick) {
                        Text("Cancelar", color = Color(0xFFD32F2F))
                    }
                }
            }
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