package com.tecsup.books.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.books.data.repository.LibroRepository
import com.tecsup.books.domain.model.Libro
import com.tecsup.books.notification.Notificador
import com.tecsup.books.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import android.util.Log
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class LibroConCategorias(
    val libro: Libro,
    val categorias: Set<String>
)

class ListaViewModel(
    private val repositorio: LibroRepository,
    private val notificador: Notificador
) : ViewModel() {

    private val _librosCategorizados = MutableStateFlow<List<LibroConCategorias>>(emptyList())
    private val _estadoRaw = MutableStateFlow<UiState<List<LibroConCategorias>>>(UiState.Cargando)

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    private val _categoriaSeleccionada = MutableStateFlow<String?>(null)
    val categoriaSeleccionada: StateFlow<String?> = _categoriaSeleccionada.asStateFlow()

    private val _categorias = MutableStateFlow<List<CategoriaInfo>>(CATEGORIAS_DEFINICION)
    val categorias: StateFlow<List<CategoriaInfo>> = _categorias.asStateFlow()

    private val _sinConexion = MutableStateFlow(false)
    val sinConexion: StateFlow<Boolean> = _sinConexion.asStateFlow()

    private val _estado = MutableStateFlow<UiState<List<Libro>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Libro>>> = _estado.asStateFlow()

    private val _hayLibrosEnCatalogo = MutableStateFlow(false)
    val hayLibrosEnCatalogo: StateFlow<Boolean> = _hayLibrosEnCatalogo.asStateFlow()

    private var refrescoTerminado = false

    init {
        viewModelScope.launch {
            repositorio.libros.collect { lista ->
                val categorizados = lista.map { libro ->
                    LibroConCategorias(libro, clasificarLibro(libro))
                }
                _librosCategorizados.value = categorizados
                _hayLibrosEnCatalogo.value = categorizados.isNotEmpty()
                actualizarConteoCategorias(categorizados)

                when {
                    categorizados.isNotEmpty() -> _estadoRaw.value = UiState.Exito(categorizados)
                    refrescoTerminado && _estadoRaw.value !is UiState.Error ->
                        _estadoRaw.value = UiState.Exito(categorizados)
                }
            }
        }

        viewModelScope.launch {
            combine(_estadoRaw, _busqueda, _categoriaSeleccionada) { estadoActual, textoBusqueda, catId ->
                if (estadoActual is UiState.Exito) {
                    val filtrados = filtrarLibros(estadoActual.datos, textoBusqueda, catId)
                    UiState.Exito(filtrados)
                } else {
                    @Suppress("UNCHECKED_CAST")
                    (estadoActual as UiState<List<Libro>>)
                }
            }.collect { estadoFiltrado ->
                _estado.value = estadoFiltrado
            }
        }

        cargar()
    }

    fun cambiarBusqueda(texto: String) {
        _busqueda.value = texto
    }

    fun seleccionarCategoria(catId: String?) {
        if (_categoriaSeleccionada.value == catId) {
            _categoriaSeleccionada.value = null
        } else {
            _categoriaSeleccionada.value = catId
        }
    }

    private fun actualizarConteoCategorias(categorizados: List<LibroConCategorias>) {
        val conteos = mutableMapOf<String, Int>()
        for (item in categorizados) {
            for (catId in item.categorias) {
                conteos[catId] = (conteos[catId] ?: 0) + 1
            }
        }
        Log.d("CATEGORIAS", "Conteos: $conteos")
        categorizados.take(5).forEach { item ->
            Log.d("CATEGORIAS", "Libro: ${item.libro.titulo} | temas: ${item.libro.temas} | categorias: ${item.categorias}")
        }

        _categorias.value = CATEGORIAS_DEFINICION.map { def ->
            def.copy(conteo = conteos[def.id] ?: 0)


        }
    }

    private fun filtrarLibros(
        lista: List<LibroConCategorias>,
        textoBusqueda: String,
        catIdSeleccionada: String?
    ): List<Libro> {
        val query = textoBusqueda.trim().lowercase()

        return lista.filter { item ->
            val coincideBusqueda = query.isEmpty() ||
                    item.libro.titulo.lowercase().contains(query) ||
                    item.libro.autor.lowercase().contains(query)

            val coincideCategoria = catIdSeleccionada == null ||
                    item.categorias.contains(catIdSeleccionada)

            coincideBusqueda && coincideCategoria
        }.map { it.libro }
    }

    fun cargar() {
        viewModelScope.launch {
            refrescoTerminado = false
            if (_estadoRaw.value !is UiState.Exito) {
                _estadoRaw.value = UiState.Cargando
            }
            repositorio.refrescar()
                .onSuccess { cantidad ->
                    refrescoTerminado = true
                    _sinConexion.value = false
                    val listaActualizada = repositorio.libros.first()
                    val categorizados = listaActualizada.map { libro ->
                        LibroConCategorias(libro, clasificarLibro(libro))
                    }
                    _librosCategorizados.value = categorizados
                    _hayLibrosEnCatalogo.value = categorizados.isNotEmpty()
                    actualizarConteoCategorias(categorizados)
                    _estadoRaw.value = UiState.Exito(categorizados)
                    notificador.notificarSincronizacion(cantidad)
                }
                .onFailure {
                    refrescoTerminado = true
                    if (repositorio.cantidadGuardada() == 0) {
                        _sinConexion.value = false
                        _estadoRaw.value = UiState.Error(
                            "No se pudo cargar la lista. Revisa tu conexión e inténtalo de nuevo."
                        )
                    } else {
                        _sinConexion.value = true
                        val listaLocal = repositorio.libros.first()
                        val categorizados = listaLocal.map { libro ->
                            LibroConCategorias(libro, clasificarLibro(libro))
                        }
                        _librosCategorizados.value = categorizados
                        _hayLibrosEnCatalogo.value = listaLocal.isNotEmpty()
                        actualizarConteoCategorias(categorizados)
                        _estadoRaw.value = UiState.Exito(categorizados)
                    }
                }
        }
    }

    fun alternarFavorito(id: String) {
        viewModelScope.launch { repositorio.alternarFavorito(id) }
    }
}