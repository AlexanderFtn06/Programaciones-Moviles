package com.faustini.lab04carritotecsup.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.faustini.lab04carritotecsup.data.ProductoTienda

@Composable
fun HomeScreen(
    productos: List<ProductoTienda>,
    favoritos: List<String>,
    onToggleFavorito: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(productos, key = { it.id }) { producto ->
            ProductCard(
                producto = producto,
                esFavorito = producto.nombre.lowercase() in favoritos,
                onFavorito = { onToggleFavorito(producto.nombre) }
            )
        }
    }
}