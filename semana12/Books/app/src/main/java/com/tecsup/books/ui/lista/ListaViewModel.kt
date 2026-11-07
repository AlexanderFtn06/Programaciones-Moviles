package com.tecsup.books.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.books.data.repository.LibroRepository
import com.tecsup.books.domain.model.Libro
import com.tecsup.books.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ListaViewModel(
    private val repositorio: LibroRepository
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<List<Libro>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Libro>>> = _estado.asStateFlow()

    // Mientras no termine el primer refresco, una lista vacía significa "todavía cargando"
    private var refrescoTerminado = false

    init {
        viewModelScope.launch {
            repositorio.libros.collect { lista ->
                if (lista.isNotEmpty() || refrescoTerminado) {
                    _estado.value = UiState.Exito(lista)
                }
            }
        }
        cargar()
    }

    fun cargar() {
        viewModelScope.launch {
            if (_estado.value !is UiState.Exito) {
                _estado.value = UiState.Cargando
            }
            repositorio.refrescar()
                .onSuccess {
                    refrescoTerminado = true
                    _estado.value = UiState.Exito(repositorio.libros.first())
                }
                .onFailure {
                    refrescoTerminado = true
                    // Si no hay nada guardado, mostramos el error. Si hay datos, se mantiene la lista
                    if (repositorio.cantidadGuardada() == 0) {
                        _estado.value = UiState.Error(
                            "No se pudo cargar la lista. Revisa tu conexión e inténtalo de nuevo."
                        )
                    }
                }
        }
    }
}