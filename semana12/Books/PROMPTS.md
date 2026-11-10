# PROMPTS.md

Registro del uso de IA en la Fase 2 del laboratorio (rama `mejora-ia-Lab12`).
Herramientas: Gemini en Android Studio 
---

## Entrada 1: Pruebas unitarias del ViewModel de la lista (R12)

- **Fecha:** 7/11/2026
- **Herramienta:** Gemini en Android Studio 
- **Prompt A (plan):**
  > Actúa como experto en desarrollo de aplicaciones móviles en Android Studio. Quiero pruebas unitarias
  > para ListaViewModel con tres casos:
  > 1) Refresco exitoso: el estado pasa a Exito con los libros.
  > 2) Error sin datos guardados: el estado es Error.
  > 3) Error con datos guardados: el estado sigue en Exito y sinConexion es true.
  >
  > Usa un repositorio falso que implemente LibroRepository, sin Room ni Firebase.
  > ListaViewModel recibe también un Notificador: usa uno falso.
  > Como usa viewModelScope, explica cómo configurar Dispatchers.setMain con kotlinx-coroutines-test.
  >
  > Dame primero el plan: archivos de prueba, dependencias con versión, y qué verifica cada test.
  > No escribas código todavía.
- **Respuesta:** Plan con `Dispatchers.setMain`, un fake de repositorio y de notificador, y tres tests.
- **Corrección (prompt):**
  > El plan es correcto en lo general, pero corrige estos puntos antes de escribir código:
  > 1) FakeNotificador no puede tener un contador "cantidadNotificada"; debe guardar las llamadas en una lista.
  > 2) Usa StandardTestDispatcher, no UnconfinedTestDispatcher, con setMain en @Before y resetMain en @After.
  > 3) En los casos 2 y 3, verifica que refrescar() se llamó exactamente una vez.
  > 4) Confirma que kotlinx-coroutines-test tiene la misma versión que kotlinx-coroutines-core del proyecto.
  > Entrega el plan corregido. No escribas código todavía.
- **Prompt B (implementación):**
  > Apruebo el plan corregido. Implementa exactamente lo aprobado, sin cambios extra.
  > Antes de escribir los tests, verifica que ListaViewModel compile sin cambios. Si hay que modificar
  > ListaViewModel para testearlo, detente y pregúntame antes.
  > Entrega el código completo de gradle/libs.versions.toml, app/build.gradle.kts (solo la línea de test)
  > y app/src/test/java/com/tecsup/books/ui/lista/ListaViewModelTest.kt.
  > Sin Room, sin Firebase, sin Context. Indica qué comando ejecutar para correr solo esta clase de test.
- **Qué corregí:** Gemini usaba `UnconfinedTestDispatcher` y un contador sin sentido en el fake.
- **Qué usé:** Copié los tres archivos. Ejecuté `ListaViewModeltTest.kt` solo para esta clase: pasaron 3 de 3 tests.
- **Qué aprendí:** Aprendi que nn las pruebas unitarias de Kotlin con corrutinas, Dispatchers.setMain es necesario en JVM porque este entorno no cuenta con un hilo principal de Android (Main). Por eso, permite reemplazar el dispatcher principal por uno de prueba, evitando errores y facilitando que las corrutinas se ejecuten de manera controlada.

**Captura**

<img width="858" height="627" alt="image" src="https://github.com/user-attachments/assets/bc744715-d9ec-4ff3-8b49-5992e72c6fc1" />

---

## Entrada 2: Conteo de usuarios para el panel Perfil (mejora 2, opcional)

- **Fecha:** 7/11/2026
- **Herramienta:** Gemini en Android Studio 
- **Prompt A (plan):**
  > Sigue actuando como experto en desarrollo de aplicaciones móviles en Android Studio. Quiero mostrar en
  > Perfil, solo para el rol admin, el total de usuarios registrados, leyendo la colección usuarios de Firestore.
  > Estado actual: AuthRepository tiene iniciarSesion, cerrarSesion y sesionActual. FirebaseAuthRepository
  > lee el rol desde usuarios/{uid}. Las reglas publicadas solo permiten leer el documento propio.
  > Cambios esperados: contarUsuarios() en AuthRepository y FirebaseAuthRepository; PerfilViewModel
  > recibe el rol y solo cuenta si es ADMIN; reglas de Firestore con list solo para admin; tarjeta
  > Usuarios visible solo para admin.
  > Dame primero el plan: archivos a modificar, cómo se pasa el rol al ViewModel, las reglas completas
  > que propones, y los riesgos. No escribas código todavía.
- **Respuesta:** Plan con las reglas de Firestore y la forma de pasar el rol al ViewModel.
- **Corrección (prompt):**
  > Corrige el plan de conteo de usuarios antes de implementar: las reglas de list deben verificar con
  > exists() que el documento del admin existe antes de leer rol; el conteo solo se llama si
  > sesion.rolConfirmado es verdadero; el plan debe indicar qué pasa sin conexión.
- **Prompt B (implementación):**
  > Apruebo el plan corregido. Implementa exactamente lo aprobado, sin cambios extra.
  > Entrega el código completo de AuthRepository.kt, FirebaseAuthRepository.kt, PerfilViewModel.kt,
  > PerfilViewModelFactory.kt, PerfilScreen.kt y firestore.rules.
  > Sin Context en ViewModels, sin Firebase en composables. Imports completos.
- **Qué corregí:** Las reglas de `list` debían verificar con `exists()` que el documento del admin existiera
  antes de leer `rol`. Además, el conteo solo se llama si `rolConfirmado` es verdadero.
- **Qué aprendí:** Aprendi que la regla list depende del rol guardado en Firestore y no de un valor enviado por el cliente porque es una forma de garantizar la seguridad de los datos. Si el sistema confiara en el rol que envía el cliente, cualquier usuario podría modificarlo y hacerse pasar por un administrador para acceder a información restringida.

**Captura**

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/203e4b15-698a-4ce1-9de5-9b73071efe04" />

---

## Entrada 3: Modo oscuro con DataStore (mejora 5, opcional)

- **Fecha:** 7/11/2026
- **Herramienta:** Gemini en Android Studio 
- **Prompt A (plan):**
  > Sigue actuando como experto en desarrollo de aplicaciones móviles en Android Studio. Quiero un interruptor "Modo oscuro" en la pantalla Perfil, visible para todos los roles, que cambie
  > el tema de toda la app y guarde la preferencia en el dispositivo con DataStore Preferences.
  > Restricciones: el ViewModel no recibe Context. El DataStore se crea en AppContainer y se accede
  > por una interface AjustesRepository. El tema lee la preferencia como Flow.
  > Dame primero el plan: dependencias con versión compatible con Kotlin 2.0.21, archivos nuevos y
  > modificados, cómo se aplica el tema y cómo se prueba. No escribas código todavía.
- **Respuesta:** Plan con `AjustesRepository`, `AjustesRepositoryImpl` y DataStore 1.1.1.
- **Corrección (prompt):**
  > Corrige el manejo del modo oscuro. Problema actual: MainActivity muestra un estado de carga mientras
  > modoOscuro es null. No quiero ninguna pantalla de carga.
  > Usa collectAsStateWithLifecycle(initialValue = isSystemInDarkTheme()) y pasa ese valor a BooksTheme.
  > Entrega MainActivity.kt completo, sin Cargando, sin duplicar BooksTheme.
- **Prompt B (implementación):**
  > Apruebo el plan corregido. Implementa exactamente lo aprobado, sin cambios extra.
  > Entrega el código completo de gradle/libs.versions.toml, app/build.gradle.kts, AjustesRepository.kt,
  > AjustesRepositoryImpl.kt, AppContainer.kt, MainActivity.kt y los archivos de ui/perfil/ que cambien.
  > Sin Context en ViewModels. Imports completos.
- **Qué corregí:** Gemini mostraba `Cargando` mientras DataStore respondía. Lo quité y usé el tema del sistema
  como valor inicial, porque la pantalla de carga no aportaba nada.
- **Qué aprendí:** Aprendí que al implementar el modo oscuro con DataStore es importante ofrecer una buena experiencia al usuario. Utilizar isSystemInDarkTheme() como valor inicial permite que la aplicación muestre inmediatamente el tema del dispositivo mientras se recupera la preferencia guardada, evitando pantallas de carga innecesarias

**Captura**

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/4a46d7c3-e9df-4b13-85ae-edae75925cec" />

---

## Entrada 4: Gráficos en el panel Admin (mejora 6, opcional)

- **Fecha:** 7/11/2026
- **Herramienta:** Gemini en Android Studio 
- **Prompt A (plan):**
  > Sigue actuando como experto en desarrollo de aplicaciones móviles en Android Studio. Quiero mejorar el
  > panel Admin de la app BookVerse con gráficos hechos con Compose Canvas, sin librerías nuevas: una gráfica
  > de barras con libros guardados, favoritos y usuarios registrados; y una gráfica de dona con favoritos
  > frente a libros que no son favoritos. Los datos vienen de AdminViewModel como StateFlow.
  > Dame primero el plan: componentes nuevos, cómo se reciben los datos, cómo se verifica en el celular
  > y cómo se ve en modo oscuro. No escribas código todavía.
- **Corrección (prompt):**
  > Corrige el plan: cargarDatos(sesion) consulta el total de usuarios una sola vez; sincronizar() no vuelve
  > a contar usuarios; el LaunchedEffect de la ruta admin usa sesionAdmin como clave; las barras muestran
  > el valor encima de cada barra.
- **Prompt B (implementación):**
  > Apruebo el plan corregido. Implementa exactamente lo aprobado, sin cambios extra.
  > Antes de escribir código, confirma que AdminUiState tiene totalUsuarios: Int? = null y que AdminScreen
  > lo lee desde el mismo ui. cargarDatos(sesion) valida sesion.rol == Rol.ADMIN && sesion.rolConfirmado
  > antes de llamar a authRepository.contarUsuarios(). sincronizar(sesion) no llama a contarUsuarios().
  > Entrega el código completo de GraficaBarras.kt, GraficaDona.kt, AdminViewModel.kt, AdminViewModelFactory.kt,
  > AdminScreen.kt y el bloque de la ruta "admin" de AppNav.kt.
  > Sin Context en ViewModels, sin Firebase en composables. En GraficaBarras, las alturas siguen
  > Hᵢ = (Vᵢ / M) × H_disponible, con M mínimo de 1. En GraficaDona, si el total es 0, se dibuja el aro
  > neutro y el texto "Sin libros". Al final indica qué archivos no cambian.
- **Qué pasó:** Gemini agotó la cuota durante la implementación (`RESOURCE_EXHAUSTED`). Los archivos
  `GraficaBarras.kt` y `GraficaDona.kt` los completé con Claude siguiendo el plan aprobado. Revisé cada
  archivo, compilé y probé en el celular, incluido el modo avión.
- **Qué corregí:** Sin conexión, la barra de usuarios mostraba el último valor. Cambié `cargarDatos` para que
  ponga `totalUsuarios = null` antes de consultar, y que la consulta use solo el servidor.
- **Qué aprendí:** Aprendí que implementar gráficos con Compose Canvas permite representar información de manera visual sin necesidad de utilizar librerías externas. También comprendí la importancia de manejar correctamente los datos que llegan desde el StateFlow del ViewModel, para que los gráficos se actualicen según la información disponible

**Captura**

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/5262a935-5c1b-4451-bdd0-ee0d0b6c933f" />

---

## Entrada 5: Registro de usuarios con perfil extendido (mejora opcional, no exigida por la guía)

- **Fecha:** 7/11/2026
- **Herramienta:** Gemini en Android Studio 
- **Nota:** La guía del laboratorio no exige el registro. Los usuarios de prueba se crean en la consola de
  Firebase. Esta mejora se hizo por iniciativa propia.
- **Prompt A (plan):** 

Quiero una pantalla de registro en BookVerse, accesible desde el enlace "Regístrate"
de la pantalla de login.

ESTADO ACTUAL
- LoginScreen tiene el enlace "Regístrate" sin función (onClick vacío).
- FirebaseAuthRepository tiene iniciarSesion, cerrarSesion, sesionActual y contarUsuarios.
- Sesion tiene uid, correo, rol, nombre y rolConfirmado. No tiene apellido ni edad.
- Las reglas de Firestore permiten get al propio documento y list solo al admin.
- Las cuentas existentes no tienen apellido ni edad; se completan a mano en la consola.

CAMBIOS ESPERADOS
1) Pantalla RegistroScreen con campos: nombre, apellido, edad (entero entre 1 y 120),
   correo, contraseña y confirmar contraseña.
2) RegistroViewModel con validación local antes de llamar a Firebase: campos vacíos,
   edad fuera de rango, contraseñas distintas, contraseña menor de 6 caracteres.
3) AuthRepository.registrar(datos): crea la cuenta con createUserWithEmailAndPassword
   y luego escribe usuarios/{uid} con nombre, apellido, edad, correo y rol = "usuario".
4) Si la cuenta se crea pero falla la escritura en Firestore, indica qué hace la app
   (reintento o eliminar la cuenta) y cómo informa al usuario.
5) Sesion incluye apellido y edad. Perfil muestra nombre completo y edad.
6) Reglas de Firestore: create solo si request.auth.uid == uid y el campo rol == "usuario".
   update y delete bloqueados.

RESTRICCIONES
- La contraseña nunca se guarda en Firestore.
- El rol no lo elige el usuario: siempre se escribe "usuario".
- Sin Context en ViewModels; sin Firebase en composables.

ENTREGA
1) Plan: archivos a crear y modificar, con una línea por archivo.
2) Reglas de Firestore completas con explicación.
3) Qué pasa si la escritura falla después de crear la cuenta.
4) Cómo verificarlo en el celular, paso a paso.

No escribas código todavía.
- **Corrección 1 (prompt):**
  > Corrige el plan de registro antes de aprobarlo: el rollback con user.delete() no puede ser garantizado;
  > separa las reglas en allow create, allow update: if false y allow delete: if false; confirma que
  > createUserWithEmailAndPassword deja la sesión iniciada.
- **Corrección 2 (prompt):**
  > Corrige el bloque try/catch de registrar(): los catch de Auth solo deben aplicar antes de crear la cuenta.
  > Si la cuenta ya se creó, cualquier error posterior debe ir al rollback. Separa en dos bloques.
- **Prompt B (implementación):**
  > Apruebo el plan corregido. Implementa exactamente lo aprobado, sin cambios extra.
  > En firestore.rules, la regla allow create debe limitar los campos con
  > request.resource.data.keys().hasOnly(['rol', 'nombre', 'apellido', 'edad', 'correo']), además de
  > rol == 'usuario' y request.auth.uid == uid. En registrar(datos), la contraseña solo se usa en
  > createUserWithEmailAndPassword; el documento de Firestore tiene solo nombre, apellido, edad, correo y rol.
  > Entrega el código completo en este orden: DatosRegistro.kt y Sesion.kt; AuthRepository.kt y
  > FirebaseAuthRepository.kt; AuthViewModel.kt (solo actualizarSesion); ui/registro/ (cuatro archivos);
  > LoginScreen.kt (solo onIrARegistro); PerfilScreen.kt (solo la tarjeta de usuario); AppNav.kt (solo la
  > ruta "registro"); firestore.rules. Sin Context en ViewModels. Imports completos.
- **Qué corregí:** Los `catch` de Auth capturaban errores de Firestore, y el rollback no se ejecutaba.
  Separé la creación de la cuenta de la escritura del documento. La regla `allow create` limita los
  campos con `hasOnly`, y el documento no incluye la contraseña.
- **Qué aprendí:** Aprendí que implementar un registro de usuarios con Firebase Authentication y Firestore requiere no solo crear cuentas, sino también garantizar la seguridad de la información y manejar correctamente los posibles errores. Comprendí la importancia de validar los datos antes de enviarlos a Firebase y de evitar almacenar información sensible, como las contraseñas, en Firestore.


**Captura**

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/cf49ef87-7244-4ddc-a485-4dd1862bb353" />

## Entrada 6: Logo y eliminación de recuperación de contraseña (login)

- **Fecha:** 9/11/2026
- **Herramienta:** Gemini en Android Studio 
- **Prompt A (plan):**
  > Sigue actuando como experto en desarrollo de aplicaciones móviles en Android Studio. Quiero mejorar la
  > pantalla de login de la app BookVerse (Compose, Material 3, MVVM, tema con colorScheme).
  >
  > ESTADO ACTUAL
  > - LoginScreen muestra el texto "BookVerse" como título pequeño arriba, los textos "Bienvenido" e "Ingresa
  >   para continuar", los campos Correo y Contraseña, un botón "¿Olvidaste tu contraseña?" sin función,
  >   el botón Ingresar y el enlace "Regístrate".
  > - La pantalla usa Column con verticalArrangement = Arrangement.Center.
  >
  > CAMBIOS ESPERADOS
  > 1) Agregar un logo centrado arriba del título. Puede ser un ícono vectorial de Material (sin archivo
  >    externo) dentro de un círculo con el color primary del tema.
  > 2) Quitar el botón "¿Olvidaste tu contraseña?" y el Row que lo contiene. No dejar espacio vacío.
  > 3) Mantener sin cambios la lógica: onCorreoChange, onClaveChange, onIngresar, onIrARegistro, ui.error
  >    y el estado de carga.
  > 4) El diseño debe verse bien en modo claro y oscuro, usando solo MaterialTheme.colorScheme.
  >
  > RESTRICCIONES
  > - No modificar LoginViewModel ni AuthViewModel.
  > - No agregar dependencias nuevas.
  > - No tocar AppNav.kt.
  >
  > ENTREGA
  > 1) Plan: qué cambia en LoginScreen.kt, con una línea por cambio.
  > 2) Cómo se ve el logo (qué ícono usa y de qué librería).
  > 3) Riesgos: si el logo no cabe en pantallas pequeñas, cómo se adapta.
  >
  > No escribas código todavía.
- **Corrección (prompt):**
  > Apruebo el plan con estas correcciones:
  > 1) Confirma que BookVerse, Bienvenido, el subtítulo y el logo quedan centrados horizontalmente, y que los
  >    campos de texto y el botón ocupan el ancho completo.
  > 2) Explica qué pasa con verticalArrangement = Arrangement.Center cuando el contenido es más alto que la
  >    pantalla y se usa verticalScroll. Propón si conviene usar Arrangement.Top con un Spacer superior.
  > 3) Indica que el logo es un ícono de Material (Icons.Filled.Book), no un logo diseñado para la app.
  >
  > Entrega el plan corregido. No escribas código todavía.
- **Prompt B (implementación):**
  > Apruebo el plan corregido. Implementa exactamente lo aprobado, sin cambios extra.
  >
  > Entrega el código completo de ui/login/LoginScreen.kt. Usa:
  > - Column con verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally,
  >   Modifier.verticalScroll(rememberScrollState()) y padding(horizontal = 24.dp, vertical = 24.dp).
  > - Un Spacer(modifier = Modifier.height(32.dp)) al inicio, antes del logo.
  > - Box de 72.dp con CircleShape y fondo primaryContainer, con Icon de Icons.Filled.Book de 36.dp en
  >   onPrimaryContainer, centrado.
  > - BookVerse, Bienvenido y el subtítulo centrados (textAlign = TextAlign.Center).
  > - Campos Correo y Contraseña y el botón Ingresar con fillMaxWidth().
  > - Sin el Row del botón "¿Olvidaste tu contraseña?". Un Spacer(modifier = Modifier.height(24.dp))
  >   antes del botón Ingresar.
  > - Mantén onCorreoChange, onClaveChange, onIngresar, onIrARegistro, ui.error y el estado de carga.
  >
  > No cambies otros archivos. Sin Context ni Firebase en el composable. Imports completos.
  > Al final, indica qué archivos NO cambian.
- **Respuesta:** Plan y código de LoginScreen.kt con Icons.Filled.Book.
- **Qué corregí:** Cambié el ícono de Material por una imagen propia transparente (`res/drawable/logo_bookverse.png`)
  para que se vea bien en modo oscuro.
  implementa.
- **Qué aprendí:** Aprendí que mejorar una pantalla de login no solo consiste en cambiar su apariencia, sino también en mantener su funcionalidad. Comprendí la importancia de organizar correctamente los elementos con Column, Spacer y verticalScroll para que la interfaz se adapte a diferentes tamaños de pantalla. También aprendí a utilizar los colores de MaterialTheme.colorScheme para mantener la compatibilidad con el modo claro y oscuro.Quité el botón "¿Olvidaste tu contraseña?" porque la función no se implementa.
  
<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/150a3cd1-c783-4f43-a9c9-5fc3d4f3cd5b" />

---

## Entrada 7: Búsqueda y filtros por categoría en el catálogo

### 7.1 Búsqueda por texto y filtro por temas (primera versión)

- **Fecha:** 9/11/2026
- **Herramienta:** Gemini en Android Studio
- **Prompt A (plan):**
  > Sigue actuando como experto en desarrollo de aplicaciones móviles en Android Studio. Quiero agregar
  > búsqueda y filtros por tema en el catálogo de BookVerse (Compose, Material 3, MVVM).
  >
  > ESTADO ACTUAL
  > - ListaScreen muestra la lista de libros con LazyColumn y tarjetas (LibroItem) con portada, título,
  >   autor y corazón. El encabezado "BookVerse" está arriba.
  > - ListaViewModel expone estado: StateFlow<UiState<List<Libro>>> y sinConexion. Recibe LibroRepository
  >   y Notificador.
  > - LibroRepository.libros es un Flow<List<Libro>> que sale de Room (fuente única de verdad).
  > - Libro tiene id, titulo, autor, portadaUrl, esFavorito, descripcion, anio y temas: List<String>.
  > - Los temas vienen de la API en inglés (por ejemplo "Fiction", "Science fiction"). Pueden ser muchos por libro.
  >
  > CAMBIOS ESPERADOS
  > 1) Campo de búsqueda arriba de la lista. Filtra por título o autor, sin distinguir mayúsculas, con
  >    coincidencia parcial.
  > 2) Fila horizontal de chips (LazyRow) con los temas más frecuentes de la lista cargada, máximo 8.
  >    Solo un tema activo a la vez; tocar el chip activo lo desactiva y muestra todos.
  > 3) El filtro se aplica en memoria, dentro de ListaViewModel. No se cambia Room, LibroDao ni LibroRepository.
  > 4) El estado de búsqueda y el tema seleccionado viven en el ViewModel, como StateFlow.
  > 5) Si el filtro no devuelve resultados, se muestra "No hay libros que coincidan con la búsqueda".
  >    Si no hay libros guardados, se mantiene el estado Vacío actual.
  > 6) Los estados Cargando, Error y el aviso de sin conexión siguen funcionando igual.
  >
  > RESTRICCIONES
  > - Sin Context en ViewModels; sin Room ni Firebase en composables.
  > - No agregar dependencias nuevas.
  > - No cambiar AppNav.kt más allá de pasar los nuevos parámetros de ListaScreen.
  > - No tocar el filtro de favoritos ni la pantalla Favoritos.
  >
  > ENTREGA
  > 1) Plan: archivos a modificar, con una línea por archivo.
  > 2) Cómo se calculan los temas más frecuentes (fórmula o pasos) y qué pasa con los libros sin temas.
  > 3) Cómo se combinan la búsqueda y el chip activo (AND o OR) y por qué.
  > 4) Riesgos: lista vacía, temas en mayúsculas o minúsculas distintas, y rendimiento con 30 libros.
  > 5) Cómo verificarlo en el celular, paso a paso.
  >
  > No escribas código todavía.
- **Corrección (prompt):**
  > Apruebo el plan con estas correcciones antes de implementar:
  > 1) Para los temas frecuentes, usa groupingBy { it.trim().lowercase() } para contar, y guarda como
  >    etiqueta el primer nombre original encontrado para cada grupo. El chip muestra la etiqueta original;
  >    el filtro compara siempre en minúsculas.
  > 2) Normaliza también los signos de puntuación al final ("fiction." y "fiction" cuentan como el mismo tema).
  > 3) Con filtros activos, el aviso sinConexion sigue visible arriba de la búsqueda y los chips.
  > 4) Muestra solo temas que aparecen en al menos 2 libros. Si no hay ninguno, no se muestra la fila de chips.
  >
  > Entrega el plan corregido. No escribas código todavía.
- **Prompt B (implementación):**
  > Apruebo el plan corregido. Implementa exactamente lo aprobado, sin cambios extra.
  >
  > Antes de escribir código, confirma en tu respuesta:
  > - ListaViewModel expone la lista ya filtrada (combinación AND de búsqueda y tema), y ListaScreen no
  >   contiene lógica de filtrado.
  > - Un filtro sin resultados no cambia UiState a Error ni a Vacío; solo se muestra el mensaje de
  >   "No hay libros que coincidan con la búsqueda".
  >
  > Entrega el código completo de:
  > 1) app/src/main/java/com/tecsup/books/ui/lista/ListaViewModel.kt
  > 2) app/src/main/java/com/tecsup/books/ui/lista/ListaScreen.kt
  > 3) el bloque de la ruta "lista" en app/src/main/java/com/tecsup/books/ui/navigation/AppNav.kt
  >
  > Reglas: sin Context en ViewModels, sin Room ni Firebase en composables, imports completos.
  > No cambies LibroRepository, LibroDao ni otros archivos.
  > Al final, indica qué archivos NO cambian.
- **Corrección posterior (prompt):**
  > Corrige ListaScreen.kt y FavoritosScreen.kt para que la sección de Favoritos no tenga ningún filtro.
  >
  > CAMBIO 1 en ListaScreen.kt:
  > - Agrega el parámetro mostrarBuscador: Boolean = true a la firma.
  > - Envuelve el OutlinedTextField de búsqueda y el LazyRow de FilterChip en un bloque
  >   if (mostrarBuscador) { ... }.
  > - No cambies la lógica de VistaVacia ni VistaSinResultados.
  >
  > CAMBIO 2 en FavoritosScreen.kt:
  > - Llama a ListaScreen con mostrarBuscador = false y valores fijos de búsqueda y tema.
  > - No agregues ningún campo de búsqueda ni chips en FavoritosScreen.
  >
  > Entrega el código completo de ambos archivos.
- **Qué pasó:** Gemini dejó `FavoritosScreen.kt` con la firma anterior y el proyecto no compilaba. Lo corregí
  con una segunda instrucción.
- **Qué aprendí:** Aprendí a implementar búsquedas y filtros por categoría en una aplicación utilizando StateFlow y el patrón MVVM, manteniendo la lógica de filtrado en el ViewModel y no en la interfaz. También comprendí cómo combinar filtros mediante la condición AND y normalizar los textos para evitar problemas con mayúsculas, minúsculas y signos de puntuación.

### 7.2 Categorías generales en lugar de temas frecuentes

- **Fecha:** 9//1/2026
- **Herramienta:** Gemini en Android Studio 
- **Prompt A (plan):**
  > Sigue actuando como experto en desarrollo de aplicaciones móviles en Android Studio. Quiero cambiar los
  > filtros por tema del catálogo de BookVerse para que usen categorías generales en vez de los temas
  > específicos de Open Library.
  >
  > ESTADO ACTUAL
  > - ListaViewModel calcula temasFrecuentes a partir de Libro.temas (strings en inglés como "Fiction",
  >   "Science fiction, American", "Space flight"), mostrando solo los que aparecen en al menos 2 libros.
  >   En la práctica casi no hay chips, porque cada libro trae temas muy específicos.
  > - ListaScreen muestra el buscador y un LazyRow de FilterChip cuando mostrarBuscador es true.
  > - FavoritosScreen llama a ListaScreen con mostrarBuscador = false, sin filtros.
  > - AdminScreen no usa ListaScreen ni filtros, y no debe tenerlos.
  > - El buscador por título o autor ya funciona y no debe cambiar.
  >
  > CAMBIOS ESPERADOS
  > 1) Definir 6 categorías generales fijas: Ciencia ficción, Fantasía, Misterio, Historia, Terror y Otros.
  >    Cada una con una lista de palabras clave en inglés que se buscan dentro de los temas del libro,
  >    sin distinguir mayúsculas.
  > 2) Un libro pertenece a una categoría si al menos un tema contiene alguna palabra clave de esa categoría.
  >    Un libro puede pertenecer a varias categorías.
  > 3) Mostrar siempre las 6 categorías como chips, aunque no tengan libros. Si una categoría no tiene libros
  >    en la lista cargada, su chip se muestra deshabilitado (enabled = false).
  > 4) "Otros" agrupa los libros que no pertenecen a ninguna de las otras categorías.
  > 5) El filtro de tema se combina con la búsqueda por texto con lógica AND, igual que antes.
  > 6) El mapa de categorías y palabras clave vive en un archivo propio, por ejemplo
  >    ui/lista/CategoriasLibros.kt, sin dependencias de Android.
  > 7) ListaViewModel reemplaza temasFrecuentes por la lista de categorías con su conteo de libros.
  > 8) ListaScreen muestra los chips de categoría solo si mostrarBuscador es true. Admin no usa ListaScreen
  >    ni filtros, y Favoritos sigue sin filtros.
  >
  > RESTRICCIONES
  > - Sin Context en ViewModels. Sin dependencias nuevas.
  > - No cambiar LibroRepository, LibroDao ni Room.
  > - No cambiar FavoritosScreen ni AdminScreen.
  > - El buscador por título o autor no debe cambiar.
  >
  > ENTREGA
  > 1) Plan: archivos a crear y modificar, con una línea por archivo.
  > 2) Lista de categorías y sus palabras clave (la tabla completa).
  > 3) Cómo se decide la categoría "Otros" y qué pasa con los libros sin temas.
  > 4) Riesgos: palabras clave que coinciden con temas no relacionados, y cómo se evita.
  > 5) Cómo verificarlo en el celular, paso a paso.
  >
  > No escribas código todavía.
- **Corrección 1 (prompt):**
  > Apruebo el plan con estas correcciones antes de implementar:
  > 1) Define que "Otros" contiene solo los libros que no pertenecen a ninguna de las cinco categorías
  >    principales, y que esa regla se aplica una sola vez al cargar.
  > 2) Reemplaza la comparación con contains por comparación de palabra completa: divide cada tema en
  >    palabras (o usa \b en una expresión regular). Las frases como "world war" o "science fiction" se
  >    buscan como frase completa dentro del tema. No uses contains sobre palabras sueltas.
  > 3) Revisa las palabras clave genéricas y elimina las que causen falsos positivos evidentes. Incluye,
  >    para cada palabra eliminada, el motivo.
  > 4) Calcula la pertenencia de cada libro a las categorías una sola vez, cuando llega la lista desde
  >    Room, y guárdala. El filtro solo compara ese resultado guardado con el chip activo y la búsqueda.
  >
  > Entrega el plan corregido. No escribas código todavía.
- **Corrección 2 (prompt):**
  > Apruebo el plan con estas correcciones antes de implementar:
  > 1) Las comparaciones de categoría se hacen siempre sobre la clave normalizada del tema (trim, sin
  >    puntuación final, en minúsculas), nunca sobre el texto original.
  > 2) Confirma que cada frase de la tabla es un término que aparece o puede aparecer en temas de Open
  >    Library. Para las que sean supuestos, márcalas como "por verificar con datos reales".
  > 3) Revisa que todas las expresiones regulares estén escritas sin errores de tipeo y que el guion en
  >    "sci-fi" se escape correctamente en la expresión regular.
  > 4) Entrega la lista final de expresiones regulares en un solo bloque, para revisarlas antes del Prompt B.
  >
  > Entrega el plan corregido. No escribas código todavía.
- **Corrección 3 (prompt):**
  > Apruebo el plan con estas correcciones antes del Prompt B:
  > 1) Quita \brobots?\b de ciencia_ficcion, o cámbialo por la frase "robot fiction" (que ya está).
  >    Confirma que el plan no reintroduce robots como palabra suelta.
  > 2) Confirma que cada Regex se evalúa sobre la clave normalizada completa del tema, y que un tema con
  >    varias categorías ("Science fiction, Fantasy") cuenta para ambas.
  > 3) Entrega la lista de Regex final en un solo bloque, sin cambios respecto a la del plan salvo el punto 1.
  >
  > No escribas código todavía.
- **Prompt B (implementación):**
  > Apruebo el plan final. Implementa exactamente lo aprobado, sin cambios extra.
  >
  > Entrega el código completo de:
  > 1) app/src/main/java/com/tecsup/books/ui/lista/CategoriasLibros.kt (nuevo):
  >    - data class CategoriaInfo(val id: String, val etiqueta: String)
  >    - val REGEX_CATEGORIAS con exactamente las expresiones de la lista final, sin cambios.
  >    - Etiquetas de chip: "Ciencia ficción", "Fantasía", "Misterio", "Historia", "Terror", "Otros".
  >    - fun clasificarLibro(libro: Libro): Set<String>: aplica cada Regex sobre la clave normalizada de cada
  >      tema (trim, trimEnd('.', ',', ';', ':', '!', '?'), lowercase). Si no hay coincidencias, devuelve
  >      setOf("otros"). Los libros sin temas devuelven setOf("otros").
  > 2) app/src/main/java/com/tecsup/books/ui/lista/ListaViewModel.kt:
  >    - Clasifica cada libro una sola vez al recibirlo de Room y guarda LibroConCategorias.
  >    - Calcula el conteo de cada categoría (los 6 IDs, incluido "otros").
  >    - El filtro combina categoría (AND) y búsqueda por texto (AND). Expone la lista ya filtrada.
  >    - Un filtro sin resultados no cambia UiState a Error ni a Vacío.
  > 3) app/src/main/java/com/tecsup/books/ui/lista/ListaScreen.kt:
  >    - Muestra los 6 chips cuando mostrarBuscador == true, con enabled = count > 0 en los que tienen 0 libros.
  >    - No contiene lógica de clasificación ni de filtrado.
  >    - FavoritosScreen y AdminScreen no cambian.
  > 4) El bloque de la ruta "lista" en app/src/main/java/com/tecsup/books/ui/navigation/AppNav.kt.
  >
  > Reglas: sin Context en ViewModels, sin Room ni Firebase en composables, imports completos.
  > Al final, indica qué archivos NO cambian.
- **Qué pasó:** Gemini cambió la firma de ListaScreen y no incluyó FavoritosScreen.kt en la lista de archivos.
  Lo corregí con el código de FavoritosScreen (valores fijos y sin filtros). El chip "Historia" no tenía
  libros con esos temas, y después de revisar Logcat lo reemplacé por "Robots" (ver 7.3).
- **Qué aprendí:** Aprendí que es importante organizar las categorías de los libros de manera general para facilitar la búsqueda y mejorar la experiencia del usuario. También comprendí cómo utilizar expresiones regulares (Regex) para clasificar los libros según sus temas, evitando coincidencias incorrectas mediante la normalización de textos.

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/97b685d8-eade-4d89-a53d-cd7b8d812a84" />


### 7.3 Reemplazo de "Historia" por "Robots"

- **Fecha:** 9/11/2026
- **Herramienta:** Gemini en Android Studio 
- **Prompt A (plan):**
  > Sigue actuando como experto en desarrollo de aplicaciones móviles en Android Studio. En la app BookVerse,
  > quiero reemplazar la categoría "Historia" por "Robots" en los chips de categorías del catálogo.
  >
  > ESTADO ACTUAL
  > - CategoriasLibros.kt define CATEGORIAS_DEFINICION (ciencia_ficcion, fantasia, misterio, historia, terror,
  >   otros) y REGEX_CATEGORIAS, un mapa con listas de Regex por categoría, aplicadas sobre la clave normalizada
  >   de cada tema.
  > - Los temas reales de los libros llegan de Open Library con la forma "Robots", "Robots in fiction",
  >   "Fiction, science fiction, hard science fiction", etc.
  > - clasificarLibro(libro) devuelve un Set de IDs de categoría. Si no coincide ninguna, devuelve "otros".
  >
  > CAMBIOS ESPERADOS
  > 1) En CATEGORIAS_DEFINICION, reemplaza ("historia", "Historia") por ("robots", "Robots").
  >    El orden final es: ciencia_ficcion, fantasia, misterio, robots, terror, otros.
  > 2) En REGEX_CATEGORIAS, elimina la entrada "historia" y agrega la entrada "robots" con expresiones de
  >    frase completa, sin la palabra suelta "robot" ni "robots" dentro de otra frase. Usa \b en cada expresión.
  > 3) Verifica que ninguna expresión de robots coincida con temas de manualidades, robótica educativa o
  >    ingeniería, y explica por qué cada una es segura.
  > 4) Las demás categorías y la lógica de otros no cambian.
  > 5) Si alguna expresión de "historia" aún estaba en otra parte del código, indícala para eliminarla.
  >
  > RESTRICCIONES
  > - Solo modifica CategoriasLibros.kt.
  > - No cambies ListaViewModel, ListaScreen, AppNav, ni FavoritosScreen.
  > - Sin Context ni dependencias nuevas.
  > - Mantén Otros como residual: se asigna solo si ninguna categoría coincide.
  >
  > ENTREGA
  > 1) Plan: qué cambia en CategoriasLibros.kt, con una línea por cambio.
  > 2) Lista de expresiones Regex de robots, con el motivo de cada una.
  > 3) Riesgos de falsos positivos y cómo se evitan.
  > 4) Cómo verificarlo en el celular: el chip Robots debe mostrar Caves of Steel y Naked Sun.
  >
  > No escribas código todavía.
- **Corrección (prompt):**
  > Apruebo el plan con estas precisiones antes de implementar:
  > 1) Confirma que ^\brobots?\b$ se aplica sobre la clave normalizada del tema completo (ya en minúsculas
  >    y sin puntuación final), y que el tema "Robots" exacto produce coincidencia.
  > 2) Confirma que "Robots, Industrial" no coincide con ninguna expresión, y explica por qué es aceptable.
  > 3) Entrega el bloque completo de CategoriasLibros.kt con: CATEGORIAS_DEFINICION actualizado, REGEX_CATEGORIAS
  >    sin la entrada historia y con la entrada robots del plan, sin cambios en las demás categorías.
  >
  > No cambies otros archivos.
- **Prompt B:** No hubo un Prompt B separado: el bloque de CategoriasLibros.kt se entregó con la corrección
  anterior. Después quité `\brobot fiction\b` de `ciencia_ficcion`, porque quedaba duplicada con robots.
- **Qué aprendí:** Aprendí a mejorar la clasificación de libros mediante el uso de expresiones regulares (Regex), utilizando coincidencias exactas para evitar que se incluyan libros de categorías incorrectas. También comprendí la importancia de normalizar los textos antes de compararlos y de revisar los temas reales que llegan desde Open Library. Al reemplazar la categoría Historia por Robots, aprendí que los filtros deben adaptarse al contenido disponible en la aplicación. Además, entendí cómo evitar falsos positivos, por ejemplo, diferenciando libros de robots de temas relacionados con robótica industrial.

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/dd8acdf1-cb8d-4c2e-b3b2-b54b2e202f8c" />

---

## Entrada 8: Corrección de temas (campo subject en la búsqueda)

- **Fecha:** 9/11/2026
- **Herramienta:** Gemini en Android Studio 
- **Prompt (cadena de temas):**
  > Sigue actuando como experto en desarrollo de aplicaciones móviles en Android Studio. En la app BookVerse,
  > los libros se guardan en Room sin temas: el diagnóstico muestra temas: [] en todos los libros, aunque
  > search.json sí devuelve el campo "subject" para cada libro.
  >
  > ESTADO ACTUAL
  > - LibroDto tiene val subject: List<String>? = null (mapeado desde "subject" en la búsqueda).
  > - Libro tiene temas: List<String>.
  > - LibroEntity tiene temas: String, guardado como texto separado por "|" (SEPARADOR).
  > - Mappers.kt tiene: LibroDto.aModelo(), Libro.aEntity() y LibroEntity.aModelo().
  > - LibroRepositoryImpl.refrescar() descarga la lista, la fusiona con la base local y guarda con guardarTodos().
  >
  > OBJETIVO
  > Encontrar y corregir la pérdida de temas en toda la cadena: DTO → Libro → LibroEntity → Room → Libro.
  >
  > INSTRUCCIONES
  > 1) Revisa LibroDto.aModelo(). Confirma que temas se llena desde subject (con take(MAX_TEMAS)).
  > 2) Revisa Libro.aEntity(). Confirma que temas se convierte a texto con SEPARADOR.
  > 3) Revisa LibroEntity.aModelo(). Confirma que temas se convierte de texto a lista sin vacíos.
  > 4) Revisa LibroRepositoryImpl.refrescar(). Asegúrate de que el libro nuevo conserve sus temas.
  > 5) Confirma que el conteo de categorías se calcula sobre los temas ya guardados.
  >
  > RESTRICCIONES
  > - Solo modifica Mappers.kt y LibroRepositoryImpl.kt.
  > - No cambies LibroDao, LibroEntity ni AppDatabase.
  > - Sin Context en ViewModels. Sin dependencias nuevas.
  >
  > ENTREGA
  > 1) Explica en una frase dónde se pierden los temas (o confirma que la cadena está bien).
  > 2) Código completo de Mappers.kt y LibroRepositoryImpl.kt, solo si cambian.
  > 3) Qué debo hacer después para que los libros ya guardados recuperen sus temas.
  > 4) Al final, indica qué archivos NO cambian.
  >
  > No escribas código de otros archivos.
- **Prompt (diagnóstico con Logcat):**
  > Sigue actuando como experto en desarrollo de aplicaciones móviles en Android Studio. En la app BookVerse,
  > los libros llegan con temas: [] en el catálogo, aunque search.json devuelve el campo "subject".
  > Quiero ubicar en qué paso se pierden los temas, sin cambiar la lógica de la app.
  >
  > TAREA
  > 1) En LibroRepositoryImpl.refrescar(), justo después de calcular "remotos", agrega un registro temporal
  >    con android.util.Log.d de la etiqueta "TEMAS_API" con el título y los temas de los primeros 3 libros.
  > 2) En LibroRepositoryImpl.libros, agrega un registro temporal con android.util.Log.d de la etiqueta
  >    "TEMAS_ROOM" con el título y los temas de los primeros 3 libros que salen de Room.
  > 3) No cambies ninguna otra línea de la lógica.
  > 4) Indica qué líneas exactas agregaste y dónde van.
  >
  > RESTRICCIONES
  > - Solo modifica LibroRepositoryImpl.kt.
  > - Los registros son temporales: al final indica cómo se quitan.
  >
  > ENTREGA
  > 1) Código completo de LibroRepositoryImpl.kt con solo los dos registros agregados.
  > 2) Qué debo hacer para generar los registros: sincronizar desde Admin y revisar Logcat.
  > 3) Cómo interpretar el resultado.
  > 4) Al final, indica qué archivos NO cambian.
- **Qué pasó:** Los libros llegaban con `temas: []`. Con Logcat confirmé que `TEMAS_API` ya venía vacío, y que la
  búsqueda con `language=spa` no devolvía el campo `subject` (al compararla con el navegador). La corrección fue
  pedir `fields` con `subject` en `ApiService.kt`.
- **Qué pasó con Gemini:** La primera propuesta cambiaba `LibroRepositoryImpl.kt` sin tocar la causa real. La
  revisé antes de copiarla.
- **Qué aprendí:** Aprendí la importancia de revisar todo el recorrido de los datos desde una API hasta su almacenamiento en Room, pasando por los DTO, modelos y mappers. También comprendí cómo utilizar Logcat y Log.d para identificar en qué parte del proceso se estaba perdiendo la información. En este caso, descubrí que el problema no estaba en Room ni en el repositorio, sino en la petición a Open Library, que no estaba devolviendo el campo subject

---

## Entrada 9: Navegación y diseño del Perfil

- **Fecha:** 9/11/2026
- **Herramienta:** Gemini en Android Studio 
- **Prompt A (plan):**
  > Sigue actuando como experto en desarrollo de aplicaciones móviles en Android Studio. En la app BookVerse
  > (Compose, Material 3, MVVM, navegación con Navigation Compose) quiero mejorar la pantalla Perfil en dos
  > aspectos: navegación por rol y diseño.
  >
  > ESTADO ACTUAL
  > - PerfilScreen muestra TarjetaUsuario, tarjetas de estadística (Libros, Favoritos y, solo para admin, Usuarios),
  >   el interruptor de modo oscuro, un mensaje "Modo sin conexión" si rolConfirmado es false, y el botón Cerrar sesión.
  > - La pantalla deja un espacio vacío grande en la parte inferior.
  > - AppNav tiene la función navegarA(navController, ruta) que cambia de pestaña sin acumular pantallas.
  > - Destinos: usuario tiene lista, favoritos, perfil. Admin tiene lista, admin, perfil.
  >
  > CAMBIOS ESPERADOS
  > 1) Navegación desde Perfil: usuario va a Catálogo o Favoritos; admin va a Catálogo, y Favoritos y Usuarios
  >    van a Admin. Usa navegarA. PerfilScreen recibe callbacks (onIrACatalogo, onIrAFavoritos, onIrAAdmin).
  > 2) Diseño: tarjetas de Libros y Favoritos clicables con flecha; bloque "Accesos rápidos"; bloque "Actividad"
  >    con datos que ya existen. No inventes datos. Reduce el espacio vacío.
  > 3) Mantén sin cambios el interruptor de modo oscuro, el mensaje Modo sin conexión y Cerrar sesión.
  >
  > RESTRICCIONES
  > - Sin Context ni navController en composables de Perfil.
  > - Sin Firebase ni Room en composables.
  > - No agregar dependencias nuevas.
  >
  > ENTREGA
  > 1) Plan: archivos a modificar (PerfilScreen.kt, AppNav.kt bloque perfil, y PerfilViewModel.kt solo si hace falta).
  > 2) Diseño propuesto por rol.
  > 3) Cómo se usa navegarA desde AppNav y por qué no se acumula la pila.
  > 4) Riesgos: pantallas pequeñas, rol sin confirmar y datos vacíos.
  > 5) Cómo verificarlo en el celular, para usuario y admin.
  >
  > No escribas código todavía.
- **Corrección (prompt):**
  > Apruebo el plan con estas precisiones antes de entregar el código:
  > 1) En el diseño de admin, la tarjeta "Favoritos" navega a Admin y usa un ícono distinto al de "Usuarios".
  > 2) Confirma que navegarA se usa solo para las pestañas principales (lista, favoritos, admin). La navegación
  >    a Detalle sigue usando navigate.
  > 3) "Estado de conexión" se toma de sesion.rolConfirmado: muestra "Conectado" o "Sin conexión".
  > 4) Entrega PerfilScreen.kt completo, sin navController. Entrega solo el bloque perfil de AppNav.kt con los
  >    tres callbacks.
  >
  > No cambies PerfilViewModel. Imports completos.
- **Prompt B (ajustes finales de PerfilScreen):**
  > Sigue actuando como experto en desarrollo de aplicaciones móviles en Android Studio. En la app BookVerse,
  > quiero tres cambios puntuales en la pantalla Perfil (PerfilScreen.kt).
  >
  > CAMBIOS ESPERADOS
  > 1) Para el rol ADMIN, en "Resumen de Actividad" quita únicamente la línea "Estado de conexión".
  > 2) Para el rol USUARIO, no se muestran los bloques "Accesos rápidos" ni "Resumen de Actividad".
  > 3) Para el rol ADMIN, se mantienen los bloques "Accesos rápidos" (Catálogo y Panel Admin) y "Resumen de
  >    Actividad" (sin la línea de estado de conexión).
  > 4) No cambies la lógica de la edad: TarjetaUsuario debe seguir mostrando "{edad} años" cuando sesion.edad
  >    no es nulo. Confirma en tu respuesta que el campo edad viene de Sesion.
  > 5) Las tarjetas estadísticas clicables, el interruptor de modo oscuro, el mensaje de error y Cerrar sesión no cambian.
  >
  > RESTRICCIONES
  > - Solo modifica PerfilScreen.kt. No cambies AppNav, PerfilViewModel, Sesion ni FirebaseAuthRepository.
  > - Sin navController, Context, Room ni Firebase en el composable.
  >
  > ENTREGA
  > 1) Código completo de PerfilScreen.kt.
  > 2) Confirmación de que los bloques se muestran según el rol.
  > 3) Confirmación de que la edad no requiere cambios en código, y que depende de que el campo edad exista
  >    en Firestore como número.
  > 4) Al final, indica qué archivos NO cambian.
- **Qué corregí:** Gemini dejó "Accesos rápidos" y "Resumen de Actividad" para todos los roles. Ajusté la
  pantalla para mostrar "Accesos rápidos" solo al admin, quité la línea de estado de conexión del admin, y
  completé el campo `edad` (número) en Firestore para las cuentas de prueba, porque sin ese campo la edad no
  aparecía en Perfil.
- **Qué aprendí:** Aprendí a mejorar la navegación y el diseño de una pantalla de perfil utilizando Jetpack Compose y Navigation Compose, teniendo en cuenta los diferentes roles de usuario y administrador. También comprendí la importancia de utilizar callbacks para manejar la navegación sin incluir directamente el navController en los composables, manteniendo una mejor organización del código. Además, aprendí a mostrar u ocultar componentes según el rol del usuario, evitando que se muestren opciones que no le corresponden.

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/51c22b36-e82d-408c-bdec-94ec88d46785" />

---
---





  
