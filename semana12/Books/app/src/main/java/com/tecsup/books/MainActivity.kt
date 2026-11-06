package com.tecsup.books

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.books.ui.lista.ListaScreen
import com.tecsup.books.ui.lista.ListaViewModel
import com.tecsup.books.ui.lista.ListaViewModelFactory
import com.tecsup.books.ui.theme.BooksTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val container = AppContainer()

        setContent {
            BooksTheme {
                val viewModel: ListaViewModel = viewModel(
                    factory = ListaViewModelFactory(container)
                )
                val estado by viewModel.estado.collectAsStateWithLifecycle()

                Scaffold { innerPadding ->
                    ListaScreen(
                        estado = estado,
                        onReintentar = viewModel::cargar,
                        onLibroClick = { },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}