package com.example.myapplicasion

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DashboardCalescuActivity : AppCompatActivity() {

    private lateinit var tvBienvenida: TextView
    private lateinit var btnAlumnos: Button
    private lateinit var btnMaterias: Button
    private lateinit var btnCalificar: Button
    private lateinit var btnVerNotas: Button
    private lateinit var btnCerrarSesion: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_calescu)

        sharedPreferences = getSharedPreferences("CalescuPrefs", MODE_PRIVATE)
        val usuario = sharedPreferences.getString("usuario", "Usuario")

        tvBienvenida = findViewById(R.id.tvBienvenidaUsuario)
        btnAlumnos = findViewById(R.id.btnAlumnos)
        btnMaterias = findViewById(R.id.btnMaterias)
        btnCalificar = findViewById(R.id.btnCalificar)
        btnVerNotas = findViewById(R.id.btnVerNotas)
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)

        tvBienvenida.text = "Bienvenido: $usuario"

        btnAlumnos.setOnClickListener {
            startActivity(Intent(this, AlumnosActivity::class.java))
        }

        btnMaterias.setOnClickListener {
            startActivity(Intent(this, MateriasActivity::class.java))
        }

        btnCalificar.setOnClickListener {
            startActivity(Intent(this, CalificarActivity::class.java))
        }

        btnVerNotas.setOnClickListener {
            startActivity(Intent(this, VerNotasActivity::class.java))
        }

        btnCerrarSesion.setOnClickListener {
            sharedPreferences.edit().clear().apply()
            val intent = Intent(this, LoginCalescuActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
