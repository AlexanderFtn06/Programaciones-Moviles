package com.tecsup.books.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.books.AppContainer
import com.tecsup.books.ui.detalle.DetalleScreen
import com.tecsup.books.ui.detalle.DetalleViewModel
import com.tecsup.books.ui.detalle.DetalleViewModelFactory
import com.tecsup.books.ui.lista.ListaScreen
import com.tecsup.books.ui.lista.ListaViewModel
import com.tecsup.books.ui.lista.ListaViewModelFactory

@Composable
fun AppNav(container: AppContainer) {
    val navController = rememberNavController()

    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "lista",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("lista") {
                val viewModel: ListaViewModel = viewModel(
                    factory = ListaViewModelFactory(container)
                )
                val estado by viewModel.estado.collectAsStateWithLifecycle()

                ListaScreen(
                    estado = estado,
                    onReintentar = viewModel::cargar,
                    onLibroClick = { id -> navController.navigate("detalle/$id") }
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
                    onVolver = { navController.popBackStack() }
                )
            }
        }
    }
}