package com.faustino.centrotareas.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException

class TareasRepositoryLocal(
    private val dataStore: DataStore<Preferences>
) : TareasRepository {

    private val KEY_HISTORIAL = stringPreferencesKey("historial")

    override fun historial(): Flow<List<String>> = dataStore.data
        .catch { e ->
            if (e is IOException) {
                emit(emptyPreferences())
            } else {
                throw e
            }
        }
        .map { prefs ->
            val raw = prefs[KEY_HISTORIAL]
            if (raw.isNullOrEmpty()) {
                emptyList()
            } else {
                raw.split("\n").filter { it.isNotEmpty() }
            }
        }

    override suspend fun guardarResultado(texto: String) {
        try {
            dataStore.edit { prefs ->
                val actual = prefs[KEY_HISTORIAL]?.split("\n")?.filter { it.isNotEmpty() } ?: emptyList()
                val nueva = listOf(texto) + actual
                prefs[KEY_HISTORIAL] = nueva.take(5).joinToString("\n")
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: IOException) {
            Log.e("HISTORIAL", "Error al guardar historial", e)
        }
    }

    override fun progresoTarea(): Flow<Float> = flow {
        for (i in 1..100) {
            if (i == 1) Log.d("HILO", "Dentro: ${Thread.currentThread().name}")
            delay(40)
            emit(i / 100f)
        }
    }.flowOn(Dispatchers.Default)

    override suspend fun cargarPerfil(): String {
        delay(2000)
        return "Ana Torres (ficticia)"
    }

    override suspend fun cargarPedidos(simularError: Boolean): List<String> {
        delay(3000)
        if (simularError) throw IOException("Sin conexión")
        return listOf("Pedido 101", "Pedido 102", "Pedido 103")
    }

    override suspend fun contarPrimos(
        hasta: Int,
        onProgreso: (Float) -> Unit
    ): Int = withContext(Dispatchers.Default) {
        var cantidad = 0
        var porcentajeAnterior = -1
        for (n in 2..hasta) {
            ensureActive()               // permite cancelar a mitad del cálculo
            if (esPrimo(n)) cantidad++
            val porcentajeActual = (n * 100L / hasta).toInt()
            if (porcentajeActual != porcentajeAnterior) {
                porcentajeAnterior = porcentajeActual
                onProgreso(porcentajeActual / 100f)
            }
        }
        cantidad
    }

    private fun esPrimo(n: Int): Boolean {
        if (n < 2) return false
        var d = 2
        while (d.toLong() * d <= n) {    // prueba de 2 hasta la raíz de n
            if (n % d == 0) return false
            d++
        }
        return true
    }
}