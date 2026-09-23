# Clínica Salud+

## Requerimientos Funcionales

**RF01 — Búsqueda y selección de médico**
El sistema permite al paciente filtrar médicos por especialidad mediante chips (`LazyRow`, categorías: Cardiología, Pediatría, Dermatología), mostrando únicamente los médicos cuya categoría coincide exactamente con la seleccionada, y visualizar la lista completa de médicos disponibles (`LazyColumn`), cada uno con nombre, especialidad y calificación. Al seleccionar un médico, se muestra su perfil completo —con años de experiencia y descripción— recibiendo los datos por parámetro de navegación (`doctorId`).

**RF02 — Agendamiento de citas**
El sistema permite al paciente seleccionar una fecha (3 opciones) y una hora (3 opciones) de forma única para su cita con el médico elegido, y muestra una pantalla de confirmación con el resumen de la cita agendada (médico, fecha, hora).

**RF03 — Navegación mediante menú lateral**
El sistema ofrece un menú lateral (drawer), accesible mediante un ícono ☰ en la barra superior, con 4 destinos de navegación: Inicio, Mis citas, Historial médico y Perfil.

**RF04 — Gestión de citas del paciente**
El sistema permite al paciente visualizar el listado de sus citas agendadas (`LazyColumn`), diferenciando visualmente su estado (Confirmada en verde / Completada en gris) mediante un `AssistChip`, y marcar una cita confirmada como completada.


## Fase 2 — Mejora con IA: cancelar cita (rama `mejora-ia(Actividad06)`)

Mejora funcional agregada mediante asistencia de IA, generada a partir de la rama `main` (commit 9), consistente en permitir al paciente cancelar una cita confirmada, con diálogo de confirmación y aviso visual.

### Commit 1 — Modelo de datos para cancelar cita

**Prompt usado:**
```
En mi app Jetpack Compose necesito agregar la funcionalidad de cancelar
una cita agendada. Por ahora SOLO quiero el cambio en el modelo de datos,
sin tocar todavía la UI.

Archivo: data/Appointment.kt

1. Agrega un campo "id: Int" a la data class Appointment.
2. En AppointmentsStore, actualiza addAppointment() para que genere el id
   automáticamente con un contador incremental.
3. Agrega una función cancelAppointment(id: Int) que elimine la cita con
   ese id de la lista appointments.

No modifiques ningún otro archivo. No agregues UI.
```

**Resultado:** se agregó el campo `id: Int` a `Appointment`, un contador `nextId` en `AppointmentsStore`, y la función `cancelAppointment(id: Int)`. Se ajustó `ConfirmationScreen.kt` para adaptarse a la nueva firma de `addAppointment()`.

### Commit 2 — UI de cancelación con confirmación

**Prompt usado:**
```
Continuando con la funcionalidad de cancelar cita. Ya existe
AppointmentsStore.cancelAppointment(id: Int).

Archivo: screens/MyAppointmentsScreen.kt

1. En cada AppointmentCard, agrega un botón/ícono "Cancelar", visible solo
   si el estado es "Confirmada" (no en "Completada").
2. Al tocar "Cancelar", muestra un AlertDialog de confirmación con título,
   mensaje personalizado con el nombre del médico, botón "Sí, cancelar"
   (rojo) que llama a cancelAppointment(appointment.id), y botón
   "No, mantener" que solo cierra el diálogo.
3. Mantén el mismo estilo visual (Card, RoundedCornerShape, AssistChip).
```

**Resultado:** se agregó el botón "Cancelar" (visible solo en citas Confirmadas) y un `AlertDialog` con las opciones "Sí, cancelar" / "No, mantener", controlado con un estado local `appointmentToCancel`.

### Commit 3 — Snackbar de confirmación

**Prompt usado:**
```
Agrega un SnackbarHost en MyAppointmentsScreen que muestre "Cita cancelada
correctamente" justo después de confirmar la cancelación en el AlertDialog.
Usa rememberCoroutineScope() y SnackbarHostState. No cambies nada más del
diseño existente.
```

**Resultado:** se envolvió la pantalla en un `Scaffold` con `snackbarHost`, y al confirmar la cancelación se dispara `snackbarHostState.showSnackbar("Cita cancelada correctamente")` mediante `rememberCoroutineScope()`.


## Capturas

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/851d282b-8959-42db-992b-20b551a6283e" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/304d57a3-c12f-42bd-80a8-ad70baaaa643" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/c52d67ec-2479-4ee8-aa0e-1366ff32504d" />




