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

data class FormularioRecuperar(
    val paso: Int = 1,
    val correo: String = "",
    val nombre: String = "",
    val nuevaClave: String = "",
    val confirmarClave: String = "",
    val errorCorreo: String? = null,
    val errorNombre: String? = null,
    val errorNuevaClave: String? = null,
    val errorConfirmarClave: String? = null,
    val enviando: Boolean = false
)

class RecuperarClaveViewModel(private val repo: AyudaRepository) : ViewModel() {

    private val _form = MutableStateFlow(FormularioRecuperar())
    val form: StateFlow<FormularioRecuperar> = _form.asStateFlow()

    private val _listo = MutableStateFlow(false)
    val listo: StateFlow<Boolean> = _listo.asStateFlow()

    private var usuarioEncontrado: Usuario? = null

    fun cambiarCorreo(c: String) = _form.update { it.copy(correo = c, errorCorreo = null) }
    fun cambiarNombre(n: String) = _form.update { it.copy(nombre = n, errorNombre = null) }
    fun cambiarNuevaClave(c: String) = _form.update { it.copy(nuevaClave = c, errorNuevaClave = null) }
    fun cambiarConfirmarClave(c: String) = _form.update { it.copy(confirmarClave = c, errorConfirmarClave = null) }

    fun volverAlPaso1() {
        _form.update { it.copy(paso = 1, nuevaClave = "", confirmarClave = "", errorNuevaClave = null, errorConfirmarClave = null) }
        usuarioEncontrado = null
    }

    fun verificar() {
        val f = _form.value
        if (f.enviando) return

        val emailRegex = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
        val errorCorreo = when {
            f.correo.isBlank() -> "Ingresa tu correo"
            !emailRegex.matches(f.correo.trim()) -> "Formato de correo inválido (ej: texto@texto.texto)"
            else -> null
        }
        val errorNombre = if (f.nombre.isBlank()) "Ingresa tu nombre" else null

        if (errorCorreo != null || errorNombre != null) {
            _form.value = f.copy(errorCorreo = errorCorreo, errorNombre = errorNombre)
            return
        }

        _form.value = f.copy(enviando = true)
        viewModelScope.launch {
            val correoNorm = f.correo.trim().lowercase()
            val usuario = repo.buscarUsuarioPorCorreo(correoNorm)
            when {
                usuario == null -> {
                    _form.value = f.copy(enviando = false, errorCorreo = "No hay una cuenta con ese correo")
                }
                !usuario.nombre.trim().equals(f.nombre.trim(), ignoreCase = true) -> {
                    _form.value = f.copy(enviando = false, errorNombre = "El nombre no coincide con la cuenta")
                }
                else -> {
                    usuarioEncontrado = usuario
                    _form.value = f.copy(enviando = false, paso = 2, errorCorreo = null, errorNombre = null)
                }
            }
        }
    }

    fun guardar() {
        val f = _form.value
        val usuario = usuarioEncontrado ?: return
        if (f.enviando) return

        val errorNuevaClave = when {
            f.nuevaClave.length < 4 -> "La contraseña debe tener al menos 4 caracteres"
            f.nuevaClave == usuario.clave -> "La nueva contraseña debe ser distinta de la anterior"
            else -> null
        }
        val errorConfirmarClave = if (f.confirmarClave != f.nuevaClave) "Las contraseñas no coinciden" else null

        if (errorNuevaClave != null || errorConfirmarClave != null) {
            _form.value = f.copy(errorNuevaClave = errorNuevaClave, errorConfirmarClave = errorConfirmarClave)
            return
        }

        _form.value = f.copy(enviando = true)
        viewModelScope.launch {
            repo.cambiarClave(usuario.id, f.nuevaClave)
            _listo.value = true
        }
    }
}
