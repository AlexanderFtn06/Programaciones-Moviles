package com.tecsup.books

import com.tecsup.books.data.remote.ApiService
import com.tecsup.books.data.remote.RetrofitClient
import com.tecsup.books.data.repository.LibroRepository
import com.tecsup.books.data.repository.LibroRepositoryImpl

class AppContainer {
    private val api: ApiService by lazy { RetrofitClient.crearApi() }

    val libroRepository: LibroRepository by lazy { LibroRepositoryImpl(api) }
}