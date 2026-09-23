package com.faustino.tecsupfit.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.faustino.tecsupfit.screens.*
import com.faustino.tecsupfit.ui.theme.VerdeTecsup

private data class BottomBarItem(val screen: Screen, val label: String, val icon: ImageVector)

private val bottomBarItems = listOf(
    BottomBarItem(Screen.Home, "Inicio", Icons.Default.Home),
    BottomBarItem(Screen.Reservas, "Reservas", Icons.Default.Event),
    BottomBarItem(Screen.Rutinas, "Rutinas", Icons.Default.FitnessCenter),
    BottomBarItem(Screen.Perfil, "Perfil", Icons.Default.Person)
)


@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    // El bottomBar solo se muestra en las 4 pestañas principales
    val mostrarBottomBar = currentRoute in bottomBarItems.map { it.screen.route }

    Scaffold(
        topBar = {
            // Cada pantalla tiene su propia cabecera según el diseño
            when (currentRoute) {
                Screen.Home.route -> HomeTopBar()
                Screen.DetalleClase.route -> DetalleTopBar { navController.popBackStack() }
                Screen.Reservas.route -> TituloTopBar("Mis reservas")
                Screen.Rutinas.route -> TituloTopBar("Rutinas")
                Screen.Perfil.route -> TituloTopBar("Mi perfil")
                else -> {}
            }
        },
        bottomBar = {
            if (mostrarBottomBar) {
                NavigationBar(containerColor = Color.White) {
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
            modifier = Modifier.padding(innerPadding)
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

// Cabecera verde de Inicio
@Composable
private fun HomeTopBar() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(VerdeTecsup)
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Text(
            text = "TECSUP Fit",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Hola, Alexander",
            color = Color.White,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

// Cabecera de Detalle con flecha para volver
@Composable
private fun DetalleTopBar(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .statusBarsPadding()
            .padding(horizontal = 4.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
        }
        Text(
            text = "Detalle de clase",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

// Cabecera blanca con título (Reservas, Rutinas, Perfil)
@Composable
private fun TituloTopBar(titulo: String) {
    Text(
        text = titulo,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    )
}