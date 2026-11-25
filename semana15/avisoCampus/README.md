# AvisosCampus

App Android desarrollada con Kotlin, Jetpack Compose y Material 3, bajo el patrón MVVM + Repository. Muestra notificaciones locales, programa un recordatorio que llega a los 10 segundos y escucha el modo avión del celular con un `BroadcastReceiver`. Todo lo que ocurre queda registrado en un historial.

## Estructura de paquetes

```
com.faustino.avisoscampus/
├── AvisosApp.kt                  Application: crea el canal "Avisos"
├── MainActivity.kt
├── model/
│   └── Modelos.kt                Aviso, Origen, PermisoUi
├── data/
│   └── AvisosRepository.kt       interface + AvisosRepositoryMemoria
├── notificacion/
│   └── Notificador.kt            canal, permiso y mostrar notificación
├── receiver/
│   └── ModoAvionReceiver.kt      escucha el modo avión
├── viewmodel/
│   ├── AvisosViewModel.kt
│   └── AvisosViewModelFactory.kt
└── ui/
    ├── PantallaAvisos.kt
    ├── HistorialAvisos.kt
    └── theme/
        ├── Color.kt
        ├── Theme.kt
        └── Type.kt
```

## Arquitectura MVVM + Repository

```
View (PantallaAvisos)
   |  eventos: botones, switch
   v
ViewModel (AvisosViewModel) --- StateFlow (avisos, permiso, faltan, escuchandoAvion, mensaje) ---> View
   |  registra el aviso                    |  emite: eventos (SharedFlow<Aviso>)
   v                                       v
AvisosRepository (interface)         View recibe el evento y llama a
   |                                 Notificador.mostrar(context, aviso)
   v
AvisosRepositoryMemoria
```

El ViewModel decide qué se avisa y lo anota en el Repository. Después emite un evento; la pantalla, que sí tiene `Context`, lo recibe y llama al `Notificador`. Así el ViewModel nunca toca el sistema de notificaciones.

## Capturas

**Notificación en el panel**

<img width="171" height="383" alt="Captura de pantalla 2026-11-25 a las 5 02 41 p  m" src="https://github.com/user-attachments/assets/16ac752d-5c8c-4cf4-992c-423be2d1241e" />


**Recordatorio con cuenta regresiva**

<img width="173" height="381" alt="Captura de pantalla 2026-11-25 a las 5 03 55 p  m" src="https://github.com/user-attachments/assets/84706238-8d76-45c9-bfbd-8b1af8c46918" />

<img width="170" height="367" alt="Captura de pantalla 2026-11-25 a las 5 04 23 p  m" src="https://github.com/user-attachments/assets/6349900f-fb34-4e58-b7d2-d34cba939553" />




**Aviso del modo avión**

<img width="176" height="354" alt="Captura de pantalla 2026-11-25 a las 5 04 33 p  m" src="https://github.com/user-attachments/assets/d052f349-41ff-4c7a-a60c-0b29ac3ff6dc" />

<img width="169" height="359" alt="Captura de pantalla 2026-11-25 a las 5 04 44 p  m" src="https://github.com/user-attachments/assets/2f9bd5d8-fc91-4ed3-90d4-ae3f34b012d7" />

**Historial de avisos**

<img width="162" height="344" alt="Captura de pantalla 2026-11-25 a las 5 05 08 p  m" src="https://github.com/user-attachments/assets/8f28cc99-6db9-47d6-841e-9a37d83b77ca" />


**Permiso denegado**

<img width="159" height="355" alt="Captura de pantalla 2026-11-25 a las 5 05 18 p  m" src="https://github.com/user-attachments/assets/a2a4c222-894b-4321-8258-33c59dd08336" />


## Para pensar

**1. ¿Por qué una notificación necesita un canal y un permiso? ¿Qué pasa si falta cada uno?**

Desde Android 8 toda notificación pertenece a un canal, que el usuario puede configurar (importancia, sonido). Si falta el canal, la notificación no se muestra en Android 8 o superior. Desde Android 13 también se necesita el permiso `POST_NOTIFICATIONS`: sin él, el sistema bloquea las notificaciones y la app debe manejarlo sin cerrarse.

**2. ¿Por qué el ViewModel emite un evento y no muestra la notificación él mismo?**

Porque mostrar una notificación requiere `Context` y el sistema de notificaciones, y el ViewModel no debe conocerlos. Así queda desacoplado de Android, se puede probar sin emulador y no filtra el `Context` de una Activity.

**3. ¿Qué pasa si usas el mismo id en dos notificaciones?**

La segunda reemplaza a la primera en lugar de aparecer como otra. Por eso se usa el id del aviso: cada uno es distinto y se muestran dos notificaciones.

**4. ¿Por qué `onReceive` debe ser corto y por qué hay que quitar el receptor al salir?**

`onReceive` se ejecuta en el hilo principal y tiene un tiempo límite: un trabajo largo congela la app. Si no se llama a `unregisterReceiver` (en `onDispose`), el receptor sigue registrado, se filtra memoria y puede ejecutarse cuando la pantalla ya no existe.

**5. ¿En qué casos usarías una notificación local y en cuáles una push de Firebase?**

Local: cuando la propia app sabe el momento, como un recordatorio programado o un evento del dispositivo (modo avión). Push de Firebase: cuando el aviso nace en un servidor o depende de otro usuario (mensajes, ofertas, cambios en un pedido) y la app puede estar cerrada.
