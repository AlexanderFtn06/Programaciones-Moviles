package com.faustino.navlab.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.faustino.navlab.navigation.Screen

data class Student(val id: Int, val name: String, val career: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(navController: NavController) {
    val students = listOf(
        Student(1, "Alexander Faustino", "Diseño y Desarrollo de Software"),
        Student(2, "Juan Perez", "Big Data y Ciencia de Datos"),
        Student(3, "Maria Garcia", "Operaciones Mineras"),
        Student(4, "Luis Rodriguez", "Mantenimiento de Maquinaria"),
        Student(5, "Ana Martinez", "Electricidad Industrial"),
        Student(6, "Carlos Lopez", "Gestión y Logística"),
        Student(7, "Elena Gomez", "Producción y Gestión Industrial"),
        Student(8, "Jorge Castro", "Mecatrónica Industrial")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Directorio de Alumnos", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color(0xFF4A2E83),
                    navigationIconContentColor = Color(0xFF4A2E83)
                )
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize().background(Color(0xFFF8F7FF))
        ) {
            items(students) { student ->
                ListItem(
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.Detail.createRoute(student.id))
                    },
                    headlineContent = {
                        Text(
                            text = student.name,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    },
                    supportingContent = {
                        Text(
                            text = student.career,
                            color = Color(0xFF4A2E83)
                        )
                    },
                    leadingContent = {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(Color(0xFFE8E0FF), shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color(0xFF4A2E83),
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    },
                    trailingContent = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFE8E0FF))
            }
        }
    }
}
