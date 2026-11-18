package com.faustino.centrotareas.viewmodel

import androidx.lifecycle.ViewModel
import com.faustino.centrotareas.data.TareasRepository
import com.faustino.centrotareas.model.CargaUi
import com.faustino.centrotareas.model.PrimosUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope


class TareasViewModel(private val repo: TareasRepository) : ViewModel() {

    // ----- Tarea con progreso (pasos 1 a 4)
    private val _mensaje = MutableStateFlow("Listo")
    private val _progreso = MutableStateFlow(0f)
    val progreso: StateFlow<Float> = _progreso.asStateFlow()
    val mensaje: StateFlow<String> = _mensaje.asStateFlow()

    private var trabajo: Job? = null

    private val _corriendo = MutableStateFlow(false)
    val corriendo: StateFlow<Boolean> = _corriendo.asStateFlow()

    fun tareaLarga() {
        if (_corriendo.value) return          // doble inicio: no hace nada
        _corriendo.value = true
        trabajo = viewModelScope.launch {
            try {
                _progreso.value = 0f
                _mensaje.value = "Trabajando..."
                Log.d("HILO", "Antes: ${Thread.currentThread().name}")
                for (i in 1..100) {
                    withContext(Dispatchers.Default) {
                        if (i == 1) Log.d("HILO", "Dentro: ${Thread.currentThread().name}")
                        delay(40)
                    }
                    _progreso.value = i / 100f
                }
                Log.d("HILO", "Después: ${Thread.currentThread().name}")
                _mensaje.value = "Terminó"
            } finally {
                _corriendo.value = false      // termine bien o cancelada
            }
        }
    }

    fun cancelar() {
        if (!_corriendo.value) return
        trabajo?.cancel()
        _mensaje.value = "Cancelado"          // la barra se queda donde estaba
    }
    // ----- Contar primos (paso 5)
    private val _primos = MutableStateFlow<PrimosUi>(PrimosUi.Inicial)
    val primos: StateFlow<PrimosUi> = _primos.asStateFlow()
    private var trabajoPrimos: Job? = null

    fun contar(hasta: Int) {
        if (_primos.value is PrimosUi.Calculando) return
        _primos.value = PrimosUi.Calculando
        trabajoPrimos = viewModelScope.launch {
            val inicio = System.currentTimeMillis()
            try {
                val cantidad = repo.contarPrimos(hasta)
                val ms = System.currentTimeMillis() - inicio
                _primos.value = PrimosUi.Listo(cantidad, ms)
            } catch (e: CancellationException) {
                _primos.value = PrimosUi.Cancelado
                throw e                  // se vuelve a lanzar la cancelación
            }
        }
    }

    fun cancelarPrimos() {
        trabajoPrimos?.cancel()
    }

    // ----- Dos cargas (pasos 6 y 7)
    private val _carga = MutableStateFlow<CargaUi>(CargaUi.Inicial)
    val carga: StateFlow<CargaUi> = _carga.asStateFlow()
    fun cargarUnaPorUna() {
        if (_carga.value is CargaUi.Cargando) return
        viewModelScope.launch {
            _carga.value = CargaUi.Cargando
            val inicio = System.currentTimeMillis()
            val perfil = repo.cargarPerfil()              // primero uno...
            val pedidos = repo.cargarPedidos(false)       // ...y después el otro
            val ms = System.currentTimeMillis() - inicio
            _carga.value = CargaUi.Exito("Una por una", perfil, pedidos.size, ms)
        }
    }

    fun cargarALaVez() {
        if (_carga.value is CargaUi.Cargando) return
        viewModelScope.launch {
            _carga.value = CargaUi.Cargando
            val inicio = System.currentTimeMillis()
            val (perfil, pedidos) = coroutineScope {
                val perfilDeferred = async { repo.cargarPerfil() }
                val pedidosDeferred = async { repo.cargarPedidos(false) }
                perfilDeferred.await() to pedidosDeferred.await()   // espera ambas
            }
            val ms = System.currentTimeMillis() - inicio
            _carga.value = CargaUi.Exito("A la vez", perfil, pedidos.size, ms)
        }
    }
    // TODO Paso 7: simularError y manejo de errores
}