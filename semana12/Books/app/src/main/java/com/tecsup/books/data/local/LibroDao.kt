package com.tecsup.books.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface LibroDao {

    @Query("SELECT * FROM libros ORDER BY titulo")
    fun todos(): Flow<List<LibroEntity>>

    @Query("SELECT * FROM libros WHERE esFavorito = 1 ORDER BY titulo")
    fun favoritos(): Flow<List<LibroEntity>>

    @Query("SELECT id FROM libros WHERE esFavorito = 1")
    suspend fun idsFavoritos(): List<String>

    @Query("SELECT COUNT(*) FROM libros")
    suspend fun contar(): Int

    @Query("SELECT COUNT(*) FROM libros WHERE esFavorito = 1")
    suspend fun contarFavoritos(): Int

    @Upsert
    suspend fun guardarTodos(libros: List<LibroEntity>)

    @Query("UPDATE libros SET esFavorito = NOT esFavorito WHERE id = :id")
    suspend fun alternarFavorito(id: String)

    @Query("DELETE FROM libros")
    suspend fun borrarTodo()

    @Query("SELECT * FROM libros WHERE id = :id")
    suspend fun obtener(id: String): LibroEntity?

    @Query("SELECT * FROM libros")
    suspend fun listaActual(): List<LibroEntity>
}