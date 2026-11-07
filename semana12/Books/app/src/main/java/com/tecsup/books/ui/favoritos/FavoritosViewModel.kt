package com.tecsup.books.ui.favoritos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.books.data.repository.LibroRepository
import com.tecsup.books.domain.model.Libro
import com.tecsup.books.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritosViewModel(
    private val repositorio: LibroRepository
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<List<Libro>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Libro>>> = _estado.asStateFlow()

    init {
        // Room emite la lista cada vez que cambian los favoritos
        viewModelScope.launch {
            repositorio.favoritos.collect { lista ->
                _estado.value = UiState.Exito(lista)
            }
        }
    }
}