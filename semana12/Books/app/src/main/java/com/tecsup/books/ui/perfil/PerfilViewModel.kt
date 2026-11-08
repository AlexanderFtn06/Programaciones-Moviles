package com.tecsup.books.ui.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.books.data.auth.AuthRepository
import com.tecsup.books.data.repository.LibroRepository
import com.tecsup.books.data.settings.AjustesRepository
import com.tecsup.books.domain.model.Rol
import com.tecsup.books.domain.model.Sesion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PerfilUiState(
    val totalGuardados: Int = 0,
    val totalFavoritos: Int = 0,
    val totalUsuarios: Int? = null,
    val cargandoUsuarios: Boolean = false,
    val modoOscuro: Boolean = false,
    val mensaje: String? = null
)

class PerfilViewModel(
    private val repositorio: LibroRepository,
    private val authRepository: AuthRepository,
    private val ajustesRepository: AjustesRepository
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

        viewModelScope.launch {
            ajustesRepository.modoOscuro.collect { modo ->
                _ui.update { it.copy(modoOscuro = modo ?: false) }
            }
        }
    }

    fun cargarDatosPerfil(sesion: Sesion) {
        if (sesion.rol == Rol.ADMIN && sesion.rolConfirmado) {
            viewModelScope.launch {
                _ui.update { it.copy(cargandoUsuarios = true, mensaje = null) }
                authRepository.contarUsuarios()
                    .onSuccess { total ->
                        _ui.update { it.copy(totalUsuarios = total, cargandoUsuarios = false) }
                    }
                    .onFailure {
                        _ui.update { it.copy(totalUsuarios = null, cargandoUsuarios = false) }
                    }
            }
        } else if (!sesion.rolConfirmado) {
            _ui.update { it.copy(mensaje = "Requiere conexión activa para validar permisos de administrador") }
        }
    }

    fun cambiarModoOscuro(esOscuro: Boolean) {
        viewModelScope.launch {
            ajustesRepository.guardarModoOscuro(esOscuro)
        }
    }
}