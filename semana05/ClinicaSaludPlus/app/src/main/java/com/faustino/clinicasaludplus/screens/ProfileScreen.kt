package com.faustino.clinicasaludplus.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

// Datos fijos del paciente (sin backend), siguiendo el mismo patrón que
// doctorsList en Doctor.kt — no requiere ViewModel ni estado mutable
// porque el perfil no cambia dentro de esta app.
private data class ProfileField(val icon: ImageVector, val label: String, val value: String)

private val profileFields = listOf(
    ProfileField(Icons.Default.Email, "Correo electrónico", "alexander.faustino@tecsup.edu.pe"),
    ProfileField(Icons.Default.Phone, "Teléfono", "+51 906 259 697"),
    ProfileField(Icons.Default.Cake, "Fecha de nacimiento", "17/08/2006 (20 años)"),
    ProfileField(Icons.Default.Bloodtype, "Tipo de sangre", "O+"),
    ProfileField(Icons.Default.LocationOn, "Dirección", "Ate, Lima")
)
@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text("AF", style = MaterialTheme.typography.titleMedium)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text("Alexander Faustino", style = MaterialTheme.typography.titleLarge)
        Text(
            "Paciente",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                profileFields.forEachIndexed { index, field ->
                    ProfileRow(field)
                    if (index != profileFields.lastIndex) {
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    }
                }
            }
        }
    }
}
@Composable
private fun ProfileRow(field: ProfileField) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = field.icon,
            contentDescription = field.label,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = field.label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = field.value,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}