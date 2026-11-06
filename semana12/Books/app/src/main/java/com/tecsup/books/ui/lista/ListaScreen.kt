package com.tecsup.books.ui.lista

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.books.domain.model.Libro
import com.tecsup.books.ui.UiState
import com.tecsup.books.ui.components.Cargando
import com.tecsup.books.ui.components.ErrorView

@Composable
fun ListaScreen(
    estado: UiState<List<Libro>>,
    onReintentar: () -> Unit,
    onLibroClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    when (estado) {
        is UiState.Cargando -> Cargando(modifier)

        is UiState.Error -> ErrorView(
            mensaje = estado.mensaje,
            onReintentar = onReintentar,
            modifier = modifier
        )

        is UiState.Exito -> {
            if (estado.datos.isEmpty()) {
                VistaVacia(modifier)
            } else {
                LazyColumn(modifier = modifier.fillMaxSize()) {
                    items(estado.datos, key = { it.id }) { libro ->
                        LibroItem(
                            libro = libro,
                            onClick = { onLibroClick(libro.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LibroItem(libro: Libro, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = libro.titulo,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = libro.autor,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun VistaVacia(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No hay libros para mostrar",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}