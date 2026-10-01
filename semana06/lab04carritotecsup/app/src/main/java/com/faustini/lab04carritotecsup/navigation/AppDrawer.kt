package com.faustini.lab04carritotecsup.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.faustini.lab04carritotecsup.data.usuarioActual

private data class DrawerItem(
    val label: String,
    val route: String,
    val icon: ImageVector
)

@Composable
fun AppDrawer(
    currentRoute: String?,
    cantidadFavoritos: Int,
    onItemClick: (String) -> Unit,
    onSalir: () -> Unit
) {
    val drawerItems = listOf(
        DrawerItem("Inicio", Screen.Inicio.route, Icons.Default.Home),
        DrawerItem("Mis pedidos", Screen.Pedidos.route, Icons.Default.ShoppingCart),
        DrawerItem("Favoritos", Screen.Favoritos.route, Icons.Default.Favorite),
        DrawerItem("Perfil", Screen.Perfil.route, Icons.Default.Person)
    )

    ModalDrawerSheet {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = usuarioActual.iniciales,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    text = usuarioActual.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = usuarioActual.correo,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        HorizontalDivider()
        Spacer(Modifier.height(8.dp))

        drawerItems.forEach { item ->
            val badge: (@Composable () -> Unit)? =
                if (item.route == Screen.Favoritos.route && cantidadFavoritos > 0) {
                    { Badge { Text(cantidadFavoritos.toString()) } }
                } else null

            NavigationDrawerItem(
                label = { Text(item.label) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                badge = badge,
                selected = currentRoute == item.route,
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary
                ),
                onClick = { onItemClick(item.route) },
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }

        NavigationDrawerItem(
            label = { Text("Salir") },
            icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Salir") },
            selected = false,
            onClick = onSalir,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
    }
}