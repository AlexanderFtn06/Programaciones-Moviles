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
    fun markAsCompleted(appointment: Appointment) {
        val index = appointments.indexOf(appointment)
        if (index != -1) {
            appointments[index] = appointment.copy(estado = "Completada")
        }
    }
}