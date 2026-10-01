package com.faustini.lab04carritotecsup.navigation

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import com.faustini.lab04carritotecsup.data.Producto
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.platform.LocalContext
import com.faustini.lab04carritotecsup.data.ProductoTienda
import com.faustini.lab04carritotecsup.data.productosEjemplo
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.faustini.lab04carritotecsup.screens.HomeScreen
import com.faustini.lab04carritotecsup.screens.MyOrdersScreen
import com.faustini.lab04carritotecsup.screens.PantallaPerfil
import com.faustini.lab04carritotecsup.screens.PantallaSimple
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavegacion() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val pedidos = remember { mutableStateListOf<Producto>() }
    val catalogo = remember { productosEjemplo.toMutableStateList() }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                currentRoute = currentRoute,
                onItemClick = { route ->
                    scope.launch { drawerState.close() }
                    navController.navigate(route) {
                        popUpTo(Screen.Inicio.route)
                        launchSingleTop = true
                    }
                },
                onSalir = { (context as? Activity)?.finish() }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("TECSUP Store") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                NavHost(
                    navController = navController,
                    startDestination = Screen.Inicio.route
                ) {
                    composable(Screen.Inicio.route) { HomeScreen(catalogo) }
                    composable(Screen.Pedidos.route) {
                        MyOrdersScreen(
                            productos = pedidos,
                            onAgregar = { producto ->
                                pedidos.add(producto)
                                if (catalogo.none { it.nombre.equals(producto.nombre, ignoreCase = true) }) {
                                    val nuevoId = (catalogo.maxOfOrNull { it.id } ?: 0) + 1
                                    catalogo.add(ProductoTienda(nuevoId, producto.nombre, producto.precio))
                                }
                            },
                            onEliminar = { producto ->
                                pedidos.remove(producto)
                                val quedanIguales = pedidos.any { it.nombre.equals(producto.nombre, ignoreCase = true) }
                                if (!quedanIguales) {
                                    catalogo.removeAll {
                                        it.nombre.equals(producto.nombre, ignoreCase = true) && it !in productosEjemplo
                                    }
                                }
                            }
                        )
                    }
                    composable(Screen.Favoritos.route) { PantallaSimple("Favoritos") }
                    composable(Screen.Perfil.route) { PantallaPerfil() }                }
            }
        }
    }
}