package com.faustino.clinicasaludplus.data

data class Doctor(
    val id: Int,
    val name: String,
    val specialty: String,
    val category: String,
    val rating: Double,
    val experience: String,
    val description: String
)

val doctorsList = listOf(
    Doctor(id = 1, name = "Dra. Ana Torres", specialty = "Cardióloga",category = "Cardiología",rating = 4.9,experience = "12 años exp.",
        description = "Especialista en arritmias e hipertensión, formación en la Clínica Mayo."
    ),
    Doctor(id = 2, name = "Dr. Luis Vega", specialty = "Pediatra",category = "Pediatría", rating = 4.7,experience = "8 años exp.",description = "Enfocado en el desarrollo infantil y vacunación preventiva." ),
    Doctor(id = 3, name = "Dra. Rosa Díaz", specialty = "Dermatóloga",category = "Dermatología", rating = 4.8, experience = "10 años exp.",
        description = "Especialista en dermatología clínica y estética facial.")
)

val specialties = listOf("Todas", "Cardiología", "Pediatría", "Dermatología")