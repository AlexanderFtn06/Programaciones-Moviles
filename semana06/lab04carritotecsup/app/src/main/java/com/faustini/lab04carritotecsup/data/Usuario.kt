package com.faustini.lab04carritotecsup.data

data class Usuario(
    val nombre: String,
    val correo: String,
    val carrera: String,
    val institucion: String,
    val curso: String
) {
    val iniciales: String
        get() = nombre.split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .joinToString("") { it.first().uppercase() }
}

val usuarioActual = Usuario(
    nombre = "Alexander Faustino",
    correo = "alexander@tecsup.edu.pe",
    carrera = "Diseño y Desarrollo de Software",
    institucion = "Tecsup",
    curso = "Programación en Móviles"
)