package com.faustino.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faustino.ayudacampus.data.local.entity.Categoria
import com.faustino.ayudacampus.data.repository.AyudaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CategoriasViewModel(private val repo: AyudaRepository) : ViewModel() {

    // Flow -> StateFlow con stateIn
    val categorias: StateFlow<List<Categoria>> = repo.observarCategorias()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Cantidad de tickets por nombre de categoría (para la vista previa)
    val conteos: StateFlow<Map<String, Int>> = repo.observarCantidadPorCategoria()
        .map { lista -> lista.associate { it.nombre to it.cantidad } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // true cuando se guardó o eliminó: el formulario se cierra
    private val _listo = MutableStateFlow(false)
    val listo: StateFlow<Boolean> = _listo.asStateFlow()

    fun alternar(c: Categoria) {
        viewModelScope.launch { repo.cambiarActiva(c.id, !c.activa) }
    }

    fun limpiarError() {
        _error.value = null
    }

    fun guardar(c: Categoria) {
        val nombre = c.nombre.trim()
        when {
            nombre.length < 3 ->
                _error.value = "El nombre debe tener al menos 3 caracteres"

            categorias.value.any { it.id != c.id && it.nombre.equals(nombre, ignoreCase = true) } ->
                _error.value = "Ya existe una categoría con ese nombre"

            else -> viewModelScope.launch {
                repo.guardarCategoria(c.copy(nombre = nombre))   // id = 0 inserta, si no actualiza
                _listo.value = true
            }
        }
    }

    fun eliminar(c: Categoria) {
        viewModelScope.launch {
            // Regla: no se puede eliminar una categoría que ya tiene tickets (solo desactivarla)
            if (repo.categoriaTieneTickets(c.id)) {
                _error.value = "No se puede eliminar: tiene tickets. Desactívala en su lugar."
            } else {
                repo.eliminarCategoria(c)
                _listo.value = true
            }
        }
    }
}