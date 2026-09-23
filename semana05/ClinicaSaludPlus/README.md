# Actividad — Clinica Salud+

**Autor:** Alexander Faustino Quispe

## Requerimientos Funcionales

**RF01 — Búsqueda y selección de médico**
El sistema permite al paciente filtrar médicos por especialidad mediante chips (`LazyRow`, categorías: Cardiología, Pediatría, Dermatología), mostrando únicamente los médicos cuya categoría coincide exactamente con la seleccionada, y visualizar la lista completa de médicos disponibles (`LazyColumn`), cada uno con nombre, especialidad y calificación. Al seleccionar un médico, se muestra su perfil completo —con años de experiencia y descripción— recibiendo los datos por parámetro de navegación (`doctorId`).

**RF02 — Agendamiento de citas**
El sistema permite al paciente seleccionar una fecha (3 opciones) y una hora (3 opciones) de forma única para su cita con el médico elegido, y muestra una pantalla de confirmación con el resumen de la cita agendada (médico, fecha, hora).

**RF03 — Navegación mediante menú lateral**
El sistema ofrece un menú lateral (drawer), accesible mediante un ícono ☰ en la barra superior, con 4 destinos de navegación: Inicio, Mis citas, Historial médico y Perfil.

**RF04 — Gestión de citas del paciente**
El sistema permite al paciente visualizar el listado de sus citas agendadas (`LazyColumn`), diferenciando visualmente su estado (Confirmada en verde / Completada en gris) mediante un `AssistChip`, y marcar una cita confirmada como completada.
## Capturas

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/fde4e6fa-a134-426f-9974-f768613fb026" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/40eb4945-522b-4e08-b55d-61856487e74d" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/fb06ed3b-899b-4f16-8085-179bea15960e" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/73997b3f-1b18-4aa5-a205-383cb42bb694" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/4f394b70-debd-40ff-9c3d-a5f69decbd00" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/5849fa07-7ccf-4a24-8c27-ee47b57989d0" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/04986366-47ba-4bea-a0e5-82f427055bdc" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/87852517-8895-405d-bcf1-0394ceca0475" />

