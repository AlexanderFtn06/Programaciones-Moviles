package com.tecsup.books.data.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore
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
            // Sin Firestore ni caché: por seguridad se trata como usuario normal
            Sesion(
                uid = usuario.uid,
                correo = usuario.email.orEmpty(),
                rol = Rol.USUARIO,
                nombre = usuario.email.orEmpty()
            )
        }
    }

    // Si el documento no existe, el rol es USUARIO: es el valor más seguro por defecto
    private suspend fun leerSesion(uid: String, correo: String): Sesion {
        val documento = firestore.collection("usuarios").document(uid).get().await()
        val rol = if (documento.getString("rol") == "admin") Rol.ADMIN else Rol.USUARIO
        return Sesion(
            uid = uid,
            correo = correo,
            rol = rol,
            nombre = documento.getString("nombre") ?: correo
        )
    }
}