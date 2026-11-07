package com.tecsup.books

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.tecsup.books.data.auth.AuthRepository
import com.tecsup.books.data.auth.FirebaseAuthRepository
import com.tecsup.books.data.local.AppDatabase
import com.tecsup.books.data.local.LibroDao
import com.tecsup.books.data.remote.ApiService
import com.tecsup.books.data.remote.RetrofitClient
import com.tecsup.books.data.repository.LibroRepository
import com.tecsup.books.data.repository.LibroRepositoryImpl
import com.tecsup.books.data.settings.AjustesRepository
import com.tecsup.books.data.settings.AjustesRepositoryImpl
import com.tecsup.books.notification.Notificador
import com.tecsup.books.notification.NotificadorAndroid

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ajustes")

class AppContainer(context: Context) {
    private val appContext = context.applicationContext

    private val api: ApiService by lazy { RetrofitClient.crearApi() }
    private val dao: LibroDao by lazy { AppDatabase.obtener(appContext).libroDao() }

    val libroRepository: LibroRepository by lazy { LibroRepositoryImpl(api, dao) }
    val authRepository: AuthRepository by lazy { FirebaseAuthRepository() }
    val ajustesRepository: AjustesRepository by lazy { AjustesRepositoryImpl(appContext.dataStore) }
    val notificador: Notificador by lazy { NotificadorAndroid(appContext) }
}