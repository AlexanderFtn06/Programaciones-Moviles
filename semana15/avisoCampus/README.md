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

![Captura de pantalla 2026-11-25 a las 4.58.21 p. m..png](../../../../Captura%20de%20pantalla%202026-11-25%20a%20las%204.58.21%E2%80%AFp.%E2%80%AFm..png)

**Recordatorio con cuenta regresiva**

![Captura de pantalla 2026-11-25 a las 4.59.40 p. m..png](../../../../../../../var/folders/nq/3v5cbqjs067gf7m6f4z15f100000gn/T/TemporaryItems/NSIRD_screencaptureui_pQm7aY/Captura%20de%20pantalla%202026-11-25%20a%20las%204.59.40%E2%80%AFp.%E2%80%AFm..png)


**Aviso del modo avión**

![Captura de pantalla 2026-11-25 a las 5.00.02 p. m..png](../../../../../../../var/folders/nq/3v5cbqjs067gf7m6f4z15f100000gn/T/TemporaryItems/NSIRD_screencaptureui_eQZHUm/Captura%20de%20pantalla%202026-11-25%20a%20las%205.00.02%E2%80%AFp.%E2%80%AFm..png)

![Captura de pantalla 2026-11-25 a las 5.00.17 p. m..png](../../../../../../../var/folders/nq/3v5cbqjs067gf7m6f4z15f100000gn/T/TemporaryItems/NSIRD_screencaptureui_c2MlrQ/Captura%20de%20pantalla%202026-11-25%20a%20las%205.00.17%E2%80%AFp.%E2%80%AFm..png)

**Historial de avisos**

![Captura de pantalla 2026-11-25 a las 5.00.27 p. m..png](../../../../../../../var/folders/nq/3v5cbqjs067gf7m6f4z15f100000gn/T/TemporaryItems/NSIRD_screencaptureui_PMqksG/Captura%20de%20pantalla%202026-11-25%20a%20las%205.00.27%E2%80%AFp.%E2%80%AFm..png)

**Permiso denegado**

![Captura de pantalla 2026-11-25 a las 5.00.50 p. m..png](../../../../../../../var/folders/nq/3v5cbqjs067gf7m6f4z15f100000gn/T/TemporaryItems/NSIRD_screencaptureui_22IfBR/Captura%20de%20pantalla%202026-11-25%20a%20las%205.00.50%E2%80%AFp.%E2%80%AFm..png)


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