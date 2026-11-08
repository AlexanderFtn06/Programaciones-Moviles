package com.tecsup.books.ui.registro

data class RegistroUiState(
    val nombre: String = "",
    val apellido: String = "",
    val edad: String = "",
    val correo: String = "",
    val clave: String = "",
    val confirmarClave: String = "",
    val cargando: Boolean = false,
    val error: String? = null
)