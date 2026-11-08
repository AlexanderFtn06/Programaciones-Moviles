package com.tecsup.books.data.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.tecsup.books.domain.model.DatosRegistro
import com.tecsup.books.domain.model.Rol
import com.tecsup.books.domain.model.Sesion
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : AuthRepository {

    override suspend fun iniciarSesion(correo: String, clave: String): Result<Sesion> {
        return try {
            val usuario = auth.signInWithEmailAndPassword(correo, clave).await().user
                ?: throw IllegalStateException("Firebase no devolvió un usuario")
            Result.success(leerSesion(usuario.uid, usuario.email ?: correo))
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Result.failure(CredencialesInvalidasException())
        } catch (e: FirebaseAuthInvalidUserException) {
            Result.failure(CredencialesInvalidasException())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun cerrarSesion() {
        auth.signOut()
    }

    override suspend fun sesionActual(): Sesion? {
        val usuario = auth.currentUser ?: return null
        return try {
            leerSesion(usuario.uid, usuario.email.orEmpty())
        } catch (e: Exception) {
            Sesion(
                uid = usuario.uid,
                correo = usuario.email.orEmpty(),
                rol = Rol.USUARIO,
                nombre = usuario.email.orEmpty(),
                rolConfirmado = false
            )
        }
    }

    override suspend fun contarUsuarios(): Result<Int> {
        return try {
            val snapshot = firestore.collection("usuarios").get(Source.SERVER).await()
            Result.success(snapshot.size())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    override suspend fun registrar(datos: DatosRegistro): Result<Sesion> {
        val usuario = try {
            val resultadoAuth = auth.createUserWithEmailAndPassword(datos.correo, datos.clave).await()
            resultadoAuth.user ?: throw IllegalStateException("Firebase no creó el usuario")
        } catch (e: FirebaseAuthUserCollisionException) {
            return Result.failure(Exception("El correo ya está registrado en el sistema."))
        } catch (e: FirebaseAuthWeakPasswordException) {
            return Result.failure(Exception("La contraseña es demasiado débil. Usa al menos 6 caracteres."))
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            return Result.failure(Exception("El correo no es válido."))
        } catch (e: Exception) {
            return Result.failure(Exception("No se pudo crear la cuenta. Revisa tu conexión."))
        }

        val usuarioUid = usuario.uid

        // CONFIRMACIÓN: El mapa de Firestore contiene ÚNICAMENTE los campos nombre, apellido, edad, correo y rol.
        // La contraseña NUNCA se escribe ni se guarda en Firestore.
        val mapaUsuario = hashMapOf<String, Any>(
            "nombre" to datos.nombre,
            "apellido" to datos.apellido,
            "edad" to datos.edad,
            "correo" to datos.correo,
            "rol" to "usuario"
        )

        try {
            firestore.collection("usuarios").document(usuarioUid).set(mapaUsuario).await()
        } catch (e: Exception) {
            try {
                usuario.delete().await()
            } catch (deletionException: Exception) {
                // COMENTARIO EXPLICATIVO: Si el borrado de la cuenta de Firebase Auth falla (por red o por
                // FirebaseAuthRecentLoginRequiredException), la cuenta queda registrada en Auth pero sin
                // documento de perfil en Firestore, requiriendo intervención manual del docente para vincular el documento o borrar la cuenta.
                return Result.failure(
                    Exception("La cuenta fue creada pero no se pudo guardar tu perfil. Contacta al docente para vincular tus datos.")
                )
            }
            return Result.failure(Exception("No se pudo completar el registro. Revisa tu conexión e inténtalo de nuevo."))
        }

        return Result.success(
            Sesion(
                uid = usuarioUid,
                correo = datos.correo,
                rol = Rol.USUARIO,
                nombre = datos.nombre,
                apellido = datos.apellido,
                edad = datos.edad,
                rolConfirmado = true
            )
        )
    }

    private suspend fun leerSesion(uid: String, correo: String): Sesion {
        val documento = firestore.collection("usuarios").document(uid).get().await()
        val rol = if (documento.getString("rol") == "admin") Rol.ADMIN else Rol.USUARIO
        return Sesion(
            uid = uid,
            correo = correo,
            rol = rol,
            nombre = documento.getString("nombre") ?: correo,
            apellido = documento.getString("apellido"),
            edad = documento.getLong("edad")?.toInt(),
            rolConfirmado = true
        )
    }
}