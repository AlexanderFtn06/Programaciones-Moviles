package com.tecsup.mibodega.ui.cliente.screens.categorias

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.Rutas
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.cliente.modelo.listaCategorias
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake
import com.tecsup.mibodega.ui.componentes.BarraInferior
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.GrisClaro

/**
 * Destino "Categorías" de la barra inferior.
 * Muestra cada categoría con su cantidad de productos.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriasScreen(
    onCategoriaClick: (String) -> Unit,
    onNavegar: (String) -> Unit,
    productos: List<Producto> = listaProductosFake
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Categorías", fontWeight = FontWeight.Bold) }) },
        bottomBar = { BarraInferior(rutaActual = Rutas.CATEGORIAS, onNavegar = onNavegar) }
    ) { paddingInterno ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(listaCategorias.filter { it != "Todos" }) { categoria ->
                FilaCategoria(
                    nombre = categoria,
                    cantidad = productos.count { it.categoria == categoria },
                    onClick = { onCategoriaClick(categoria) }
                )
            }
        }
    }
}

@Composable
private fun FilaCategoria(
    nombre: String,
    cantidad: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(GrisClaro, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = nombre,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "$cantidad productos",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Icon(Icons.Default.KeyboardArrowRight, contentDescription = null)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CategoriasPreview() {
    BodegaTheme {
        CategoriasScreen(onCategoriaClick = {}, onNavegar = {})
    }
}
