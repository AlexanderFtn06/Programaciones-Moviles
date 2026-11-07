package com.tecsup.books.data.mapper

import com.tecsup.books.data.remote.dto.DetalleLibroDto
import com.tecsup.books.data.remote.dto.LibroDto
import com.tecsup.books.domain.model.Libro
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

fun LibroDto.aModelo(): Libro = Libro(
    id = key.removePrefix("/works/"),
    titulo = title,
    autor = authorName?.firstOrNull() ?: "Autor desconocido",
    portadaUrl = coverId?.let { "https://covers.openlibrary.org/b/id/$it-M.jpg" },
    anio = anio?.takeIf { it > 0 }?.toString()
)

// El detalle no trae autor: se toma del libro de la lista si está disponible
fun DetalleLibroDto.aModeloDetalle(id: String, base: Libro?): Libro = Libro(
    id = id,
    titulo = title ?: base?.titulo ?: "Sin título",
    autor = base?.autor ?: "Autor desconocido",
    portadaUrl = base?.portadaUrl
        ?: covers?.firstOrNull()?.let { "https://covers.openlibrary.org/b/id/$it-L.jpg" },
    esFavorito = base?.esFavorito ?: false,
    descripcion = description.aTexto(),
    anio = base?.anio ?: fechaPublicacion
)

// La descripción puede ser texto o un objeto { "type", "value" }
private fun JsonElement?.aTexto(): String? = when (this) {
    is JsonNull -> null
    is JsonPrimitive -> this.content
    is JsonObject -> (this["value"] as? JsonPrimitive)?.content
    else -> null
}