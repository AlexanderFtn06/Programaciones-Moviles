package com.tecsup.books.ui.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.books.data.repository.LibroRepository
import com.tecsup.books.domain.model.Libro
import com.tecsup.books.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetalleViewModel(
    private val repositorio: LibroRepository,
    private val libroId: String
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<Libro>>(UiState.Cargando)
    val estado: StateFlow<UiState<Libro>> = _estado.asStateFlow()

    init {
        cargar()
    }

    fun cargar() {
        viewModelScope.launch {
            _estado.value = UiState.Cargando
            repositorio.obtenerDetalle(libroId)
                .onSuccess { _estado.value = UiState.Exito(it) }
                .onFailure {
                    _estado.value = UiState.Error(
                        "No se pudo cargar el detalle. Revisa tu conexión e inténtalo de nuevo."
                    )
                }
        }
    }

    fun alternarFavorito() {
        val actual = (_estado.value as? UiState.Exito)?.datos ?: return
        viewModelScope.launch {
            repositorio.alternarFavorito(libroId)
            _estado.value = UiState.Exito(actual.copy(esFavorito = !actual.esFavorito))
        }
    }
}