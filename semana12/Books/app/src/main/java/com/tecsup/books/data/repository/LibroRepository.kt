package com.tecsup.books.data.repository

import com.tecsup.books.domain.model.Libro
import kotlinx.coroutines.flow.Flow

interface LibroRepository {
    val libros: Flow<List<Libro>>
    val favoritos: Flow<List<Libro>>

    suspend fun refrescar(): Result<Int>
    suspend fun alternarFavorito(id: String)
    suspend fun cantidadGuardada(): Int
    suspend fun cantidadFavoritos(): Int
    suspend fun limpiarCache()
    suspend fun obtenerDetalle(id: String): Result<Libro>
}