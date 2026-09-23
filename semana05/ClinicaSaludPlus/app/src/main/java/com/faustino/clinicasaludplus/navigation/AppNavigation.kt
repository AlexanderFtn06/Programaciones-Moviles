package com.faustino.clinicasaludplus.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.faustino.clinicasaludplus.data.doctorsList
import com.faustino.clinicasaludplus.screens.ConfirmationScreen
import com.faustino.clinicasaludplus.screens.DoctorProfileScreen
import com.faustino.clinicasaludplus.screens.HomeScreen
import com.faustino.clinicasaludplus.screens.MedicalHistoryScreen
import com.faustino.clinicasaludplus.screens.MyAppointmentsScreen
import com.faustino.clinicasaludplus.screens.ScheduleAppointmentScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(
            route = Screen.DoctorProfile.route,
            arguments = listOf(navArgument("doctorId") { type = NavType.IntType; defaultValue = 0 })
        ) { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getInt("doctorId") ?: 0
            val doctor = doctorsList.find { it.id == doctorId }
            if (doctor != null) DoctorProfileScreen(navController, doctor)
        }
        composable(
            route = Screen.ScheduleAppointment.route,
            arguments = listOf(navArgument("doctorId") { type = NavType.IntType; defaultValue = 0 })
        ) { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getInt("doctorId") ?: 0
            val doctor = doctorsList.find { it.id == doctorId }
            if (doctor != null) ScheduleAppointmentScreen(navController, doctor)
        }
        composable(
            route = Screen.Confirmation.route,
            arguments = listOf(
                navArgument("doctorId") { type = NavType.IntType; defaultValue = 0 },
                navArgument("fecha") { type = NavType.StringType },
                navArgument("hora") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getInt("doctorId") ?: 0
            val fecha = backStackEntry.arguments?.getString("fecha") ?: ""
            val hora = backStackEntry.arguments?.getString("hora") ?: ""
            val doctor = doctorsList.find { it.id == doctorId }
            if (doctor != null) ConfirmationScreen(navController, doctor, fecha, hora)
        }
        composable(Screen.MyAppointments.route) {
            MyAppointmentsScreen()
        }
        composable(Screen.MedicalHistory.route) {
            MedicalHistoryScreen()
        }
    }
}