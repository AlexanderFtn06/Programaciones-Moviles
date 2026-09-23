package com.faustino.tecsupfit.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.faustino.tecsupfit.navigation.Screen
import com.faustino.tecsupfit.repository.ReservasRepository

@Composable
fun HomeScreen(navController: NavController) {

    // Filtro seleccionado ("Hoy" o "Esta semana")
    var categoriaSeleccionada by remember { mutableStateOf("Hoy") }
    val categorias = listOf("Hoy", "Esta semana")

    // Clases filtradas según el chip activo
    val clasesFiltradas = ReservasRepository.clases.filter {
        it.categoria == categoriaSeleccionada
    }

    Column(modifier = Modifier.fillMaxSize()) {

        Text(
            text = "Clases disponibles",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )

        // LazyRow de chips de filtro
        LazyRow(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categorias) { categoria ->
                FilterChip(
                    selected = categoria == categoriaSeleccionada,
                    onClick = { categoriaSeleccionada = categoria },
                    label = { Text(categoria) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // LazyColumn con la lista de clases filtradas
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(clasesFiltradas) { clase ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(Screen.DetalleClase.createRoute(clase.id))
                        }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = clase.nombre, style = MaterialTheme.typography.titleMedium)
                        Text(text = clase.horario, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}