package com.tecsup.books.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.books.AppContainer
import com.tecsup.books.ui.components.Cargando
import com.tecsup.books.ui.detalle.DetalleScreen
import com.tecsup.books.ui.detalle.DetalleViewModel
import com.tecsup.books.ui.detalle.DetalleViewModelFactory
import com.tecsup.books.ui.favoritos.FavoritosScreen
import com.tecsup.books.ui.favoritos.FavoritosViewModel
import com.tecsup.books.ui.favoritos.FavoritosViewModelFactory
import com.tecsup.books.ui.lista.ListaScreen
import com.tecsup.books.ui.lista.ListaViewModel
import com.tecsup.books.ui.lista.ListaViewModelFactory
import com.tecsup.books.ui.login.AuthViewModel
import com.tecsup.books.ui.login.AuthViewModelFactory
import com.tecsup.books.ui.login.LoginScreen

@Composable
fun AppNav(container: AppContainer) {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(container))
    val sesion by authViewModel.sesion.collectAsStateWithLifecycle()
    val restaurando by authViewModel.restaurando.collectAsStateWithLifecycle()

    // Si cambia la sesión, redirige: al entrar va a Lista; al salir vuelve a Login
    LaunchedEffect(sesion) {
        val rutaActual = navController.currentBackStackEntry?.destination?.route
        if (sesion != null && rutaActual == "login") {
            navController.navigate("lista") {
                popUpTo("login") { inclusive = true }
            }
        } else if (sesion == null && rutaActual != null && rutaActual != "login") {
            navController.navigate("login") {
                popUpTo(navController.graph.id) { inclusive = true }
            }
        }
    }

    Scaffold { innerPadding ->
        if (restaurando) {
            Cargando(Modifier.padding(innerPadding))
        } else {
            NavHost(
                navController = navController,
                startDestination = if (sesion == null) "login" else "lista",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("login") {
                    val ui by authViewModel.ui.collectAsStateWithLifecycle()

                    LoginScreen(
                        ui = ui,
                        onCorreoChange = authViewModel::cambiarCorreo,
                        onClaveChange = authViewModel::cambiarClave,
                        onIngresar = authViewModel::iniciarSesion,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                composable("lista") {
                    val viewModel: ListaViewModel = viewModel(
                        factory = ListaViewModelFactory(container)
                    )
                    val estado by viewModel.estado.collectAsStateWithLifecycle()
                    val sinConexion by viewModel.sinConexion.collectAsStateWithLifecycle()

                    Column(modifier = Modifier.fillMaxSize()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextButton(onClick = { navController.navigate("favoritos") }) {
                                Text("Ver favoritos")
                            }
                            TextButton(onClick = authViewModel::cerrarSesion) {
                                Text("Cerrar sesión")
                            }
                        }

                        ListaScreen(
                            estado = estado,
                            onReintentar = viewModel::cargar,
                            onLibroClick = { id -> navController.navigate("detalle/$id") },
                            modifier = Modifier.weight(1f),
                            sinConexion = sinConexion
                        )
                    }
                }

                composable("favoritos") {
                    val viewModel: FavoritosViewModel = viewModel(
                        factory = FavoritosViewModelFactory(container)
                    )
                    val estado by viewModel.estado.collectAsStateWithLifecycle()

                    FavoritosScreen(
                        estado = estado,
                        onLibroClick = { id -> navController.navigate("detalle/$id") },
                        onVolver = { navController.popBackStack() }
                    )
                }

                composable(
                    route = "detalle/{libroId}",
                    arguments = listOf(navArgument("libroId") { type = NavType.StringType })
                ) { entrada ->
                    val libroId = entrada.arguments?.getString("libroId").orEmpty()
                    val viewModel: DetalleViewModel = viewModel(
                        key = libroId,
                        factory = DetalleViewModelFactory(container, libroId)
                    )
                    val estado by viewModel.estado.collectAsStateWithLifecycle()

                    DetalleScreen(
                        estado = estado,
                        onReintentar = viewModel::cargar,
                        onVolver = { navController.popBackStack() },
                        onAlternarFavorito = viewModel::alternarFavorito
                    )
                }
            }
        }
    }
}