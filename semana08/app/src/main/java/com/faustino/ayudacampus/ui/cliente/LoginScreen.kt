package com.faustino.ayudacampus.ui.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.faustino.ayudacampus.data.local.entity.Usuario
import com.faustino.ayudacampus.data.model.Rol
import com.faustino.ayudacampus.ui.components.AyudaButton
import com.faustino.ayudacampus.ui.components.AyudaTextField
import com.faustino.ayudacampus.ui.theme.AyudaTheme
import com.faustino.ayudacampus.ui.theme.ColorResuelto
import com.faustino.ayudacampus.viewmodel.SesionViewModel

/** Un solo Composable para ambos roles. */
@Composable
fun LoginScreen(
    vm: SesionViewModel,
    onIngresar: (Usuario) -> Unit,
    onRegistrarse: () -> Unit,
    onOlvidoClave: () -> Unit
) {
    val usuario by vm.usuarioActual.collectAsState()
    val mensajeError by vm.error.collectAsState()
    val aviso by vm.aviso.collectAsState()
    val cargando by vm.cargando.collectAsState()
    val oscuro by vm.modoOscuro.collectAsState()

    // Estado propio de la pantalla (lo que el usuario escribe)
    var rol by rememberSaveable { mutableStateOf(Rol.CLIENTE) }
    var correo by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }

    // Cuando el ViewModel confirma el login, navegamos
    LaunchedEffect(usuario) {
        usuario?.let(onIngresar)
    }

    // El color de toda la pantalla sigue a la pestaña elegida
    AyudaTheme(rol = rol, oscuro = oscuro) {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(
                Modifier
                    .fillMaxSize()
                    .systemBarsPadding()
                    .imePadding()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Spacer(Modifier.height(40.dp))
                Text(
                    "Bienvenido",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Ingresa para continuar",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.height(28.dp))

                SelectorRol(
                    seleccionado = rol,
                    onSeleccionar = {
                        rol = it
                        vm.limpiarError()
                        vm.limpiarAviso()
                    }
                )

                Spacer(Modifier.height(24.dp))

                AyudaTextField(
                    valor = correo,
                    onCambio = {
                        correo = it
                        vm.limpiarError()
                        vm.limpiarAviso()
                    },
                    etiqueta = "Correo",
                    icono = Icons.Outlined.Email,
                    teclado = KeyboardType.Email
                )
                Spacer(Modifier.height(12.dp))
                AyudaTextField(
                    valor = clave,
                    onCambio = {
                        clave = it
                        vm.limpiarError()
                        vm.limpiarAviso()
                    },
                    etiqueta = "Contraseña",
                    icono = Icons.Outlined.Lock,
                    esClave = true
                )

                TextButton(
                    onClick = onOlvidoClave,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        "¿Olvidaste tu contraseña?",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                mensajeError?.let { msg ->
                    Text(
                        msg,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.height(8.dp))
                }

                aviso?.let { msg ->
                    Surface(
                        color = ColorResuelto.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = ColorResuelto)
                            Spacer(Modifier.width(10.dp))
                            Text(
                                msg,
                                style = MaterialTheme.typography.bodyMedium,
                                color = ColorResuelto
                            )
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                // Botón del color del rol (MaterialTheme.colorScheme.primary)
                AyudaButton(
                    texto = if (cargando) "Ingresando…" else "Ingresar",
                    onClick = { vm.login(correo, clave, rol) },
                    habilitado = !cargando
                )

                Spacer(Modifier.height(20.dp))

                if (rol == Rol.CLIENTE) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "¿No tienes cuenta?",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        // Enlace de registro
                        TextButton(onClick = onRegistrarse) {
                            Text(
                                "Regístrate",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                } else {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(
                                "Modo práctica",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "Puedes cambiar de perfil desde Ajustes para probar ambos roles.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

/** Selector tipo pastilla: Estudiante / Soporte TI */
@Composable
private fun SelectorRol(seleccionado: Rol, onSeleccionar: (Rol) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(14.dp))
            .padding(4.dp)
    ) {
        Rol.entries.forEach { opcion ->
            val activo = opcion == seleccionado
            Box(
                Modifier
                    .weight(1f)
                    .height(44.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (activo) MaterialTheme.colorScheme.primary else Color.Transparent)
                    .selectable(
                        selected = activo,
                        onClick = { onSeleccionar(opcion) },
                        role = Role.Tab
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    opcion.etiqueta,
                    style = MaterialTheme.typography.labelLarge,
                    color = if (activo) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
