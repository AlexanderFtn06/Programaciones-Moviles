package com.tecsup.books.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.books.AppContainer
import com.tecsup.books.domain.model.Rol
import com.tecsup.books.ui.admin.AdminScreen
import com.tecsup.books.ui.admin.AdminViewModel
import com.tecsup.books.ui.admin.AdminViewModelFactory
import com.tecsup.books.ui.components.Cargando
import com.tecsup.books.ui.components.EncabezadoSeccion
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
import com.tecsup.books.ui.perfil.PerfilScreen
import com.tecsup.books.ui.perfil.PerfilViewModel
import com.tecsup.books.ui.perfil.PerfilViewModelFactory

private data class Destino(
    val ruta: String,
    val etiqueta: String,
    val icono: ImageVector
)

// El rol decide la tercera pestaña: el administrador ve Admin; el usuario ve Favoritos
private fun destinosPara(esAdmin: Boolean): List<Destino> = buildList {
    add(Destino("lista", "Catálogo", Icons.Filled.Home))
    if (esAdmin) {
        add(Destino("admin", "Admin", Icons.Filled.AdminPanelSettings))
    } else {
        add(Destino("favoritos", "Favoritos", Icons.Filled.Favorite))
    }
    add(Destino("perfil", "Perfil", Icons.Filled.Person))
}

@Composable
fun AppNav(container: AppContainer) {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(container))
    val sesion by authViewModel.sesion.collectAsStateWithLifecycle()
    val restaurando by authViewModel.restaurando.collectAsStateWithLifecycle()
    val esAdmin = sesion?.rol == Rol.ADMIN

    val destinos = destinosPara(esAdmin)
    val rutasPrincipales = destinos.map { it.ruta }

    val entrada by navController.currentBackStackEntryAsState()
    val rutaActual = entrada?.destination?.route

    // Si cambia la sesión, redirige: al entrar va a Catálogo; al salir vuelve a Login
    LaunchedEffect(sesion) {
        val rutaActualEfecto = navController.currentBackStackEntry?.destination?.route
        if (sesion != null && rutaActualEfecto == "login") {
            navController.navigate("lista") {
                popUpTo("login") { inclusive = true }
            }
        } else if (sesion == null && rutaActualEfecto != null && rutaActualEfecto != "login") {
            navController.navigate("login") {
                popUpTo(navController.graph.id) { inclusive = true }
            }
        }
    }

    Scaffold(
        bottomBar = {
            if (sesion != null && rutaActual != null && rutaActual in rutasPrincipales) {
                NavigationBar {
                    destinos.forEach { destino ->
                        NavigationBarItem(
                            selected = rutaActual == destino.ruta,
                            onClick = { navegarA(navController, destino.ruta) },
                            icon = { Icon(destino.icono, contentDescription = destino.etiqueta) },
                            label = { Text(destino.etiqueta) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
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
                        EncabezadoSeccion("BookVerse")
                        ListaScreen(
                            estado = estado,
                            onReintentar = viewModel::cargar,
                            onLibroClick = { id -> navController.navigate("detalle/$id") },
                            modifier = Modifier.weight(1f),
                            sinConexion = sinConexion,
                            onAlternarFavorito = if (esAdmin) null else viewModel::alternarFavorito
                        )
                    }
                }

                composable("favoritos") {
                    val viewModel: FavoritosViewModel = viewModel(
                        factory = FavoritosViewModelFactory(container)
                    )
                    val estado by viewModel.estado.collectAsStateWithLifecycle()

                    Column(modifier = Modifier.fillMaxSize()) {
                        EncabezadoSeccion("Favoritos")
                        FavoritosScreen(
                            estado = estado,
                            onLibroClick = { id -> navController.navigate("detalle/$id") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                composable("admin") {
                    val sesionAdmin = sesion
                    if (esAdmin && sesionAdmin != null) {
                        val viewModel: AdminViewModel = viewModel(
                            factory = AdminViewModelFactory(container)
                        )
                        val ui by viewModel.ui.collectAsStateWithLifecycle()

                        LaunchedEffect(sesionAdmin) {
                            viewModel.cargarDatos(sesionAdmin)
                        }

                        Column(modifier = Modifier.fillMaxSize()) {
                            EncabezadoSeccion("Admin")
                            AdminScreen(
                                ui = ui,
                                onSincronizar = { viewModel.sincronizar(sesionAdmin) },
                                onLimpiarCache = { viewModel.limpiarCache(sesionAdmin) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    } else {
                        Text(
                            text = "No tienes acceso a esta pantalla.",
                            modifier = Modifier.padding(24.dp)
                        )
                    }
                }

                composable("perfil") {
                    val viewModel: PerfilViewModel = viewModel(
                        factory = PerfilViewModelFactory(container)
                    )
                    val ui by viewModel.ui.collectAsStateWithLifecycle()
                    val sesionPerfil = sesion

                    LaunchedEffect(sesionPerfil) {
                        sesionPerfil?.let { viewModel.cargarDatosPerfil(it) }
                    }

                    if (sesionPerfil != null) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            EncabezadoSeccion("Perfil")
                            PerfilScreen(
                                sesion = sesionPerfil,
                                ui = ui,
                                onModoOscuroChange = viewModel::cambiarModoOscuro,
                                onCerrarSesion = authViewModel::cerrarSesion,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
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
                        onAlternarFavorito = viewModel::alternarFavorito,
                        mostrarFavorito = !esAdmin
                    )
                }
            }
        }
    }
}

// Cambia de pestaña sin acumular pantallas en la pila de navegación
private fun navegarA(navController: NavHostController, ruta: String) {
    navController.navigate(ruta) {
        popUpTo(navController.graph.startDestinationId) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}