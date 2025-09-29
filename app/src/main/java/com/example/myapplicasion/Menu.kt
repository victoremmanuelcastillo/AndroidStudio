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
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}