# Prompts utilizados

**Proyecto:** AyudaCampus · Laboratorio 08 · Fase 2 (con IA)
**Alumno:** Alexander Faustino
**Rama:** `mejora-ia-AyudaCampus`
**Herramienta:** Gemini en Android Studio (modelo Gemini 3.5 Flash Lite)

**Método de trabajo:** cada prompt incluyó un bloque de contexto con la arquitectura (MVVM + Repository, Room, StateFlow) y las reglas del lab, y una sección "Forma de trabajo" que obligaba a Gemini a mostrar primero un plan y el código propuesto, sin modificar archivos, hasta recibir mi "Confirmo". Revisé cada propuesta contra MVVM, pedí correcciones cuando hizo falta, apliqué los cambios, compilé, probé en el teléfono e hice un commit por mejora.

| N° | Mejora | Commit |
|---|---|---|
| 1 | Tiempo promedio de resolución | `ia: agrega tiempo promedio de resolucion al panel` |
| 2 | Contador de SLA por prioridad | `ia: agrega contador de SLA por prioridad` |
| 3 | Búsqueda avanzada en la bandeja | `ia: agrega busqueda avanzada en la bandeja` |
| 4 | Registro de estudiante | `ia: agrega registro de estudiante` |
| 5 | Comentarios internos | `ia: agrega comentarios internos de soporte` |
| 6 | Animaciones y estados vacíos | `ia: agrega animaciones y estados vacios` |
| 7 | Recuperar contraseña (extra) | `ia: agrega recuperacion de contrasena` |

---

## Prompt 1 · Tiempo promedio de resolución

**Texto enviado a Gemini:**

```text
Contexto: app Android en Kotlin con Jetpack Compose y Material 3 (sin XML). Paquete com.faustino.ayudacampus. Arquitectura MVVM + Repository:
- La View (paquete ui/) solo observa StateFlow con collectAsState y avisa eventos. No usa DAO ni Repository.
- Los ViewModels (paquete viewmodel/) tienen las reglas de negocio y solo conocen la interface AyudaRepository.
- AyudaRepositoryRoom implementa AyudaRepository usando los DAOs de Room 2.6.1.
- Lecturas que cambian en el tiempo devuelven Flow (sin suspend); escrituras y acciones únicas son suspend.
- Los colores salen de MaterialTheme.colorScheme o de ui/theme/Color.kt, nunca valores fijos.
- En la tabla tickets, "fecha" es la creación y "fechaCierre" el cierre (Long en milisegundos; fechaCierre es null si no está cerrado). El estado se guarda como String: ABIERTO, EN_PROCESO, RESUELTO o CERRADO.
Reglas obligatorias: si agregas una función al Repository, declárala PRIMERO en la interface AyudaRepository y luego impleméntala en AyudaRepositoryRoom.

Pedido: quiero mostrar en el panel de Soporte el tiempo promedio de resolución de los tickets CERRADOS (fechaCierre - fecha).

1. En TicketDao agrega una consulta con AVG(fechaCierre - fecha), solo para estado = 'CERRADO' y fechaCierre IS NOT NULL, que devuelva Flow<Long?> (null si no hay cerrados).
2. Declara primero en AyudaRepository: fun observarTiempoPromedioResolucion(): Flow<Long?>, y luego impleméntala en AyudaRepositoryRoom solo delegando en el DAO.
3. En PanelViewModel exponla como StateFlow<String> con stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Sin datos"), ya formateada: "1 d 3 h", "2 h 15 min", "45 min" o "Sin datos".
4. En PanelScreen muéstrala debajo de las tres tarjetas de contadores, en una tarjeta blanca con el mismo estilo de las existentes: icono de reloj, el texto "Tiempo promedio de resolución" y el valor en grande con MaterialTheme.colorScheme.primary.

Forma de trabajo (muy importante):
- NO modifiques ningún archivo todavía.
- Primero muéstrame un plan: una lista de los archivos que vas a cambiar y qué harás en cada uno.
- Después muéstrame el código propuesto de cada archivo (el fragmento exacto que agregas o reemplazas) y explica cada cambio.
- Indica cómo cumple cada regla de MVVM del contexto.
- Espera a que yo responda "Confirmo" antes de aplicar los cambios. Si te pido correcciones, muéstrame de nuevo la propuesta corregida y vuelve a esperar mi confirmación.
- No ejecutes comandos de Git ni hagas commits.
```

**Qué generó la IA:** Un plan con 5 archivos: la consulta `AVG` en `TicketDao` con `Flow<Long?>`, la declaración en `AyudaRepository`, la implementación en `AyudaRepositoryRoom`, un `StateFlow<String>` formateado en `PanelViewModel` y una tarjeta con icono de reloj en `PanelScreen`.

**Qué tuve que corregir yo:** En el primer intento envié solo el bloque de contexto, sin el pedido, y en modo Agente: Gemini empezó a explorar, compilar y ejecutar `git status` por su cuenta. Detuve la respuesta, abrí un chat nuevo y agregué la sección "Forma de trabajo" para que mostrara primero un plan y esperara mi confirmación. Al revisar ese plan antes de aplicarlo detecté tres problemas: faltaba el import de `map` en `PanelViewModel` (no habría compilado), se mostraría "0 min" con promedios menores a un minuto y `AVG` devuelve un decimal en SQLite aunque la función retorna `Long?`. Le indiqué a Gemini las tres correcciones (Prompt 1b) y verifiqué la versión corregida antes de confirmar.

**¿Respeta MVVM?:** Sí. La consulta está en el DAO, la función se declaró primero en la interface, el ViewModel solo conoce `AyudaRepository` y se encarga del formato, y la pantalla solo observa el `StateFlow` con colores del tema.

### Prompt 1b · Corrección

```text
Antes de aplicar, corrige estos puntos:
1. En PanelViewModel usas .map pero no incluiste el import kotlinx.coroutines.flow.map. Agrégalo y lista todos los imports nuevos de cada archivo.
2. En formatearTiempo: si millis es null devuelve "Sin datos"; si es menor a 1 minuto devuelve "Menos de 1 min" (no "0 min").
3. En la consulta de TicketDao usa CAST(AVG(fechaCierre - fecha) AS INTEGER), porque AVG en SQLite devuelve un decimal y la función retorna Long?.
Muéstrame de nuevo solo los fragmentos corregidos y espera mi "Confirmo".
```

---

## Prompt 2 · Contador de SLA por prioridad

**Texto enviado a Gemini:**

```text
Contexto: app Android en Kotlin con Jetpack Compose y Material 3 (sin XML). Paquete com.faustino.ayudacampus. Arquitectura MVVM + Repository:
- La View (paquete ui/) solo observa StateFlow con collectAsState y avisa eventos. No usa DAO ni Repository.
- Los ViewModels (paquete viewmodel/) tienen las reglas de negocio y solo conocen la interface AyudaRepository.
- Lecturas que cambian en el tiempo devuelven Flow (sin suspend); escrituras y acciones únicas son suspend.
- Los colores salen de MaterialTheme.colorScheme o de ui/theme/Color.kt (ColorAlta, ColorAbierto), nunca valores fijos.
- Ticket tiene: id, prioridad (String con Prioridad.name: BAJA, MEDIA, ALTA), estado (String con EstadoTicket.name: ABIERTO, EN_PROCESO, RESUELTO, CERRADO) y fecha (Long, creación en milisegundos). TicketConDetalle contiene ticket, comentarios, categoria y cliente.
- TicketCard (ui/components) es la tarjeta que usan MisTicketsScreen (estudiante) y BandejaScreen (soporte).
Reglas obligatorias: no cambies las entidades, los DAOs ni el Repository; esta mejora solo toca ViewModel y View.

Pedido: quiero que cada ticket muestre el tiempo que le queda según su prioridad (SLA): ALTA 4 horas, MEDIA 24 horas y BAJA 72 horas, contadas desde ticket.fecha. Solo aplica a tickets ABIERTO o EN_PROCESO; en RESUELTO y CERRADO no se muestra.

1. Crea viewmodel/Sla.kt con:
   - las horas por prioridad definidas en un solo lugar (por ejemplo, una función horasSla(prioridad: Prioridad): Int),
   - data class EstadoSla(val texto: String, val vencido: Boolean, val porVencer: Boolean),
   - una función pura calcularSla(ticket: Ticket, ahora: Long): EstadoSla? que devuelva null si el ticket no está ABIERTO ni EN_PROCESO, y textos como "Quedan 3 h 20 min", "Quedan 25 min" o "Vencido hace 1 h 5 min". porVencer es true cuando queda menos del 25 % del plazo y no está vencido.
2. Haz que NuevoTicketViewModel.tiempoRespuesta use horasSla en lugar de tener los números 4, 24 y 72 repetidos.
3. En MisTicketsViewModel y BandejaViewModel crea un flujo "reloj" que emita System.currentTimeMillis() de inmediato y luego cada 60 segundos. Combínalo con la lista de tickets que ya se muestra y expón un StateFlow<Map<Int, EstadoSla>> llamado slas (id del ticket -> SLA) con stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap()), para que el contador se actualice solo.
4. TicketCard recibe un parámetro opcional sla: EstadoSla? = null. Si no es null, muestra debajo de la fila de chips una línea pequeña (labelSmall) con un icono de reloj y el texto: ColorAlta si está vencido, ColorAbierto si está por vencer y MaterialTheme.colorScheme.onSurfaceVariant en otro caso.
5. MisTicketsScreen y BandejaScreen observan slas con collectAsState y pasan a cada TicketCard el SLA de su ticket (slas[ticket.id]).

[Forma de trabajo: la misma del Prompt 1, pidiendo además todos los imports nuevos de cada archivo.]
```

**Qué generó la IA:** Un archivo nuevo `viewmodel/Sla.kt` con las horas por prioridad, `EstadoSla` y la función pura `calcularSla`; un flujo "reloj" que emite cada 60 segundos en `MisTicketsViewModel` y `BandejaViewModel`, combinado con los tickets para exponer un `StateFlow<Map<Int, EstadoSla>>`; y una línea con icono de reloj en `TicketCard`. `NuevoTicketViewModel` pasó a usar `horasSla`.

**Qué tuve que corregir yo:** Al revisar la propuesta encontré código que no se usaba en `Sla.kt` (una variable y una función que no hacían nada), la misma lógica de formato repetida para "Quedan…" y "Vencido hace…", el caso "Quedan 0 min", una consulta a Room duplicada en `BandejaViewModel` y el cálculo del color repetido en `TicketCard`. Le indiqué a Gemini cómo resolver cada punto (Prompt 2b), revisé el archivo `Sla.kt` completo corregido y luego probé en el teléfono tickets vencidos, por vencer, cerrados y recién creados.

**¿Respeta MVVM?:** Sí. No se tocaron entidades, DAOs ni Repository; el cálculo del SLA y el reloj viven en los ViewModels, y las pantallas solo observan `slas` y pasan el valor a `TicketCard`, con colores del tema.

### Prompt 2b · Corrección

```text
Antes de aplicar, corrige estos puntos:
1. En Sla.kt elimina el código que no se usa: la variable "horas" con el comentario "simplificado abajo" y la función privada minutosTotales.
2. En Sla.kt crea una función privada formatearDuracion(minutos: Long): String que devuelva "3 d 2 h", "5 h 20 min", "25 min" o "menos de 1 min", y úsala en los dos casos para no repetir la lógica: "Quedan ..." y "Vencido hace ...".
3. En BandejaViewModel guarda repo.observarTodosLosTickets() en una sola propiedad privada (por ejemplo, private val todos) y úsala tanto en tickets como en slas, para no hacer dos consultas a Room.
4. En TicketCard calcula el color del SLA una sola vez en una variable y úsala en el icono y en el texto.
Muéstrame de nuevo el archivo Sla.kt completo y los fragmentos corregidos de BandejaViewModel y TicketCard, y espera mi "Confirmo".
```

---

## Prompt 3 · Búsqueda avanzada en la bandeja

**Texto enviado a Gemini:**

```text
Contexto: app Android en Kotlin con Jetpack Compose y Material 3 (sin XML). Paquete com.faustino.ayudacampus. Arquitectura MVVM + Repository:
- La View (paquete ui/) solo observa StateFlow con collectAsState y avisa eventos. No usa DAO ni Repository.
- Los ViewModels (paquete viewmodel/) tienen las reglas de negocio y solo conocen la interface AyudaRepository.
- Lecturas que cambian en el tiempo devuelven Flow (sin suspend); escrituras y acciones únicas son suspend.
- Los colores salen de MaterialTheme.colorScheme o de ui/theme/Color.kt (colorPrioridad(p), ColorAlta), nunca valores fijos.
- TicketConDetalle contiene ticket (id, titulo, prioridad como String con Prioridad.name, estado, categoriaId), comentarios, categoria y cliente (Usuario con nombre).
- FiltroEstado (Todos, Abiertos, En proceso, Resueltos) está definido en MisTicketsViewModel.kt.
- BandejaViewModel ya tiene: la propiedad privada "todos" con repo.observarTodosLosTickets(), los filtros de estado y "soloAlta" (llega desde la alerta del panel), el StateFlow "tickets" y el contador de SLA ("reloj" y "slas"). El SLA debe seguir funcionando igual.
Reglas obligatorias: no cambies entidades, DAOs ni el Repository (observarCategorias() ya existe en AyudaRepository); esta mejora solo toca BandejaViewModel y BandejaScreen.

Pedido: en la bandeja de Soporte quiero filtrar por estado, prioridad, categoría y texto al mismo tiempo.

1. En BandejaViewModel agrupa todos los filtros en data class FiltrosBandeja(val estado: FiltroEstado = FiltroEstado.TODOS, val prioridad: Prioridad? = null, val categoriaId: Int? = null, val texto: String = "", val soloAlta: Boolean = false), guardada en un solo MutableStateFlow y expuesta como StateFlow<FiltrosBandeja> llamado filtros. El valor inicial de soloAlta y estado se toma de soloAltaInicial, como ahora.
2. El StateFlow tickets combina "todos" con filtros y aplica todos los filtros a la vez, conservando el orden que ya entrega el DAO (ALTA primero y luego por fecha). El texto busca en el título, en el número del ticket (acepta "7", "0007" o "#0007") y en el nombre del cliente, sin distinguir mayúsculas.
3. Funciones: seleccionarFiltro(f: FiltroEstado), elegirPrioridad(p: Prioridad?) (si se toca la misma prioridad otra vez, se quita), elegirCategoria(id: Int?), buscar(texto: String), quitarSoloAlta() y limpiarFiltros().
4. Expón las categorías con repo.observarCategorias() como StateFlow<List<Categoria>> y un StateFlow<Boolean> hayFiltrosActivos (true si algo es distinto del valor por defecto).
5. En BandejaScreen, debajo de la barra superior:
   - un buscador redondeado con icono de lupa y placeholder "Buscar por título, número o estudiante…", con el mismo estilo del buscador de MisTicketsScreen;
   - los chips de estado actuales y el chip "Prioridad alta" que ya existe;
   - una fila de FilterChip Baja / Media / Alta; el seleccionado se rellena con colorPrioridad(p) y texto blanco;
   - un selector de categoría con ExposedDropdownMenuBox, con la opción "Todas las categorías" y luego cada categoría;
   - un TextButton "Limpiar filtros" visible solo si hayFiltrosActivos es true.
6. Si no hay resultados, muestra "No hay tickets con estos filtros".

[Forma de trabajo: la misma del Prompt 1, pidiendo además el código completo de BandejaViewModel.kt y BandejaScreen.kt con todos sus imports.]
```

**Qué generó la IA:** Una `data class FiltrosBandeja` en un único `StateFlow`, el filtrado combinado en `BandejaViewModel` (conservando el orden del DAO y el SLA), y en `BandejaScreen` un buscador, chips de prioridad con su color, un selector de categoría con `ExposedDropdownMenuBox`, el botón "Limpiar filtros" y el mensaje de lista vacía.

**Qué tuve que corregir yo:** En la revisión detecté que `hayFiltrosActivos` usaba `combine` con un solo flujo y desestructuraba un arreglo, cuando bastaba con `map` comparando contra `FiltrosBandeja()`, y que los chips de prioridad armaban el texto a mano en vez de usar `p.etiqueta`, como el resto de la app. Le indiqué ambas correcciones (Prompt 3b). Después, al probar en el teléfono, noté que el texto de ayuda del buscador se partía en dos líneas y le pedí acortarlo y limitarlo a una línea (Prompt 3c). También comprobé que la falta de alerta en el panel no era un error: ningún ticket ALTA llevaba más de una hora abierto.

**¿Respeta MVVM?:** Sí. Solo cambiaron `BandejaViewModel` y `BandejaScreen`; el ViewModel tiene toda la lógica de filtros y usa `observarCategorias()` de la interface; la pantalla solo observa y avisa eventos, con colores del tema.

### Prompt 3b · Corrección

```text
Antes de aplicar, corrige estos puntos:
1. En BandejaViewModel, hayFiltrosActivos no debe usar combine con un solo flujo ni desestructurar un arreglo. Usa _filtros.map { it != FiltrosBandeja() } con stateIn, y agrega el import de kotlinx.coroutines.flow.map.
2. En BandejaScreen, el texto de los chips de prioridad debe usar p.etiqueta en lugar de p.name.lowercase().replaceFirstChar { ... }.
Muéstrame de nuevo solo los fragmentos corregidos y espera mi "Confirmo".
```

### Prompt 3c · Ajuste visual tras probar

```text
El placeholder del buscador en BandejaScreen se parte en dos líneas y no se ve bien. Cámbialo a "Buscar ticket o estudiante…" y agrégale maxLines = 1 y overflow = TextOverflow.Ellipsis (con su import), para que siempre se vea en una sola línea. Muéstrame solo el fragmento corregido y espera mi "Confirmo".
```

---

## Prompt 4 · Registro de estudiante

**Texto enviado a Gemini:**

```text
Contexto: app Android en Kotlin con Jetpack Compose y Material 3 (sin XML). Paquete com.faustino.ayudacampus. Arquitectura MVVM + Repository:
- La View (paquete ui/) solo observa StateFlow con collectAsState y avisa eventos. No usa DAO ni Repository.
- Los ViewModels (paquete viewmodel/) tienen las reglas de negocio y solo conocen la interface AyudaRepository.
- AyudaRepositoryRoom implementa AyudaRepository usando los DAOs de Room 2.6.1.
- Lecturas que cambian en el tiempo devuelven Flow (sin suspend); escrituras y acciones únicas son suspend.
- Los colores salen de MaterialTheme.colorScheme, nunca valores fijos.
- Usuario tiene: id (autogenerado), nombre, correo, clave y rol (String con Rol.name: CLIENTE o ADMIN).
- SesionViewModel maneja la sesión y guarda el perfil en DataStore con prefs.guardarSesion(rol, usuarioId).
- Los ViewModels se crean en ui/navigation/NavAyuda.kt con ayudaViewModel { ... }. Las rutas están en el objeto Rutas.
- Componentes existentes: AyudaTextField(valor, onCambio, etiqueta, modifier, error, icono, esClave, lineas, teclado) y AyudaButton(texto, onClick, modifier, habilitado, secundario, icono).
Reglas obligatorias: no cambies las entidades ni la versión de la base de datos. Si agregas una función al Repository, declárala PRIMERO en la interface AyudaRepository y luego impleméntala en AyudaRepositoryRoom.

Pedido: quiero una pantalla para que un estudiante cree su cuenta.

Reglas de negocio (van en el ViewModel, con el error junto a cada campo):
- Nombre de al menos 3 caracteres.
- Correo con formato válido (regex simple: texto@texto.texto) y único: no puede existir otro usuario con ese correo, sin distinguir mayúsculas. Se guarda con trim() y en minúsculas.
- Contraseña de al menos 4 caracteres, y "Confirmar contraseña" debe ser igual.
- El rol siempre es Rol.CLIENTE.name.

1. UsuarioDao: agrega una consulta suspend que cuente usuarios con LOWER(correo) = LOWER(:correo), y un @Insert suspend fun insertar(u: Usuario): Long.
2. Declara primero en AyudaRepository: suspend fun correoRegistrado(correo: String): Boolean y suspend fun registrarEstudiante(nombre: String, correo: String, clave: String): Usuario. Luego impleméntalas en AyudaRepositoryRoom (registrarEstudiante inserta y devuelve el Usuario con el id generado). En login, normaliza el correo con trim().lowercase() antes de consultar.
3. Crea viewmodel/RegistroViewModel.kt: una data class FormularioRegistro con nombre, correo, clave, confirmarClave, sus errores por campo y "enviando"; funciones para cambiar cada campo (cada una limpia su propio error); registrar(), que valida y llama al repositorio; y un StateFlow<Usuario?> llamado registrado con el usuario creado.
4. En SesionViewModel agrega fun iniciarSesionCon(usuario: Usuario), que guarda la sesión con prefs.guardarSesion(Rol.CLIENTE, usuario.id) y actualiza usuarioActual.
5. Crea ui/cliente/RegistroScreen.kt con el mismo estilo del LoginScreen (fondo, márgenes, systemBarsPadding, imePadding y verticalScroll): título "Crear cuenta", subtítulo "Regístrate como estudiante", AyudaTextField para nombre (icono de persona), correo (icono de sobre, teclado de correo), contraseña y confirmar contraseña (icono de candado, esClave = true), AyudaButton "Crear cuenta" (texto "Creando…" mientras envía) y debajo un enlace "¿Ya tienes cuenta? Inicia sesión". Cuando registrado deje de ser null, llama a onRegistrado(usuario).
6. Agrega const val REGISTRO = "registro" en Rutas.
7. En LoginScreen agrega el parámetro onRegistrarse: () -> Unit y úsalo en el enlace "Regístrate" (hoy su onClick está vacío).
8. En NavAyuda: agrega el destino REGISTRO creando RegistroViewModel con ayudaViewModel. En el Splash, "Comenzar" abre el registro y "Ya tengo una cuenta" sigue abriendo el Login. El enlace "Regístrate" del Login abre el registro, y "Inicia sesión" del registro vuelve al Login. Al registrarse, llama a sesionVm.iniciarSesionCon(usuario) y navega a Rutas.MIS_TICKETS limpiando la pila con popUpTo(nav.graph.id) { inclusive = true }.

[Forma de trabajo: la misma del Prompt 1, pidiendo además el código completo de los archivos nuevos y todos los imports nuevos.]
```

**Qué generó la IA:** Las consultas en `UsuarioDao`, las funciones `correoRegistrado` y `registrarEstudiante` (declaradas primero en la interface), el `RegistroViewModel` con todas las validaciones y el correo único, `iniciarSesionCon` en `SesionViewModel`, la pantalla `RegistroScreen`, la ruta `REGISTRO` y la navegación desde el Splash y el Login.

**Qué tuve que corregir yo:** Fue la propuesta que más revisión necesitó. Detecté que `RegistroScreen` se envolvía en `AyudaTheme(oscuro = false)` e ignoraba el modo oscuro guardado; que "Inicia sesión" con `popBackStack()` dejaba la pantalla en blanco si el registro se abría desde el Splash, porque no había nada detrás en la pila; que `iniciarSesionCon` guardaba en DataStore antes de actualizar el usuario, dejando Mis tickets vacío un instante; y que faltaban imports en `NavAyuda`. Le indiqué las correcciones y le pedí confirmar que reemplazaría el `login` existente en vez de duplicarlo (Prompt 4b). Luego probé el flujo completo desde el Splash y desde el Login.

**¿Respeta MVVM?:** Sí. Las validaciones y la regla del correo único están en `RegistroViewModel`, que solo conoce la interface; el Repository declaró primero las funciones nuevas; la pantalla solo observa el formulario y avisa eventos.

### Prompt 4b · Corrección

```text
Antes de aplicar, corrige estos puntos:
1. RegistroScreen: quita el envoltorio AyudaTheme(rol = Rol.CLIENTE, oscuro = false), porque ignora el modo oscuro guardado; NavAyuda ya aplica el tema del estudiante con el modo oscuro. Quita también los imports que dejen de usarse (AyudaTheme y Rol).
2. SesionViewModel.iniciarSesionCon: actualiza primero _usuarioActual.value = usuario y después guarda la sesión en DataStore dentro de viewModelScope.launch, para que Mis tickets tenga el usuario apenas se navega.
3. NavAyuda, onIrLogin del registro: si el registro se abrió desde el Splash, popBackStack() deja la pila vacía y la pantalla en blanco. Usa: if (!nav.popBackStack(Rutas.LOGIN, inclusive = false)) { nav.navigate(Rutas.LOGIN) { popUpTo(Rutas.REGISTRO) { inclusive = true } } }
4. NavAyuda: agrega los imports de RegistroScreen y RegistroViewModel.
5. Confirma que en AyudaRepositoryRoom vas a reemplazar la función login existente y no a agregar una segunda.
Muéstrame de nuevo solo los fragmentos corregidos (RegistroScreen completo, iniciarSesionCon y los bloques de NavAyuda con sus imports) y espera mi "Confirmo".
```

---

## Prompt 5 · Comentarios internos de Soporte

**Texto enviado a Gemini:**

```text
Contexto: app Android en Kotlin con Jetpack Compose y Material 3 (sin XML). Paquete com.faustino.ayudacampus. Arquitectura MVVM + Repository:
- La View (paquete ui/) solo observa StateFlow con collectAsState y avisa eventos. No usa DAO ni Repository.
- Los ViewModels (paquete viewmodel/) tienen las reglas de negocio y solo conocen la interface AyudaRepository.
- AyudaRepositoryRoom implementa AyudaRepository usando los DAOs de Room 2.6.1. AyudaDatabase usa fallbackToDestructiveMigration().
- Lecturas que cambian en el tiempo devuelven Flow (sin suspend); escrituras y acciones únicas son suspend.
- Los colores salen de MaterialTheme.colorScheme o de ui/theme/Color.kt (ColorAbierto), nunca valores fijos.
- Comentario tiene: id, ticketId, autorId, autorNombre, autorRol, texto, fecha. TicketDetalleViewModel es la pantalla del estudiante; AtenderViewModel y AtenderTicketScreen son de Soporte. En AtenderViewModel, responder() agrega el comentario y, si el ticket está ABIERTO, lo pasa a EN_PROCESO.
- BurbujaComentario(texto, autor, fecha, propio, rolAutor, modifier) dibuja cada mensaje de la conversación.
Reglas obligatorias: si agregas una función al Repository, declárala PRIMERO en la interface AyudaRepository y luego impleméntala en AyudaRepositoryRoom.

Pedido: quiero que Soporte pueda escribir notas internas en un ticket que el estudiante NO ve.

1. En la entidad Comentario agrega val interno: Boolean = false (al final, con valor por defecto para no romper los datos iniciales) y sube en 1 la versión de AyudaDatabase.
2. Declara primero en AyudaRepository: suspend fun agregarNotaInterna(ticketId: Int, autor: Usuario, texto: String), y luego impleméntala en AyudaRepositoryRoom insertando un Comentario con interno = true (igual que agregarComentario: copia nombre y rol del autor y usa texto.trim()).
3. Regla de visibilidad: en TicketDetalleViewModel (estudiante), la lista de comentarios excluye los que tienen interno = true. AtenderViewModel (Soporte) sigue mostrando todos.
4. En AtenderViewModel agrega modoNotaInterna: StateFlow<Boolean> y fun alternarNotaInterna(). responder() envía una nota interna con repo.agregarNotaInterna si el modo está activo. Una nota interna NO cambia el estado del ticket (la regla "responder un ticket ABIERTO lo pasa a EN_PROCESO" solo aplica a respuestas al estudiante) y tampoco se permite si el ticket está CERRADO.
5. En AtenderTicketScreen, justo encima de la caja de respuesta, agrega un FilterChip "Nota interna" con icono de candado que llama a alternarNotaInterna(). Si está activo, el placeholder de la caja dice "Nota interna (el estudiante no la ve)…" y el botón de enviar usa ColorAbierto como fondo.
6. BurbujaComentario recibe un parámetro opcional interno: Boolean = false. Si es true, la burbuja usa ColorAbierto con transparencia como fondo y MaterialTheme.colorScheme.onSurface para el texto (debe verse bien en modo claro y oscuro), y junto al autor muestra un icono de candado pequeño y la etiqueta "Nota interna". AtenderTicketScreen le pasa interno = comentario.interno.

[Forma de trabajo: la misma del Prompt 1, pidiendo además todos los imports nuevos.]
```

**Qué generó la IA:** El campo `interno` en `Comentario` con la versión de la base subida de 2 a 3, la función `agregarNotaInterna` (declarada primero en la interface), el filtro de notas internas en `TicketDetalleViewModel`, el modo "nota interna" en `AtenderViewModel` sin cambiar el estado del ticket, el chip "Nota interna" en la caja de respuesta y el estilo ámbar con candado en `BurbujaComentario`.

**Qué tuve que corregir yo:** Verifiqué primero lo más importante: que las notas se filtraran en el ViewModel del estudiante (no solo se ocultaran en pantalla) y que una nota interna no cambiara el estado del ticket; ambas cosas estaban bien. Sin embargo, detecté que solo listó uno de los siete imports que necesitaba `BurbujaComentario` (no habría compilado), que el icono de enviar siempre blanco casi no se vería en modo oscuro sobre el violeta claro, y que en `AtenderTicketScreen` escribió "imports (si faltaran)" sin confirmar la lista. Le pedí corregirlo (Prompt 5b) y después probé con ambos perfiles que el estudiante no viera la nota.

**¿Respeta MVVM?:** Sí. La regla de visibilidad está en el ViewModel del estudiante (las notas no le llegan, no solo se ocultan en pantalla), la regla de no cambiar el estado está en `AtenderViewModel`, y la función nueva se declaró primero en la interface.

### Prompt 5b · Corrección

```text
Antes de aplicar, corrige estos puntos:
1. BurbujaComentario.kt ahora usa Icon, Icons, Icons.Outlined.Lock, Alignment, Modifier.width, Modifier.size y ColorAbierto. Lista todos los imports que debo agregar en ese archivo, revisando cuáles ya existen.
2. En CajaRespuesta, el contentColor del botón de enviar debe ser Color.White solo cuando es nota interna (fondo ColorAbierto); en una respuesta normal usa MaterialTheme.colorScheme.onPrimary, para que el icono se vea bien en modo oscuro.
3. En AtenderTicketScreen, confirma la lista exacta de imports nuevos (no "si faltaran").
Muéstrame de nuevo solo los imports de BurbujaComentario.kt y AtenderTicketScreen.kt y el fragmento corregido del botón de enviar, y espera mi "Confirmo".
```

---

## Prompt 6 · Animaciones y estados vacíos

**Texto enviado a Gemini:**

```text
Contexto: app Android en Kotlin con Jetpack Compose y Material 3 (sin XML). Paquete com.faustino.ayudacampus. Arquitectura MVVM + Repository:
- La View (paquete ui/) solo observa StateFlow con collectAsState y avisa eventos. No usa DAO ni Repository.
- Los ViewModels (paquete viewmodel/) tienen las reglas de negocio.
- Los colores salen de MaterialTheme.colorScheme o de ui/theme/Color.kt, nunca valores fijos. Debe verse bien en modo claro y oscuro.
- Versiones: Compose BOM 2024.09.00 (foundation 1.7, Material 3 1.3) y Navigation Compose 2.8.2.
- StepperTicket(estado, modifier) en ui/components dibuja Abierto → En proceso → Resuelto → Cerrado: completados con check en círculo verde (ColorResuelto), el actual con un punto y un anillo, y los siguientes como círculos vacíos.
- MisTicketsScreen recibe onNuevoTicket y tiene un estado vacío privado (EstadoVacio). BandejaScreen muestra "No hay tickets con estos filtros" y su ViewModel ya tiene limpiarFiltros(). DetalleTicketScreen muestra "Aún no hay comentarios".
Reglas obligatorias: esta mejora solo toca la View (ui/). No cambies ViewModels, entidades, DAOs ni Repository. Usa solo las APIs de animación de Compose (androidx.compose.animation), sin librerías nuevas ni imágenes externas.

Pedido: agrega animaciones y estados vacíos con ilustración.

1. Transiciones de navegación: en el NavHost de NavAyuda agrega enterTransition, exitTransition, popEnterTransition y popExitTransition con un fade y un deslizamiento horizontal corto (unos 300 ms). Que se sientan suaves y no lentas.
2. Stepper animado: en StepperTicket anima los cambios de color de líneas y círculos con animateColorAsState, y haz que el anillo del paso actual "lata" suavemente (escala o transparencia con rememberInfiniteTransition). Mantén el diseño actual (check verde, punto con anillo, círculos vacíos).
3. Crea ui/components/EstadoVacio.kt con un Composable reutilizable EstadoVacio(icono: ImageVector, titulo: String, mensaje: String, modifier: Modifier = Modifier, textoAccion: String? = null, onAccion: (() -> Unit)? = null). La "ilustración" se dibuja con Compose: un círculo grande con MaterialTheme.colorScheme.primaryContainer, el icono en el centro en primary y algunos círculos pequeños decorativos alrededor. Debajo, el título, el mensaje y, si hay acción, un botón. Que aparezca con una animación de entrada (AnimatedVisibility con fade y escala).
4. Usa EstadoVacio en:
   - MisTicketsScreen (reemplaza el EstadoVacio privado): título "No tienes tickets aquí", mensaje "Cambia el filtro o reporta una incidencia", acción "Crear ticket" que llama a onNuevoTicket.
   - BandejaScreen: título "No hay tickets con estos filtros", mensaje "Prueba con otra búsqueda o limpia los filtros", acción "Limpiar filtros" que llama a vm.limpiarFiltros().
   - DetalleTicketScreen: en lugar del texto "Aún no hay comentarios", un EstadoVacio pequeño sin acción, con título "Aún no hay comentarios" y mensaje "Escribe abajo para hablar con Soporte".
5. En las LazyColumn de MisTicketsScreen y BandejaScreen agrega Modifier.animateItem() a cada TicketCard, para que las tarjetas se acomoden con animación al filtrar o cuando aparece un ticket nuevo.

[Forma de trabajo: la misma del Prompt 1, pidiendo además el código completo de EstadoVacio.kt y StepperTicket.kt.]
```

**Qué generó la IA:** Transiciones de 300 ms en el `NavHost`, el `StepperTicket` con colores animados y un anillo que "late", el componente `EstadoVacio` con una ilustración dibujada con círculos e icono, su uso en Mis tickets, la Bandeja y el Detalle, y `Modifier.animateItem()` en las tarjetas.

**Qué tuve que corregir yo:** Al revisar `EstadoVacio` noté que usaba `AnimatedVisibility(visible = true)`: compilaba y parecía correcto, pero como empezaba ya visible, la animación de entrada nunca se habría visto. Le indiqué usar un `MutableTransitionState` que empiece oculto. También vi que la versión "pequeña" pedida para el Detalle tenía el mismo tamaño grande, así que le pedí un parámetro `compacto` (Prompt 6b). Confirmé además que no tocara ningún ViewModel, como exigía la mejora.

**¿Respeta MVVM?:** Sí. Solo se cambiaron archivos de `ui/`; ningún ViewModel, entidad, DAO ni Repository. Los botones de los estados vacíos solo avisan eventos que ya existían (`onNuevoTicket`, `limpiarFiltros`).

### Prompt 6b · Corrección

```text
Antes de aplicar, corrige estos puntos:
1. En EstadoVacio, AnimatedVisibility(visible = true) no anima, porque empieza ya visible. Usa un estado que empiece oculto y pase a visible: val visible = remember { MutableTransitionState(false).apply { targetState = true } } y AnimatedVisibility(visibleState = visible, enter = fadeIn(tween(300)) + scaleIn(tween(300), initialScale = 0.8f)). Agrega los imports necesarios.
2. Agrega a EstadoVacio un parámetro compacto: Boolean = false. Si es true, la ilustración es más pequeña (círculo principal de 56 dp, icono de 24 dp, decorativos proporcionales) y el margen interno es de 12 dp. En DetalleTicketScreen usa compacto = true.
Muéstrame de nuevo el archivo EstadoVacio.kt completo y el fragmento corregido de DetalleTicketScreen, y espera mi "Confirmo".
```

---

## Prompt 7 · Recuperar contraseña (mejora extra)

**Texto enviado a Gemini:**

```text
Contexto: app Android en Kotlin con Jetpack Compose y Material 3 (sin XML). Paquete com.faustino.ayudacampus. Arquitectura MVVM + Repository:
- La View (paquete ui/) solo observa StateFlow con collectAsState y avisa eventos. No usa DAO ni Repository.
- Los ViewModels (paquete viewmodel/) tienen las reglas de negocio y solo conocen la interface AyudaRepository.
- AyudaRepositoryRoom implementa AyudaRepository usando los DAOs de Room 2.6.1.
- Lecturas que cambian en el tiempo devuelven Flow (sin suspend); escrituras y acciones únicas son suspend.
- Los colores salen de MaterialTheme.colorScheme o de ui/theme/Color.kt (ColorResuelto), nunca valores fijos.
- Usuario tiene: id, nombre, correo (guardado en minúsculas), clave y rol.
- Sigue el mismo estilo de RegistroViewModel (data class del formulario con errores por campo y "enviando") y de RegistroScreen (systemBarsPadding, imePadding, verticalScroll, AyudaTextField y AyudaButton).
- SesionViewModel se crea una sola vez en NavAyuda y lo comparten todas las pantallas.
Reglas obligatorias: no cambies las entidades ni la versión de la base de datos. Si agregas una función al Repository, declárala PRIMERO en la interface AyudaRepository y luego impleméntala en AyudaRepositoryRoom.

Pedido: quiero que el enlace "¿Olvidaste tu contraseña?" del LoginScreen permita cambiar la contraseña sin servidor, en dos pasos.

1. UsuarioDao: agrega suspend fun obtenerPorCorreo(correo: String): Usuario? con LOWER(correo) = LOWER(:correo), y una consulta @Query UPDATE usuarios SET clave = :clave WHERE id = :id como suspend fun actualizarClave(id: Int, clave: String).
2. Declara primero en AyudaRepository: suspend fun buscarUsuarioPorCorreo(correo: String): Usuario? y suspend fun cambiarClave(usuarioId: Int, nuevaClave: String). Luego impleméntalas en AyudaRepositoryRoom (normaliza el correo con trim().lowercase()).
3. Crea viewmodel/RecuperarClaveViewModel.kt con una data class FormularioRecuperar (paso: 1 o 2, correo, nombre, nuevaClave, confirmarClave, errores por campo y enviando) y estas reglas:
   - verificar() (paso 1): correo con formato válido; si no existe una cuenta con ese correo, error "No hay una cuenta con ese correo"; si el nombre no coincide con el registrado (comparando con trim() y sin distinguir mayúsculas), error "El nombre no coincide con la cuenta". Si todo está bien, pasa al paso 2 y guarda internamente el usuario encontrado.
   - guardar() (paso 2): nueva contraseña de al menos 4 caracteres, confirmación igual, y distinta de la contraseña actual ("La nueva contraseña debe ser distinta de la anterior"). Si todo está bien, llama a repo.cambiarClave y expone listo: StateFlow<Boolean> = true.
   - volverAlPaso1() para corregir el correo o el nombre.
4. En SesionViewModel agrega aviso: StateFlow<String?>, fun mostrarAviso(texto: String) y fun limpiarAviso().
5. Crea ui/cliente/RecuperarClaveScreen.kt con AppTopBar("Recuperar contraseña", onAtras):
   - Paso 1: texto "Confirma tu identidad con el correo y el nombre con que te registraste", campos Correo (icono de sobre, teclado de correo) y Nombre completo (icono de persona), y botón "Continuar".
   - Paso 2: texto "Hola, {nombre}. Crea tu nueva contraseña", campos Nueva contraseña y Confirmar contraseña (icono de candado, esClave = true), botón "Guardar contraseña" y un TextButton "Cambiar correo" que llama a volverAlPaso1().
   - Cuando listo sea true, llama a onListo().
6. Agrega const val RECUPERAR_CLAVE = "recuperar_clave" en Rutas.
7. En LoginScreen agrega el parámetro onOlvidoClave: () -> Unit y úsalo en el TextButton "¿Olvidaste tu contraseña?" (hoy su onClick está vacío). Además, si sesionVm.aviso no es null, muéstralo arriba del botón Ingresar en un recuadro con ColorResuelto con transparencia, un icono de check y el texto en ColorResuelto; el aviso se limpia cuando el usuario escribe en un campo o cambia de pestaña.
8. En NavAyuda agrega el destino RECUPERAR_CLAVE creando RecuperarClaveViewModel con ayudaViewModel. El enlace del Login navega ahí. Al terminar (onListo), llama a sesionVm.mostrarAviso("Contraseña actualizada. Ingresa con tu nueva contraseña.") y vuelve al Login con nav.popBackStack(). La flecha atrás también vuelve con nav.popBackStack().

[Forma de trabajo: la misma del Prompt 1, pidiendo además el código completo de los archivos nuevos y todos los imports nuevos.]
```

**Qué generó la IA:** Las consultas `obtenerPorCorreo` y `actualizarClave`, las funciones del Repository (declaradas primero en la interface), el `RecuperarClaveViewModel` con los dos pasos y sus reglas, el aviso compartido en `SesionViewModel`, la pantalla `RecuperarClaveScreen`, la ruta y la navegación, y el recuadro verde de aviso en el Login.

**Qué tuve que corregir yo:** Revisé primero que la verificación de identidad y la regla "distinta de la anterior" estuvieran en el ViewModel y que la contraseña guardada nunca llegara a la pantalla; eso estaba bien. Detecté tres problemas: la pantalla combinaba `Scaffold` con `systemBarsPadding()`, lo que dejaría un espacio vacío bajo la barra superior (el mismo problema de márgenes duplicados que ya había visto en Nuevo ticket); faltaban los imports del aviso en `LoginScreen` y la limpieza del aviso estaba solo descrita en texto; y nadie limpiaba el aviso al iniciar sesión, así que "Contraseña actualizada" reaparecería tras cerrar sesión. Le indiqué las correcciones (Prompt 7b) y probé todo el flujo, incluido volver a dejar la contraseña de prueba en `1234`.

**¿Respeta MVVM?:** Sí. La verificación de identidad, la regla "distinta de la anterior" y el cambio de clave están en el ViewModel; el usuario encontrado (con su contraseña) es privado y nunca llega a la pantalla; las funciones nuevas se declararon primero en la interface.

**Nota:** en una app real, la recuperación enviaría un código o enlace al correo desde un servidor. Como AyudaCampus guarda todo localmente en Room, la identidad se verifica con el correo y el nombre registrado.

### Prompt 7b · Corrección

```text
Antes de aplicar, corrige estos puntos:
1. RecuperarClaveScreen: el Scaffold ya aplica los márgenes de las barras del sistema, así que systemBarsPadding() deja un espacio extra bajo la barra superior. Quita el Surface y el systemBarsPadding(), y en el Column usa: Modifier.fillMaxSize().padding(padding).consumeWindowInsets(padding).imePadding().verticalScroll(rememberScrollState()).padding(horizontal = 24.dp, vertical = 16.dp). Agrega el import de consumeWindowInsets y quita los que ya no se usen.
2. LoginScreen: lista los imports nuevos exactos (Icon, Icons.Filled.CheckCircle, Modifier.width, ColorResuelto, revisando cuáles ya existen) y muéstrame el código exacto de los onCambio de los dos AyudaTextField y del onSeleccionar del SelectorRol, llamando a vm.limpiarError() y vm.limpiarAviso().
3. SesionViewModel: en login(), llama a limpiarAviso() al inicio, para que el aviso "Contraseña actualizada" no vuelva a aparecer después de iniciar sesión y cerrarla.
Muéstrame de nuevo RecuperarClaveScreen.kt completo y los fragmentos corregidos de LoginScreen y SesionViewModel, y espera mi "Confirmo".
```

---

## Reflexión (5 líneas)

**¿Qué mejoró la IA y qué no?** 

Aceleró mucho el trabajo repetitivo (consultas, StateFlow, pantallas) y respetó las capas cuando el prompt explicaba la arquitectura y le exigía mostrar un plan antes de editar; no siempre revisó su propio resultado.


**¿Qué error cometió?** 

Olvidó imports, dejó código sin uso y repitió consultas, pero lo más delicado fueron errores que compilaban y fallaban al usarse: una animación que nunca se veía, un tema que ignoraba el modo oscuro, una navegación que dejaba la pantalla en blanco y márgenes duplicados.

**¿Qué aprendí sobre MVVM?** 

Que las reglas (visibilidad, validaciones, SLA) van en el ViewModel y que declarar primero cada función en la interface del Repository mantiene la View aislada de Room.

**¿Qué aprendí sobre Room y reglas de negocio?** 

Que cambiar una entidad obliga a subir la versión de la base, que `Flow` hace que todo se actualice solo, y que filtrar en el ViewModel (no solo ocultar en pantalla) es lo que realmente protege los datos.
Conclusión: la IA es un buen asistente, pero la responsabilidad de revisar, probar y corregir sigue siendo mía.


## Capturas

**Registro de estudiante** 

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/81dc2d16-8010-4867-a9fc-ddb3b8d21e5c" />


**Tiempo promedio de resolución** 

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/dd6c3b9f-7169-4bfc-8541-81108cdd3568" />


**Búsqueda avanzada en la bandeja** 

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/69bcc48b-441a-4306-83d7-df1560713955" />


**Comentarios internos** 

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/d76d2aec-123e-42c8-be89-18d010109578" />


**Contador de SLA** 

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/6eff779d-aaa9-4654-b55e-4216afd61810" />
