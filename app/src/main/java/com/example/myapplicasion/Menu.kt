package com.example.myapplicasion

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Menu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_menu)
            
            val boton1 = findViewById<Button>(R.id.Button1)
            val boton2 = findViewById<Button>(R.id.Button2)
            val boton3 = findViewById<Button>(R.id.Button3)
            val boton4 = findViewById<Button>(R.id.Button4)
            val boton5 = findViewById<Button>(R.id.Button5)
            val boton6 = findViewById<Button>(R.id.Button6)
            val boton7 = findViewById<Button>(R.id.Button7)
            val boton8 = findViewById<Button>(R.id.Button8)
            val boton9 = findViewById<Button>(R.id.Button9)
            val boton10 = findViewById<Button>(R.id.Button10)
            val boton11 = findViewById<Button>(R.id.Button11)
            val boton12 = findViewById<Button>(R.id.Button12)

            boton1.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            
            boton2.setOnClickListener {
                val intent = Intent(this, MainActivity2::class.java)
                startActivity(intent)
            }
            
            boton3.setOnClickListener {
                val intent = Intent(this, GalleryActivity::class.java)
                startActivity(intent)
            }

            boton4.setOnClickListener {
                val intent = Intent(this, MainActivity4::class.java)
                startActivity(intent)
            }

            boton5.setOnClickListener {
                val intent = Intent(this, Option5Activity::class.java)
                startActivity(intent)
            }

            boton6.setOnClickListener {
                val intent = Intent(this, EjemploCorrootinaActivity::class.java)
                startActivity(intent)
            }

            boton7.setOnClickListener {
                val intent = Intent(this, ListActivity::class.java)
                startActivity(intent)
            }

            boton8.setOnClickListener {
                val intent = Intent(this, StopwatchActivity::class.java)
                startActivity(intent)
            }

            boton9.setOnClickListener {
                val intent = Intent(this, ArticulItemActivity::class.java)
                startActivity(intent)
            }

            boton10.setOnClickListener {
                val intent = Intent(this, Option7Activity::class.java)
                startActivity(intent)
            }

            boton11.setOnClickListener {
                val intent = Intent(this, PreferenciasActivity::class.java)
                startActivity(intent)
            }

            boton12.setOnClickListener {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}