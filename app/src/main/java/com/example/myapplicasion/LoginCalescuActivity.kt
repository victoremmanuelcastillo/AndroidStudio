package com.example.myapplicasion

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginCalescuActivity : AppCompatActivity() {

    private lateinit var etUsuario: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnIniciarSesion: Button
    private lateinit var btnRegistrarse: Button
    private lateinit var dbHelper: CalescuDatabaseHelper
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_calescu)

        dbHelper = CalescuDatabaseHelper(this)
        sharedPreferences = getSharedPreferences("CalescuPrefs", MODE_PRIVATE)

        etUsuario = findViewById(R.id.etUsuario)
        etPassword = findViewById(R.id.etPassword)
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion)
        btnRegistrarse = findViewById(R.id.btnRegistrarse)

        btnIniciarSesion.setOnClickListener {
            iniciarSesion()
        }

        btnRegistrarse.setOnClickListener {
            val intent = Intent(this, RegistroCalescuActivity::class.java)
            startActivity(intent)
        }
    }

    private fun iniciarSesion() {
        val usuario = etUsuario.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (usuario.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (dbHelper.validarUsuario(usuario, password)) {
            sharedPreferences.edit().putString("usuario", usuario).apply()
            Toast.makeText(this, "Bienvenido $usuario", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, DashboardCalescuActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
        }
    }
}
