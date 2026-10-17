package com.faustino.ayudacampus.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun EstadoVacio(
    icono: ImageVector,
    titulo: String,
    mensaje: String,
    modifier: Modifier = Modifier,
    textoAccion: String? = null,
    onAccion: (() -> Unit)? = null,
    compacto: Boolean = false
) {
    val visibleState = remember {
        MutableTransitionState(false).apply { targetState = true }
    }

    AnimatedVisibility(
        visibleState = visibleState,
        enter = fadeIn(animationSpec = tween(300)) + scaleIn(animationSpec = tween(300), initialScale = 0.8f),
        modifier = modifier
    ) {
        val paddingVal = if (compacto) 12.dp else 32.dp
        val sizeBox = if (compacto) 72.dp else 112.dp
        val sizeMainCircle = if (compacto) 48.dp else 80.dp
        val sizeIcon = if (compacto) 20.dp else 36.dp
        val sizeDeco1 = if (compacto) 10.dp else 16.dp
        val sizeDeco2 = if (compacto) 12.dp else 20.dp

        Column(
            Modifier
                .fillMaxWidth()
                .padding(paddingVal),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Ilustración dibujada con Compose (círculo principal + decorativos)
            Box(
                Modifier.size(sizeBox),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    Modifier
                        .size(sizeDeco1)
                        .background(
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                            CircleShape
                        )
                        .align(Alignment.TopStart)
                )
                Box(
                    Modifier
                        .size(sizeDeco2)
                        .background(
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f),
                            CircleShape
                        )
                        .align(Alignment.BottomEnd)
                )
                Box(
                    Modifier
                        .size(sizeMainCircle)
                        .background(
                            MaterialTheme.colorScheme.primaryContainer,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        icono,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(sizeIcon)
                    )
                }
            }

            Spacer(Modifier.height(if (compacto) 8.dp else 16.dp))
            Text(
                titulo,
                style = if (compacto) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(4.dp))
            Text(
                mensaje,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            if (!compacto && textoAccion != null && onAccion != null) {
                Spacer(Modifier.height(20.dp))
                AyudaButton(
                    texto = textoAccion,
                    onClick = onAccion
                )
            }
        }
    }
}
