package com.faustino.ayudacampus.data.model

enum class Prioridad(val etiqueta: String) {
    BAJA("Baja"),
    MEDIA("Media"),
    ALTA("Alta");

    companion object {
        fun desde(nombre: String): Prioridad =
            entries.firstOrNull { it.name == nombre } ?: MEDIA
    }
}