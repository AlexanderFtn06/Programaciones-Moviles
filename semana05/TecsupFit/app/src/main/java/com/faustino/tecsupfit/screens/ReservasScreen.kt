package com.faustino.tecsupfit.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.faustino.tecsupfit.repository.ReservasRepository
import androidx.compose.foundation.background

@Composable
fun ReservasScreen() {
    val reservas = ReservasRepository.reservas

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Mis reservas",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )

        if (reservas.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Aún no tienes reservas. Ve a Inicio y reserva una clase.")
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(reservas) { reserva ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = reserva.claseNombre, style = MaterialTheme.typography.titleMedium)
                                Text(text = reserva.horario, style = MaterialTheme.typography.bodyMedium)
                            }

                            // Estado diferenciado visualmente (color + fondo)
                            val colorFondo = if (reserva.estado == "Confirmada") {
                                Color(0xFFDCEEDC)
                            } else {
                                Color(0xFFE0E0E0)
                            }
                            val colorTexto = if (reserva.estado == "Confirmada") {
                                Color(0xFF2E7D32)
                            } else {
                                Color(0xFF616161)
                            }

                            Box(
                                modifier = Modifier
                                    .background(colorFondo, shape = RoundedCornerShape(8.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = reserva.estado,
                                    color = colorTexto,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}