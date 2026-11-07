package com.tecsup.books.ui.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tecsup.books.AppContainer

class DetalleViewModelFactory(
    private val container: AppContainer,
    private val libroId: String
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetalleViewModel(container.libroRepository, libroId) as T
    }
}