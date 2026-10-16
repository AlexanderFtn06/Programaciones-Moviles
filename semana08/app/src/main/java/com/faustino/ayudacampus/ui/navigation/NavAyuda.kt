package com.faustino.ayudacampus.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
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
import com.faustino.ayudacampus.ui.cliente.SplashScreen
import com.faustino.ayudacampus.ui.components.AppTopBar
import com.faustino.ayudacampus.ui.components.AyudaButton
import com.faustino.ayudacampus.ui.components.numeroTicket
import com.faustino.ayudacampus.ui.soporte.BandejaScreen
import com.faustino.ayudacampus.ui.soporte.CategoriasScreen
import com.faustino.ayudacampus.ui.soporte.PanelScreen
import com.faustino.ayudacampus.ui.theme.AyudaTheme
import com.faustino.ayudacampus.viewmodel.BandejaViewModel
import com.faustino.ayudacampus.viewmodel.CategoriasViewModel
import com.faustino.ayudacampus.viewmodel.MisTicketsViewModel
import com.faustino.ayudacampus.viewmodel.NuevoTicketViewModel
import com.faustino.ayudacampus.viewmodel.PanelViewModel
import com.faustino.ayudacampus.viewmodel.SesionViewModel
import com.faustino.ayudacampus.viewmodel.TicketDetalleViewModel
import com.faustino.ayudacampus.viewmodel.ayudaViewModel

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

    // El tema sigue al perfil activo
    val rolActual = usuario?.let { Rol.desde(it.rol) } ?: Rol.CLIENTE

    val cerrarSesion: () -> Unit = {
        sesionVm.cerrarSesion()
        nav.navigate(Rutas.LOGIN) {
            popUpTo(nav.graph.id) { inclusive = true }
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
                    onComenzar = irALogin,
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
                        NuevoTicketViewModel(repo, u)
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

            // TEMPORAL: se reemplaza en el Paso 12
            composable(Rutas.PERFIL) {
                PantallaPendiente(
                    titulo = "Perfil",
                    nota = "El perfil se construye en el Paso 12.",
                    usuario = usuario,
                    onCerrarSesion = cerrarSesion,
                    barraInferior = { BarraEstudiante(Rutas.PERFIL, irEstudiante) }
                )
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

            // TEMPORAL: se reemplaza en el Paso 11
            composable(
                Rutas.ATENDER_TICKET,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { entrada ->
                val id = entrada.arguments?.getInt("id") ?: 0
                PantallaPendiente(
                    titulo = "Ticket ${numeroTicket(id)}",
                    nota = "La atención del ticket se construye en el Paso 11.",
                    onAtras = { nav.popBackStack() }
                )
            }

            composable(Rutas.CATEGORIAS) {
                val vm = ayudaViewModel { CategoriasViewModel(repo) }
                Scaffold(
                    topBar = { AppTopBar("Categorías") },
                    bottomBar = { BarraSoporte(Rutas.CATEGORIAS, irSoporte) }
                ) { padding ->
                    Box(Modifier.padding(padding)) {
                        CategoriasScreen(vm, onEditar = { /* TODO (Paso 11): abrir formulario */ })
                    }
                }
            }

            // TEMPORAL: se reemplaza en el Paso 12
            composable(Rutas.AJUSTES) {
                PantallaPendiente(
                    titulo = "Ajustes",
                    nota = "Los ajustes se construyen en el Paso 12.",
                    usuario = usuario,
                    onCerrarSesion = cerrarSesion,
                    barraInferior = { BarraSoporte(Rutas.AJUSTES, irSoporte) }
                )
            }

            // TODO (Paso 11): ATENDER_TICKET real y FORMULARIO_CATEGORIA
            // TODO (Paso 12): PERFIL y AJUSTES reales
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

/** Pantalla provisional mientras se construyen las reales. */
@Composable
private fun PantallaPendiente(
    titulo: String,
    nota: String,
    usuario: Usuario? = null,
    onAtras: (() -> Unit)? = null,
    onCerrarSesion: (() -> Unit)? = null,
    barraInferior: @Composable () -> Unit = {}
) {
    Scaffold(
        topBar = {
            AppTopBar(titulo, onAtras = onAtras, acciones = {
                if (onCerrarSesion != null) {
                    IconButton(onClick = onCerrarSesion) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Cerrar sesión")
                    }
                }
            })
        },
        bottomBar = barraInferior
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (usuario != null) {
                Text("Hola, ${usuario.nombre}", style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(8.dp))
            }
            Text(nota, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(24.dp))
            if (onCerrarSesion != null) {
                AyudaButton("Cerrar sesión", onClick = onCerrarSesion, secundario = true)
            }
        }
    }
}