package com.faustino.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faustino.ayudacampus.data.local.entity.Usuario
import com.faustino.ayudacampus.data.model.Rol
import com.faustino.ayudacampus.data.prefs.PreferenciasStore
import com.faustino.ayudacampus.data.repository.AyudaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SesionViewModel(
    private val repo: AyudaRepository,
    private val prefs: PreferenciasStore
) : ViewModel() {

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    // true cuando ya se revisó si había una sesión guardada (lo usa el Splash)
    private val _sesionVerificada = MutableStateFlow(false)
    val sesionVerificada: StateFlow<Boolean> = _sesionVerificada.asStateFlow()

    // ---------- Preferencias (DataStore) ----------
    val modoOscuro: StateFlow<Boolean> = prefs.modoOscuro
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val notificaciones: StateFlow<Boolean> = prefs.notificaciones
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val recibirTickets: StateFlow<Boolean> = prefs.recibirTickets
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    init {
        restaurarSesion()
    }

    /** Si DataStore tiene un perfil guardado, recupera al usuario y salta el login. */
    private fun restaurarSesion() {
        viewModelScope.launch {
            val perfil = prefs.perfilActivo.first()
            val id = prefs.usuarioId.first()
            if (perfil.isNotEmpty() && id != 0) {
                _usuarioActual.value = repo.obtenerUsuario(id)
            }
            _sesionVerificada.value = true
        }
    }

    fun login(correo: String, clave: String, rolSeleccionado: Rol) {
        _error.value = null

        // Regla 1: campos vacíos
        if (correo.isBlank() || clave.isBlank()) {
            _error.value = "Ingresa tu correo y tu contraseña"
            return
        }

        viewModelScope.launch {
            _cargando.value = true
            val usuario = repo.login(correo, clave)
            when {
                // Regla 2: credenciales incorrectas
                usuario == null ->
                    _error.value = "Correo o contraseña incorrectos"

                // Regla 3: el rol no coincide con la pestaña elegida
                usuario.rol != rolSeleccionado.name ->
                    _error.value = "Esta cuenta no pertenece al perfil ${rolSeleccionado.etiqueta}"

                // Éxito: guarda el perfil en DataStore
                else -> {
                    prefs.guardarSesion(rolSeleccionado, usuario.id)
                    _usuarioActual.value = usuario
                }
            }
            _cargando.value = false
        }
    }

    fun limpiarError() {
        _error.value = null
    }

    // ---------- Paso 12 ----------

    fun cambiarModoOscuro(activo: Boolean) {
        viewModelScope.launch { prefs.guardarModoOscuro(activo) }
    }

    fun cambiarNotificaciones(activas: Boolean) {
        viewModelScope.launch { prefs.guardarNotificaciones(activas) }
    }

    fun cambiarRecibirTickets(recibir: Boolean) {
        viewModelScope.launch { prefs.guardarRecibirTickets(recibir) }
    }

    /**
     * Modo práctica: guarda el OTRO perfil en DataStore, entra con su cuenta
     * y avisa a la navegación para ir a su grafo.
     */
    fun cambiarPerfil(onCambiado: (Usuario) -> Unit) {
        val actual = _usuarioActual.value ?: return
        val otroRol = if (actual.rol == Rol.ADMIN.name) Rol.CLIENTE else Rol.ADMIN
        viewModelScope.launch {
            val otro = repo.obtenerUsuarioPorRol(otroRol)
            if (otro == null) {
                _error.value = "No hay una cuenta de ${otroRol.etiqueta}"
                return@launch
            }
            prefs.guardarSesion(otroRol, otro.id)
            _usuarioActual.value = otro
            onCambiado(otro)
        }
    }

    /** Borra el perfil activo; el modo oscuro y los demás ajustes se conservan. */
    fun cerrarSesion() {
        _usuarioActual.value = null            // primero, para que el Login no redirija
        viewModelScope.launch { prefs.cerrarSesion() }
    }
}