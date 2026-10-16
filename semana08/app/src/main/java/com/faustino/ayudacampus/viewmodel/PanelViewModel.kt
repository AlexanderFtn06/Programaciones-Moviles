package com.faustino.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faustino.ayudacampus.data.model.ConteoCategoria
import com.faustino.ayudacampus.data.model.EstadoTicket
import com.faustino.ayudacampus.data.repository.AyudaRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

/** Contadores por estado para las tarjetas y la dona. */
data class ResumenEstados(
    val abiertos: Int = 0,
    val enProceso: Int = 0,
    val resueltos: Int = 0,
    val cerrados: Int = 0
)

@OptIn(ExperimentalCoroutinesApi::class)
class PanelViewModel(private val repo: AyudaRepository) : ViewModel() {

    // Los 4 contadores son Flow: el panel se actualiza solo
    val resumen: StateFlow<ResumenEstados> = combine(
        repo.observarCantidadPorEstado(EstadoTicket.ABIERTO),
        repo.observarCantidadPorEstado(EstadoTicket.EN_PROCESO),
        repo.observarCantidadPorEstado(EstadoTicket.RESUELTO),
        repo.observarCantidadPorEstado(EstadoTicket.CERRADO)
    ) { a, p, r, c -> ResumenEstados(a, p, r, c) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ResumenEstados())

    // Datos para el gráfico de barras (LEFT JOIN: incluye categorías con 0)
    val porCategoria: StateFlow<List<ConteoCategoria>> = repo.observarCantidadPorCategoria()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // El límite "hace 1 hora" se recalcula cada minuto,
    // así un ticket ALTA entra a la alerta cuando cumple su hora
    private val limiteUnaHora = flow {
        while (true) {
            emit(System.currentTimeMillis() - UNA_HORA)
            delay(60_000)
        }
    }

    val altasSinAtender: StateFlow<Int> = limiteUnaHora
        .flatMapLatest { limite -> repo.observarAltasSinAtender(limite) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    private companion object {
        const val UNA_HORA = 60 * 60 * 1000L
    }
}