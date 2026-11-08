package com.tecsup.books.domain.model

data class Sesion(
    val uid: String,
    val correo: String,
    val rol: Rol,
    val nombre: String,
    val apellido: String? = null,
    val edad: Int? = null,
    val rolConfirmado: Boolean = true
)

enum class Rol { ADMIN, USUARIO }