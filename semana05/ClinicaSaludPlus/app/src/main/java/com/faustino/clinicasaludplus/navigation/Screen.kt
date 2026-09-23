package com.faustino.clinicasaludplus.navigation

sealed class Screen(val route: String) {
    object Home : Screen(route = "home")
    object MyAppointments : Screen(route = "myAppointments")
    object MedicalHistory : Screen(route = "medicalHistory")
    object Profile : Screen(route = "profile")
    object DoctorProfile : Screen(route = "doctorProfile/{doctorId}") {
        fun createRoute(doctorId: Int): String = "doctorProfile/$doctorId"
    }

    object ScheduleAppointment : Screen(route = "scheduleAppointment/{doctorId}") {
        fun createRoute(doctorId: Int): String = "scheduleAppointment/$doctorId"
    }

    object Confirmation : Screen(route = "confirmation/{doctorId}/{fecha}/{hora}") {
        fun createRoute(doctorId: Int, fecha: String, hora: String): String =
            "confirmation/$doctorId/$fecha/$hora"
    }
}