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

    // true cuando el último refresco ya terminó (con éxito o con error)
    private var refrescoTerminado = false

    init {
        viewModelScope.launch {
            repositorio.libros.collect { lista ->
                when {
                    // Hay datos guardados: se muestran
                    lista.isNotEmpty() -> _estado.value = UiState.Exito(lista)

                    // Room está vacío después del refresco y no hubo error: estado Vacío
                    // Si hubo error, no se sobrescribe: el usuario debe ver el mensaje con Reintentar
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
                .onSuccess {
                    refrescoTerminado = true
                    _estado.value = UiState.Exito(repositorio.libros.first())
                }
                .onFailure {
                    refrescoTerminado = true
                    if (repositorio.cantidadGuardada() == 0) {
                        _estado.value = UiState.Error(
                            "No se pudo cargar la lista. Revisa tu conexión e inténtalo de nuevo."
                        )
                    }
                }
        }
    }
}