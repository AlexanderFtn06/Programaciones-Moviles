package com.tecsup.books.ui.favoritos

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tecsup.books.domain.model.Libro
import com.tecsup.books.ui.UiState
import com.tecsup.books.ui.lista.ListaScreen

@Composable
fun FavoritosScreen(
    estado: UiState<List<Libro>>,
    onLibroClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val hayLibros = (estado as? UiState.Exito)?.datos?.isNotEmpty() ?: false

    ListaScreen(
        estado = estado,
        busqueda = "",
        categoriaSeleccionada = null,
        categorias = emptyList(),
        hayLibrosEnCatalogo = hayLibros,
        onBusquedaChange = {},
        onCategoriaSelect = {},
        onReintentar = {},
        onLibroClick = onLibroClick,
        modifier = modifier,
        mostrarBuscador = false
    )
}