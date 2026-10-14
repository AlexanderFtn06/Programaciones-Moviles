package com.faustino.ayudacampus.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faustino.ayudacampus.data.model.Prioridad
import com.faustino.ayudacampus.ui.theme.colorPrioridad

@Composable
fun PrioridadChip(prioridad: Prioridad, modifier: Modifier = Modifier) {
    EtiquetaColor(prioridad.etiqueta, colorPrioridad(prioridad), modifier)
}