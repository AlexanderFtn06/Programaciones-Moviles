package com.tecsup.mibodega.ui.componentes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.tecsup.mibodega.ui.cliente.Rutas
import com.tecsup.mibodega.ui.theme.VerdeBodega

private data class DestinoBarra(
    val etiqueta: String,
    val icono: ImageVector,
    val ruta: String
)

private val destinos = listOf(
    DestinoBarra("Inicio", Icons.Default.Home, Rutas.INICIO),
    DestinoBarra("Categorías", Icons.Default.List, Rutas.CATEGORIAS),
    DestinoBarra("Pedidos", Icons.Default.Receipt, Rutas.PEDIDOS),
    DestinoBarra("Perfil", Icons.Default.Person, Rutas.PERFIL)
)

/**
 * Menú principal de la app (NavigationBar con 4 destinos).
 * No navega sola: avisa la ruta tocada con onNavegar y marca como
 * seleccionado el destino que coincide con rutaActual.
 */
@Composable
fun BarraInferior(
    rutaActual: String,
    onNavegar: (String) -> Unit
) {
    NavigationBar {
        destinos.forEach { destino ->
            NavigationBarItem(
                selected = rutaActual == destino.ruta,
                onClick = { onNavegar(destino.ruta) },
                icon = { Icon(destino.icono, contentDescription = destino.etiqueta) },
                label = { Text(destino.etiqueta) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = VerdeBodega,
                    selectedTextColor = VerdeBodega
                )
            )
        }
    }
}
