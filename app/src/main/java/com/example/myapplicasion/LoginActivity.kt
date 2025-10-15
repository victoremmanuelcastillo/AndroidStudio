package com.example.myapplicasion

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    private lateinit var etUsuario: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegistrar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializar vistas
        etUsuario = findViewById(R.id.etUsuario)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnRegistrar = findViewById(R.id.btnRegistrar)

        // Verificar si ya hay una sesión activa
        val sharedPref = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val usuarioActivo = sharedPref.getString("usuario_activo", null)

        if (usuarioActivo != null) {
            // Si ya hay sesión, ir directo a la pantalla principal
            irAPantallaPrincipal(usuarioActivo)
            return
        }

        // Botón de Login
        btnLogin.setOnClickListener {
            val usuario = etUsuario.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (usuario.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validar credenciales
            if (validarCredenciales(usuario, password)) {
                // Guardar sesión
                guardarSesion(usuario)
                irAPantallaPrincipal(usuario)
            } else {
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón de Registrar
        btnRegistrar.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validarCredenciales(usuario: String, password: String): Boolean {
        val sharedPref = getSharedPreferences("UsersData", Context.MODE_PRIVATE)
        val passwordGuardada = sharedPref.getString("user_${usuario}_pass", null)
        return passwordGuardada == password
    }

    private fun guardarSesion(usuario: String) {
        val sharedPref = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("usuario_activo", usuario)
            apply()
        }
    }

    private fun irAPantallaPrincipal(usuario: String) {
        val intent = Intent(this, PrincipalActivity::class.java)
        intent.putExtra("usuario", usuario)
        startActivity(intent)
        finish()
    }
}
