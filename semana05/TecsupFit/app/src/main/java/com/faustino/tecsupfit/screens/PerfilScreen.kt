package com.faustino.tecsupfit.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.faustino.tecsupfit.repository.ReservasRepository

@Composable
fun PerfilScreen() {

    // Estadísticas calculadas a partir de las reservas del repositorio
    val reservas = ReservasRepository.reservas
    val clasesTomadas = reservas.count { it.estado == "Completada" }
    val reservasActivas = reservas.count { it.estado == "Confirmada" }
    val rachaDias = 5 // dato de ejemplo (racha de asistencia)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(96.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Foto de perfil",
                modifier = Modifier.size(56.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Datos del usuario
        Text(
            text = "Alexander Faustino",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "Plan Mensual · Miembro TECSUP Fit",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Mis estadísticas",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            EstadisticaCard("Clases tomadas", clasesTomadas.toString(), Modifier.weight(1f))
            EstadisticaCard("Reservas activas", reservasActivas.toString(), Modifier.weight(1f))
            EstadisticaCard("Racha (días)", rachaDias.toString(), Modifier.weight(1f))
        }
    }
}

@Composable
private fun EstadisticaCard(etiqueta: String, valor: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = valor, style = MaterialTheme.typography.headlineMedium)
            Text(text = etiqueta, style = MaterialTheme.typography.labelMedium)
        }
    }
}