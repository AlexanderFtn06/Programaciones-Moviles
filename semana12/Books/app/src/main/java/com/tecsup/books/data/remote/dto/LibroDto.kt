package com.tecsup.books.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

// Respuesta de search.json: el envoltorio tiene la lista en "docs"
@Serializable
data class BusquedaDto(
    val numFound: Int = 0,
    val docs: List<LibroDto> = emptyList()
)

@Serializable
data class LibroDto(
    val key: String = "",
    val title: String = "Sin título",
    @SerialName("author_name") val authorName: List<String>? = null,
    @SerialName("cover_i") val coverId: Int? = null,
    @SerialName("first_publish_year") val anio: Int? = null
)

// Respuesta de works/{id}.json
@Serializable
data class DetalleLibroDto(
    val key: String? = null,
    val title: String? = null,
    // Puede ser texto o un objeto { "type", "value" }. Lo tratamos en el mapper.
    val description: JsonElement? = null,
    val subjects: List<String>? = null,
    val covers: List<Int>? = null,
    @SerialName("first_publish_date") val fechaPublicacion: String? = null
)