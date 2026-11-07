package com.tecsup.books.ui.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.books.data.repository.LibroRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PerfilUiState(
    val totalGuardados: Int = 0,
    val totalFavoritos: Int = 0
)

class PerfilViewModel(
    private val repositorio: LibroRepository
) : ViewModel() {

    private val _ui = MutableStateFlow(PerfilUiState())
    val ui: StateFlow<PerfilUiState> = _ui.asStateFlow()

    init {
        // Los totales se actualizan solos cuando Room cambia
        viewModelScope.launch {
            combine(repositorio.libros, repositorio.favoritos) { libros, favoritos ->
                libros.size to favoritos.size
            }.collect { (guardados, favoritos) ->
                _ui.update { it.copy(totalGuardados = guardados, totalFavoritos = favoritos) }
            }
        }
    }
}