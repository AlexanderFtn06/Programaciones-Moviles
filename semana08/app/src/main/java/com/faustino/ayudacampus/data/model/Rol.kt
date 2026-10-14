package com.faustino.ayudacampus.data.model

enum class Rol(val etiqueta: String) {
    CLIENTE("Estudiante"),
    ADMIN("Soporte TI");

    companion object {
        fun desde(nombre: String?): Rol? = entries.firstOrNull { it.name == nombre }
    }
}