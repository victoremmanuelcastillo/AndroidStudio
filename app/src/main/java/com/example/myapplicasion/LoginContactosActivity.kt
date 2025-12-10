package com.example.myapplicasion

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplicasion.api.ContactosApiService

class LoginContactosActivity : AppCompatActivity() {

    private lateinit var etUsuario: EditText
    private lateinit var etContrasena: EditText
    private lateinit var btnLogin: Button
    private lateinit var apiService: ContactosApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_contactos)

        // Inicializar vistas
        etUsuario = findViewById(R.id.etUsuario)
        etContrasena = findViewById(R.id.etContrasena)
        btnLogin = findViewById(R.id.btnLogin)

        // Inicializar API
        apiService = ContactosApiService(this)

        // Configurar botón de login
        btnLogin.setOnClickListener {
            realizarLogin()
        }
    }

    private fun realizarLogin() {
        val usuario = etUsuario.text.toString().trim()
        val contrasena = etContrasena.text.toString().trim()

        // Validar campos
        if (usuario.isEmpty()) {
            etUsuario.error = "El usuario es requerido"
            etUsuario.requestFocus()
            return
        }

        if (contrasena.isEmpty()) {
            etContrasena.error = "La contraseña es requerida"
            etContrasena.requestFocus()
            return
        }

        // Deshabilitar botón mientras se procesa
        btnLogin.isEnabled = false
        btnLogin.text = "Iniciando..."

        // Realizar login
        apiService.login(
            nombreUsuario = usuario,
            contrasena = contrasena,
            onSuccess = { response ->
                btnLogin.isEnabled = true
                btnLogin.text = "Iniciar Sesión"

                if (response.success) {
                    // Guardar datos de sesión
                    guardarSesion(response.id_usuario, response.nombre_usuario)

                    // Mostrar mensaje
                    Toast.makeText(this, "Bienvenido ${response.nombre_usuario}", Toast.LENGTH_SHORT).show()

                    // Ir al menú de contactos
                    val intent = Intent(this, MenuContactosActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, response.message, Toast.LENGTH_LONG).show()
                }
            },
            onError = { error ->
                btnLogin.isEnabled = true
                btnLogin.text = "Iniciar Sesión"
                Toast.makeText(this, "Error: $error", Toast.LENGTH_LONG).show()
            }
        )
    }

    private fun guardarSesion(idUsuario: Int, nombreUsuario: String) {
        val prefs = getSharedPreferences("ContactosPrefs", Context.MODE_PRIVATE)
        prefs.edit().apply {
            putInt("id_usuario", idUsuario)
            putString("nombre_usuario", nombreUsuario)
            putBoolean("sesion_activa", true)
            apply()
        }
    }
}
