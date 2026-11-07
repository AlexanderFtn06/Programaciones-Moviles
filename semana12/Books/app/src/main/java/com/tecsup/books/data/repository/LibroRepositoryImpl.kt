package com.tecsup.books.data.repository

import com.tecsup.books.data.mapper.aModelo
import com.tecsup.books.data.mapper.aModeloDetalle
import com.tecsup.books.data.remote.ApiService
import com.tecsup.books.domain.model.Libro
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import java.io.IOException
import retrofit2.HttpException

class LibroRepositoryImpl(
    private val api: ApiService
) : LibroRepository {

    // Temporal: en H9 esta lista se reemplaza por Room como fuente de verdad
    private val _libros = MutableStateFlow<List<Libro>>(emptyList())

    override val libros: Flow<List<Libro>> = _libros.asStateFlow()

    override val favoritos: Flow<List<Libro>> =
        _libros.map { lista -> lista.filter { it.esFavorito } }

    override suspend fun refrescar(): Result<Int> = try {
        val nuevos = api.listar(subject = "science_fiction", language = "spa", limit = 30)
            .docs
            .map { it.aModelo() }
        _libros.value = nuevos
        Result.success(nuevos.size)
    } catch (e: IOException) {
        Result.failure(e)
    } catch (e: HttpException) {
        Result.failure(e)
    }

    override suspend fun alternarFavorito(id: String) = TODO("Se implementa en H10")

    override suspend fun cantidadGuardada(): Int = _libros.value.size

    override suspend fun cantidadFavoritos(): Int =
        _libros.value.count { it.esFavorito }

    override suspend fun limpiarCache() {
        _libros.value = emptyList()
    }

    override suspend fun obtenerDetalle(id: String): Result<Libro> = try {
        val base = _libros.value.find { it.id == id }
        val detalle = api.detalle(id)
        Result.success(detalle.aModeloDetalle(id, base))
    } catch (e: IOException) {
        Result.failure(e)
    } catch (e: HttpException) {
        Result.failure(e)
    }
}