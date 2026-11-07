package com.tecsup.books.domain.model

data class Libro(
    val id: String,
    val titulo: String,
    val autor: String,
    val portadaUrl: String?,
    val esFavorito: Boolean = false,
    val descripcion: String? = null,
    val anio: String? = null,
    val temas: List<String> = emptyList()
)