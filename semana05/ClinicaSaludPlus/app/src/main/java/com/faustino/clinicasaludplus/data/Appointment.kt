package com.faustino.clinicasaludplus.data


import androidx.compose.runtime.mutableStateListOf

data class Appointment(
    val doctorName: String,
    val fecha: String,
    val hora: String,
    val estado: String
)
object AppointmentsStore {
    val appointments = mutableStateListOf<Appointment>()

    fun addAppointment(appointment: Appointment) {
        appointments.add(0, appointment)
    }
}