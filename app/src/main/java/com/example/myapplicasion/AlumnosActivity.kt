package com.example.myapplicasion

import android.app.AlertDialog
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AlumnosActivity : AppCompatActivity() {

    private lateinit var etMatricula: EditText
    private lateinit var etNombre: EditText
    private lateinit var etCarrera: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnGuardar: Button
    private lateinit var btnEliminar: Button
    private lateinit var btnConsultar: Button
    private lateinit var btnVer: Button
    private lateinit var dbHelper: CalescuDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alumnos)

        dbHelper = CalescuDatabaseHelper(this)

        etMatricula = findViewById(R.id.etMatricula)
        etNombre = findViewById(R.id.etNombreAlumno)
        etCarrera = findViewById(R.id.etCarrera)
        etEmail = findViewById(R.id.etEmail)
        btnGuardar = findViewById(R.id.btnGuardarAlumno)
        btnEliminar = findViewById(R.id.btnEliminarAlumno)
        btnConsultar = findViewById(R.id.btnConsultarAlumnos)
        btnVer = findViewById(R.id.btnVerAlumno)

        btnGuardar.setOnClickListener { guardarAlumno() }
        btnEliminar.setOnClickListener { eliminarAlumno() }
        btnConsultar.setOnClickListener { consultarAlumnos() }
        btnVer.setOnClickListener { verAlumno() }
    }

    private fun guardarAlumno() {
        val matricula = etMatricula.text.toString().trim()
        val nombre = etNombre.text.toString().trim()
        val carrera = etCarrera.text.toString().trim()
        val email = etEmail.text.toString().trim()

        if (matricula.isEmpty() || nombre.isEmpty() || carrera.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email inválido", Toast.LENGTH_SHORT).show()
            return
        }

        if (dbHelper.insertarAlumno(matricula, nombre, carrera, email)) {
            Toast.makeText(this, "Alumno guardado exitosamente", Toast.LENGTH_SHORT).show()
            limpiarCampos()
        } else {
            Toast.makeText(this, "Error: La matrícula ya existe", Toast.LENGTH_SHORT).show()
        }
    }

    private fun eliminarAlumno() {
        val matricula = etMatricula.text.toString().trim()

        if (matricula.isEmpty()) {
            Toast.makeText(this, "Ingrese la matrícula", Toast.LENGTH_SHORT).show()
            return
        }

        if (dbHelper.eliminarAlumno(matricula)) {
            Toast.makeText(this, "Alumno eliminado", Toast.LENGTH_SHORT).show()
            limpiarCampos()
        } else {
            Toast.makeText(this, "Alumno no encontrado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun consultarAlumnos() {
        val cursor = dbHelper.obtenerAlumnos()
        if (cursor.count == 0) {
            Toast.makeText(this, "No hay alumnos registrados", Toast.LENGTH_SHORT).show()
            return
        }

        val alumnos = StringBuilder()
        while (cursor.moveToNext()) {
            val matricula = cursor.getString(cursor.getColumnIndexOrThrow(CalescuDatabaseHelper.COL_MATRICULA))
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow(CalescuDatabaseHelper.COL_NOMBRE))
            val carrera = cursor.getString(cursor.getColumnIndexOrThrow(CalescuDatabaseHelper.COL_CARRERA))
            alumnos.append("Matrícula: $matricula\nNombre: $nombre\nCarrera: $carrera\n\n")
        }
        cursor.close()

        AlertDialog.Builder(this)
            .setTitle("Lista de Alumnos")
            .setMessage(alumnos.toString())
            .setPositiveButton("Cerrar", null)
            .show()
    }

    private fun verAlumno() {
        val matricula = etMatricula.text.toString().trim()

        if (matricula.isEmpty()) {
            Toast.makeText(this, "Ingrese la matrícula", Toast.LENGTH_SHORT).show()
            return
        }

        val cursor = dbHelper.obtenerAlumnoPorMatricula(matricula)
        if (cursor.moveToFirst()) {
            etNombre.setText(cursor.getString(cursor.getColumnIndexOrThrow(CalescuDatabaseHelper.COL_NOMBRE)))
            etCarrera.setText(cursor.getString(cursor.getColumnIndexOrThrow(CalescuDatabaseHelper.COL_CARRERA)))
            etEmail.setText(cursor.getString(cursor.getColumnIndexOrThrow(CalescuDatabaseHelper.COL_EMAIL)))
        } else {
            Toast.makeText(this, "Alumno no encontrado", Toast.LENGTH_SHORT).show()
        }
        cursor.close()
    }

    private fun limpiarCampos() {
        etMatricula.text.clear()
        etNombre.text.clear()
        etCarrera.text.clear()
        etEmail.text.clear()
    }
}
