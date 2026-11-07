package com.tecsup.books.ui.lista

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.tecsup.books.domain.model.Libro
import com.tecsup.books.ui.UiState
import com.tecsup.books.ui.components.AvisoSinConexion
import com.tecsup.books.ui.components.Cargando
import com.tecsup.books.ui.components.ErrorView
import com.tecsup.books.ui.theme.TealClaro

@Composable
fun ListaScreen(
    estado: UiState<List<Libro>>,
    onReintentar: () -> Unit,
    onLibroClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    sinConexion: Boolean = false,
    onAlternarFavorito: ((String) -> Unit)? = null
) {
    when (estado) {
        is UiState.Cargando -> Cargando(modifier)

        is UiState.Error -> ErrorView(
            mensaje = estado.mensaje,
            onReintentar = onReintentar,
            modifier = modifier
        )

        is UiState.Exito -> Column(modifier = modifier.fillMaxSize()) {
            if (sinConexion) {
                AvisoSinConexion()
            }

            if (estado.datos.isEmpty()) {
                VistaVacia(Modifier.weight(1f))
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(estado.datos, key = { it.id }) { libro ->
                        LibroItem(
                            libro = libro,
                            onClick = { onLibroClick(libro.id) },
                            mostrarFavorito = onAlternarFavorito != null,
                            onFavoritoClick = { onAlternarFavorito?.invoke(libro.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LibroItem(
    libro: Libro,
    onClick: () -> Unit,
    mostrarFavorito: Boolean,
    onFavoritoClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = TealClaro)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PortadaLibro(libro)

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = libro.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2
                )
                Text(
                    text = libro.autor,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (mostrarFavorito) {
                IconButton(onClick = onFavoritoClick) {
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
        }
    }
}

@Composable
private fun PortadaLibro(libro: Libro) {
    Box(
        modifier = Modifier
            .size(width = 64.dp, height = 88.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = libro.portadaUrl,
            contentDescription = libro.titulo,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
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