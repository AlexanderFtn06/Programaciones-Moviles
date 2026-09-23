package com.faustino.tecsupfit.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Modelo local de la pantalla (solo se usa aquí, datos de ejemplo)
private data class Rutina(
    val nombre: String,
    val duracion: String,
    val nivel: String,
    val descripcion: String
)

private val rutinas = listOf(
    Rutina("Full Body", "45 min", "Principiante", "Circuito de cuerpo completo con peso corporal."),
    Rutina("Pierna y glúteo", "50 min", "Intermedio", "Sentadillas, zancadas y peso muerto con mancuernas."),
    Rutina("Torso y espalda", "40 min", "Intermedio", "Press, remos y dominadas asistidas."),
    Rutina("HIIT Cardio", "30 min", "Avanzado", "Intervalos de alta intensidad para quemar calorías.")
)

@Composable
fun RutinasScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Rutinas recomendadas",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(rutinas) { rutina ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = rutina.nombre, style = MaterialTheme.typography.titleMedium)
                            Text(text = rutina.duracion, style = MaterialTheme.typography.labelMedium)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = rutina.descripcion, style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        AssistChip(
                            onClick = { },
                            label = { Text(rutina.nivel) }
                        )
                    }
                }
            }
        }
    }
}
