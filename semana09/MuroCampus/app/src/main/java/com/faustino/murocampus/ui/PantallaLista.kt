package com.faustino.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.faustino.murocampus.viewmodel.PostsViewModel
import com.faustino.murocampus.viewmodel.UiState

@Composable
fun PantallaLista(
    vm: PostsViewModel,
    onPostClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by vm.lista.collectAsStateWithLifecycle()
    val filtro by vm.usuarioFiltro.collectAsStateWithLifecycle()

    Column(modifier) {
        FilaFiltros(seleccionado = filtro, onSeleccion = { vm.filtrar(it) })

        when (val e = estado) {
            UiState.Cargando -> Cargando(Modifier.weight(1f))

            is UiState.Error -> ErrorConReintento(
                mensaje = e.mensaje,
                onReintentar = { vm.cargar() },
                modifier = Modifier.weight(1f)
            )

            is UiState.Exito -> {
                if (e.datos.isEmpty()) {
                    Box(
                        modifier = Modifier.weight(1f).fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Sin publicaciones")
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(e.datos, key = { it.id }) { post ->
                            TarjetaPost(
                                userId = post.userId,
                                titulo = post.title,
                                cuerpo = post.body,
                                onClick = { onPostClick(post.id) },
                                maxLines = 2
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FilaFiltros(seleccionado: Int?, onSeleccion: (Int?) -> Unit) {
    val colores = FilterChipDefaults.filterChipColors(
        selectedContainerColor = MaterialTheme.colorScheme.primary,
        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
    )

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = seleccionado == null,
                onClick = { onSeleccion(null) },
                label = { Text("Todos") },
                colors = colores
            )
        }
        items((1..10).toList()) { userId ->
            FilterChip(
                selected = seleccionado == userId,
                onClick = { onSeleccion(userId) },
                label = { Text("Usuario $userId") },
                colors = colores
            )
        }
    }
}