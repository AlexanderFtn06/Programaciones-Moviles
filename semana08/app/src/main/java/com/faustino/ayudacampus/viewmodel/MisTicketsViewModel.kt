package com.faustino.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faustino.ayudacampus.data.local.entity.Usuario
import com.faustino.ayudacampus.data.local.relation.TicketConDetalle
import com.faustino.ayudacampus.data.model.EstadoTicket
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

/** Filtros de los chips. También los usa la Bandeja de Soporte (Paso 10). */
enum class FiltroEstado(val etiqueta: String) {
    TODOS("Todos"),
    ABIERTOS("Abiertos"),
    EN_PROCESO("En proceso"),
    RESUELTOS("Resueltos");

    fun incluye(estado: String): Boolean = when (this) {
        TODOS -> true                                   // incluye también los CERRADOS
        ABIERTOS -> estado == EstadoTicket.ABIERTO.name
        EN_PROCESO -> estado == EstadoTicket.EN_PROCESO.name
        RESUELTOS -> estado == EstadoTicket.RESUELTO.name
    }
}

/** Contadores del Perfil: Abiertos · Resueltos · Total */
data class ResumenMisTickets(
    val abiertos: Int = 0,
    val resueltos: Int = 0,      // RESUELTO + CERRADO
    val total: Int = 0
)

class MisTicketsViewModel(
    repo: AyudaRepository,
    usuario: Usuario
) : ViewModel() {

    val primerNombre: String = usuario.nombre.substringBefore(" ")

    private val _filtro = MutableStateFlow(FiltroEstado.TODOS)
    val filtro: StateFlow<FiltroEstado> = _filtro.asStateFlow()

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    // Regla de visibilidad: el estudiante solo ve SUS tickets (el DAO ya los ordena)
    private val misTickets = repo.observarTicketsCliente(usuario.id)

    // Combina los tres flujos: tickets + filtro + texto de búsqueda
    val tickets: StateFlow<List<TicketConDetalle>> =
        combine(misTickets, _filtro, _busqueda) { lista, filtro, texto ->
            lista.filter { filtro.incluye(it.ticket.estado) && coincideBusqueda(it, texto) }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val reloj = flow {
        while (true) {
            emit(System.currentTimeMillis())
            delay(60_000)
        }
    }

    val slas: StateFlow<Map<Int, EstadoSla>> =
        combine(misTickets, reloj) { lista, ahora ->
            lista.mapNotNull { t ->
                calcularSla(t.ticket, ahora)?.let { sla -> t.ticket.id to sla }
            }.toMap()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    // Para el saludo "Hola, Juan · 3 abiertos"
    val abiertos: StateFlow<Int> = misTickets
        .map { lista -> lista.count { it.ticket.estado == EstadoTicket.ABIERTO.name } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Paso 12: contadores del Perfil
    val resumen: StateFlow<ResumenMisTickets> = misTickets
        .map { lista ->
            ResumenMisTickets(
                abiertos = lista.count { it.ticket.estado == EstadoTicket.ABIERTO.name },
                resueltos = lista.count {
                    it.ticket.estado == EstadoTicket.RESUELTO.name ||
                            it.ticket.estado == EstadoTicket.CERRADO.name
                },
                total = lista.size
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ResumenMisTickets())

    fun seleccionarFiltro(f: FiltroEstado) {
        _filtro.value = f
    }

    fun buscar(texto: String) {
        _busqueda.value = texto
    }

    /** Busca por título o por número (acepta "3", "0003" o "#0003"). */
    private fun coincideBusqueda(t: TicketConDetalle, texto: String): Boolean {
        val q = texto.trim().removePrefix("#")
        if (q.isEmpty()) return true
        val numero = "%04d".format(t.ticket.id)
        return t.ticket.titulo.contains(q, ignoreCase = true) || numero.contains(q)
    }
}