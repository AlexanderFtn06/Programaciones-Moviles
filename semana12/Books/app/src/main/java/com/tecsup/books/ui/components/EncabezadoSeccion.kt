package com.tecsup.books.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding

@Composable
fun EncabezadoSeccion(titulo: String, modifier: Modifier = Modifier) {
    Text(
        text = titulo,
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = modifier.padding(horizontal = 16.dp, vertical = 12.dp)
    )
}