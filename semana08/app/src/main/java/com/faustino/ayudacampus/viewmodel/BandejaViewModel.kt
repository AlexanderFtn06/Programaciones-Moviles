package com.faustino.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faustino.ayudacampus.data.local.relation.TicketConDetalle
import com.faustino.ayudacampus.data.model.Prioridad
import com.faustino.ayudacampus.data.repository.AyudaRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class BandejaViewModel(
    repo: AyudaRepository,
    soloAltaInicial: Boolean   // true cuando se llega desde la alerta del panel
) : ViewModel() {

    private val _filtro = MutableStateFlow(
        if (soloAltaInicial) FiltroEstado.ABIERTOS else FiltroEstado.TODOS
    )
    val filtro: StateFlow<FiltroEstado> = _filtro.asStateFlow()

    private val _soloAlta = MutableStateFlow(soloAltaInicial)
    val soloAlta: StateFlow<Boolean> = _soloAlta.asStateFlow()

    private val todos = repo.observarTodosLosTickets()

    // Regla de visibilidad: Soporte ve TODOS los tickets.
    // El DAO ya los entrega con ALTA primero y luego por fecha
    val tickets: StateFlow<List<TicketConDetalle>> =
        combine(todos, _filtro, _soloAlta) { lista, filtro, alta ->
            lista.filter { t ->
                filtro.incluye(t.ticket.estado) &&
                        (!alta || t.ticket.prioridad == Prioridad.ALTA.name)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val reloj = flow {
        while (true) {
            emit(System.currentTimeMillis())
            delay(60_000)
        }
    }

    val slas: StateFlow<Map<Int, EstadoSla>> =
        combine(todos, reloj) { lista, ahora ->
            lista.mapNotNull { t ->
                calcularSla(t.ticket, ahora)?.let { sla -> t.ticket.id to sla }
            }.toMap()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    fun seleccionarFiltro(f: FiltroEstado) {
        _filtro.value = f
    }

    fun quitarSoloAlta() {
        _soloAlta.value = false
    }
}