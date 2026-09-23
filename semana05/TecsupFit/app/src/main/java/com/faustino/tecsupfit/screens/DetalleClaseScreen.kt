package com.faustino.tecsupfit.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.faustino.tecsupfit.navigation.Screen
import com.faustino.tecsupfit.repository.ReservasRepository
import com.faustino.tecsupfit.ui.theme.VerdeTecsup
@Composable
fun DetalleClaseScreen(navController: NavController, claseId: Int) {

    // Recupera la clase elegida usando el id que llegó por navegación
    val clase = ReservasRepository.obtenerClasePorId(claseId)

    if (clase == null) {
        Text("Clase no encontrada", modifier = Modifier.padding(16.dp))
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Icono destacado de la clase
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(VerdeTecsup.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.FitnessCenter,
                contentDescription = null,
                tint = VerdeTecsup,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = clase.nombre, style = MaterialTheme.typography.headlineSmall)
        Text(
            text = "${clase.horario} · ${clase.sala} · ${clase.duracionMin} min",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(12.dp))
        Text(text = clase.descripcion, style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "${clase.cuposDisponibles} de ${clase.cuposTotales} cupos disponibles",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                ReservasRepository.agregarReserva(clase)
                navController.navigate(Screen.Confirmacion.createRoute(clase.id)) {
                    popUpTo(Screen.Home.route)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reservar cupo")
        }
    }
}