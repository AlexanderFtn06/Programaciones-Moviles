# Actividad — TECSUP Fit

**Autor:** Alexander Faustino Quispe

## Requerimientos Funcionales

RF-01: Listar y filtrar las clases disponibles (HomeScreen)
El sistema muestra las clases en una lista (LazyColumn) y permite filtrarlas por "Hoy" o "Esta semana" con chips (LazyRow con FilterChip). Cada tarjeta muestra el nombre y el horario de la clase.

RF-02: Consultar el detalle de una clase y elegir un turno (DetalleClaseScreen)
Al tocar una clase, el sistema recibe su claseId por parámetro de navegación y muestra instructor, cupos y descripción de la clase selecioanda.  

RF-03: Reservar un cupo y ver la confirmación (ReservasRepository.agregarReserva y ConfirmacionScreen)
Con el botón "Reservar cupo", el sistema registra la reserva con estado "Confirmada" y muestra un resumen (clase y horario con turno). Desde ahí se puede ir a "Ver mis reservas".

RF-04: Consultar mis reservas y mis estadísticas (ReservasScreen y PerfilScreen)
El sistema lista las reservas del usuario con su estado y un button para cambiarlo (Confirmada o Completada) diferenciado por color. El perfil muestra los datos del usuario y estadísticas calculadas desde esas reservas: clases tomadas, reservas activas y racha de asistencia.

## Capturas

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/d5148078-163e-4bca-9bba-c1ca74166cd2" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/f2309dfd-f5ea-4980-8348-f69c9d92a368" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/0516ee99-38b2-4120-af52-b5d282a2f3a6" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/b41194de-c381-4596-97a9-305577cb9934" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/0630f823-86b1-4a54-86e0-ddb732d546e7" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/9e6ebec8-7f83-4f87-b765-75d36283edf8" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/78a7d553-3dc7-45f5-8b65-72a8c0d4669f" />
