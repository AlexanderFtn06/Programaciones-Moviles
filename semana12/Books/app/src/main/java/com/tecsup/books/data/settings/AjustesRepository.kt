package com.tecsup.books.data.settings

import kotlinx.coroutines.flow.Flow

interface AjustesRepository {
    val modoOscuro: Flow<Boolean?>
    suspend fun guardarModoOscuro(esOscuro: Boolean)
}