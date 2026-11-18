package com.faustino.campusGPS.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore by preferencesDataStore(name = "preferencias_usuario")

class PreferenciasRepositoryDataStore(private val context: Context) : PreferenciasRepository {
    private object Keys {
        val ULTIMO_LUGAR_ID = intPreferencesKey("ultimo_lugar_id")
    }

    override val ultimoLugarId: Flow<Int?> = context.dataStore.data
        .catch { e -> if (e is IOException) emit(emptyPreferences()) else throw e }
        .map { preferences ->
            preferences[Keys.ULTIMO_LUGAR_ID]
        }

    override suspend fun guardarUltimoLugar(id: Int) {
        context.dataStore.edit { preferences ->
            preferences[Keys.ULTIMO_LUGAR_ID] = id
        }
    }
}
