package com.faustino.ayudacampus.data.repository

import com.faustino.ayudacampus.data.local.entity.Categoria
import com.faustino.ayudacampus.data.local.entity.Usuario
import com.faustino.ayudacampus.data.local.relation.TicketConDetalle
import com.faustino.ayudacampus.data.model.ConteoCategoria
import com.faustino.ayudacampus.data.model.Rol
import com.faustino.ayudacampus.data.model.EstadoTicket
import com.faustino.ayudacampus.data.model.Prioridad
import kotlinx.coroutines.flow.Flow

interface AyudaRepository {

    // Usuarios
    suspend fun login(correo: String, clave: String): Usuario?
    suspend fun obtenerUsuario(id: Int): Usuario?              // Paso 6: recordar la sesión

    suspend fun obtenerUsuarioPorRol(rol: Rol): Usuario?       // Paso 12: cambiar de perfil
    suspend fun correoRegistrado(correo: String): Boolean
    suspend fun registrarEstudiante(nombre: String, correo: String, clave: String): Usuario

    // Categorías (ejemplo resuelto)
    fun observarCategorias(): Flow<List<Categoria>>
    fun observarCategoriasActivas(): Flow<List<Categoria>>
    suspend fun guardarCategoria(c: Categoria)
    suspend fun eliminarCategoria(c: Categoria)
    suspend fun cambiarActiva(id: Int, activa: Boolean)
    suspend fun categoriaTieneTickets(categoriaId: Int): Boolean  // Paso 11: regla al eliminar

    // Tickets
    suspend fun crearTicket(
        clienteId: Int, categoriaId: Int, titulo: String,
        descripcion: String, ubicacion: String, prioridad: Prioridad
    ): Long
    fun observarTicketsCliente(clienteId: Int): Flow<List<TicketConDetalle>>
    fun observarTodosLosTickets(): Flow<List<TicketConDetalle>>
    fun observarTicket(id: Int): Flow<TicketConDetalle?>
    suspend fun cambiarEstado(ticketId: Int, estado: EstadoTicket)
    suspend fun cambiarPrioridad(ticketId: Int, prioridad: Prioridad)
    suspend fun cerrarConCalificacion(ticketId: Int, calificacion: Int)
    suspend fun reabrir(ticketId: Int)
    suspend fun agregarComentario(ticketId: Int, autor: Usuario, texto: String)

    // Panel
    fun observarCantidadPorEstado(estado: EstadoTicket): Flow<Int>
    fun observarCantidadPorCategoria(): Flow<List<ConteoCategoria>>
    fun observarAltasSinAtender(limite: Long): Flow<Int>
    fun observarTiempoPromedioResolucion(): Flow<Long?>
}