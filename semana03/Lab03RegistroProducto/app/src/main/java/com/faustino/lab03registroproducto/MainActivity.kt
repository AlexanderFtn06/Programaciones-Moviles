package com.faustino.lab03registroproducto
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faustino.lab03registroproducto.ui.theme.Lab03RegistroProductoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab03RegistroProductoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PantallaRegistro(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun PantallaRegistro(
    modifier: Modifier = Modifier
) {

    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var mostrarResumen by remember { mutableStateOf(false) }
    var importeTotal by remember { mutableStateOf(0.0) }
    var mensajeError by remember { mutableStateOf("") }


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Nuevo producto",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "Completa los datos y presiona Agregar",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )


        Spacer(
            modifier = Modifier.height(24.dp)
        )

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = {
                Text("Nombre del producto")
            },
            modifier = Modifier.fillMaxWidth()
        )


        Spacer(
            modifier = Modifier.height(16.dp)
        )


        Row(
            modifier = Modifier.fillMaxWidth()
        ) {

            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                label = {
                    Text("Precio (S/)")
                },
                modifier = Modifier.weight(1f)
            )


            Spacer(
                modifier = Modifier.width(16.dp)
            )


            OutlinedTextField(
                value = cantidad,
                onValueChange = { cantidad = it },
                label = {
                    Text("Cantidad")
                },
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Button(
            onClick = {
                if (nombre.isBlank() || precio.isBlank() || cantidad.isBlank()) {
                    mostrarResumen = false
                    mensajeError = "Completa todos los campos"
                } else {
                    mensajeError = ""
                    val p = precio.toDoubleOrNull() ?: 0.0
                    val c = cantidad.toIntOrNull() ?: 0

                    importeTotal = p * c
                    mostrarResumen = true
                }

            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "AGREGAR PRODUCTO"
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = {
                nombre = ""
                precio = ""
                cantidad = ""
                mostrarResumen = false
                mensajeError = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "LIMPIAR")
        }

        if (mensajeError.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = mensajeError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (mostrarResumen) {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = nombre,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(text = "Precio: S/ %.2f".format(precio.toDoubleOrNull() ?: 0.0))
                    Text(text = "Cantidad: $cantidad")

                    Text(
                        text = "Importe total: S/ %.2f".format(importeTotal),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )
            Text(
                text = "✓ Producto registrado correctamente",
                color = Color(0xFF2E7D32)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PantallaRegistroPreview() {
    Lab03RegistroProductoTheme {
        PantallaRegistro()
    }
}