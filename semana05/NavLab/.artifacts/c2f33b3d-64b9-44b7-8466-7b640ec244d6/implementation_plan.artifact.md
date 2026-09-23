# Redesign and Login Implementation Plan

Redesign the NavLab app with a purple-to-lavender gradient theme and implement a Login screen with validation.

## User Review Required

> [!IMPORTANT]
> The `Login` screen will now be the `startDestination`. Users will need to "log in" (simulated validation) to access the app.
> "Cerrar Sesión" will clear the back stack to prevent going back to authenticated screens.

## Proposed Changes

### [Theme & Colors]

#### [MODIFY] [Color.kt](file:///C:/Users/User/AndroidStudioProjects/Programaciones-Moviles/semana05/NavLab/app/src/main/java/com/faustino/navlab/ui/theme/Color.kt)
- Add `DeepPurple` (#4A2E83), `Lavender` (#C9B8F0), and `LightLavender` (#E6E0F8).

### [Navigation]

#### [MODIFY] [Screen.kt](file:///C:/Users/User/AndroidStudioProjects/Programaciones-Moviles/semana05/NavLab/app/src/main/java/com/faustino/navlab/navigation/Screen.kt)
- Add `Login` object to the `Screen` sealed class.

#### [MODIFY] [AppNavigation.kt](file:///C:/Users/User/AndroidStudioProjects/Programaciones-Moviles/semana05/NavLab/app/src/main/java/com/faustino/navlab/navigation/AppNavigation.kt)
- Add `Login` route to `NavHost`.
- Set `startDestination = Screen.Login.route`.

### [Screens]

#### [NEW] [LoginScreen.kt](file:///C:/Users/User/AndroidStudioProjects/Programaciones-Moviles/semana05/NavLab/app/src/main/java/com/faustino/navlab/screens/LoginScreen.kt)
- Centered card with "Portal Académico" title.
- Email and Password fields with validation (empty check, email format).
- "INICIAR SESIÓN" button.
- clickable "¿Olvidaste tu contraseña?" showing a Snackbar/AlertDialog.
- Navigation to `Home` upon successful validation.

#### [MODIFY] [HomeScreen.kt](file:///C:/Users/User/AndroidStudioProjects/Programaciones-Moviles/semana05/NavLab/app/src/main/java/com/faustino/navlab/screens/HomeScreen.kt)
- Apply gradient background.
- "Bienvenido" header.
- Two cards for "Directorio" and "Perfil".
- "Cerrar Sesión" link clearing back stack.

#### [MODIFY] [ListScreen.kt](file:///C:/Users/User/AndroidStudioProjects/Programaciones-Moviles/semana05/NavLab/app/src/main/java/com/faustino/navlab/screens/ListScreen.kt)
- Rename header to "Directorio de Alumnos".
- Redesign list items: circular photo, bold name, purple career, chevron.

#### [MODIFY] [DetailScreen.kt](file:///C:/Users/User/AndroidStudioProjects/Programaciones-Moviles/semana05/NavLab/app/src/main/java/com/faustino/navlab/screens/DetailScreen.kt)
- Rename header to "Expediente Académico".
- Gradient header with large circular photo.
- Card with academic details and biography.

#### [MODIFY] [ProfileScreen.kt](file:///C:/Users/User/AndroidStudioProjects/Programaciones-Moviles/semana05/NavLab/app/src/main/java/com/faustino/navlab/screens/ProfileScreen.kt)
- Rename header to "Configuración de Perfil".
- Sections for "INFORMACIÓN PERSONAL" and "ACADÉMICO".
- "Cerrar Sesión" button clearing back stack.

## Verification Plan

### Automated Tests
- N/A (Manual verification on device/emulator is preferred for UI redesign).

### Manual Verification
1. Launch app -> Verify `LoginScreen` is shown.
2. Click "INICIAR SESIÓN" with empty fields -> Verify error messages.
3. Enter invalid email -> Verify "Correo inválido" error.
4. Enter valid email/pass -> Verify navigation to `HomeScreen`.
5. Test "Cerrar Sesión" from `Home` and `Profile` -> Verify return to `Login` and back stack cleared.
6. Navigate through `List` and `Detail` -> Verify new styling and content.
