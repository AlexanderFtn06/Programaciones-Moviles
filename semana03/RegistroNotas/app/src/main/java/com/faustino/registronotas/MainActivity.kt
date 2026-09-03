package com.faustino.registronotas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faustino.registronotas.ui.theme.RegistroNotasTheme
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegistroNotasTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text("Registro de Notas") },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = Color.White
                            )
                        )
                    }
                )  { innerPadding ->
                    PantallaNotas(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun PantallaNotas(modifier: Modifier = Modifier) {

    var nota1 by remember { mutableFloatStateOf(0f) }
    var nota2 by remember { mutableFloatStateOf(0f) }
    var nota3 by remember { mutableFloatStateOf(0f) }
    var nota4 by remember { mutableFloatStateOf(0f) }
    var redondear by remember { mutableStateOf(false) }
    var confirmado by remember { mutableStateOf(false) }
    var mostrarResultado by remember { mutableStateOf(false) }
    var promedioPonderado by remember { mutableStateOf(0.0) }
    var promedioFinal by remember { mutableStateOf(0.0) }
    var observacion by remember { mutableStateOf("") }
    var colorChip by remember { mutableStateOf(Color.Gray) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Notas del ciclo",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Desliza para asignar cada nota (0 a 20)",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )
        Spacer(modifier = Modifier.height(16.dp))

        FilaCurso(
            nombre = "Fundamentos de Programación",
            peso = 0.20,
            valor = nota1,
            onValorChange = { nota1 = it }
        )
        FilaCurso(
            nombre = "Programación Orientada a Objetos",
            peso = 0.25,
            valor = nota2,
            onValorChange = { nota2 = it }
        )
        FilaCurso(
            nombre = "Programación en Móviles",
            peso = 0.30,
            valor = nota3,
            onValorChange = { nota3 = it }
        )

        FilaCurso(
            nombre = "Base de Datos",
            peso = 0.25,
            valor = nota4,
            onValorChange = { nota4 = it }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Redondear promedio final")
            Switch(
                checked = redondear,
                onCheckedChange = { redondear = it }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = confirmado,
                onCheckedChange = { confirmado = it }
            )
            Text(text = "Confirmo que las notas son correctas")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                val ponderado = nota1 * 0.20 + nota2 * 0.25 + nota3 * 0.30 + nota4 * 0.25
                val final = if (redondear) ponderado.roundToInt().toDouble() else ponderado

                promedioPonderado = ponderado
                promedioFinal = final

                when {
                    final >= 17 -> { observacion = "EXCELENTE"; colorChip = Color(0xFF1B5E20) }
                    final >= 13 -> { observacion = "APROBADO"; colorChip = Color(0xFF4CAF50) }
                    final >= 10 -> { observacion = "EN RECUPERACIÓN"; colorChip = Color(0xFFFFA000) }
                    else -> { observacion = "DESAPROBADO"; colorChip = Color(0xFFD32F2F) }
                }

                mostrarResultado = true
            },
            enabled = confirmado,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "CALCULAR PROMEDIO")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (!mostrarResultado) {
            Text(
                text = "Asigna las notas y confirma para calcular",
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
fun FilaCurso(nombre: String, peso: Double, valor: Float, onValorChange: (Float) -> Unit) {
    Column(modifier = Modifier.padding(bottom = 12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row {
                Text(text = nombre, fontWeight = FontWeight.Bold)
                Text(
                    text = " (${(peso * 100).toInt()}%)",
                    color = MaterialTheme.colorScheme.outline
                )
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${valor.toInt()}",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        Slider(
            value = valor,
            onValueChange = onValorChange,
            valueRange = 0f..20f,
            steps = 19
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PantallaNotasPreview() {
    RegistroNotasTheme  {
        PantallaNotas()
    }
}