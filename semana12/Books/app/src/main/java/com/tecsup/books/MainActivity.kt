package com.tecsup.books

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.tecsup.books.domain.model.Libro
import com.tecsup.books.ui.UiState
import com.tecsup.books.ui.lista.ListaScreen
import com.tecsup.books.ui.lista.librosDePrueba
import com.tecsup.books.ui.theme.BooksTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BooksTheme {
                var estado by remember {
                    mutableStateOf<UiState<List<Libro>>>(UiState.Exito(emptyList()))
                }

                Scaffold { innerPadding ->
                    ListaScreen(
                        estado = estado,
                        onReintentar = { estado = UiState.Exito(librosDePrueba) },
                        onLibroClick = { },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}