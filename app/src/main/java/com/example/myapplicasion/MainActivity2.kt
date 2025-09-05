package com.example.myapplicasion

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity2 : AppCompatActivity() {
    
    private var imagesVisible = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_main2)
            
            val mainImage = findViewById<ImageView>(R.id.mainImage)
            val bottomImagesLayout = findViewById<LinearLayout>(R.id.bottomImagesLayout)
            
            // Inicialmente ocultar las imágenes de abajo
            bottomImagesLayout.visibility = View.GONE
            
            // Configurar click en la imagen principal
            mainImage.setOnClickListener {
                if (imagesVisible) {
                    bottomImagesLayout.visibility = View.GONE
                    imagesVisible = false
                } else {
                    bottomImagesLayout.visibility = View.VISIBLE
                    imagesVisible = true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}