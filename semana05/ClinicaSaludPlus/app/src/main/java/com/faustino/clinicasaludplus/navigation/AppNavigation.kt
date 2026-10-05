package com.faustino.clinicasaludplus.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.faustino.clinicasaludplus.data.doctorsList
import com.faustino.clinicasaludplus.screens.ConfirmationScreen
import com.faustino.clinicasaludplus.screens.DoctorProfileScreen
import com.faustino.clinicasaludplus.screens.HomeScreen
import com.faustino.clinicasaludplus.screens.MedicalHistoryScreen
import com.faustino.clinicasaludplus.screens.MyAppointmentsScreen
import com.faustino.clinicasaludplus.screens.ProfileScreen
import com.faustino.clinicasaludplus.screens.ScheduleAppointmentScreen
import kotlinx.coroutines.launch

private data class DrawerItem(
    val label: String,
    val route: String,
    val icon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val scope = rememberCoroutineScope()

    val drawerItems = listOf(
        DrawerItem("Inicio", Screen.Home.route, Icons.Default.Home),
        DrawerItem("Mis citas", Screen.MyAppointments.route, Icons.Default.EventNote),
        DrawerItem("Historial médico", Screen.MedicalHistory.route, Icons.Default.History),
        DrawerItem("Perfil", Screen.Profile.route, Icons.Default.Person)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Alexander Faustino",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Text(
                    text = "Paciente",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider()
                drawerItems.forEach { item ->
                    NavigationDrawerItem(
                        label = { Text(item.label) },
                        icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                        selected = currentRoute == item.route,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(item.route) {
                                popUpTo(Screen.Home.route)
                                launchSingleTop = true
                            }
                        },
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Clínica Salud+") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = "Menú")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
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
                    composable(Screen.Profile.route) {
                        ProfileScreen()
                    }
                }
            }
        }
    }
}