package com.tecsup.books.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.books.data.auth.AuthRepository
import com.tecsup.books.data.repository.LibroRepository
import com.tecsup.books.domain.model.Rol
import com.tecsup.books.domain.model.Sesion
import com.tecsup.books.notification.Notificador
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AdminUiState(
    val totalGuardados: Int = 0,
    val totalFavoritos: Int = 0,
    val totalUsuarios: Int? = null,
    val sincronizando: Boolean = false,
    val mensaje: String? = null
)

class AdminViewModel(
    private val repositorio: LibroRepository,
    private val notificador: Notificador,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _ui = MutableStateFlow(AdminUiState())
    val ui: StateFlow<AdminUiState> = _ui.asStateFlow()

    init {
        viewModelScope.launch {
            combine(repositorio.libros, repositorio.favoritos) { libros, favoritos ->
                libros.size to favoritos.size
            }.collect { (guardados, favoritos) ->
                _ui.update { it.copy(totalGuardados = guardados, totalFavoritos = favoritos) }
            }
        }
    }

    fun cargarDatos(sesion: Sesion) {
        if (sesion.rol == Rol.ADMIN && sesion.rolConfirmado) {
            viewModelScope.launch {
                authRepository.contarUsuarios()
                    .onSuccess { total ->
                        _ui.update { it.copy(totalUsuarios = total) }
                    }
                    .onFailure {
                        _ui.update { it.copy(totalUsuarios = null) }
                    }
            }
        }
    }

    fun sincronizar(sesion: Sesion) {
        if (sesion.rol != Rol.ADMIN || !sesion.rolConfirmado) {
            _ui.update {
                it.copy(mensaje = "Requiere conexión activa para validar permisos de administrador")
            }
            return
        }
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

    fun limpiarCache(sesion: Sesion) {
        if (sesion.rol != Rol.ADMIN || !sesion.rolConfirmado) {
            _ui.update {
                it.copy(mensaje = "Requiere conexión activa para validar permisos de administrador")
            }
            return
        }
        viewModelScope.launch {
            repositorio.limpiarCache()
            _ui.update { it.copy(mensaje = "Caché limpiada") }
        }
    }
}