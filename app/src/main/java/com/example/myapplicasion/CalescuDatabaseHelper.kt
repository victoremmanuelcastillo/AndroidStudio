package com.example.myapplicasion

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class CalescuDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "calescu.db"
        private const val DATABASE_VERSION = 1

        // Tabla Usuarios
        const val TABLE_USUARIOS = "usuarios"
        const val COL_USUARIO = "usuario"
        const val COL_PASSWORD = "password"

        // Tabla Alumnos
        const val TABLE_ALUMNOS = "alumnos"
        const val COL_MATRICULA = "matricula"
        const val COL_NOMBRE = "nombre"
        const val COL_CARRERA = "carrera"
        const val COL_EMAIL = "email"

        // Tabla Materias
        const val TABLE_MATERIAS = "materias"
        const val COL_CODIGO = "codigo"
        const val COL_NOMBRE_MATERIA = "nombre_materia"
        const val COL_CREDITOS = "creditos"

        // Tabla Calificaciones
        const val TABLE_CALIFICACIONES = "calificaciones"
        const val COL_ID_CALIFICACION = "id"
        const val COL_MATRICULA_FK = "matricula"
        const val COL_CODIGO_FK = "codigo"
        const val COL_U1 = "unidad1"
        const val COL_U2 = "unidad2"
        const val COL_U3 = "unidad3"
        const val COL_U4 = "unidad4"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Crear tabla Usuarios
        val createUsuarios = """
            CREATE TABLE $TABLE_USUARIOS (
                $COL_USUARIO TEXT PRIMARY KEY,
                $COL_PASSWORD TEXT NOT NULL
            )
        """.trimIndent()

        // Crear tabla Alumnos
        val createAlumnos = """
            CREATE TABLE $TABLE_ALUMNOS (
                $COL_MATRICULA TEXT PRIMARY KEY,
                $COL_NOMBRE TEXT NOT NULL,
                $COL_CARRERA TEXT NOT NULL,
                $COL_EMAIL TEXT NOT NULL
            )
        """.trimIndent()

        // Crear tabla Materias
        val createMaterias = """
            CREATE TABLE $TABLE_MATERIAS (
                $COL_CODIGO TEXT PRIMARY KEY,
                $COL_NOMBRE_MATERIA TEXT NOT NULL,
                $COL_CREDITOS INTEGER NOT NULL
            )
        """.trimIndent()

        // Crear tabla Calificaciones
        val createCalificaciones = """
            CREATE TABLE $TABLE_CALIFICACIONES (
                $COL_ID_CALIFICACION INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_MATRICULA_FK TEXT NOT NULL,
                $COL_CODIGO_FK TEXT NOT NULL,
                $COL_U1 REAL NOT NULL,
                $COL_U2 REAL NOT NULL,
                $COL_U3 REAL NOT NULL,
                $COL_U4 REAL NOT NULL,
                FOREIGN KEY($COL_MATRICULA_FK) REFERENCES $TABLE_ALUMNOS($COL_MATRICULA),
                FOREIGN KEY($COL_CODIGO_FK) REFERENCES $TABLE_MATERIAS($COL_CODIGO),
                UNIQUE($COL_MATRICULA_FK, $COL_CODIGO_FK)
            )
        """.trimIndent()

        db?.execSQL(createUsuarios)
        db?.execSQL(createAlumnos)
        db?.execSQL(createMaterias)
        db?.execSQL(createCalificaciones)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CALIFICACIONES")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_MATERIAS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_ALUMNOS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USUARIOS")
        onCreate(db)
    }

    // ===== OPERACIONES USUARIOS =====
    fun insertarUsuario(usuario: String, password: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_USUARIO, usuario)
            put(COL_PASSWORD, password)
        }
        val result = db.insert(TABLE_USUARIOS, null, values)
        return result != -1L
    }

    fun validarUsuario(usuario: String, password: String): Boolean {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USUARIOS,
            null,
            "$COL_USUARIO = ? AND $COL_PASSWORD = ?",
            arrayOf(usuario, password),
            null, null, null
        )
        val existe = cursor.count > 0
        cursor.close()
        return existe
    }

    // ===== OPERACIONES ALUMNOS =====
    fun insertarAlumno(matricula: String, nombre: String, carrera: String, email: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_MATRICULA, matricula)
            put(COL_NOMBRE, nombre)
            put(COL_CARRERA, carrera)
            put(COL_EMAIL, email)
        }
        val result = db.insert(TABLE_ALUMNOS, null, values)
        return result != -1L
    }

    fun obtenerAlumnos(): Cursor {
        val db = readableDatabase
        return db.query(TABLE_ALUMNOS, null, null, null, null, null, COL_NOMBRE)
    }

    fun obtenerAlumnoPorMatricula(matricula: String): Cursor {
        val db = readableDatabase
        return db.query(
            TABLE_ALUMNOS,
            null,
            "$COL_MATRICULA = ?",
            arrayOf(matricula),
            null, null, null
        )
    }

    fun eliminarAlumno(matricula: String): Boolean {
        val db = writableDatabase
        val result = db.delete(TABLE_ALUMNOS, "$COL_MATRICULA = ?", arrayOf(matricula))
        return result > 0
    }

    // ===== OPERACIONES MATERIAS =====
    fun insertarMateria(codigo: String, nombre: String, creditos: Int): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_CODIGO, codigo)
            put(COL_NOMBRE_MATERIA, nombre)
            put(COL_CREDITOS, creditos)
        }
        val result = db.insert(TABLE_MATERIAS, null, values)
        return result != -1L
    }

    fun obtenerMaterias(): Cursor {
        val db = readableDatabase
        return db.query(TABLE_MATERIAS, null, null, null, null, null, COL_NOMBRE_MATERIA)
    }

    fun obtenerMateriaPorCodigo(codigo: String): Cursor {
        val db = readableDatabase
        return db.query(
            TABLE_MATERIAS,
            null,
            "$COL_CODIGO = ?",
            arrayOf(codigo),
            null, null, null
        )
    }

    fun eliminarMateria(codigo: String): Boolean {
        val db = writableDatabase
        val result = db.delete(TABLE_MATERIAS, "$COL_CODIGO = ?", arrayOf(codigo))
        return result > 0
    }

    // ===== OPERACIONES CALIFICACIONES =====
    fun insertarCalificacion(
        matricula: String,
        codigo: String,
        u1: Double,
        u2: Double,
        u3: Double,
        u4: Double
    ): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_MATRICULA_FK, matricula)
            put(COL_CODIGO_FK, codigo)
            put(COL_U1, u1)
            put(COL_U2, u2)
            put(COL_U3, u3)
            put(COL_U4, u4)
        }
        val result = db.insertWithOnConflict(
            TABLE_CALIFICACIONES,
            null,
            values,
            SQLiteDatabase.CONFLICT_REPLACE
        )
        return result != -1L
    }

    fun obtenerCalificacionesPorAlumno(matricula: String): Cursor {
        val db = readableDatabase
        return db.rawQuery(
            """
            SELECT c.*, m.$COL_NOMBRE_MATERIA, a.$COL_NOMBRE, a.$COL_CARRERA, a.$COL_EMAIL
            FROM $TABLE_CALIFICACIONES c
            INNER JOIN $TABLE_MATERIAS m ON c.$COL_CODIGO_FK = m.$COL_CODIGO
            INNER JOIN $TABLE_ALUMNOS a ON c.$COL_MATRICULA_FK = a.$COL_MATRICULA
            WHERE c.$COL_MATRICULA_FK = ?
            """.trimIndent(),
            arrayOf(matricula)
        )
    }

    fun eliminarCalificacion(matricula: String, codigo: String): Boolean {
        val db = writableDatabase
        val result = db.delete(
            TABLE_CALIFICACIONES,
            "$COL_MATRICULA_FK = ? AND $COL_CODIGO_FK = ?",
            arrayOf(matricula, codigo)
        )
        return result > 0
    }
}
