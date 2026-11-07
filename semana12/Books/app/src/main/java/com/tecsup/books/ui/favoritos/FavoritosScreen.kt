package com.tecsup.books.ui.favoritos

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.books.domain.model.Libro
import com.tecsup.books.ui.UiState
import com.tecsup.books.ui.lista.ListaScreen

@Composable
fun FavoritosScreen(
    estado: UiState<List<Libro>>,
    onLibroClick: (String) -> Unit,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        TextButton(
            onClick = onVolver,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Text("← Volver")
        }

        Text(
            text = "Favoritos",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        ListaScreen(
            estado = estado,
            onReintentar = {},
            onLibroClick = onLibroClick,
            modifier = Modifier.weight(1f)
        )
    }
}