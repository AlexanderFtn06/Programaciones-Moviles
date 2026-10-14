package com.faustino.ayudacampus.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.faustino.ayudacampus.data.local.entity.Comentario
import com.faustino.ayudacampus.data.local.entity.Ticket
import com.faustino.ayudacampus.data.local.relation.TicketConDetalle
import com.faustino.ayudacampus.data.model.ConteoCategoria
import kotlinx.coroutines.flow.Flow

@Dao
interface TicketDao {

    // ---------- Escritura (suspend) ----------

    @Insert
    suspend fun insertarTicket(t: Ticket): Long

    @Insert
    suspend fun insertarComentario(c: Comentario)

    @Query("UPDATE tickets SET estado = :estado WHERE id = :id")
    suspend fun actualizarEstado(id: Int, estado: String)

    @Query("UPDATE tickets SET prioridad = :prioridad WHERE id = :id")
    suspend fun actualizarPrioridad(id: Int, prioridad: String)

    @Query(
        """UPDATE tickets
           SET estado = 'CERRADO', calificacion = :calificacion, fechaCierre = :fechaCierre
           WHERE id = :id"""
    )
    suspend fun cerrar(id: Int, calificacion: Int, fechaCierre: Long)

    @Query(
        """UPDATE tickets
           SET estado = 'ABIERTO', calificacion = NULL, fechaCierre = NULL
           WHERE id = :id"""
    )
    suspend fun reabrir(id: Int)

    // Se usará en el Paso 11: no se puede eliminar una categoría con tickets
    @Query("SELECT COUNT(*) FROM tickets WHERE categoriaId = :categoriaId")
    suspend fun contarDeCategoria(categoriaId: Int): Int

    // ---------- Lectura (Flow, sin suspend) ----------
    // Las consultas que devuelven una clase con @Relation necesitan @Transaction

    @Transaction
    @Query("SELECT * FROM tickets WHERE clienteId = :clienteId ORDER BY fecha DESC")
    fun observarPorCliente(clienteId: Int): Flow<List<TicketConDetalle>>

    @Transaction
    @Query(
        """SELECT * FROM tickets
           ORDER BY CASE prioridad WHEN 'ALTA' THEN 0 WHEN 'MEDIA' THEN 1 ELSE 2 END,
                    fecha DESC"""
    )
    fun observarTodos(): Flow<List<TicketConDetalle>>

    @Transaction
    @Query("SELECT * FROM tickets WHERE id = :id")
    fun observarUno(id: Int): Flow<TicketConDetalle?>

    // ---------- Panel ----------

    @Query("SELECT COUNT(*) FROM tickets WHERE estado = :estado")
    fun contarPorEstado(estado: String): Flow<Int>

    // LEFT JOIN + COUNT(t.id): incluye también las categorías sin tickets (con 0)
    @Query(
        """SELECT c.nombre AS nombre, c.icono AS icono, COUNT(t.id) AS cantidad
           FROM categorias c
           LEFT JOIN tickets t ON t.categoriaId = c.id
           GROUP BY c.id
           ORDER BY cantidad DESC, c.nombre"""
    )
    fun contarPorCategoria(): Flow<List<ConteoCategoria>>

    // ALTA + ABIERTO creados antes del límite (ahora - 1 hora)
    @Query(
        """SELECT COUNT(*) FROM tickets
           WHERE prioridad = 'ALTA' AND estado = 'ABIERTO' AND fecha < :limite"""
    )
    fun contarAltasSinAtender(limite: Long): Flow<Int>
}