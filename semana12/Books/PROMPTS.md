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

---





  
