package com.faustino.centrotareas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.faustino.centrotareas.data.TareasRepository

class TareasViewModelFactory(
    private val repo: TareasRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TareasViewModel(repo) as T
    }
}