package com.faustino.clinicasaludplus.data

data class Doctor(
    val id: Int,
    val name: String,
    val specialty: String,
    val rating: Double
)

val doctorsList = listOf(
    Doctor(id = 1, name = "Dra. Ana Torres", specialty = "Cardióloga", rating = 4.9),
    Doctor(id = 2, name = "Dr. Luis Vega", specialty = "Pediatra", rating = 4.7),
    Doctor(id = 3, name = "Dra. Rosa Díaz", specialty = "Dermatóloga", rating = 4.8)
)

val specialties = listOf("Todas", "Cardiología", "Pediatría", "Dermatología")