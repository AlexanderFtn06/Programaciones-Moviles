package com.faustino.clinicasaludplus.data


import androidx.compose.runtime.mutableStateListOf

data class Appointment(
    val id: Int,
    val doctorName: String,
    val fecha: String,
    val hora: String,
    val estado: String
)
object AppointmentsStore {
    private var nextId = 1
    val appointments = mutableStateListOf<Appointment>()

    fun addAppointment(doctorName: String, fecha: String, hora: String, estado: String) {
        appointments.add(
            0,
            Appointment(
                id = nextId++,
                doctorName = doctorName,
                fecha = fecha,
                hora = hora,
                estado = estado
            )
        )
    }
    fun cancelAppointment(id: Int) {
        appointments.removeAll { it.id == id }
    }
}