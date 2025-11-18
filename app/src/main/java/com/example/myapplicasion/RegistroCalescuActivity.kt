package com.example.myapplicasion

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegistroCalescuActivity : AppCompatActivity() {

    private lateinit var etUsuario: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmar: EditText
    private lateinit var btnGuardar: Button
    private lateinit var dbHelper: CalescuDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_calescu)

        dbHelper = CalescuDatabaseHelper(this)

        etUsuario = findViewById(R.id.etUsuarioRegistro)
        etPassword = findViewById(R.id.etPasswordRegistro)
        etConfirmar = findViewById(R.id.etConfirmarPassword)
        btnGuardar = findViewById(R.id.btnGuardarRegistro)

        btnGuardar.setOnClickListener {
            registrarUsuario()
        }
    }

    private fun registrarUsuario() {
        val usuario = etUsuario.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confirmar = etConfirmar.text.toString().trim()

        if (usuario.isEmpty() || password.isEmpty() || confirmar.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmar) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }

        if (dbHelper.insertarUsuario(usuario, password)) {
            Toast.makeText(this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error: El usuario ya existe", Toast.LENGTH_SHORT).show()
        }
    }
}
