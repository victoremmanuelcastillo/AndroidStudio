package com.example.myapplicasion

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "ContactosDB"
        private const val DATABASE_VERSION = 1
        private const val TABLE_CONTACTOS = "contactos"

        private const val COLUMN_ID = "id"
        private const val COLUMN_NOMBRE = "nombre"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_TELEFONO = "telefono"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = """
            CREATE TABLE $TABLE_CONTACTOS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NOMBRE TEXT NOT NULL,
                $COLUMN_EMAIL TEXT NOT NULL,
                $COLUMN_TELEFONO TEXT NOT NULL
            )
        """.trimIndent()
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTOS")
        onCreate(db)
    }

    // Insertar contacto
    fun insertarContacto(nombre: String, email: String, telefono: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE, nombre)
            put(COLUMN_EMAIL, email)
            put(COLUMN_TELEFONO, telefono)
        }
        val id = db.insert(TABLE_CONTACTOS, null, values)
        db.close()
        return id
    }

    // Obtener todos los contactos
    fun obtenerContactos(): List<Contacto> {
        val contactos = mutableListOf<Contacto>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_CONTACTOS ORDER BY $COLUMN_ID DESC", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE))
                val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
                val telefono = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TELEFONO))
                contactos.add(Contacto(id, nombre, email, telefono))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return contactos
    }

    // Actualizar contacto
    fun actualizarContacto(id: Int, nombre: String, email: String, telefono: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE, nombre)
            put(COLUMN_EMAIL, email)
            put(COLUMN_TELEFONO, telefono)
        }
        val result = db.update(TABLE_CONTACTOS, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
        return result
    }

    // Eliminar contacto
    fun eliminarContacto(id: Int): Int {
        val db = writableDatabase
        val result = db.delete(TABLE_CONTACTOS, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
        return result
    }

    // Buscar contacto por ID
    fun buscarContactoPorId(id: Int): Contacto? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_CONTACTOS WHERE $COLUMN_ID = ?", arrayOf(id.toString()))

        var contacto: Contacto? = null
        if (cursor.moveToFirst()) {
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE))
            val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
            val telefono = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TELEFONO))
            contacto = Contacto(id, nombre, email, telefono)
        }
        cursor.close()
        db.close()
        return contacto
    }
}
