package com.faustino.ayudacampus.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.faustino.ayudacampus.AyudaApp
import com.faustino.ayudacampus.data.model.Rol
import com.faustino.ayudacampus.ui.cliente.SplashScreen
import com.faustino.ayudacampus.ui.components.AppTopBar
import com.faustino.ayudacampus.ui.soporte.CategoriasScreen
import com.faustino.ayudacampus.ui.theme.AyudaTheme
import com.faustino.ayudacampus.viewmodel.CategoriasViewModel
import com.faustino.ayudacampus.viewmodel.ayudaViewModel

@Composable
fun NavAyuda(app: AyudaApp) {
    val nav = rememberNavController()
    val repo = app.repository

    // TODO (Pasos 6 y 12): el rol y el modo oscuro vendrán de DataStore
    AyudaTheme(rol = Rol.ADMIN) {
        NavHost(navController = nav, startDestination = Rutas.SPLASH) {

            composable(Rutas.SPLASH) {
                SplashScreen(onTerminar = {
                    // TODO (Paso 6): ir a LOGIN o a la pantalla principal según el perfil guardado
                    nav.navigate(Rutas.CATEGORIAS) {
                        popUpTo(Rutas.SPLASH) { inclusive = true }
                    }
                })
            }

            composable(Rutas.CATEGORIAS) {
                val vm = ayudaViewModel { CategoriasViewModel(repo) }
                Scaffold(topBar = { AppTopBar("Categorías") }) { padding ->
                    Box(Modifier.padding(padding)) {
                        CategoriasScreen(vm, onEditar = { /* TODO (Paso 11): abrir formulario */ })
                    }
                }
            }

            // TODO (Paso 6): LOGIN
            // TODO (Pasos 7-9): MIS_TICKETS, NUEVO_TICKET, DETALLE_TICKET
            // TODO (Pasos 10-11): PANEL, BANDEJA, ATENDER_TICKET, FORMULARIO_CATEGORIA
            // TODO (Paso 12): PERFIL, AJUSTES y NavigationBar de cada perfil
        }
    }
}