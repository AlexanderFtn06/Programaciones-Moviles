package com.faustino.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.faustino.murocampus.viewmodel.PostsViewModel
import com.faustino.murocampus.viewmodel.UiState

@Composable
fun PantallaLista( vm: PostsViewModel, onPostClick: (Int) -> Unit, modifier: Modifier = Modifier) {
    val estado by vm.lista.collectAsStateWithLifecycle()

    when (val e = estado) {
        UiState.Cargando -> Cargando(modifier)

        is UiState.Error -> ErrorConReintento(
            mensaje = e.mensaje,
            onReintentar = { vm.cargar() },
            modifier = modifier
        )

        is UiState.Exito -> LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(e.datos, key = { it.id }) { post ->
                TarjetaPost(
                    userId = post.userId,
                    titulo = post.title,
                    cuerpo = post.body,
                    maxLines = 2,
                    onClick = { onPostClick(post.id) }
                )
            }
        }
    }
}