package com.faustino.tecsupfit.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.faustino.tecsupfit.model.ReservaModel
import com.faustino.tecsupfit.repository.ReservasRepository
import com.faustino.tecsupfit.ui.theme.VerdeTecsup
@Composable
fun ReservasScreen() {
    val reservas = ReservasRepository.reservas
    var reservaACancelar by remember { mutableStateOf<ReservaModel?>(null) }

    if (reservas.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Aún no tienes reservas. Ve a Inicio y reserva una clase.")
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(reservas) { reserva ->
                val confirmada = reserva.estado == "Confirmada"

                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.height(IntrinsicSize.Min)) {

                        // Franja verde solo en reservas confirmadas
                        if (confirmada) {
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .fillMaxHeight()
                                    .background(VerdeTecsup)
                            )
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = reserva.claseNombre, style = MaterialTheme.typography.titleMedium)
                                    Text(text = reserva.horario, style = MaterialTheme.typography.bodyMedium)
                                }

                                // Estado diferenciado visualmente (color + fondo)
                                Box(
                                    modifier = Modifier
                                        .background(
                                            if (confirmada) Color(0xFFDCEEDC) else Color(0xFFE0E0E0),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = reserva.estado,
                                        color = if (confirmada) Color(0xFF2E7D32) else Color(0xFF616161),
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }
                            }

                            // Botón visible solo si la reserva está confirmada
                            if (confirmada) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = { ReservasRepository.completarReserva(reserva.id) }
                                    ) {
                                        Text("Marcar como completada")
                                    }
                                    OutlinedButton(
                                        onClick = { reservaACancelar = reserva },
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            contentColor = Color(0xFFB3261E)
                                        )
                                    ) {
                                        Icon(
                                            Icons.Default.Close,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Cancelar")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    reservaACancelar?.let { reserva ->
        AlertDialog(
            onDismissRequest = { reservaACancelar = null },
            title = { Text("Cancelar reserva") },
            text = { Text("¿Seguro que quieres cancelar tu reserva de \"${reserva.claseNombre}\"? Se liberará el cupo.") },
            confirmButton = {
                TextButton(onClick = {
                    ReservasRepository.cancelarReserva(reserva.id)
                    reservaACancelar = null
                }) {
                    Text("Sí, cancelar", color = Color(0xFFB3261E))
                }
            },
            dismissButton = {
                TextButton(onClick = { reservaACancelar = null }) {
                    Text("No")
                }
            }
        )
    }

}