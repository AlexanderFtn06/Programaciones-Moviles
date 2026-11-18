package com.faustino.campusGPS.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faustino.campusGPS.data.LugaresRepository
import com.faustino.campusGPS.data.PreferenciasRepository
import com.faustino.campusGPS.data.UbicacionRepository
import com.faustino.campusGPS.model.LugarConDistancia
import com.faustino.campusGPS.model.Lugar
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import com.faustino.campusGPS.data.UbicacionNoDisponibleException
import com.faustino.campusGPS.model.TipoError
import com.faustino.campusGPS.model.Posicion
import com.faustino.campusGPS.model.UbicacionUi
import com.faustino.campusGPS.util.distanciaMetros
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.launch


private const val MENSAJE_SIN_PERMISO = "Sin permiso de ubicación no puedo mostrar dónde estás"

class MapaViewModel(
    private val ubicacionRepo: UbicacionRepository,
    private val lugaresRepo: LugaresRepository,
    private val preferenciasRepo: PreferenciasRepository
) : ViewModel() {

    private val _estado = MutableStateFlow<UbicacionUi>(UbicacionUi.Inicial)
    val estado: StateFlow<UbicacionUi> = _estado.asStateFlow()

    private val _lugares = MutableStateFlow(
        lugaresRepo.obtenerLugares().map { LugarConDistancia(it, null) }
    )
    val lugares: StateFlow<List<LugarConDistancia>> = _lugares.asStateFlow()

    private val _radioMetros = MutableStateFlow(200)
    val radioMetros: StateFlow<Int> = _radioMetros.asStateFlow()

    val idsEnRadio: StateFlow<Set<Int>> = combine(_lugares, radioMetros) { lista, radio ->
        lista.filter { it.metros != null && it.metros <= radio }
            .map { it.lugar.id }
            .toSet()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    val categorias: StateFlow<List<String>> = MutableStateFlow(
        lugaresRepo.obtenerLugares().map { it.categoria }.distinct().sorted()
    ).asStateFlow()

    private val _categoriaSeleccionada = MutableStateFlow<String?>(null)
    val categoriaSeleccionada: StateFlow<String?> = _categoriaSeleccionada.asStateFlow()

    val lugaresFiltrados: StateFlow<List<LugarConDistancia>> = combine(
        _lugares,
        _categoriaSeleccionada
    ) { lista, cat ->
        if (cat == null) lista
        else lista.filter { it.lugar.categoria == cat }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _lugares.value)

    private val _seleccionado = MutableStateFlow<Int?>(null)
    val seleccionado: StateFlow<Int?> = _seleccionado.asStateFlow()

    init {
        viewModelScope.launch {
            val idGuardado = preferenciasRepo.ultimoLugarId.first()
            if (idGuardado != null && _seleccionado.value == null) {
                val existe = _lugares.value.any { it.lugar.id == idGuardado }
                if (existe) {
                    _seleccionado.value = idGuardado
                }
            }
        }
    }

    fun buscar() {
        if (!ubicacionRepo.playServicesDisponible()) {
            _estado.value = UbicacionUi.Error(
                "Este dispositivo no tiene Google Play Services",
                TipoError.SIN_PLAY_SERVICES
            )
            return
        }
        _estado.value = UbicacionUi.Buscando
        viewModelScope.launch {
            ubicacionRepo.obtenerPosicion()
                .onSuccess {
                    _estado.value = UbicacionUi.Lista(it)
                    actualizarDistancias(it)
                }
                .onFailure { _estado.value = errorDe(it) }
        }
    }

    fun permisoDenegado() {
        _estado.value = UbicacionUi.Error(MENSAJE_SIN_PERMISO, TipoError.SIN_PERMISO)
    }

    fun seleccionar(id: Int?) {
        _seleccionado.value = id
        if (id != null) {
            viewModelScope.launch {
                preferenciasRepo.guardarUltimoLugar(id)
            }
        }
    }

    fun cambiarRadio(metros: Int) {
        _radioMetros.value = metros
    }

    fun seleccionarCategoria(categoria: String?) {
        _categoriaSeleccionada.value = categoria
        val idSel = _seleccionado.value
        if (categoria != null && idSel != null) {
            val lugarSel = _lugares.value.firstOrNull { it.lugar.id == idSel }?.lugar
            if (lugarSel != null && lugarSel.categoria != categoria) {
                _seleccionado.value = null
            }
        }
    }

    val masCercano: StateFlow<Lugar?> = lugaresFiltrados
        .map { lista -> lista.firstOrNull { it.metros != null }?.lugar }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private fun actualizarDistancias(pos: Posicion) {
        _lugares.value = lugaresRepo.obtenerLugares()
            .map { LugarConDistancia(it, distanciaMetros(pos, it)) }
            .sortedBy { it.metros }
    }


    private fun errorDe(e: Throwable): UbicacionUi.Error = when (e) {
        is SecurityException ->
            UbicacionUi.Error(MENSAJE_SIN_PERMISO, TipoError.SIN_PERMISO)
        is UbicacionNoDisponibleException -> UbicacionUi.Error(
            "No se pudo obtener la ubicación. Activa el GPS e intenta de nuevo",
            TipoError.GPS_APAGADO
        )
        else -> UbicacionUi.Error(
            e.message ?: "No se pudo obtener la ubicación",
            TipoError.OTRO
        )
    }
}