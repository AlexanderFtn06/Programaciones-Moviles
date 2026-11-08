package com.tecsup.books.data.auth

import com.tecsup.books.domain.model.DatosRegistro
import com.tecsup.books.domain.model.Sesion

interface AuthRepository {
    suspend fun iniciarSesion(correo: String, clave: String): Result<Sesion>
    suspend fun cerrarSesion()
    suspend fun sesionActual(): Sesion?
    suspend fun contarUsuarios(): Result<Int>
    suspend fun registrar(datos: DatosRegistro): Result<Sesion>
}