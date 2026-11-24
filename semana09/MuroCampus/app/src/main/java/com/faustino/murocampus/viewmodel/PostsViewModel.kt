package com.faustino.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faustino.murocampus.data.PostRepository
import com.faustino.murocampus.model.Comentario
import com.faustino.murocampus.model.NuevoPost
import com.faustino.murocampus.model.Post
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetalleUi(
    val post: Post,
    val comentarios: List<Comentario>
)

class PostsViewModel(private val repo: PostRepository) : ViewModel() {

    private val _listaRaw = MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    private val _borrados = MutableStateFlow<Set<Int>>(emptySet())
    private val _titulosEditados = MutableStateFlow<Map<Int, String>>(emptyMap())

    val lista: StateFlow<UiState<List<Post>>> = combine(_listaRaw, _busqueda, _borrados, _titulosEditados) { estado, query, borrados, titulosEditados ->
        if (estado is UiState.Exito) {
            val q = query.trim()
            UiState.Exito(
                estado.datos.map { post ->
                    val tituloEditado = titulosEditados[post.id]
                    if (tituloEditado != null) post.copy(title = tituloEditado) else post
                }.filter { post ->
                    post.id !in borrados && (q.isEmpty() || post.title.contains(q, ignoreCase = true))
                }
            )
        } else {
            estado
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState.Cargando
    )

    private val _detalle = MutableStateFlow<UiState<DetalleUi>>(UiState.Cargando)
    val detalle: StateFlow<UiState<DetalleUi>> = _detalle.asStateFlow()

    // null significa "Todos"
    private val _usuarioFiltro = MutableStateFlow<Int?>(null)
    val usuarioFiltro: StateFlow<Int?> = _usuarioFiltro.asStateFlow()

    // null = todavía no se envió nada
    private val _creacion = MutableStateFlow<UiState<Post>?>(null)
    val creacion: StateFlow<UiState<Post>?> = _creacion.asStateFlow()

    // null = todavía no se intentó borrar
    private val _borrado = MutableStateFlow<UiState<Unit>?>(null)
    val borrado: StateFlow<UiState<Unit>?> = _borrado.asStateFlow()

    // null = todavía no se intentó editar
    private val _edicion = MutableStateFlow<UiState<Post>?>(null)
    val edicion: StateFlow<UiState<Post>?> = _edicion.asStateFlow()

    private var jobLista: Job? = null

    init {
        cargar()
    }

    fun cargar() {
        jobLista?.cancel()
        jobLista = viewModelScope.launch {
            _listaRaw.value = UiState.Cargando
            val userId = _usuarioFiltro.value
            val resultado = if (userId == null) repo.listar() else repo.porUsuario(userId)
            resultado
                .onSuccess { _listaRaw.value = UiState.Exito(it) }
                .onFailure { _listaRaw.value = UiState.Error(mensajeDe(it)) }
        }
    }

    fun buscar(texto: String) {
        _busqueda.value = texto
    }

    fun filtrar(userId: Int?) {
        _usuarioFiltro.value = userId
        cargar()
    }

    fun abrir(id: Int) {
        reiniciarBorrado()
        reiniciarEdicion()
        viewModelScope.launch {
            _detalle.value = UiState.Cargando

            // Post y comentarios en paralelo
            val (resultadoPost, resultadoComentarios) = coroutineScope {
                val post = async { repo.detalle(id) }
                val comentarios = async { repo.comentarios(id) }
                post.await() to comentarios.await()
            }

            val falla = resultadoPost.exceptionOrNull()
                ?: resultadoComentarios.exceptionOrNull()

            _detalle.value = if (falla != null) {
                UiState.Error(mensajeDe(falla))
            } else {
                val postOriginal = resultadoPost.getOrThrow()
                val tituloEditado = _titulosEditados.value[id]
                val postFinal = if (tituloEditado != null) postOriginal.copy(title = tituloEditado) else postOriginal
                UiState.Exito(
                    DetalleUi(postFinal, resultadoComentarios.getOrThrow())
                )
            }
        }
    }

    fun publicar(titulo: String, texto: String) {
        viewModelScope.launch {
            _creacion.value = UiState.Cargando
            repo.crear(NuevoPost(userId = 1, title = titulo, body = texto))
                .onSuccess { _creacion.value = UiState.Exito(it) }
                .onFailure { _creacion.value = UiState.Error(mensajeDe(it)) }
        }
    }

    fun reiniciarCreacion() {
        _creacion.value = null
    }

    fun borrar(id: Int) {
        viewModelScope.launch {
            _borrado.value = UiState.Cargando
            repo.borrar(id)
                .onSuccess {
                    _borrados.update { it + id }
                    _borrado.value = UiState.Exito(Unit)
                }
                .onFailure {
                    _borrado.value = UiState.Error(mensajeDe(it))
                }
        }
    }

    fun reiniciarBorrado() {
        _borrado.value = null
    }

    fun editarTitulo(id: Int, nuevoTitulo: String) {
        val tituloLimpio = nuevoTitulo.trim()
        if (tituloLimpio.isEmpty()) return
        viewModelScope.launch {
            _edicion.value = UiState.Cargando
            repo.editarTitulo(id, tituloLimpio)
                .onSuccess { postServidor ->
                    val tituloFinal = postServidor.title.ifBlank { tituloLimpio }
                    _titulosEditados.update { it + (id to tituloFinal) }

                    val detalleActual = _detalle.value
                    if (detalleActual is UiState.Exito && detalleActual.datos.post.id == id) {
                        val postActualizado = detalleActual.datos.post.copy(title = tituloFinal)
                        _detalle.value = UiState.Exito(
                            detalleActual.datos.copy(post = postActualizado)
                        )
                    }

                    _edicion.value = UiState.Exito(postServidor.copy(title = tituloFinal))
                }
                .onFailure {
                    _edicion.value = UiState.Error(mensajeDe(it))
                }
        }
    }

    fun reiniciarEdicion() {
        _edicion.value = null
    }
}