package com.faustino.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faustino.ayudacampus.data.local.entity.Categoria
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

data class FiltrosBandeja(
    val estado: FiltroEstado = FiltroEstado.TODOS,
    val prioridad: Prioridad? = null,
    val categoriaId: Int? = null,
    val texto: String = "",
    val soloAlta: Boolean = false
)

class BandejaViewModel(
    repo: AyudaRepository,
    soloAltaInicial: Boolean   // true cuando se llega desde la alerta del panel
) : ViewModel() {

    private val estadoInicial = if (soloAltaInicial) FiltroEstado.ABIERTOS else FiltroEstado.TODOS

    private val _filtros = MutableStateFlow(
        FiltrosBandeja(
            estado = estadoInicial,
            soloAlta = soloAltaInicial
        )
    )
    val filtros: StateFlow<FiltrosBandeja> = _filtros.asStateFlow()

    // Categorías para el selector
    val categorias: StateFlow<List<Categoria>> = repo.observarCategorias()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // True si hay algún filtro activo distinto del valor por defecto
    val hayFiltrosActivos: StateFlow<Boolean> = _filtros
        .map { it != FiltrosBandeja() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private val todos = repo.observarTodosLosTickets()

    // Regla de visibilidad: Soporte ve TODOS los tickets aplicando todos los filtros a la vez
    val tickets: StateFlow<List<TicketConDetalle>> =
        combine(todos, _filtros) { lista, f ->
            lista.filter { t ->
                f.estado.incluye(t.ticket.estado) &&
                        (f.prioridad == null || t.ticket.prioridad == f.prioridad.name) &&
                        (f.categoriaId == null || t.ticket.categoriaId == f.categoriaId) &&
                        coincideBusqueda(t, f.texto) &&
                        (!f.soloAlta || t.ticket.prioridad == Prioridad.ALTA.name)
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

    fun seleccionarFiltro(e: FiltroEstado) {
        _filtros.update { it.copy(estado = e) }
    }

    fun elegirPrioridad(p: Prioridad?) {
        _filtros.update { it.copy(prioridad = if (it.prioridad == p) null else p) }
    }

    fun elegirCategoria(id: Int?) {
        _filtros.update { it.copy(categoriaId = id) }
    }

    fun buscar(texto: String) {
        _filtros.update { it.copy(texto = texto) }
    }

    fun quitarSoloAlta() {
        _filtros.update { it.copy(soloAlta = false) }
    }

    fun limpiarFiltros() {
        _filtros.value = FiltrosBandeja()
    }

    private fun coincideBusqueda(t: TicketConDetalle, texto: String): Boolean {
        val q = texto.trim().removePrefix("#")
        if (q.isEmpty()) return true
        val numero = "%04d".format(t.ticket.id)
        return t.ticket.titulo.contains(q, ignoreCase = true) ||
                numero.contains(q) ||
                t.cliente.nombre.contains(q, ignoreCase = true)
    }
}
