package com.tecsup.books.data.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class AjustesRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : AjustesRepository {

    private companion object {
        val CLAVE_MODO_OSCURO = booleanPreferencesKey("modo_oscuro")
    }

    override val modoOscuro: Flow<Boolean?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[CLAVE_MODO_OSCURO]
        }

    override suspend fun guardarModoOscuro(esOscuro: Boolean) {
        dataStore.edit { preferences ->
            preferences[CLAVE_MODO_OSCURO] = esOscuro
        }
    }
}