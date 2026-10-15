package com.faustino.ayudacampus.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.faustino.ayudacampus.data.model.Rol
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

// Declarada UNA sola vez, a nivel de archivo (fuera de la clase)
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ajustes")

class PreferenciasStore(private val context: Context) {

    private object Claves {
        val PERFIL_ACTIVO = stringPreferencesKey("perfilActivo")      // CLIENTE | ADMIN | ""
        val USUARIO_ID = intPreferencesKey("usuarioId")
        val MODO_OSCURO = booleanPreferencesKey("modoOscuro")
        val NOTIFICACIONES = booleanPreferencesKey("notificaciones")
        val RECIBIR_TICKETS = booleanPreferencesKey("recibirTickets")
    }

    // Si el archivo no se puede leer, se usan los valores por defecto
    private val datos: Flow<Preferences> = context.dataStore.data
        .catch { e -> if (e is IOException) emit(emptyPreferences()) else throw e }

    // ---------- Lectura: un Flow por cada preferencia ----------

    val perfilActivo: Flow<String> = datos.map { it[Claves.PERFIL_ACTIVO] ?: "" }

    val usuarioId: Flow<Int> = datos.map { it[Claves.USUARIO_ID] ?: 0 }

    val modoOscuro: Flow<Boolean> = datos.map { it[Claves.MODO_OSCURO] ?: false }

    val notificaciones: Flow<Boolean> = datos.map { it[Claves.NOTIFICACIONES] ?: true }

    val recibirTickets: Flow<Boolean> = datos.map { it[Claves.RECIBIR_TICKETS] ?: true }

    // ---------- Escritura: funciones suspend ----------

    suspend fun guardarSesion(rol: Rol, usuarioId: Int) {
        context.dataStore.edit {
            it[Claves.PERFIL_ACTIVO] = rol.name
            it[Claves.USUARIO_ID] = usuarioId
        }
    }

    suspend fun guardarModoOscuro(activo: Boolean) {
        context.dataStore.edit { it[Claves.MODO_OSCURO] = activo }
    }

    suspend fun guardarNotificaciones(activas: Boolean) {
        context.dataStore.edit { it[Claves.NOTIFICACIONES] = activas }
    }

    suspend fun guardarRecibirTickets(recibir: Boolean) {
        context.dataStore.edit { it[Claves.RECIBIR_TICKETS] = recibir }
    }

    // Borra solo la sesión; el modo oscuro y los demás ajustes se conservan
    suspend fun cerrarSesion() {
        context.dataStore.edit {
            it.remove(Claves.PERFIL_ACTIVO)
            it.remove(Claves.USUARIO_ID)
        }
    }
}