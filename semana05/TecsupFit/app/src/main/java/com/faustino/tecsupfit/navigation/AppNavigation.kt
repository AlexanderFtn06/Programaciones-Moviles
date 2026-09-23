package com.faustino.tecsupfit.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.faustino.tecsupfit.screens.*

private data class BottomBarItem(val screen: Screen, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

private val bottomBarItems = listOf(
    BottomBarItem(Screen.Home, "Inicio", Icons.Default.Home),
    BottomBarItem(Screen.Reservas, "Reservas", Icons.Default.Event),
    BottomBarItem(Screen.Rutinas, "Rutinas", Icons.Default.FitnessCenter),
    BottomBarItem(Screen.Perfil, "Perfil", Icons.Default.Person)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    // El bottomBar solo se muestra en las 4 pestañas principales
    val mostrarBottomBar = currentRoute in bottomBarItems.map { it.screen.route }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("TECSUP Fit") })
        },
        bottomBar = {
            if (mostrarBottomBar) {
                NavigationBar {
                    bottomBarItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.screen.route,
                            onClick = {
                                navController.navigate(item.screen.route) {
                                    popUpTo(Screen.Home.route)
                                    launchSingleTop = true
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = androidx.compose.ui.Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(navController)
            }
            composable(
                route = Screen.DetalleClase.route,
                arguments = listOf(navArgument("claseId") { type = NavType.IntType })
            ) { backStack ->
                val claseId = backStack.arguments?.getInt("claseId") ?: 0
                DetalleClaseScreen(navController, claseId)
            }
            composable(
                route = Screen.Confirmacion.route,
                arguments = listOf(navArgument("claseId") { type = NavType.IntType })
            ) { backStack ->
                val claseId = backStack.arguments?.getInt("claseId") ?: 0
                ConfirmacionScreen(navController, claseId)
            }
            composable(Screen.Reservas.route) {
                ReservasScreen()
            }
            composable(Screen.Rutinas.route) {
                RutinasScreen()
            }
            composable(Screen.Perfil.route) {
                PerfilScreen()
            }
        }
    }
}