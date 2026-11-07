package com.tecsup.books.ui.lista

import com.tecsup.books.data.repository.LibroRepository
import com.tecsup.books.domain.model.Libro
import com.tecsup.books.notification.Notificador
import com.tecsup.books.ui.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ListaViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeRepository: FakeLibroRepository
    private lateinit var fakeNotificador: FakeNotificador

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeLibroRepository()
        fakeNotificador = FakeNotificador()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun refrescoExitoso_estadoPasaAExitoConLibros() = runTest(testDispatcher) {
        val librosFalsos = listOf(
            Libro(id = "1", titulo = "Libro 1", autor = "Autor 1", portadaUrl = null),
            Libro(id = "2", titulo = "Libro 2", autor = "Autor 2", portadaUrl = null)
        )
        fakeRepository.setLibros(librosFalsos)
        fakeRepository.resultadoRefrescar = Result.success(2)
        fakeRepository.cantidadGuardadaVal = 2

        val viewModel = ListaViewModel(fakeRepository, fakeNotificador)
        advanceUntilIdle()

        assertEquals(1, fakeRepository.cantidadLlamadasRefrescar)
        assertEquals(UiState.Exito(librosFalsos), viewModel.estado.value)
        assertEquals(false, viewModel.sinConexion.value)
        assertEquals(listOf(2), fakeNotificador.llamadas)
    }

    @Test
    fun errorSinDatosGuardados_estadoEsError() = runTest(testDispatcher) {
        fakeRepository.setLibros(emptyList())
        fakeRepository.resultadoRefrescar = Result.failure(Exception("Error de red"))
        fakeRepository.cantidadGuardadaVal = 0

        val viewModel = ListaViewModel(fakeRepository, fakeNotificador)
        advanceUntilIdle()

        assertEquals(1, fakeRepository.cantidadLlamadasRefrescar)
        assertEquals(
            UiState.Error("No se pudo cargar la lista. Revisa tu conexión e inténtalo de nuevo."),
            viewModel.estado.value
        )
        assertEquals(false, viewModel.sinConexion.value)
        assertTrue(fakeNotificador.llamadas.isEmpty())
    }

    @Test
    fun errorConDatosGuardados_estadoSigueEnExitoYSinConexionEsTrue() = runTest(testDispatcher) {
        val librosGuardados = listOf(
            Libro(id = "1", titulo = "Libro En Cache", autor = "Autor Cache", portadaUrl = null)
        )
        fakeRepository.setLibros(librosGuardados)
        fakeRepository.resultadoRefrescar = Result.failure(Exception("Sin conexión"))
        fakeRepository.cantidadGuardadaVal = 1

        val viewModel = ListaViewModel(fakeRepository, fakeNotificador)
        advanceUntilIdle()

        assertEquals(1, fakeRepository.cantidadLlamadasRefrescar)
        assertEquals(UiState.Exito(librosGuardados), viewModel.estado.value)
        assertEquals(true, viewModel.sinConexion.value)
        assertTrue(fakeNotificador.llamadas.isEmpty())
    }
}

class FakeLibroRepository : LibroRepository {
    private val _librosFlow = MutableStateFlow<List<Libro>>(emptyList())
    override val libros: Flow<List<Libro>> = _librosFlow
    override val favoritos: Flow<List<Libro>> = MutableStateFlow(emptyList())

    var resultadoRefrescar: Result<Int> = Result.success(0)
    var cantidadGuardadaVal: Int = 0
    var cantidadLlamadasRefrescar: Int = 0

    fun setLibros(lista: List<Libro>) {
        _librosFlow.value = lista
    }

    override suspend fun refrescar(): Result<Int> {
        cantidadLlamadasRefrescar++
        return resultadoRefrescar
    }

    override suspend fun cantidadGuardada(): Int = cantidadGuardadaVal

    override suspend fun alternarFavorito(id: String) {}
    override suspend fun cantidadFavoritos(): Int = 0
    override suspend fun limpiarCache() {}
    override suspend fun obtenerDetalle(id: String): Result<Libro> = Result.failure(Exception("Not implemented"))
}

class FakeNotificador : Notificador {
    val llamadas = mutableListOf<Int>()

    override fun notificarSincronizacion(cantidad: Int) {
        llamadas.add(cantidad)
    }
}
