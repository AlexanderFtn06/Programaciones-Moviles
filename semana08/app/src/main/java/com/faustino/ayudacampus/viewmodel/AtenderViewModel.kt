package com.faustino.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faustino.ayudacampus.data.local.entity.Comentario
import com.faustino.ayudacampus.data.local.entity.Usuario
import com.faustino.ayudacampus.data.local.relation.TicketConDetalle
import com.faustino.ayudacampus.data.model.EstadoTicket
import com.faustino.ayudacampus.data.model.Prioridad
import com.faustino.ayudacampus.data.repository.AyudaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AtenderViewModel(
    private val repo: AyudaRepository,
    private val ticketId: Int,
    private val usuario: Usuario
) : ViewModel() {

    val usuarioId: Int = usuario.id

    // Regla de visibilidad: Soporte ve todos los tickets
    val ticket: StateFlow<TicketConDetalle?> = repo.observarTicket(ticketId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val comentarios: StateFlow<List<Comentario>> = ticket
        .map { t -> t?.comentarios?.sortedBy { it.fecha } ?: emptyList() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Cambios elegidos en pantalla que aún no se guardan (null = sin cambio)
    private val _estadoEditado = MutableStateFlow<EstadoTicket?>(null)
    private val _prioridadEditada = MutableStateFlow<Prioridad?>(null)

    val estadoSeleccionado: StateFlow<EstadoTicket?> =
        combine(ticket, _estadoEditado) { t, e -> e ?: t?.let { EstadoTicket.desde(it.ticket.estado) } }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val prioridadSeleccionada: StateFlow<Prioridad?> =
        combine(ticket, _prioridadEditada) { t, p -> p ?: t?.let { Prioridad.desde(it.ticket.prioridad) } }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // "Guardar cambios" solo se habilita si algo es distinto a lo guardado
    val hayCambios: StateFlow<Boolean> =
        combine(ticket, _estadoEditado, _prioridadEditada) { t, e, p ->
            t != null && ((e != null && e.name != t.ticket.estado) ||
                    (p != null && p.name != t.ticket.prioridad))
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private val _texto = MutableStateFlow("")
    val texto: StateFlow<String> = _texto.asStateFlow()

    private val _modoNotaInterna = MutableStateFlow(false)
    val modoNotaInterna: StateFlow<Boolean> = _modoNotaInterna.asStateFlow()

    fun alternarNotaInterna() {
        _modoNotaInterna.value = !_modoNotaInterna.value
    }

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    private fun estadoActual(): EstadoTicket? =
        ticket.value?.let { EstadoTicket.desde(it.ticket.estado) }

    fun cambiarTexto(t: String) {
        _texto.value = t
    }

    fun elegirEstado(e: EstadoTicket) {
        if (estadoActual() == EstadoTicket.CERRADO) {
            _mensaje.value = "El ticket está cerrado"
            return
        }
        // Regla: Soporte solo puede pasar un ticket a EN_PROCESO o RESUELTO
        if (e != EstadoTicket.EN_PROCESO && e != EstadoTicket.RESUELTO) return
        _estadoEditado.value = e
    }

    fun elegirPrioridad(p: Prioridad) {
        if (estadoActual() == EstadoTicket.CERRADO) {
            _mensaje.value = "El ticket está cerrado"
            return
        }
        _prioridadEditada.value = p
    }

    fun guardarCambios() {
        val t = ticket.value ?: return
        val estado = _estadoEditado.value
        val prioridad = _prioridadEditada.value
        viewModelScope.launch {
            if (estado != null && estado.name != t.ticket.estado) repo.cambiarEstado(ticketId, estado)
            if (prioridad != null && prioridad.name != t.ticket.prioridad) repo.cambiarPrioridad(ticketId, prioridad)
            _mensaje.value = "Cambios guardados"
        }
    }

    fun responder() {
        val texto = _texto.value.trim()
        if (texto.isEmpty()) return

        val estado = estadoActual()
        if (estado == null || estado == EstadoTicket.CERRADO) {
            _mensaje.value = "El ticket está cerrado"
            return
        }
        _texto.value = ""
        viewModelScope.launch {
            if (_modoNotaInterna.value) {
                repo.agregarNotaInterna(ticketId, usuario, texto)
            } else {
                repo.agregarComentario(ticketId, usuario, texto)
                // Regla: al responder un ticket ABIERTO, pasa automáticamente a EN_PROCESO
                if (estado == EstadoTicket.ABIERTO) {
                    repo.cambiarEstado(ticketId, EstadoTicket.EN_PROCESO)
                }
            }
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }
}