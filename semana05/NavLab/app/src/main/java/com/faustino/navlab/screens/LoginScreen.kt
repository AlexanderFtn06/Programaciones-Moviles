package com.faustino.navlab.screens

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.faustino.navlab.navigation.Screen
import kotlinx.coroutines.launch

data class User(val email: String, val password: String, val name: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    val users = remember {
        listOf(
            User("alexander@tecsup.edu.pe", "12345678", "Alexander"),
            User("afaustino@tecsup.edu.pe", "87654321", "Faustino")
        )
    }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF4A2E83), Color(0xFFC9B8F0))
    )

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBrush)
                .padding(paddingValues)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Portal Académico",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4A2E83)
                        ),
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            emailError = null
                        },
                        label = { Text("Correo Institucional") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email Icon",
                                tint = Color(0xFF4A2E83)
                            )
                        },
                        isError = emailError != null,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4A2E83),
                            unfocusedBorderColor = Color(0xFFC9B8F0)
                        )
                    )
                    if (emailError != null) {
                        Text(
                            text = emailError ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 4.dp, top = 4.pxToDp())
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            passwordError = null
                        },
                        label = { Text("Contraseña") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Lock Icon",
                                tint = Color(0xFF4A2E83)
                            )
                        },
                        isError = passwordError != null,
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4A2E83),
                            unfocusedBorderColor = Color(0xFFC9B8F0)
                        )
                    )
                    if (passwordError != null) {
                        Text(
                            text = passwordError ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 4.dp, top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            emailError = null
                            passwordError = null

                            // 1. Campo vacío
                            if (email.isBlank()) {
                                emailError = "Este campo es obligatorio"
                                return@Button
                            }
                            if (password.isBlank()) {
                                passwordError = "Este campo es obligatorio"
                                return@Button
                            }

                            // 2. Correo con formato inválido
                            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                emailError = "Correo inválido"
                                return@Button
                            }

                            // 3. Contraseña < 8 caracteres
                            if (password.length < 8) {
                                passwordError = "La contraseña debe tener al menos 8 caracteres"
                                return@Button
                            }

                            // 4. Correo no está en la lista
                            val userFound = users.find { it.email.equals(email, ignoreCase = true) }
                            if (userFound == null) {
                                emailError = "Usuario no encontrado"
                                return@Button
                            }

                            // 5. Contraseña no coincide
                            if (userFound.password != password) {
                                passwordError = "Contraseña incorrecta"
                                return@Button
                            }

                            // 6. Todo correcto → navegar a Home pasando el "nombre"
                            navController.navigate(Screen.Home.createRoute(userFound.name)) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A2E83)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(
                            text = "INICIAR SESIÓN",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "¿Olvidaste tu contraseña?",
                        color = Color(0xFF4A2E83),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .clickable {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Se enviará un enlace de recuperación a tu correo institucional")
                                }
                            }
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun Int.pxToDp() = this.dp
