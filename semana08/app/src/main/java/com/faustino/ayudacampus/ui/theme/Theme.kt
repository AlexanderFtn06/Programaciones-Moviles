package com.faustino.ayudacampus.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.faustino.ayudacampus.data.model.Rol

private fun esquemaClaro(
    primario: Color, contenedor: Color, textoContenedor: Color, fondo: Color, variante: Color
): ColorScheme = lightColorScheme(
    primary = primario, onPrimary = Color.White,
    primaryContainer = contenedor, onPrimaryContainer = textoContenedor,
    secondary = primario, onSecondary = Color.White,
    secondaryContainer = contenedor, onSecondaryContainer = textoContenedor,
    background = fondo, onBackground = Color(0xFF111827),
    surface = Color.White, onSurface = Color(0xFF111827),
    surfaceVariant = variante, onSurfaceVariant = Color(0xFF4B5563),
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = fondo,
    surfaceContainer = Color.White,
    surfaceContainerHigh = variante,
    surfaceContainerHighest = variante,
    outlineVariant = Color(0xFFE5E7EB)
)

private fun esquemaOscuro(primario: Color, contenedor: Color): ColorScheme = darkColorScheme(
    primary = primario, onPrimary = Color(0xFF0B1220),
    primaryContainer = contenedor, onPrimaryContainer = Color.White,
    secondary = primario, onSecondary = Color(0xFF0B1220),
    secondaryContainer = contenedor, onSecondaryContainer = Color.White,
    background = FondoOscuro, onBackground = Color(0xFFE5E7EB),
    surface = SuperficieOscura, onSurface = Color(0xFFE5E7EB),
    surfaceVariant = VarianteOscura, onSurfaceVariant = Color(0xFF9CA3AF),
    surfaceContainerLowest = FondoOscuro,
    surfaceContainerLow = FondoOscuro,
    surfaceContainer = SuperficieOscura,
    surfaceContainerHigh = VarianteOscura,
    surfaceContainerHighest = VarianteOscura,
    outlineVariant = Color(0xFF374151)
)

private val EstudianteClaro =
    esquemaClaro(CianPrimario, CianContenedor, CianTextoContenedor, CianFondo, CianVariante)
private val SoporteClaro =
    esquemaClaro(VioletaPrimario, VioletaContenedor, VioletaTextoContenedor, VioletaFondo, VioletaVariante)
private val EstudianteOscuro = esquemaOscuro(CianClaro, CianContenedorOscuro)
private val SoporteOscuro = esquemaOscuro(VioletaClaro, VioletaContenedorOscuro)

/** Entrega un ColorScheme según el perfil. En las pantallas usa MaterialTheme.colorScheme.* */
@Composable
fun AyudaTheme(
    rol: Rol = Rol.CLIENTE,
    oscuro: Boolean = false,
    content: @Composable () -> Unit
) {
    val esquema = when (rol) {
        Rol.CLIENTE -> if (oscuro) EstudianteOscuro else EstudianteClaro
        Rol.ADMIN -> if (oscuro) SoporteOscuro else SoporteClaro
    }
    MaterialTheme(colorScheme = esquema, typography = AyudaTypography, content = content)
}