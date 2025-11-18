package com.example.myapplicasion

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CalificarActivity : AppCompatActivity() {

    private lateinit var spinnerAlumnos: Spinner
    private lateinit var spinnerMaterias: Spinner
    private lateinit var etU1: EditText
    private lateinit var etU2: EditText
    private lateinit var etU3: EditText
    private lateinit var etU4: EditText
    private lateinit var btnGuardar: Button
    private lateinit var btnEliminar: Button
    private lateinit var btnConsultar: Button
    private lateinit var btnVer: Button
    private lateinit var dbHelper: CalescuDatabaseHelper

    private val listaAlumnos = mutableListOf<Pair<String, String>>() // matricula, nombre
    private val listaMaterias = mutableListOf<Pair<String, String>>() // codigo, nombre
    private var matriculaSeleccionada = ""
    private var codigoSeleccionado = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calificar)

        dbHelper = CalescuDatabaseHelper(this)

        spinnerAlumnos = findViewById(R.id.spinnerAlumnos)
        spinnerMaterias = findViewById(R.id.spinnerMaterias)
        etU1 = findViewById(R.id.etUnidad1)
        etU2 = findViewById(R.id.etUnidad2)
        etU3 = findViewById(R.id.etUnidad3)
        etU4 = findViewById(R.id.etUnidad4)
        btnGuardar = findViewById(R.id.btnGuardarCalificacion)
        btnEliminar = findViewById(R.id.btnEliminarCalificacion)
        btnConsultar = findViewById(R.id.btnConsultarCalificacion)
        btnVer = findViewById(R.id.btnVerCalificacion)

        cargarSpinners()

        btnGuardar.setOnClickListener { guardarCalificacion() }
        btnEliminar.setOnClickListener { eliminarCalificacion() }
        btnConsultar.setOnClickListener { consultarCalificaciones() }
        btnVer.setOnClickListener { verCalificacion() }
    }

    private fun cargarSpinners() {
        // Cargar alumnos
        val cursorAlumnos = dbHelper.obtenerAlumnos()
        listaAlumnos.clear()
        val nombresAlumnos = mutableListOf<String>()

        while (cursorAlumnos.moveToNext()) {
            val matricula = cursorAlumnos.getString(cursorAlumnos.getColumnIndexOrThrow(CalescuDatabaseHelper.COL_MATRICULA))
            val nombre = cursorAlumnos.getString(cursorAlumnos.getColumnIndexOrThrow(CalescuDatabaseHelper.COL_NOMBRE))
            listaAlumnos.add(Pair(matricula, nombre))
            nombresAlumnos.add("$matricula - $nombre")
        }
        cursorAlumnos.close()

        val adapterAlumnos = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombresAlumnos)
        adapterAlumnos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerAlumnos.adapter = adapterAlumnos

        spinnerAlumnos.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (listaAlumnos.isNotEmpty()) {
                    matriculaSeleccionada = listaAlumnos[position].first
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Cargar materias
        val cursorMaterias = dbHelper.obtenerMaterias()
        listaMaterias.clear()
        val nombresMaterias = mutableListOf<String>()

        while (cursorMaterias.moveToNext()) {
            val codigo = cursorMaterias.getString(cursorMaterias.getColumnIndexOrThrow(CalescuDatabaseHelper.COL_CODIGO))
            val nombre = cursorMaterias.getString(cursorMaterias.getColumnIndexOrThrow(CalescuDatabaseHelper.COL_NOMBRE_MATERIA))
            listaMaterias.add(Pair(codigo, nombre))
            nombresMaterias.add("$codigo - $nombre")
        }
        cursorMaterias.close()

        val adapterMaterias = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombresMaterias)
        adapterMaterias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMaterias.adapter = adapterMaterias

        spinnerMaterias.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (listaMaterias.isNotEmpty()) {
                    codigoSeleccionado = listaMaterias[position].first
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun guardarCalificacion() {
        if (matriculaSeleccionada.isEmpty() || codigoSeleccionado.isEmpty()) {
            Toast.makeText(this, "Seleccione alumno y materia", Toast.LENGTH_SHORT).show()
            return
        }

        val u1Str = etU1.text.toString().trim()
        val u2Str = etU2.text.toString().trim()
        val u3Str = etU3.text.toString().trim()
        val u4Str = etU4.text.toString().trim()

        if (u1Str.isEmpty() || u2Str.isEmpty() || u3Str.isEmpty() || u4Str.isEmpty()) {
            Toast.makeText(this, "Complete todas las calificaciones", Toast.LENGTH_SHORT).show()
            return
        }

        val u1 = u1Str.toDoubleOrNull()
        val u2 = u2Str.toDoubleOrNull()
        val u3 = u3Str.toDoubleOrNull()
        val u4 = u4Str.toDoubleOrNull()

        if (u1 == null || u2 == null || u3 == null || u4 == null) {
            Toast.makeText(this, "Calificaciones inválidas", Toast.LENGTH_SHORT).show()
            return
        }

        if (u1 < 0 || u1 > 100 || u2 < 0 || u2 > 100 || u3 < 0 || u3 > 100 || u4 < 0 || u4 > 100) {
            Toast.makeText(this, "Las calificaciones deben estar entre 0 y 100", Toast.LENGTH_SHORT).show()
            return
        }

        if (dbHelper.insertarCalificacion(matriculaSeleccionada, codigoSeleccionado, u1, u2, u3, u4)) {
            Toast.makeText(this, "Calificación guardada exitosamente", Toast.LENGTH_SHORT).show()
            limpiarCampos()
        } else {
            Toast.makeText(this, "Error al guardar calificación", Toast.LENGTH_SHORT).show()
        }
    }

    private fun eliminarCalificacion() {
        if (matriculaSeleccionada.isEmpty() || codigoSeleccionado.isEmpty()) {
            Toast.makeText(this, "Seleccione alumno y materia", Toast.LENGTH_SHORT).show()
            return
        }

        if (dbHelper.eliminarCalificacion(matriculaSeleccionada, codigoSeleccionado)) {
            Toast.makeText(this, "Calificación eliminada", Toast.LENGTH_SHORT).show()
            limpiarCampos()
        } else {
            Toast.makeText(this, "Calificación no encontrada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun consultarCalificaciones() {
        if (matriculaSeleccionada.isEmpty() || codigoSeleccionado.isEmpty()) {
            Toast.makeText(this, "Seleccione alumno y materia", Toast.LENGTH_SHORT).show()
            return
        }

        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            """
            SELECT c.*, m.${CalescuDatabaseHelper.COL_NOMBRE_MATERIA}, a.${CalescuDatabaseHelper.COL_NOMBRE}
            FROM ${CalescuDatabaseHelper.TABLE_CALIFICACIONES} c
            INNER JOIN ${CalescuDatabaseHelper.TABLE_MATERIAS} m ON c.${CalescuDatabaseHelper.COL_CODIGO_FK} = m.${CalescuDatabaseHelper.COL_CODIGO}
            INNER JOIN ${CalescuDatabaseHelper.TABLE_ALUMNOS} a ON c.${CalescuDatabaseHelper.COL_MATRICULA_FK} = a.${CalescuDatabaseHelper.COL_MATRICULA}
            WHERE c.${CalescuDatabaseHelper.COL_MATRICULA_FK} = ? AND c.${CalescuDatabaseHelper.COL_CODIGO_FK} = ?
            """.trimIndent(),
            arrayOf(matriculaSeleccionada, codigoSeleccionado)
        )

        if (cursor.moveToFirst()) {
            val nombreAlumno = cursor.getString(cursor.getColumnIndexOrThrow(CalescuDatabaseHelper.COL_NOMBRE))
            val nombreMateria = cursor.getString(cursor.getColumnIndexOrThrow(CalescuDatabaseHelper.COL_NOMBRE_MATERIA))
            val u1 = cursor.getDouble(cursor.getColumnIndexOrThrow(CalescuDatabaseHelper.COL_U1))
            val u2 = cursor.getDouble(cursor.getColumnIndexOrThrow(CalescuDatabaseHelper.COL_U2))
            val u3 = cursor.getDouble(cursor.getColumnIndexOrThrow(CalescuDatabaseHelper.COL_U3))
            val u4 = cursor.getDouble(cursor.getColumnIndexOrThrow(CalescuDatabaseHelper.COL_U4))
            val promedio = (u1 + u2 + u3 + u4) / 4.0

            // Cargar los valores en los campos de texto
            etU1.setText(u1.toString())
            etU2.setText(u2.toString())
            etU3.setText(u3.toString())
            etU4.setText(u4.toString())

            val mensaje = """
                Alumno: $nombreAlumno
                Materia: $nombreMateria

                Unidad 1: $u1
                Unidad 2: $u2
                Unidad 3: $u3
                Unidad 4: $u4

                Promedio: ${String.format("%.2f", promedio)}
            """.trimIndent()

            android.app.AlertDialog.Builder(this)
                .setTitle("Calificación Encontrada")
                .setMessage(mensaje)
                .setPositiveButton("OK", null)
                .show()
        } else {
            Toast.makeText(this, "No hay calificación registrada para esta combinación", Toast.LENGTH_SHORT).show()
        }
        cursor.close()
    }

    private fun verCalificacion() {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            """
            SELECT c.*, m.${CalescuDatabaseHelper.COL_NOMBRE_MATERIA}, a.${CalescuDatabaseHelper.COL_NOMBRE}, a.${CalescuDatabaseHelper.COL_MATRICULA}
            FROM ${CalescuDatabaseHelper.TABLE_CALIFICACIONES} c
            INNER JOIN ${CalescuDatabaseHelper.TABLE_MATERIAS} m ON c.${CalescuDatabaseHelper.COL_CODIGO_FK} = m.${CalescuDatabaseHelper.COL_CODIGO}
            INNER JOIN ${CalescuDatabaseHelper.TABLE_ALUMNOS} a ON c.${CalescuDatabaseHelper.COL_MATRICULA_FK} = a.${CalescuDatabaseHelper.COL_MATRICULA}
            ORDER BY a.${CalescuDatabaseHelper.COL_NOMBRE}, m.${CalescuDatabaseHelper.COL_NOMBRE_MATERIA}
            """.trimIndent(),
            null
        )

        if (cursor.count == 0) {
            Toast.makeText(this, "No hay calificaciones registradas", Toast.LENGTH_SHORT).show()
            cursor.close()
            return
        }

        val calificaciones = StringBuilder()
        calificaciones.append("TODAS LAS CALIFICACIONES\n")
        calificaciones.append("========================\n\n")

        while (cursor.moveToNext()) {
            val matricula = cursor.getString(cursor.getColumnIndexOrThrow(CalescuDatabaseHelper.COL_MATRICULA))
            val nombreAlumno = cursor.getString(cursor.getColumnIndexOrThrow(CalescuDatabaseHelper.COL_NOMBRE))
            val nombreMateria = cursor.getString(cursor.getColumnIndexOrThrow(CalescuDatabaseHelper.COL_NOMBRE_MATERIA))
            val u1 = cursor.getDouble(cursor.getColumnIndexOrThrow(CalescuDatabaseHelper.COL_U1))
            val u2 = cursor.getDouble(cursor.getColumnIndexOrThrow(CalescuDatabaseHelper.COL_U2))
            val u3 = cursor.getDouble(cursor.getColumnIndexOrThrow(CalescuDatabaseHelper.COL_U3))
            val u4 = cursor.getDouble(cursor.getColumnIndexOrThrow(CalescuDatabaseHelper.COL_U4))
            val promedio = (u1 + u2 + u3 + u4) / 4.0

            calificaciones.append("Alumno: $nombreAlumno ($matricula)\n")
            calificaciones.append("Materia: $nombreMateria\n")
            calificaciones.append("U1: $u1 | U2: $u2 | U3: $u3 | U4: $u4\n")
            calificaciones.append("Promedio: ${String.format("%.2f", promedio)}\n")
            calificaciones.append("------------------------\n\n")
        }
        cursor.close()

        android.app.AlertDialog.Builder(this)
            .setTitle("Todas las Calificaciones")
            .setMessage(calificaciones.toString())
            .setPositiveButton("Cerrar", null)
            .show()
    }

    private fun limpiarCampos() {
        etU1.text.clear()
        etU2.text.clear()
        etU3.text.clear()
        etU4.text.clear()
    }
}
