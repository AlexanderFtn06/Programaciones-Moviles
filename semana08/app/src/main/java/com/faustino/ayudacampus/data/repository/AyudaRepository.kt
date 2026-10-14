package com.faustino.ayudacampus.data.repository

import com.faustino.ayudacampus.data.local.entity.Categoria
import kotlinx.coroutines.flow.Flow

interface AyudaRepository {

    // Usuarios
    // TODO (Paso 4): descomentar cuando existan Usuario (Paso 1) y UsuarioDao (Paso 2)
    // suspend fun login(correo: String, clave: String): Usuario?

    // Categorías (ejemplo resuelto)
    fun observarCategorias(): Flow<List<Categoria>>
    fun observarCategoriasActivas(): Flow<List<Categoria>>
    suspend fun guardarCategoria(c: Categoria)
    suspend fun eliminarCategoria(c: Categoria)
    suspend fun cambiarActiva(id: Int, activa: Boolean)

    // Tickets
    // TODO (Paso 4): descomentar cuando existan Ticket, Comentario y TicketConDetalle
    // suspend fun crearTicket(clienteId: Int, categoriaId: Int, titulo: String,
    //                         descripcion: String, ubicacion: String, prioridad: Prioridad): Long
    // fun observarTicketsCliente(clienteId: Int): Flow<List<TicketConDetalle>>
    // fun observarTodosLosTickets(): Flow<List<TicketConDetalle>>
    // fun observarTicket(id: Int): Flow<TicketConDetalle?>
    // suspend fun cambiarEstado(ticketId: Int, estado: EstadoTicket)
    // suspend fun cambiarPrioridad(ticketId: Int, prioridad: Prioridad)
    // suspend fun cerrarConCalificacion(ticketId: Int, calificacion: Int)
    // suspend fun reabrir(ticketId: Int)
    // suspend fun agregarComentario(ticketId: Int, autor: Usuario, texto: String)

    // Panel
    // fun observarCantidadPorEstado(estado: EstadoTicket): Flow<Int>
    // fun observarCantidadPorCategoria(): Flow<List<ConteoCategoria>>
    // fun observarAltasSinAtender(limite: Long): Flow<Int>
}