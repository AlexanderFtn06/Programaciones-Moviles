package com.tecsup.books.ui.lista

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
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
    busqueda: String,
    categoriaSeleccionada: String?,
    categorias: List<CategoriaInfo>,
    hayLibrosEnCatalogo: Boolean,
    onBusquedaChange: (String) -> Unit,
    onCategoriaSelect: (String?) -> Unit,
    onReintentar: () -> Unit,
    onLibroClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    mostrarBuscador: Boolean = true,
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

            if (mostrarBuscador) {
                // Sección de Búsqueda por Título o Autor
                OutlinedTextField(
                    value = busqueda,
                    onValueChange = onBusquedaChange,
                    label = { Text("Buscar por título o autor") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                    trailingIcon = {
                        if (busqueda.isNotEmpty()) {
                            IconButton(onClick = { onBusquedaChange("") }) {
                                Icon(Icons.Filled.Clear, contentDescription = "Limpiar búsqueda")
                            }
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )

                // Sección de Chips para las 6 Categorías Fijas (se muestran siempre, deshabilitadas si conteo == 0)
                if (categorias.isNotEmpty()) {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        items(categorias, key = { it.id }) { cat ->
                            val seleccionado = cat.id == categoriaSeleccionada
                            FilterChip(
                                selected = seleccionado,
                                enabled = cat.conteo > 0,
                                onClick = { onCategoriaSelect(cat.id) },
                                label = { Text(cat.etiqueta) }
                            )
                        }
                    }
                }
            }

            // Resultados de la Lista
            when {
                !hayLibrosEnCatalogo -> VistaVacia(Modifier.weight(1f))
                estado.datos.isEmpty() -> VistaSinResultados(Modifier.weight(1f))
                else -> LazyColumn(modifier = Modifier.weight(1f)) {
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
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No hay libros para mostrar",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun VistaSinResultados(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No hay libros que coincidan con la búsqueda",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}