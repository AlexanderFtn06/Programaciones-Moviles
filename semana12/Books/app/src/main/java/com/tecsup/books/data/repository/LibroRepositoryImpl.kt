package com.tecsup.books.data.repository

import com.tecsup.books.data.local.LibroDao
import com.tecsup.books.data.mapper.aEntity
import com.tecsup.books.data.mapper.aModelo
import com.tecsup.books.data.mapper.aModeloDetalle
import com.tecsup.books.data.remote.ApiService
import com.tecsup.books.domain.model.Libro
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.IOException
import retrofit2.HttpException

class LibroRepositoryImpl(
    private val api: ApiService,
    private val dao: LibroDao
) : LibroRepository {

    // Room es la fuente única de verdad: la UI solo observa estos flujos
    override val libros: Flow<List<Libro>> =
        dao.todos().map { lista -> lista.map { it.aModelo() } }

    override val favoritos: Flow<List<Libro>> =
        dao.favoritos().map { lista -> lista.map { it.aModelo() } }

    // Retrofit solo actualiza Room; la UI no recibe datos directamente de la red
    override suspend fun refrescar(): Result<Int> = try {
        val remotos = api.listar(subject = "science_fiction", language = "spa", limit = 30)
            .docs
            .map { it.aModelo().aEntity() }

        // Conservamos los favoritos y las descripciones ya guardadas, porque upsert reemplaza la fila completa
        val previos = dao.listaActual().associateBy { it.id }
        val fusionados = remotos.map { nuevo ->
            val previo = previos[nuevo.id]
            nuevo.copy(
                esFavorito = previo?.esFavorito ?: false,
                descripcion = previo?.descripcion
            )
        }
        dao.guardarTodos(fusionados)
        Result.success(fusionados.size)
    } catch (e: IOException) {
        Result.failure(e)
    } catch (e: HttpException) {
        Result.failure(e)
    }

    override suspend fun alternarFavorito(id: String) {
        dao.alternarFavorito(id)
    }
    override suspend fun cantidadGuardada(): Int = dao.contar()

    override suspend fun cantidadFavoritos(): Int = dao.contarFavoritos()

    override suspend fun limpiarCache() {
        dao.borrarTodo()
    }

    override suspend fun obtenerDetalle(id: String): Result<Libro> {
        val base = dao.obtener(id)?.aModelo()

        // Si el detalle ya está guardado, se devuelve sin usar la red (funciona sin internet)
        if (base?.descripcion != null) return Result.success(base)

        return try {
            val detalle = api.detalle(id).aModeloDetalle(id, base)
            // Guardamos la descripción para que el detalle funcione después sin conexión
            if (base != null) dao.guardarTodos(listOf(detalle.aEntity()))
            Result.success(detalle)
        } catch (e: IOException) {
            Result.failure(e)
        } catch (e: HttpException) {
            Result.failure(e)
        }
    }
}