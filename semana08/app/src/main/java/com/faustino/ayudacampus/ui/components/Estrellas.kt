package com.faustino.ayudacampus.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.faustino.ayudacampus.ui.theme.ColorEstrella

/** Calificación de 1 a 5. Si onCambio es null, solo se muestra (no se puede tocar). */
@Composable
fun Estrellas(
    valor: Int,
    modifier: Modifier = Modifier,
    tamano: Dp = 36.dp,
    onCambio: ((Int) -> Unit)? = null
) {
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        for (i in 1..5) {
            val llena = i <= valor
            Icon(
                imageVector = if (llena) Icons.Filled.Star else Icons.Filled.StarBorder,
                contentDescription = "$i estrellas",
                tint = if (llena) ColorEstrella else MaterialTheme.colorScheme.outline,
                modifier = Modifier
                    .size(tamano)
                    .then(if (onCambio != null) Modifier.clickable { onCambio(i) } else Modifier)
            )
        }
    }
}