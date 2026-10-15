package com.faustino.ayudacampus.data.repository

import com.faustino.ayudacampus.data.local.AyudaDatabase
import com.faustino.ayudacampus.data.local.entity.Categoria
import com.faustino.ayudacampus.data.local.entity.Comentario
import com.faustino.ayudacampus.data.local.entity.Ticket
import com.faustino.ayudacampus.data.local.entity.Usuario
import com.faustino.ayudacampus.data.local.relation.TicketConDetalle
import com.faustino.ayudacampus.data.model.ConteoCategoria
import com.faustino.ayudacampus.data.model.EstadoTicket
import com.faustino.ayudacampus.data.model.Prioridad
import kotlinx.coroutines.flow.Flow

class AyudaRepositoryRoom(db: AyudaDatabase) : AyudaRepository {

    private val categoriaDao = db.categoriaDao()
    private val usuarioDao = db.usuarioDao()
    private val ticketDao = db.ticketDao()

    // Usuarios

    override suspend fun login(correo: String, clave: String): Usuario? =
        usuarioDao.login(correo.trim(), clave)

    override suspend fun obtenerUsuario(id: Int): Usuario? =
        usuarioDao.obtenerPorId(id)

    //  Categorías (ejemplo resuelto)

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

    override suspend fun categoriaTieneTickets(categoriaId: Int): Boolean =
        ticketDao.contarDeCategoria(categoriaId) > 0

    //  Tickets: escritura (suspend)

    override suspend fun crearTicket(
        clienteId: Int, categoriaId: Int, titulo: String,
        descripcion: String, ubicacion: String, prioridad: Prioridad
    ): Long {
        val ticket = Ticket(
            clienteId = clienteId,
            categoriaId = categoriaId,
            titulo = titulo.trim(),
            descripcion = descripcion.trim(),
            ubicacion = ubicacion.trim(),
            prioridad = prioridad.name,               // enum.name en la tabla
            estado = EstadoTicket.ABIERTO.name,       // estado inicial
            fecha = System.currentTimeMillis()        // fecha actual
        )
        return ticketDao.insertarTicket(ticket)       // devuelve el id
    }

    override suspend fun cambiarEstado(ticketId: Int, estado: EstadoTicket) =
        ticketDao.actualizarEstado(ticketId, estado.name)

    override suspend fun cambiarPrioridad(ticketId: Int, prioridad: Prioridad) =
        ticketDao.actualizarPrioridad(ticketId, prioridad.name)

    override suspend fun cerrarConCalificacion(ticketId: Int, calificacion: Int) =
        ticketDao.cerrar(ticketId, calificacion, System.currentTimeMillis())

    override suspend fun reabrir(ticketId: Int) =
        ticketDao.reabrir(ticketId)

    override suspend fun agregarComentario(ticketId: Int, autor: Usuario, texto: String) {
        // Copia nombre y rol del autor: "foto" del momento
        ticketDao.insertarComentario(
            Comentario(
                ticketId = ticketId,
                autorId = autor.id,
                autorNombre = autor.nombre,
                autorRol = autor.rol,
                texto = texto.trim()
            )
        )
    }

    //  Tickets: lectura (Flow, sin suspend)

    override fun observarTicketsCliente(clienteId: Int): Flow<List<TicketConDetalle>> =
        ticketDao.observarPorCliente(clienteId)

    override fun observarTodosLosTickets(): Flow<List<TicketConDetalle>> =
        ticketDao.observarTodos()

    override fun observarTicket(id: Int): Flow<TicketConDetalle?> =
        ticketDao.observarUno(id)

    //  Panel

    override fun observarCantidadPorEstado(estado: EstadoTicket): Flow<Int> =
        ticketDao.contarPorEstado(estado.name)

    override fun observarCantidadPorCategoria(): Flow<List<ConteoCategoria>> =
        ticketDao.contarPorCategoria()

    override fun observarAltasSinAtender(limite: Long): Flow<Int> =
        ticketDao.contarAltasSinAtender(limite)
}