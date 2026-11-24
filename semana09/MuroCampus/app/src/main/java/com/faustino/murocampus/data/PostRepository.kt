package com.faustino.murocampus.data

import com.faustino.murocampus.data.remote.PostApi
import com.faustino.murocampus.model.Comentario
import com.faustino.murocampus.model.NuevoPost
import com.faustino.murocampus.model.Post
import kotlin.coroutines.cancellation.CancellationException

interface PostRepository {
    suspend fun listar(): Result<List<Post>>
    suspend fun detalle(id: Int): Result<Post>
    suspend fun comentarios(id: Int): Result<List<Comentario>>
    suspend fun porUsuario(userId: Int): Result<List<Post>>
    suspend fun crear(nuevo: NuevoPost): Result<Post>
    suspend fun borrar(id: Int): Result<Unit>
}

class PostRepositoryRemote(private val api: PostApi) : PostRepository {

    // Evita repetir el try/catch en cada función
    private suspend fun <T> llamar(bloque: suspend () -> T): Result<T> =
        try {
            Result.success(bloque())
        } catch (e: CancellationException) {
            throw e // nunca se traga la cancelación
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun listar(): Result<List<Post>> =
        llamar { api.listar() }

    override suspend fun detalle(id: Int): Result<Post> =
        llamar { api.detalle(id) }

    override suspend fun comentarios(id: Int): Result<List<Comentario>> =
        llamar { api.comentarios(id) }

    override suspend fun porUsuario(userId: Int): Result<List<Post>> =
        llamar { api.porUsuario(userId) }

    override suspend fun crear(nuevo: NuevoPost): Result<Post> =
        llamar { api.crear(nuevo) }

    override suspend fun borrar(id: Int): Result<Unit> =
        try {
            val resp = api.borrar(id)
            if (resp.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(retrofit2.HttpException(resp))
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
}