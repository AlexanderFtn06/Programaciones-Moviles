package com.faustino.ayudacampus.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.faustino.ayudacampus.data.local.entity.Usuario

@Dao
interface UsuarioDao {

    // Acción única -> suspend. Devuelve null si no coincide
    @Query("SELECT * FROM usuarios WHERE LOWER(correo) = LOWER(:correo) AND clave = :clave LIMIT 1")
    suspend fun login(correo: String, clave: String): Usuario?

    @Insert
    suspend fun insertarTodos(lista: List<Usuario>)

    @Query("SELECT COUNT(*) FROM usuarios WHERE LOWER(correo) = LOWER(:correo)")
    suspend fun contarPorCorreo(correo: String): Int

    @Insert
    suspend fun insertar(u: Usuario): Long

    @Query("SELECT COUNT(*) FROM usuarios")
    suspend fun contar(): Int

    // Se usará en el Paso 6 para recuperar al usuario guardado en DataStore
    @Query("SELECT * FROM usuarios WHERE id = :id")
    suspend fun obtenerPorId(id: Int): Usuario?

    // Paso 12 (modo práctica): cuenta del otro perfil
    @Query("SELECT * FROM usuarios WHERE rol = :rol ORDER BY id LIMIT 1")
    suspend fun obtenerPorRol(rol: String): Usuario?
}