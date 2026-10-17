package com.faustino.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faustino.ayudacampus.data.local.entity.Comentario
import com.faustino.ayudacampus.data.local.entity.Usuario
import com.faustino.ayudacampus.data.local.relation.TicketConDetalle
import com.faustino.ayudacampus.data.model.EstadoTicket
import com.faustino.ayudacampus.data.repository.AyudaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TicketDetalleViewModel(
    private val repo: AyudaRepository,
    private val ticketId: Int,
    private val usuario: Usuario
) : ViewModel() {

    val usuarioId: Int = usuario.id

    // Regla de visibilidad: el estudiante solo ve SUS tickets
    val ticket: StateFlow<TicketConDetalle?> = repo.observarTicket(ticketId)
        .map { t -> t?.takeIf { it.ticket.clienteId == usuario.id } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // @Relation no ordena: los ordenamos aquí por fecha
    val comentarios: StateFlow<List<Comentario>> = ticket
        .map { t -> t?.comentarios?.filter { !it.interno }?.sortedBy { it.fecha } ?: emptyList() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _texto = MutableStateFlow("")
    val texto: StateFlow<String> = _texto.asStateFlow()

    private val _calificacion = MutableStateFlow(0)
    val calificacion: StateFlow<Int> = _calificacion.asStateFlow()

    private val _comentarioFinal = MutableStateFlow("")
    val comentarioFinal: StateFlow<String> = _comentarioFinal.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun cambiarTexto(t: String) {
        _texto.value = t
        _error.value = null
    }

    fun calificar(estrellas: Int) {
        _calificacion.value = estrellas
        _error.value = null
    }

    fun cambiarComentarioFinal(t: String) {
        _comentarioFinal.value = t
    }

    private fun estadoActual(): EstadoTicket? =
        ticket.value?.let { EstadoTicket.desde(it.ticket.estado) }

    fun enviarComentario() {
        val texto = _texto.value.trim()
        if (texto.isEmpty()) return

        // Regla: si está CERRADO no se puede comentar
        val estado = estadoActual()
        if (estado == null || estado == EstadoTicket.CERRADO) {
            _error.value = "Este ticket está cerrado"
            return
        }
        _texto.value = ""
        viewModelScope.launch { repo.agregarComentario(ticketId, usuario, texto) }
    }

    fun confirmarYCerrar() {
        // Solo se cierra un ticket RESUELTO
        if (estadoActual() != EstadoTicket.RESUELTO) return

        val estrellas = _calificacion.value
        if (estrellas !in 1..5) {
            _error.value = "Elige de 1 a 5 estrellas para cerrar el ticket"
            return
        }
        val final = _comentarioFinal.value.trim()
        viewModelScope.launch {
            if (final.isNotEmpty()) repo.agregarComentario(ticketId, usuario, final)
            repo.cerrarConCalificacion(ticketId, estrellas)
            _comentarioFinal.value = ""
        }
    }

    fun reabrir() {
        if (estadoActual() != EstadoTicket.RESUELTO) return
        viewModelScope.launch {
            repo.reabrir(ticketId)
            _calificacion.value = 0
        }
    }
}