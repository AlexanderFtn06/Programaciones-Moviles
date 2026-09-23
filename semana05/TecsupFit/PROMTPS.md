# Actividad — TECSUP Fit

**Autor:** Alexander Faustino Quispe

## Requerimientos Funcionales

RF-01: Listar y filtrar las clases disponibles (HomeScreen)
El sistema muestra las clases en una lista (LazyColumn) y permite filtrarlas por "Hoy" o "Esta semana" con chips (LazyRow con FilterChip). Cada tarjeta muestra el nombre y el horario de la clase.

RF-02: Consultar el detalle de una clase y elegir un turno (DetalleClaseScreen)
Al tocar una clase, el sistema recibe su claseId por parámetro de navegación y muestra instructor, cupos y descripción. 

RF-03: Reservar un cupo y ver la confirmación (ReservasRepository.agregarReserva y ConfirmacionScreen)
Con el botón "Reservar cupo", el sistema registra la reserva con estado "Confirmada" y muestra un resumen (clase y horario con turno). Desde ahí se puede ir a "Ver mis reservas".

RF-04: Consultar mis reservas y mis estadísticas (ReservasScreen y PerfilScreen)
El sistema lista las reservas del usuario con su estado y un boton para cambiarlo (Confirmada o Completada) diferenciado por color. El perfil muestra los datos del usuario y estadísticas calculadas desde esas reservas: clases tomadas, reservas activas y racha de asistencia.

RF-05: Elimina una reversa mediante un button en la pestaña de Mis reservas y se restablece la cantidad de cupos
 
# PROMPTS.md — Fase 2 (mejora-ia)

Prompt 1 — Modelo y repositorio

Prompt usado: "Actua como desarrollador de aplicaciones moviles ya que tengo una app de Compose para reservas de gimnasio. ReservaModel solo guarda el nombre de la clase como texto, y necesito poder devolver el cupo a la clase original cuando se cancele una reserva. Ayúdame a modificar el modelo y el repositorio para poder identificar a qué clase pertenece cada reserva, y agrega una función para cancelar una reserva que elimine el registro y devuelva el cupo."

Qué generó la IA:

Sugirió agregar el campo claseId a ReservaModel.
Generó la función cancelarReserva(id) en ReservasRepository, que elimina la reserva de la lista y le suma +1 a cuposDisponibles de la clase correspondiente.

Prompt 2 — UI del botón Cancelar y AlertDialog

Prompt usado: "Ahora en ReservasScreen quiero agregar un botón 'Cancelar' junto al de 'Marcar como completada', solo visible en reservas confirmadas. Al presionarlo debe mostrarse un AlertDialog preguntando si el usuario está seguro, y solo si confirma, se llama a cancelarReserva()."

Qué generó la IA:

Un botón "Cancelar" con ícono, y un AlertDialog controlado por una variable de estado (reservaACancelar) que guarda la reserva seleccionada.
Los botones de confirmar/rechazar el diálogo.

Prompt 3 — Arreglar el button de cancelar
Prompt usado: "Ahora arregla el diseño del button para cancelar la reserva que se ve mal diseñado ya que ocupa un espacio que no le pertenece"

Qué generó la IA:

Arreglo el button correctamente dejando un espacio adecuado para que este bien estructurado el diseño de Reservas

# Capturas
Antes de la mejora del ultimo prompt:
<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/287a788f-17b4-4953-a4cd-3baabaa585ca" />

Despues de la mejora del ultimo prompt:

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/b59b9d3d-c04c-4409-abb0-e159afb63892" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/6153fe9d-429b-467d-b1a8-6c75858d77ac" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/111aa230-e059-477a-9a84-25ace5cbca1d" />


