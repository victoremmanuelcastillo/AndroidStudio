package com.example.myapplicasion

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity2 : AppCompatActivity() {
    
    private lateinit var mainImage: CircleImageView
    private lateinit var bottomImagesLayout: LinearLayout
    private var imagesVisible = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        
        mainImage = findViewById(R.id.mainImage)
        bottomImagesLayout = findViewById(R.id.bottomImagesLayout)
        
        // Inicialmente ocultar las imágenes de abajo
        bottomImagesLayout.visibility = View.GONE
        
        // Configurar click en la imagen principal
        mainImage.setOnClickListener {
            toggleBottomImages()
        }
    }
    
    private fun toggleBottomImages() {
        if (imagesVisible) {
            bottomImagesLayout.visibility = View.GONE
            imagesVisible = false
        } else {
            bottomImagesLayout.visibility = View.VISIBLE
            imagesVisible = true
        }
    }
}