package com.tecsup.books.ui.detalle

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.books.domain.model.Libro
import com.tecsup.books.ui.UiState
import com.tecsup.books.ui.components.Cargando
import com.tecsup.books.ui.components.ErrorView

@Composable
fun DetalleScreen(
    estado: UiState<Libro>,
    onReintentar: () -> Unit,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (estado) {
        is UiState.Cargando -> Cargando(modifier)

        is UiState.Error -> ErrorView(
            mensaje = estado.mensaje,
            onReintentar = onReintentar,
            modifier = modifier
        )

        is UiState.Exito -> DetalleContenido(
            libro = estado.datos,
            onVolver = onVolver,
            modifier = modifier
        )
    }
}

@Composable
private fun DetalleContenido(
    libro: Libro,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        TextButton(onClick = onVolver) {
            Text("← Volver")
        }

        Text(
            text = libro.titulo,
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = libro.autor,
            style = MaterialTheme.typography.titleMedium
        )
        libro.anio?.let {
            Text(
                text = "Primera publicación: $it",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = libro.descripcion ?: "Este libro no tiene descripción disponible.",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}