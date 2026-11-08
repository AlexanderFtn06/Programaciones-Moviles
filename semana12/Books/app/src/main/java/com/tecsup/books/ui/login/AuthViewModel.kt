package com.tecsup.books.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.books.data.auth.AuthRepository
import com.tecsup.books.data.auth.CredencialesInvalidasException
import com.tecsup.books.domain.model.Sesion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val correo: String = "",
    val clave: String = "",
    val cargando: Boolean = false,
    val error: String? = null
)

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _ui = MutableStateFlow(LoginUiState())
    val ui: StateFlow<LoginUiState> = _ui.asStateFlow()

    private val _sesion = MutableStateFlow<Sesion?>(null)
    val sesion: StateFlow<Sesion?> = _sesion.asStateFlow()

    // true mientras se revisa si ya había una sesión guardada al abrir la app
    private val _restaurando = MutableStateFlow(true)
    val restaurando: StateFlow<Boolean> = _restaurando.asStateFlow()

    init {
        viewModelScope.launch {
            _sesion.value = authRepository.sesionActual()
            _restaurando.value = false
        }
    }

    fun cambiarCorreo(valor: String) {
        _ui.update { it.copy(correo = valor, error = null) }
    }

    fun cambiarClave(valor: String) {
        _ui.update { it.copy(clave = valor, error = null) }
    }

    fun iniciarSesion() {
        val actual = _ui.value

        // Validación local: no se llama a Firebase con campos vacíos (PA-04)
        if (actual.correo.isBlank() || actual.clave.isBlank()) {
            _ui.update { it.copy(error = "Ingresa tu correo y contraseña.") }
            return
        }

        viewModelScope.launch {
            _ui.update { it.copy(cargando = true, error = null) }
            authRepository.iniciarSesion(actual.correo.trim(), actual.clave)
                .onSuccess { sesionNueva ->
                    _ui.update { LoginUiState() }
                    _sesion.value = sesionNueva
                }
                .onFailure { e ->
                    val mensaje = if (e is CredencialesInvalidasException) {
                        e.message
                    } else {
                        "No se pudo iniciar sesión. Revisa tu conexión e inténtalo de nuevo."
                    }
                    _ui.update { it.copy(cargando = false, error = mensaje) }
                }
        }
    }

    fun actualizarSesion(nuevaSesion: Sesion) {
        _ui.update { LoginUiState() }
        _sesion.value = nuevaSesion
    }

    fun cerrarSesion() {
        viewModelScope.launch {
            authRepository.cerrarSesion()
            _ui.update { LoginUiState() }
            _sesion.value = null
        }
    }
}