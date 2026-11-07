package com.tecsup.books.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [LibroEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun libroDao(): LibroDao

    companion object {
        @Volatile
        private var instancia: AppDatabase? = null

        // Agrega la columna temas sin borrar libros ni favoritos
        private val MIGRACION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE libros ADD COLUMN temas TEXT NOT NULL DEFAULT ''")
            }
        }

        fun obtener(context: Context): AppDatabase =
            instancia ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "biblioteca.db"
                )
                    .addMigrations(MIGRACION_1_2)
                    .build()
                    .also { instancia = it }
            }
    }
}