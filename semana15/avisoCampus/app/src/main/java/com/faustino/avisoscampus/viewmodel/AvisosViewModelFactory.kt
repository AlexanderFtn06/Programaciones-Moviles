package com.faustino.avisoscampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.faustino.avisoscampus.data.AvisosRepositoryMemoria

class AvisosViewModelFactory : ViewModelProvider.Factory {
    private val repo = AvisosRepositoryMemoria()

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AvisosViewModel(repo) as T
    }
}