package com.faustino.ayudacampus.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import com.faustino.ayudacampus.ui.cliente.DetalleTicketScreen
import com.faustino.ayudacampus.viewmodel.TicketDetalleViewModel
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import com.faustino.ayudacampus.ui.cliente.NuevoTicketScreen
import com.faustino.ayudacampus.viewmodel.NuevoTicketViewModel
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import kotlinx.coroutines.delay
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
import com.faustino.ayudacampus.ui.cliente.LoginScreen
import com.faustino.ayudacampus.ui.cliente.MisTicketsScreen
import com.faustino.ayudacampus.ui.cliente.SplashScreen
import com.faustino.ayudacampus.ui.components.AppTopBar
import com.faustino.ayudacampus.ui.components.AyudaButton
import com.faustino.ayudacampus.ui.components.numeroTicket
import com.faustino.ayudacampus.ui.soporte.CategoriasScreen
import com.faustino.ayudacampus.ui.theme.AyudaTheme
import com.faustino.ayudacampus.viewmodel.CategoriasViewModel
import com.faustino.ayudacampus.viewmodel.MisTicketsViewModel
import com.faustino.ayudacampus.viewmodel.SesionViewModel
import com.faustino.ayudacampus.viewmodel.ayudaViewModel

/** Pantalla principal de cada perfil. */
private fun inicioDe(u: Usuario): String =
    if (u.rol == Rol.ADMIN.name) Rutas.PANEL else Rutas.MIS_TICKETS

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

    // Navegación de la barra inferior del estudiante
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

            // ---------- Estudiante ----------

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

            // TEMPORAL: se reemplaza en el Paso 8
            composable(Rutas.NUEVO_TICKET) {
                usuario?.let { u ->
                    val vm = ayudaViewModel(key = "nuevo_ticket_${u.id}") {
                        NuevoTicketViewModel(repo, u)
                    }
                    NuevoTicketScreen(
                        vm = vm,
                        onAtras = { nav.popBackStack() },
                        // Vuelve a Mis tickets: el nuevo aparece arriba (el DAO ordena por fecha)
                        onEnviado = { nav.popBackStack(Rutas.MIS_TICKETS, inclusive = false) },
                        barraInferior = { BarraEstudiante(Rutas.NUEVO_TICKET, irEstudiante) }
                    )
                }
            }

            // TEMPORAL: se reemplaza en el Paso 9
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

            // ---------- Soporte TI ----------

            // TEMPORAL: se reemplaza en el Paso 10
            composable(Rutas.PANEL) {
                PantallaPendiente(
                    titulo = "Panel",
                    nota = "Esta pantalla se construye en el Paso 10.",
                    usuario = usuario,
                    onCerrarSesion = cerrarSesion,
                    onExtra = { nav.navigate(Rutas.CATEGORIAS) },
                    textoExtra = "Ver categorías"
                )
            }

            composable(Rutas.CATEGORIAS) {
                val vm = ayudaViewModel { CategoriasViewModel(repo) }
                Scaffold(
                    topBar = { AppTopBar("Categorías", onAtras = { nav.popBackStack() }) }
                ) { padding ->
                    Box(Modifier.padding(padding)) {
                        CategoriasScreen(vm, onEditar = { /* TODO (Paso 11): abrir formulario */ })
                    }
                }
            }

            // TODO (Pasos 10-11): BANDEJA, ATENDER_TICKET, FORMULARIO_CATEGORIA
            // TODO (Paso 12): AJUSTES y NavigationBar de Soporte
        }
    }
}

/** Barra inferior del estudiante: Tickets, Nuevo y Perfil. */
@Composable
private fun BarraEstudiante(actual: String, onIr: (String) -> Unit) {
    val items: List<Triple<String, String, ImageVector>> = listOf(
        Triple(Rutas.MIS_TICKETS, "Tickets", Icons.Filled.Menu),
        Triple(Rutas.NUEVO_TICKET, "Nuevo", Icons.Filled.Add),
        Triple(Rutas.PERFIL, "Perfil", Icons.Outlined.Person)
    )
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
    onExtra: (() -> Unit)? = null,
    textoExtra: String = "",
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
            if (onExtra != null) {
                AyudaButton(textoExtra, onClick = onExtra)
                Spacer(Modifier.height(12.dp))
            }
            if (onCerrarSesion != null) {
                AyudaButton("Cerrar sesión", onClick = onCerrarSesion, secundario = true)
            }
        }
    }
}