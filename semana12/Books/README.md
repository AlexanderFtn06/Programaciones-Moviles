# BookVerse

Laboratorio 11-12 · Programación en Móviles · Tecsup

**Autor:** Alexander Faustino Quispe

## Descripción

BookVerse es una aplicación Android que consume la API pública de Open Library y muestra un catálogo de libros de ciencia ficción en español. Los usuarios pueden ver el detalle de cada libro, marcar favoritos y usar la app sin conexión. El administrador tiene un panel con totales, sincronización y limpieza de caché.

- **Tema:** Libros
- **Variante:** Science fiction
- **Lista:** `search.json?subject=science_fiction&language=spa&limit=30`
- **Detalle:** `works/{id}.json`

## Funcionalidades

- Inicio de sesión con correo y contraseña (Firebase Authentication), sesión persistente y cierre de sesión.
- Roles `admin` y `usuario` guardados en Cloud Firestore. El rol decide qué pantallas ve cada persona.
- Catálogo con portada, título y autor.
- Detalle con portada, categoría, descripción y temas.
- Favoritos, que se conservan al cerrar la app y al sincronizar.
- Modo sin conexión con aviso cuando se muestran datos guardados.
- Notificación local al terminar de sincronizar.
- Perfil con datos del usuario, cantidad de libros y de favoritos.
- Panel Admin con totales, Sincronizar y Limpiar caché.

## Navegación

La barra inferior cambia según el rol:

- **Usuario:** Catálogo · Favoritos · Perfil
- **Administrador:** Catálogo · Admin · Perfil

Los favoritos se gestionan desde el catálogo o el detalle. El administrador no ve el corazón, porque solo el usuario gestiona favoritos.

## Tecnologías

- Jetpack Compose y Material 3
- Arquitectura MVVM con `UiState`, ViewModel y Repository como interface
- Retrofit, OkHttp y kotlinx.serialization
- Room como fuente única de verdad (versión 2 de la base, con migración desde la 1)
- Firebase Authentication y Cloud Firestore
- Navigation Compose
- Coil para cargar portadas
- Notificaciones locales con canal y permiso `POST_NOTIFICATIONS`

## Requisitos

| Código | Requisito | Estado |
|---|---|---|
| R01 | Login con correo y contraseña, con validación | Cumplido |
| R02 | Roles admin y usuario en Firestore | Cumplido |
| R03 | Lista con estados Cargando, Error y Vacío | Cumplido |
| R04 | Detalle con el segundo endpoint | Cumplido |
| R05 | MVVM con UiState, ViewModel y Repository como interface | Cumplido |
| R06 | Room con Entity, Dao y Database | Cumplido |
| R07 | Favoritos que se conservan al sincronizar | Cumplido |
| R08 | Modo sin conexión con aviso | Cumplido |
| R09 | Notificación local con canal y permiso | Cumplido |
| R10 | Panel Admin con totales, sincronizar y limpiar caché | Cumplido |
| R11 | Sesión persistente y cierre de sesión | Cumplido |
| R12 | Pruebas unitarias del ViewModel | Pendiente (rama `mejora-ia`) |
| R13 | Mejora con IA documentada con `PROMPTS.md` | Pendiente (rama `mejora-ia`) |

## Estructura del proyecto

```
app/src/main/java/com/tecsup/books/
├── AppContainer.kt          # Crea API, base de datos, repositorios y notificador
├── MainActivity.kt
├── data/
│   ├── auth/                # Firebase Auth y lectura del rol desde Firestore
│   ├── local/               # Room: LibroEntity, LibroDao, AppDatabase y migración
│   ├── mapper/              # DTO -> Entity -> modelo de UI
│   ├── remote/              # Retrofit: ApiService, RetrofitClient, DTO
│   └── repository/          # LibroRepository (interface) y su implementación
├── domain/model/            # Libro y Sesion
├── notification/            # Notificador (interface) y NotificadorAndroid
└── ui/
    ├── admin/               # Panel Admin (rol admin)
    ├── components/          # Cargando, ErrorView, AvisoSinConexion, EncabezadoSeccion
    ├── detalle/
    ├── favoritos/
    ├── lista/
    ├── login/
    ├── navigation/          # AppNav: barra inferior según el rol
    ├── perfil/
    └── theme/
```

### Reglas de Firestore

```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /usuarios/{uid} {
      allow read: if request.auth != null && request.auth.uid == uid;
      allow write: if false;
    }
  }
}
```

## Usuarios de prueba

Crea dos usuarios en Authentication y un documento en la colección `usuarios` por cada uno. **El ID del documento debe ser el UID del usuario**, no el correo.

| Rol | Correo | Campos del documento |
|---|---|---|
| Administrador | `admin@...` | `rol` = `admin`, `nombre` = `Administrador` |
| Usuario | `usuario@...` | `rol` = `usuario`, `nombre` = `Usuario de prueba` |

Las contraseñas no se incluyen en este documento.

## Capturas

### Login

<img width="738" height="1600" alt="login" src="https://github.com/user-attachments/assets/c72dd394-5b83-4bc2-8677-e20991915cb9" />

### Catálogo

<img width="738" height="1600" alt="lista" src="https://github.com/user-attachments/assets/c30ad56c-3e63-46cd-ab00-d2af5f7f63da" />


### Detalle

<img width="738" height="1600" alt="detalle" src="https://github.com/user-attachments/assets/fa35a868-2428-48da-a268-5f6b29ac316f" />


### Favoritos

<img width="738" height="1600" alt="favoritos" src="https://github.com/user-attachments/assets/051415a5-99ad-4fc6-9426-988853268668" />

### Panel Admin

<img width="738" height="1600" alt="admin" src="https://github.com/user-attachments/assets/71c7f0a8-b6d5-43f8-a075-b4335dd0065e" />

### Perfil

<img width="738" height="1600" alt="perfil" src="https://github.com/user-attachments/assets/09b686e3-4f8f-4f43-a624-3ca2b98dd504" />

### Notificación

<img width="738" height="1600" alt="notificacion" src="https://github.com/user-attachments/assets/148766d9-0ee9-4127-a154-7122717f077d" />


## Pruebas de aceptación

Las pruebas se ejecutan en un dispositivo Android físico. Marca solo las que hayas probado.

- [ ] PA-01 Ingresar con el usuario `usuario`: entra al catálogo y no ve el panel Admin
- [ ] PA-02 Ingresar con el usuario `admin`: ve la pestaña Admin
- [ ] PA-03 Contraseña incorrecta: muestra error y no entra
- [ ] PA-04 Campos vacíos: muestra validación sin llamar a Firebase
- [ ] PA-05 Primera apertura con internet: Cargando, lista y notificación
- [ ] PA-06 Tocar un libro: muestra el detalle del segundo endpoint
- [ ] PA-07 Marcar favorito y cerrar la app: sigue en Favoritos
- [ ] PA-08 Modo avión con datos guardados: lista con aviso
- [ ] PA-09 Modo avión sin datos: error con Reintentar
- [ ] PA-10 Sincronizar después de marcar un favorito: el favorito se conserva
- [ ] PA-11 Limpiar caché: lista vacía y totales en 0; al sincronizar vuelven los datos
- [ ] PA-12 Cerrar sesión y reabrir: vuelve al login
- [ ] PA-13 Girar la pantalla: no se pierde el estado
- [ ] PA-14 Negar el permiso de notificaciones: la app funciona sin avisos


