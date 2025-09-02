package com.example.myapplicasion

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Menu : AppCompatActivity() {

    lateinit var boton1: Button
    lateinit var boton2: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        boton1 = findViewById(R.id.Button1)
        boton2 = findViewById(R.id.Button2)

        boton1.setOnClickListener {
            val llamado1 = Intent(this, MainActivity::class.java)
            startActivity(llamado1)
        }
        boton2.setOnClickListener {
            val llamado2 = Intent(this, MainActivity2::class.java)
            startActivity(llamado2)
        }
    }
}