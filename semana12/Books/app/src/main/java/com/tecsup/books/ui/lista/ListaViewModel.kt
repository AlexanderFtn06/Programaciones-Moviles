package com.tecsup.books.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.books.data.repository.LibroRepository
import com.tecsup.books.domain.model.Libro
import com.tecsup.books.notification.Notificador
import com.tecsup.books.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ListaViewModel(
    private val repositorio: LibroRepository,
    private val notificador: Notificador
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<List<Libro>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Libro>>> = _estado.asStateFlow()

    private val _sinConexion = MutableStateFlow(false)
    val sinConexion: StateFlow<Boolean> = _sinConexion.asStateFlow()

    // true cuando el último refresco ya terminó (con éxito o con error)
    private var refrescoTerminado = false

    init {
        viewModelScope.launch {
            repositorio.libros.collect { lista ->
                when {
                    lista.isNotEmpty() -> _estado.value = UiState.Exito(lista)

                    refrescoTerminado && _estado.value !is UiState.Error ->
                        _estado.value = UiState.Exito(lista)
                }
            }
        }
        cargar()
    }

    fun cargar() {
        viewModelScope.launch {
            refrescoTerminado = false
            if (_estado.value !is UiState.Exito) {
                _estado.value = UiState.Cargando
            }
            repositorio.refrescar()
                .onSuccess { cantidad ->
                    refrescoTerminado = true
                    _sinConexion.value = false
                    _estado.value = UiState.Exito(repositorio.libros.first())
                    // Solo se notifica cuando la sincronización terminó bien (PA-05)
                    notificador.notificarSincronizacion(cantidad)
                }
                .onFailure {
                    refrescoTerminado = true
                    if (repositorio.cantidadGuardada() == 0) {
                        _sinConexion.value = false
                        _estado.value = UiState.Error(
                            "No se pudo cargar la lista. Revisa tu conexión e inténtalo de nuevo."
                        )
                    } else {
                        _sinConexion.value = true
                        _estado.value = UiState.Exito(repositorio.libros.first())
                    }
                }
        }
    }
    fun alternarFavorito(id: String) {
        viewModelScope.launch { repositorio.alternarFavorito(id) }
    }
}