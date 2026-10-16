package com.faustino.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faustino.ayudacampus.data.local.entity.Categoria
import com.faustino.ayudacampus.data.local.entity.Usuario
import com.faustino.ayudacampus.data.model.Prioridad
import com.faustino.ayudacampus.data.prefs.PreferenciasStore
import com.faustino.ayudacampus.data.repository.AyudaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Estado del formulario con un error por campo. */
data class FormularioTicket(
    val categoriaId: Int? = null,
    val titulo: String = "",
    val ubicacion: String = "",
    val prioridad: Prioridad = Prioridad.MEDIA,      // Media por defecto
    val descripcion: String = "",
    val errorCategoria: String? = null,
    val errorTitulo: String? = null,
    val errorDescripcion: String? = null,
    val enviando: Boolean = false
)

class NuevoTicketViewModel(
    private val repo: AyudaRepository,
    private val usuario: Usuario,
    prefs: PreferenciasStore
) : ViewModel() {

    // Solo categorías activas: las inactivas NO aparecen
    val categorias: StateFlow<List<Categoria>> = repo.observarCategoriasActivas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Paso 12: si Soporte apagó "Recibir tickets", la mesa de ayuda está cerrada
    val recibirTickets: StateFlow<Boolean> = prefs.recibirTickets
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)
    private val _form = MutableStateFlow(FormularioTicket())
    val form: StateFlow<FormularioTicket> = _form.asStateFlow()

    // Se vuelve true cuando el ticket se guardó (la pantalla navega)
    private val _enviado = MutableStateFlow(false)
    val enviado: StateFlow<Boolean> = _enviado.asStateFlow()

    // Cada cambio limpia el error de su propio campo
    fun elegirCategoria(id: Int) = _form.update { it.copy(categoriaId = id, errorCategoria = null) }
    fun cambiarTitulo(t: String) = _form.update { it.copy(titulo = t, errorTitulo = null) }
    fun cambiarUbicacion(u: String) = _form.update { it.copy(ubicacion = u) }
    fun cambiarPrioridad(p: Prioridad) = _form.update { it.copy(prioridad = p) }
    fun cambiarDescripcion(d: String) = _form.update { it.copy(descripcion = d, errorDescripcion = null) }

    fun tiempoRespuesta(p: Prioridad): String = when (p) {
        Prioridad.ALTA -> "4 horas"
        Prioridad.MEDIA -> "24 horas"
        Prioridad.BAJA -> "72 horas"
    }

    fun enviar() {
        val f = _form.value
        if (f.enviando) return
        if (!recibirTickets.value) return // mesa de ayuda cerrada

        // Reglas de negocio del lab
        val categoriaId = f.categoriaId
        val categoriaActiva = categoriaId != null && categorias.value.any { it.id == categoriaId }

        val errorCategoria = if (!categoriaActiva) "Elige una categoría" else null
        val errorTitulo = if (f.titulo.trim().length < 5)
            "El título debe tener al menos 5 caracteres" else null
        val errorDescripcion = if (f.descripcion.trim().length < 10)
            "La descripción debe tener al menos 10 caracteres" else null

        if (errorCategoria != null || errorTitulo != null || errorDescripcion != null || categoriaId == null) {
            _form.value = f.copy(
                errorCategoria = errorCategoria,
                errorTitulo = errorTitulo,
                errorDescripcion = errorDescripcion
            )
            return
        }

        _form.value = f.copy(enviando = true)
        viewModelScope.launch {
            // El Repository lo crea con estado ABIERTO y la fecha actual
            repo.crearTicket(
                clienteId = usuario.id,
                categoriaId = categoriaId,
                titulo = f.titulo,
                descripcion = f.descripcion,
                ubicacion = f.ubicacion,
                prioridad = f.prioridad
            )
            _enviado.value = true
        }
    }
}