package com.example.myapplicasion

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MateriasActivity : AppCompatActivity() {

    private lateinit var etCodigo: EditText
    private lateinit var etNombre: EditText
    private lateinit var etCreditos: EditText
    private lateinit var btnGuardar: Button
    private lateinit var btnEliminar: Button
    private lateinit var btnConsultar: Button
    private lateinit var btnVer: Button
    private lateinit var dbHelper: CalescuDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_materias)

        dbHelper = CalescuDatabaseHelper(this)

        etCodigo = findViewById(R.id.etCodigo)
        etNombre = findViewById(R.id.etNombreMateria)
        etCreditos = findViewById(R.id.etCreditos)
        btnGuardar = findViewById(R.id.btnGuardarMateria)
        btnEliminar = findViewById(R.id.btnEliminarMateria)
        btnConsultar = findViewById(R.id.btnConsultarMaterias)
        btnVer = findViewById(R.id.btnVerMateria)

        btnGuardar.setOnClickListener { guardarMateria() }
        btnEliminar.setOnClickListener { eliminarMateria() }
        btnConsultar.setOnClickListener { consultarMaterias() }
        btnVer.setOnClickListener { verMateria() }
    }

    private fun guardarMateria() {
        val codigo = etCodigo.text.toString().trim()
        val nombre = etNombre.text.toString().trim()
        val creditosStr = etCreditos.text.toString().trim()

        if (codigo.isEmpty() || nombre.isEmpty() || creditosStr.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val creditos = creditosStr.toIntOrNull()
        if (creditos == null || creditos <= 0) {
            Toast.makeText(this, "Créditos inválidos", Toast.LENGTH_SHORT).show()
            return
        }

        if (dbHelper.insertarMateria(codigo, nombre, creditos)) {
            Toast.makeText(this, "Materia guardada exitosamente", Toast.LENGTH_SHORT).show()
            limpiarCampos()
        } else {
            Toast.makeText(this, "Error: El código ya existe", Toast.LENGTH_SHORT).show()
        }
    }

    private fun eliminarMateria() {
        val codigo = etCodigo.text.toString().trim()

        if (codigo.isEmpty()) {
            Toast.makeText(this, "Ingrese el código", Toast.LENGTH_SHORT).show()
            return
        }

        if (dbHelper.eliminarMateria(codigo)) {
            Toast.makeText(this, "Materia eliminada", Toast.LENGTH_SHORT).show()
            limpiarCampos()
        } else {
            Toast.makeText(this, "Materia no encontrada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun consultarMaterias() {
        val cursor = dbHelper.obtenerMaterias()
        if (cursor.count == 0) {
            Toast.makeText(this, "No hay materias registradas", Toast.LENGTH_SHORT).show()
            return
        }

        val materias = StringBuilder()
        while (cursor.moveToNext()) {
            val codigo = cursor.getString(cursor.getColumnIndexOrThrow(CalescuDatabaseHelper.COL_CODIGO))
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow(CalescuDatabaseHelper.COL_NOMBRE_MATERIA))
            val creditos = cursor.getInt(cursor.getColumnIndexOrThrow(CalescuDatabaseHelper.COL_CREDITOS))
            materias.append("Código: $codigo\nNombre: $nombre\nCréditos: $creditos\n\n")
        }
        cursor.close()

        AlertDialog.Builder(this)
            .setTitle("Lista de Materias")
            .setMessage(materias.toString())
            .setPositiveButton("Cerrar", null)
            .show()
    }

    private fun verMateria() {
        val codigo = etCodigo.text.toString().trim()

        if (codigo.isEmpty()) {
            Toast.makeText(this, "Ingrese el código", Toast.LENGTH_SHORT).show()
            return
        }

        val cursor = dbHelper.obtenerMateriaPorCodigo(codigo)
        if (cursor.moveToFirst()) {
            etNombre.setText(cursor.getString(cursor.getColumnIndexOrThrow(CalescuDatabaseHelper.COL_NOMBRE_MATERIA)))
            etCreditos.setText(cursor.getInt(cursor.getColumnIndexOrThrow(CalescuDatabaseHelper.COL_CREDITOS)).toString())
        } else {
            Toast.makeText(this, "Materia no encontrada", Toast.LENGTH_SHORT).show()
        }
        cursor.close()
    }

    private fun limpiarCampos() {
        etCodigo.text.clear()
        etNombre.text.clear()
        etCreditos.text.clear()
    }
}
