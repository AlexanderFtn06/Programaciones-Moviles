package com.tecsup.books.data.mapper

import com.tecsup.books.data.remote.dto.LibroDto
import com.tecsup.books.domain.model.Libro

fun LibroDto.aModelo(): Libro = Libro(
    id = key.removePrefix("/works/"),
    titulo = title,
    autor = authorName?.firstOrNull() ?: "Autor desconocido",
    portadaUrl = coverId?.let { "https://covers.openlibrary.org/b/id/$it-M.jpg" },
    anio = anio?.toString()
)