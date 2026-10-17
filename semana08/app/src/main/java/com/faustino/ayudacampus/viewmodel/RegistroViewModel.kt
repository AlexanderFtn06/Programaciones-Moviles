package com.faustino.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faustino.ayudacampus.data.local.entity.Usuario
import com.faustino.ayudacampus.data.repository.AyudaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FormularioRegistro(
    val nombre: String = "",
    val correo: String = "",
    val clave: String = "",
    val confirmarClave: String = "",
    val errorNombre: String? = null,
    val errorCorreo: String? = null,
    val errorClave: String? = null,
    val errorConfirmarClave: String? = null,
    val enviando: Boolean = false
)

class RegistroViewModel(private val repo: AyudaRepository) : ViewModel() {

    private val _form = MutableStateFlow(FormularioRegistro())
    val form: StateFlow<FormularioRegistro> = _form.asStateFlow()

    private val _registrado = MutableStateFlow<Usuario?>(null)
    val registrado: StateFlow<Usuario?> = _registrado.asStateFlow()

    fun cambiarNombre(n: String) = _form.update { it.copy(nombre = n, errorNombre = null) }
    fun cambiarCorreo(c: String) = _form.update { it.copy(correo = c, errorCorreo = null) }
    fun cambiarClave(c: String) = _form.update { it.copy(clave = c, errorClave = null) }
    fun cambiarConfirmarClave(c: String) = _form.update { it.copy(confirmarClave = c, errorConfirmarClave = null) }

    fun registrar() {
        val f = _form.value
        if (f.enviando) return

        val emailRegex = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")

        val errorNombre = if (f.nombre.trim().length < 3) "El nombre debe tener al menos 3 caracteres" else null
        val errorCorreo = when {
            f.correo.isBlank() -> "Ingresa tu correo"
            !emailRegex.matches(f.correo.trim()) -> "Formato de correo inválido (ej: texto@texto.texto)"
            else -> null
        }
        val errorClave = if (f.clave.length < 4) "La contraseña debe tener al menos 8 caracteres" else null
        val errorConfirmarClave = if (f.confirmarClave != f.clave) "Las contraseñas no coinciden" else null

        if (errorNombre != null || errorCorreo != null || errorClave != null || errorConfirmarClave != null) {
            _form.value = f.copy(
                errorNombre = errorNombre,
                errorCorreo = errorCorreo,
                errorClave = errorClave,
                errorConfirmarClave = errorConfirmarClave
            )
            return
        }

        _form.value = f.copy(enviando = true)
        viewModelScope.launch {
            val correoNorm = f.correo.trim().lowercase()
            if (repo.correoRegistrado(correoNorm)) {
                _form.value = f.copy(
                    enviando = false,
                    errorCorreo = "Este correo ya está registrado"
                )
                return@launch
            }

            val nuevoUsuario = repo.registrarEstudiante(f.nombre, correoNorm, f.clave)
            _registrado.value = nuevoUsuario
        }
    }
}
