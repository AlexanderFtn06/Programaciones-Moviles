package com.faustino.ayudacampus.viewmodel

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel

/** Fábrica genérica: permite crear ViewModels que reciben parámetros (repo, id, usuario...). */
class AyudaViewModelFactory<VM : ViewModel>(
    private val crear: () -> VM
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = crear() as T
}

/** Uso en NavAyuda:  val vm = ayudaViewModel { CategoriasViewModel(repo) } */
@Composable
inline fun <reified VM : ViewModel> ayudaViewModel(
    key: String? = null,
    noinline crear: () -> VM
): VM = viewModel(key = key, factory = AyudaViewModelFactory(crear))