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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.faustino.ayudacampus.AyudaApp
import com.faustino.ayudacampus.data.local.entity.Usuario
import com.faustino.ayudacampus.data.model.Rol
import com.faustino.ayudacampus.ui.cliente.LoginScreen
import com.faustino.ayudacampus.ui.cliente.SplashScreen
import com.faustino.ayudacampus.ui.components.AppTopBar
import com.faustino.ayudacampus.ui.components.AyudaButton
import com.faustino.ayudacampus.ui.soporte.CategoriasScreen
import com.faustino.ayudacampus.ui.theme.AyudaTheme
import com.faustino.ayudacampus.viewmodel.CategoriasViewModel
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

    AyudaTheme(rol = rolActual, oscuro = oscuro) {
        NavHost(navController = nav, startDestination = Rutas.SPLASH) {

            // Splash: si ya hay perfil guardado, salta el login
            composable(Rutas.SPLASH) {
                var animacionLista by remember { mutableStateOf(false) }
                val verificada by sesionVm.sesionVerificada.collectAsState()

                SplashScreen(onTerminar = { animacionLista = true })

                LaunchedEffect(animacionLista, verificada) {
                    if (animacionLista && verificada) {
                        val destino = usuario?.let { inicioDe(it) } ?: Rutas.LOGIN
                        nav.navigate(destino) {
                            popUpTo(Rutas.SPLASH) { inclusive = true }
                        }
                    }
                }
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

            // TEMPORAL: se reemplaza en el Paso 7
            composable(Rutas.MIS_TICKETS) {
                PantallaPendiente(
                    titulo = "Mis tickets",
                    usuario = usuario,
                    nota = "Esta pantalla se construye en el Paso 7.",
                    onCerrarSesion = cerrarSesion
                )
            }

            // TEMPORAL: se reemplaza en el Paso 10
            composable(Rutas.PANEL) {
                PantallaPendiente(
                    titulo = "Panel",
                    usuario = usuario,
                    nota = "Esta pantalla se construye en el Paso 10.",
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

            // TODO (Pasos 7-9): MIS_TICKETS, NUEVO_TICKET, DETALLE_TICKET
            // TODO (Pasos 10-11): PANEL, BANDEJA, ATENDER_TICKET, FORMULARIO_CATEGORIA
            // TODO (Paso 12): PERFIL, AJUSTES y NavigationBar de cada perfil
        }
    }
}

/** Pantalla provisional mientras se construyen las reales. */
@Composable
private fun PantallaPendiente(
    titulo: String,
    usuario: Usuario?,
    nota: String,
    onCerrarSesion: () -> Unit,
    onExtra: (() -> Unit)? = null,
    textoExtra: String = ""
) {
    Scaffold(
        topBar = {
            AppTopBar(titulo, acciones = {
                IconButton(onClick = onCerrarSesion) {
                    Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Cerrar sesión")
                }
            })
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Hola, ${usuario?.nombre ?: ""}", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(8.dp))
            Text(nota, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(24.dp))
            if (onExtra != null) {
                AyudaButton(textoExtra, onClick = onExtra)
                Spacer(Modifier.height(12.dp))
            }
            AyudaButton("Cerrar sesión", onClick = onCerrarSesion, secundario = true)
        }
    }
}