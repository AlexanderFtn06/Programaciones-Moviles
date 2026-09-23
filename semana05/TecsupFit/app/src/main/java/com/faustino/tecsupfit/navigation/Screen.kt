package com.faustino.tecsupfit.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Reservas : Screen("reservas")
    object Rutinas : Screen("rutinas")
    object Perfil : Screen("perfil")

    object Detail : Screen("detail/{claseId}") {
        fun createRoute(claseId: Int): String = "detail/$claseId"
    }

    object Confirmation : Screen("confirmation/{claseId}/{hora}") {
        fun createRoute(claseId: Int, hora: String): String = "confirmation/$claseId/$hora"
    }
}