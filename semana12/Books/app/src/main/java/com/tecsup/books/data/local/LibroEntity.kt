package com.tecsup.books.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "libros")
data class LibroEntity(
    @PrimaryKey val id: String,
    val titulo: String,
    val autor: String,
    val portadaUrl: String?,
    val anio: String?,
    val descripcion: String?,
    val esFavorito: Boolean = false,
    @ColumnInfo(defaultValue = "''") val temas: String = ""
)