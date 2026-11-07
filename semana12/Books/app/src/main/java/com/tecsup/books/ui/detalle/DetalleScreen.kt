package com.tecsup.books.ui.detalle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
    onAlternarFavorito: () -> Unit,
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
            onAlternarFavorito = onAlternarFavorito,
            modifier = modifier
        )
    }
}

@Composable
private fun DetalleContenido(
    libro: Libro,
    onVolver: () -> Unit,
    onAlternarFavorito: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = onVolver) {
                Text("← Volver")
            }

            IconButton(onClick = onAlternarFavorito) {
                Icon(
                    imageVector = if (libro.esFavorito) {
                        Icons.Filled.Favorite
                    } else {
                        Icons.Outlined.FavoriteBorder
                    },
                    contentDescription = if (libro.esFavorito) {
                        "Quitar de favoritos"
                    } else {
                        "Agregar a favoritos"
                    },
                    tint = MaterialTheme.colorScheme.primary
                )
            }
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