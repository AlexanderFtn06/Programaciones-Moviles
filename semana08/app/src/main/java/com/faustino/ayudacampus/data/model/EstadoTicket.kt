package com.faustino.ayudacampus.data.model

enum class EstadoTicket(val etiqueta: String) {
    ABIERTO("Abierto"),
    EN_PROCESO("En proceso"),
    RESUELTO("Resuelto"),
    CERRADO("Cerrado");

    companion object {
        fun desde(nombre: String): EstadoTicket =
            entries.firstOrNull { it.name == nombre } ?: ABIERTO
    }
}