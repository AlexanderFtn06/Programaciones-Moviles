package com.faustino.ayudacampus.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.faustino.ayudacampus.AyudaApp
import com.faustino.ayudacampus.data.local.entity.Usuario
import com.faustino.ayudacampus.data.model.Rol
import com.faustino.ayudacampus.ui.cliente.DetalleTicketScreen
import com.faustino.ayudacampus.ui.cliente.LoginScreen
import com.faustino.ayudacampus.ui.cliente.MisTicketsScreen
import com.faustino.ayudacampus.ui.cliente.NuevoTicketScreen
import com.faustino.ayudacampus.ui.cliente.PerfilScreen
import com.faustino.ayudacampus.ui.cliente.RegistroScreen
import com.faustino.ayudacampus.ui.cliente.SplashScreen
import com.faustino.ayudacampus.ui.components.AppTopBar
import com.faustino.ayudacampus.ui.soporte.AjustesScreen
import com.faustino.ayudacampus.ui.soporte.AtenderTicketScreen
import com.faustino.ayudacampus.ui.soporte.BandejaScreen
import com.faustino.ayudacampus.ui.soporte.CategoriasScreen
import com.faustino.ayudacampus.ui.soporte.FormularioCategoriaScreen
import com.faustino.ayudacampus.ui.soporte.PanelScreen
import com.faustino.ayudacampus.ui.theme.AyudaTheme
import com.faustino.ayudacampus.viewmodel.AtenderViewModel
import com.faustino.ayudacampus.viewmodel.BandejaViewModel
import com.faustino.ayudacampus.viewmodel.CategoriasViewModel
import com.faustino.ayudacampus.viewmodel.MisTicketsViewModel
import com.faustino.ayudacampus.viewmodel.NuevoTicketViewModel
import com.faustino.ayudacampus.viewmodel.PanelViewModel
import com.faustino.ayudacampus.viewmodel.RegistroViewModel
import com.faustino.ayudacampus.viewmodel.SesionViewModel
import com.faustino.ayudacampus.viewmodel.TicketDetalleViewModel
import com.faustino.ayudacampus.viewmodel.ayudaViewModel
import kotlinx.coroutines.delay

/** Pantalla principal de cada perfil. */
private fun inicioDe(u: Usuario): String =
    if (u.rol == Rol.ADMIN.name) Rutas.PANEL else Rutas.MIS_TICKETS

/** Bandeja con filtro opcional "solo ALTA" (lo usa la alerta del panel). */
private const val RUTA_BANDEJA = "${Rutas.BANDEJA}?alta={alta}"

@Composable
fun NavAyuda(app: AyudaApp) {
    val nav = rememberNavController()
    val repo = app.repository

    // Sesión compartida por toda la app
    val sesionVm = ayudaViewModel { SesionViewModel(repo, app.preferencias) }
    val usuario by sesionVm.usuarioActual.collectAsState()
    val oscuro by sesionVm.modoOscuro.collectAsState()

    // El tema sigue al perfil activo y al modo oscuro guardado en DataStore
    val rolActual = usuario?.let { Rol.desde(it.rol) } ?: Rol.CLIENTE

    val cerrarSesion: () -> Unit = {
        sesionVm.cerrarSesion()
        nav.navigate(Rutas.LOGIN) {
            popUpTo(nav.graph.id) { inclusive = true }
        }
    }

    // Modo práctica: guarda el otro perfil y navega a su grafo
    val cambiarPerfil: () -> Unit = {
        sesionVm.cambiarPerfil { otro ->
            nav.navigate(inicioDe(otro)) {
                popUpTo(nav.graph.id) { inclusive = true }
            }
        }
    }

    // Barra inferior del estudiante
    val irEstudiante: (String) -> Unit = { ruta ->
        if (ruta == Rutas.MIS_TICKETS) {
            nav.popBackStack(Rutas.MIS_TICKETS, inclusive = false)
        } else {
            nav.navigate(ruta) {
                popUpTo(Rutas.MIS_TICKETS)
                launchSingleTop = true
            }
        }
    }

    // Barra inferior de Soporte
    val irSoporte: (String) -> Unit = { ruta ->
        if (ruta == Rutas.PANEL) {
            nav.popBackStack(Rutas.PANEL, inclusive = false)
        } else {
            nav.navigate(ruta) {
                popUpTo(Rutas.PANEL)
                launchSingleTop = true
            }
        }
    }

    AyudaTheme(rol = rolActual, oscuro = oscuro) {
        NavHost(navController = nav, startDestination = Rutas.SPLASH) {

            // ---------- Splash ----------
            composable(Rutas.SPLASH) {
                val verificada by sesionVm.sesionVerificada.collectAsState()
                val haySesion = verificada && usuario != null

                val irALogin: () -> Unit = {
                    nav.navigate(Rutas.LOGIN) {
                        popUpTo(Rutas.SPLASH) { inclusive = true }
                    }
                }

                val irARegistro: () -> Unit = {
                    nav.navigate(Rutas.REGISTRO) {
                        popUpTo(Rutas.SPLASH) { inclusive = true }
                    }
                }

                // Regla del Paso 6: si ya hay perfil guardado, salta el login
                LaunchedEffect(haySesion) {
                    if (haySesion) {
                        delay(800)
                        usuario?.let { u ->
                            nav.navigate(inicioDe(u)) {
                                popUpTo(Rutas.SPLASH) { inclusive = true }
                            }
                        }
                    }
                }

                SplashScreen(
                    mostrarBotones = verificada && usuario == null,
                    onComenzar = irARegistro,
                    onYaTengoCuenta = irALogin
                )
            }

            composable(Rutas.LOGIN) {
                LoginScreen(
                    vm = sesionVm,
                    onIngresar = { u ->
                        nav.navigate(inicioDe(u)) {
                            popUpTo(Rutas.LOGIN) { inclusive = true }
                        }
                    },
                    onRegistrarse = { nav.navigate(Rutas.REGISTRO) }
                )
            }

            composable(Rutas.REGISTRO) {
                val vm = ayudaViewModel { RegistroViewModel(repo) }
                RegistroScreen(
                    vm = vm,
                    onRegistrado = { u ->
                        sesionVm.iniciarSesionCon(u)
                        nav.navigate(inicioDe(u)) {
                            popUpTo(nav.graph.id) { inclusive = true }
                        }
                    },
                    onIrLogin = {
                        if (!nav.popBackStack(Rutas.LOGIN, inclusive = false)) {
                            nav.navigate(Rutas.LOGIN) {
                                popUpTo(Rutas.REGISTRO) { inclusive = true }
                            }
                        }
                    }
                )
            }

            // ======================= Estudiante =======================

            composable(Rutas.MIS_TICKETS) {
                usuario?.let { u ->
                    val vm = ayudaViewModel(key = "mis_tickets_${u.id}") {
                        MisTicketsViewModel(repo, u)
                    }
                    MisTicketsScreen(
                        vm = vm,
                        onAbrirTicket = { id -> nav.navigate(Rutas.detalleTicket(id)) },
                        onNuevoTicket = { nav.navigate(Rutas.NUEVO_TICKET) },
                        barraInferior = { BarraEstudiante(Rutas.MIS_TICKETS, irEstudiante) }
                    )
                }
            }

            composable(Rutas.NUEVO_TICKET) {
                usuario?.let { u ->
                    val vm = ayudaViewModel(key = "nuevo_ticket_${u.id}") {
                        NuevoTicketViewModel(repo, u, app.preferencias)
                    }
                    NuevoTicketScreen(
                        vm = vm,
                        onAtras = { nav.popBackStack() },
                        onEnviado = { nav.popBackStack(Rutas.MIS_TICKETS, inclusive = false) },
                        barraInferior = { BarraEstudiante(Rutas.NUEVO_TICKET, irEstudiante) }
                    )
                }
            }

            composable(
                Rutas.DETALLE_TICKET,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { entrada ->
                val id = entrada.arguments?.getInt("id") ?: 0
                usuario?.let { u ->
                    val vm = ayudaViewModel(key = "detalle_${id}_${u.id}") {
                        TicketDetalleViewModel(repo, id, u)
                    }
                    DetalleTicketScreen(
                        vm = vm,
                        onAtras = { nav.popBackStack() },
                        barraInferior = { BarraEstudiante(Rutas.MIS_TICKETS, irEstudiante) }
                    )
                }
            }

            composable(Rutas.PERFIL) {
                usuario?.let { u ->
                    val ticketsVm = ayudaViewModel(key = "perfil_${u.id}") {
                        MisTicketsViewModel(repo, u)
                    }
                    PerfilScreen(
                        sesionVm = sesionVm,
                        ticketsVm = ticketsVm,
                        onAtras = { nav.popBackStack() },
                        onCambiarPerfil = cambiarPerfil,
                        onCerrarSesion = cerrarSesion,
                        barraInferior = { BarraEstudiante(Rutas.PERFIL, irEstudiante) }
                    )
                }
            }

            // ======================= Soporte TI =======================

            composable(Rutas.PANEL) {
                val vm = ayudaViewModel { PanelViewModel(repo) }
                PanelScreen(
                    vm = vm,
                    onVerAltas = { nav.navigate("${Rutas.BANDEJA}?alta=true") },
                    barraInferior = { BarraSoporte(Rutas.PANEL, irSoporte) }
                )
            }

            composable(
                RUTA_BANDEJA,
                arguments = listOf(navArgument("alta") {
                    type = NavType.BoolType
                    defaultValue = false
                })
            ) { entrada ->
                val soloAlta = entrada.arguments?.getBoolean("alta") ?: false
                val vm = ayudaViewModel(key = "bandeja_$soloAlta") {
                    BandejaViewModel(repo, soloAlta)
                }
                BandejaScreen(
                    vm = vm,
                    onAtras = { nav.popBackStack() },
                    onAbrirTicket = { id -> nav.navigate(Rutas.atenderTicket(id)) },
                    barraInferior = { BarraSoporte(Rutas.BANDEJA, irSoporte) }
                )
            }

            composable(
                Rutas.ATENDER_TICKET,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { entrada ->
                val id = entrada.arguments?.getInt("id") ?: 0
                usuario?.let { u ->
                    val vm = ayudaViewModel(key = "atender_$id") {
                        AtenderViewModel(repo, id, u)
                    }
                    AtenderTicketScreen(
                        vm = vm,
                        onAtras = { nav.popBackStack() },
                        barraInferior = { BarraSoporte(Rutas.BANDEJA, irSoporte) }
                    )
                }
            }

            composable(Rutas.CATEGORIAS) {
                val vm = ayudaViewModel { CategoriasViewModel(repo) }
                Scaffold(
                    topBar = { AppTopBar("Categorías") },
                    bottomBar = { BarraSoporte(Rutas.CATEGORIAS, irSoporte) },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { nav.navigate(Rutas.formularioCategoria()) },
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ) {
                            Icon(Icons.Filled.Add, contentDescription = "Nueva categoría")
                        }
                    }
                ) { padding ->
                    Box(Modifier.padding(padding)) {
                        CategoriasScreen(
                            vm,
                            onEditar = { id -> nav.navigate(Rutas.formularioCategoria(id)) }
                        )
                    }
                }
            }

            composable(
                Rutas.FORMULARIO_CATEGORIA,
                arguments = listOf(navArgument("id") {
                    type = NavType.IntType
                    defaultValue = -1                 // -1 = nueva categoría
                })
            ) { entrada ->
                val id = entrada.arguments?.getInt("id") ?: -1
                val vm = ayudaViewModel(key = "form_categoria_$id") { CategoriasViewModel(repo) }
                FormularioCategoriaScreen(
                    vm = vm,
                    categoriaId = id.takeIf { it > 0 },
                    onCerrar = { nav.popBackStack() }
                )
            }

            composable(Rutas.AJUSTES) {
                AjustesScreen(
                    sesionVm = sesionVm,
                    onAtras = { nav.popBackStack() },
                    onCambiarPerfil = cambiarPerfil,
                    onCerrarSesion = cerrarSesion,
                    barraInferior = { BarraSoporte(Rutas.AJUSTES, irSoporte) }
                )
            }
        }
    }
}

/** Barra inferior del estudiante: Tickets, Nuevo y Perfil. */
@Composable
private fun BarraEstudiante(actual: String, onIr: (String) -> Unit) {
    BarraInferior(
        actual = actual,
        onIr = onIr,
        items = listOf(
            Triple(Rutas.MIS_TICKETS, "Tickets", Icons.Filled.Menu),
            Triple(Rutas.NUEVO_TICKET, "Nuevo", Icons.Filled.Add),
            Triple(Rutas.PERFIL, "Perfil", Icons.Outlined.Person)
        )
    )
}

/** Barra inferior de Soporte: Panel, Tickets, Categorías y Ajustes. */
@Composable
private fun BarraSoporte(actual: String, onIr: (String) -> Unit) {
    BarraInferior(
        actual = actual,
        onIr = onIr,
        items = listOf(
            Triple(Rutas.PANEL, "Panel", Icons.Outlined.BarChart),
            Triple(Rutas.BANDEJA, "Tickets", Icons.Filled.Menu),
            Triple(Rutas.CATEGORIAS, "Categorías", Icons.Outlined.LocalOffer),
            Triple(Rutas.AJUSTES, "Ajustes", Icons.Outlined.Settings)
        )
    )
}

@Composable
private fun BarraInferior(
    actual: String,
    onIr: (String) -> Unit,
    items: List<Triple<String, String, ImageVector>>
) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        items.forEach { (ruta, etiqueta, icono) ->
            NavigationBarItem(
                selected = actual == ruta,
                onClick = { onIr(ruta) },
                icon = { Icon(icono, contentDescription = null) },
                label = { Text(etiqueta) }
            )
        }
    }
}