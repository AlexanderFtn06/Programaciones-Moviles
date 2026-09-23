package com.faustino.tecsupfit.navigation

sealed class Screen(val route: String) {

    object Home : Screen("home")

    object DetalleClase : Screen("detalle_clase/{claseId}") {
        fun createRoute(claseId: Int) = "detalle_clase/$claseId"
    }

    object Confirmacion : Screen("confirmacion/{claseId}") {
        fun createRoute(claseId: Int) = "confirmacion/$claseId"
    }

    object Reservas : Screen("reservas")

    object Rutinas : Screen("rutinas")

    object Perfil : Screen("perfil")
}