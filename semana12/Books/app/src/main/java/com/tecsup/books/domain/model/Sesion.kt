package com.tecsup.books.domain.model

data class Sesion(
    val uid: String,
    val correo: String,
    val rol: Rol,
    val nombre: String
)

enum class Rol { ADMIN, USUARIO }