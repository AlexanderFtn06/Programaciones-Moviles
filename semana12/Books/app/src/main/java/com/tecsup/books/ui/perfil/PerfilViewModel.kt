package com.tecsup.books.ui.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.books.data.repository.LibroRepository
import com.tecsup.books.notification.Notificador
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PerfilUiState(
    val totalGuardados: Int = 0,
    val totalFavoritos: Int = 0,
    val sincronizando: Boolean = false,
    val mensaje: String? = null
)

class PerfilViewModel(
    private val repositorio: LibroRepository,
    private val notificador: Notificador
) : ViewModel() {

    private val _ui = MutableStateFlow(PerfilUiState())
    val ui: StateFlow<PerfilUiState> = _ui.asStateFlow()

    init {
        viewModelScope.launch {
            combine(repositorio.libros, repositorio.favoritos) { libros, favoritos ->
                libros.size to favoritos.size
            }.collect { (guardados, favoritos) ->
                _ui.update { it.copy(totalGuardados = guardados, totalFavoritos = favoritos) }
            }
        }
    }

    fun sincronizar() {
        viewModelScope.launch {
            _ui.update { it.copy(sincronizando = true, mensaje = null) }
            repositorio.refrescar()
                .onSuccess { cantidad ->
                    _ui.update {
                        it.copy(sincronizando = false, mensaje = "Se guardaron $cantidad elementos")
                    }
                    notificador.notificarSincronizacion(cantidad)
                }
                .onFailure {
                    _ui.update {
                        it.copy(
                            sincronizando = false,
                            mensaje = "No se pudo sincronizar. Revisa tu conexión."
                        )
                    }
                }
        }
    }

    fun limpiarCache() {
        viewModelScope.launch {
            repositorio.limpiarCache()
            _ui.update { it.copy(mensaje = "Caché limpiada") }
        }
    }
}