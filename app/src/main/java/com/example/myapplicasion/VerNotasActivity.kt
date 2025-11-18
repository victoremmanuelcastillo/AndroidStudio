package com.example.myapplicasion

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class VerNotasActivity : AppCompatActivity() {

    private lateinit var btnSeleccionarAlumno: Button
    private lateinit var dbHelper: CalescuDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_notas)

        dbHelper = CalescuDatabaseHelper(this)
        btnSeleccionarAlumno = findViewById(R.id.btnSeleccionarAlumno)

        btnSeleccionarAlumno.setOnClickListener {
            mostrarListaAlumnos()
        }
    }

    private fun mostrarListaAlumnos() {
        val cursor = dbHelper.obtenerAlumnos()
        if (cursor.count == 0) {
            Toast.makeText(this, "No hay alumnos registrados", Toast.LENGTH_SHORT).show()
            cursor.close()
            return
        }

        val alumnos = mutableListOf<Pair<String, String>>() // matricula, nombre
        while (cursor.moveToNext()) {
            val matricula = cursor.getString(cursor.getColumnIndexOrThrow(CalescuDatabaseHelper.COL_MATRICULA))
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow(CalescuDatabaseHelper.COL_NOMBRE))
            alumnos.add(Pair(matricula, nombre))
        }
        cursor.close()

        val nombresAlumnos = alumnos.map { "${it.first} - ${it.second}" }.toTypedArray()

        AlertDialog.Builder(this)
            .setTitle("Seleccionar Alumno")
            .setItems(nombresAlumnos) { _, which ->
                val matricula = alumnos[which].first
                mostrarReporteCalificaciones(matricula)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun mostrarReporteCalificaciones(matricula: String) {
        val cursor = dbHelper.obtenerCalificacionesPorAlumno(matricula)

        if (cursor.count == 0) {
            Toast.makeText(this, "Este alumno no tiene calificaciones", Toast.LENGTH_SHORT).show()
            cursor.close()
            return
        }

        val reporte = StringBuilder()
        var nombreAlumno = ""
        var carrera = ""
        var email = ""
        var sumaPromedios = 0.0
        var contadorMaterias = 0

        reporte.append("📊 CALIFICACIONES\n\n")

        // Obtener información del alumno (del primer registro)
        if (cursor.moveToFirst()) {
            nombreAlumno = cursor.getString(cursor.getColumnIndexOrThrow(CalescuDatabaseHelper.COL_NOMBRE))
            carrera = cursor.getString(cursor.getColumnIndexOrThrow(CalescuDatabaseHelper.COL_CARRERA))
            email = cursor.getString(cursor.getColumnIndexOrThrow(CalescuDatabaseHelper.COL_EMAIL))

            reporte.append("👨‍🎓 Alumno: $nombreAlumno\n")
            reporte.append("🎫 Matrícula: $matricula\n")
            reporte.append("🎓 Carrera: $carrera\n")
            reporte.append("📧 Email: $email\n\n")
            reporte.append("━━━ CALIFICACIONES ━━━\n\n")

            // Procesar todas las materias
            cursor.moveToPosition(-1) // Regresar al inicio
            while (cursor.moveToNext()) {
                val nombreMateria = cursor.getString(cursor.getColumnIndexOrThrow(CalescuDatabaseHelper.COL_NOMBRE_MATERIA))
                val u1 = cursor.getDouble(cursor.getColumnIndexOrThrow(CalescuDatabaseHelper.COL_U1))
                val u2 = cursor.getDouble(cursor.getColumnIndexOrThrow(CalescuDatabaseHelper.COL_U2))
                val u3 = cursor.getDouble(cursor.getColumnIndexOrThrow(CalescuDatabaseHelper.COL_U3))
                val u4 = cursor.getDouble(cursor.getColumnIndexOrThrow(CalescuDatabaseHelper.COL_U4))

                val promedio = (u1 + u2 + u3 + u4) / 4.0
                val estado = obtenerEstado(promedio)

                reporte.append("📚 $nombreMateria\n")
                reporte.append("├ Unidad 1: ${String.format("%.1f", u1)}\n")
                reporte.append("├ Unidad 2: ${String.format("%.1f", u2)}\n")
                reporte.append("├ Unidad 3: ${String.format("%.1f", u3)}\n")
                reporte.append("├ Unidad 4: ${String.format("%.1f", u4)}\n")
                reporte.append("├ Promedio: ${String.format("%.1f", promedio)}\n")
                reporte.append("└ Estado: $estado\n\n")

                sumaPromedios += promedio
                contadorMaterias++
            }
        }
        cursor.close()

        // Calcular promedio general
        val promedioGeneral = if (contadorMaterias > 0) sumaPromedios / contadorMaterias else 0.0
        val estadoGeneral = obtenerEstado(promedioGeneral)

        reporte.append("━━━ RESUMEN GENERAL ━━━\n\n")
        reporte.append("📊 PROMEDIO GENERAL: ${String.format("%.1f", promedioGeneral)}\n")
        reporte.append("🎯 ESTADO ACADÉMICO: $estadoGeneral\n")
        reporte.append("✅ MATERIAS CURSADAS: $contadorMaterias\n")

        // Mostrar el reporte en un diálogo
        AlertDialog.Builder(this)
            .setTitle("📊 Calificaciones de $nombreAlumno")
            .setMessage(reporte.toString())
            .setPositiveButton("Cerrar", null)
            .show()
    }

    private fun obtenerEstado(promedio: Double): String {
        return when {
            promedio >= 90 -> "🎉 Excelente"
            promedio >= 80 -> "😊 Muy Bueno"
            promedio >= 70 -> "👍 Bueno"
            promedio >= 60 -> "😐 Regular"
            else -> "😞 Reprobado"
        }
    }
}
