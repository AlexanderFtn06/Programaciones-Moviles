package com.tecsup.books.ui.registro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.books.data.auth.AuthRepository
import com.tecsup.books.domain.model.DatosRegistro
import com.tecsup.books.domain.model.Sesion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegistroViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _ui = MutableStateFlow(RegistroUiState())
    val ui: StateFlow<RegistroUiState> = _ui.asStateFlow()

    fun cambiarNombre(valor: String) {
        _ui.update { it.copy(nombre = valor, error = null) }
    }

    fun cambiarApellido(valor: String) {
        _ui.update { it.copy(apellido = valor, error = null) }
    }

    fun cambiarEdad(valor: String) {
        _ui.update { it.copy(edad = valor.filter { char -> char.isDigit() }, error = null) }
    }

    fun cambiarCorreo(valor: String) {
        _ui.update { it.copy(correo = valor, error = null) }
    }

    fun cambiarClave(valor: String) {
        _ui.update { it.copy(clave = valor, error = null) }
    }

    fun cambiarConfirmarClave(valor: String) {
        _ui.update { it.copy(confirmarClave = valor, error = null) }
    }

    fun registrar(onExito: (Sesion) -> Unit) {
        val estado = _ui.value

        if (estado.nombre.isBlank() ||
            estado.apellido.isBlank() ||
            estado.edad.isBlank() ||
            estado.correo.isBlank() ||
            estado.clave.isBlank() ||
            estado.confirmarClave.isBlank()
        ) {
            _ui.update { it.copy(error = "Todos los campos son obligatorios.") }
            return
        }

        val edadInt = estado.edad.toIntOrNull()
        if (edadInt == null || edadInt !in 1..120) {
            _ui.update { it.copy(error = "La edad debe estar entre 1 y 120 años.") }
            return
        }

        if (estado.clave.length < 6) {
            _ui.update { it.copy(error = "La contraseña debe tener al menos 6 caracteres.") }
            return
        }

        if (estado.clave != estado.confirmarClave) {
            _ui.update { it.copy(error = "Las contraseñas no coinciden.") }
            return
        }

        viewModelScope.launch {
            _ui.update { it.copy(cargando = true, error = null) }
            val datos = DatosRegistro(
                nombre = estado.nombre.trim(),
                apellido = estado.apellido.trim(),
                edad = edadInt,
                correo = estado.correo.trim(),
                clave = estado.clave
            )
            authRepository.registrar(datos)
                .onSuccess { sesionNueva ->
                    _ui.update { RegistroUiState() }
                    onExito(sesionNueva)
                }
                .onFailure { error ->
                    _ui.update { it.copy(cargando = false, error = error.localizedMessage) }
                }
        }
    }
}