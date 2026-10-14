package com.faustino.ayudacampus.data.repository

import com.faustino.ayudacampus.data.local.AyudaDatabase
import com.faustino.ayudacampus.data.local.entity.Categoria
import kotlinx.coroutines.flow.Flow

class AyudaRepositoryRoom(db: AyudaDatabase) : AyudaRepository {

    private val categoriaDao = db.categoriaDao()
    // TODO (Paso 4): private val usuarioDao = db.usuarioDao()
    // TODO (Paso 4): private val ticketDao = db.ticketDao()

    // ---------- Categorías (ejemplo resuelto) ----------
    override fun observarCategorias(): Flow<List<Categoria>> =
        categoriaDao.observarTodas()

    override fun observarCategoriasActivas(): Flow<List<Categoria>> =
        categoriaDao.observarActivas()

    override suspend fun guardarCategoria(c: Categoria) {
        if (c.id == 0) categoriaDao.insertar(c) else categoriaDao.actualizar(c)
    }

    override suspend fun eliminarCategoria(c: Categoria) =
        categoriaDao.eliminar(c)

    override suspend fun cambiarActiva(id: Int, activa: Boolean) =
        categoriaDao.cambiarActiva(id, activa)

    // ---------- TODO (Paso 4): usuarios, tickets, comentarios y panel ----------
}