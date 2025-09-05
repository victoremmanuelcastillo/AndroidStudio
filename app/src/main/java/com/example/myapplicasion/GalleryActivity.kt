package com.example.myapplicasion

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class GalleryActivity : AppCompatActivity() {
    
    private lateinit var imageView: ImageView
    private lateinit var selectImageButton: Button
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    
    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        
        initializeViews()
        setupGalleryLauncher()
        setupClickListeners()
    }
    
    private fun initializeViews() {
        imageView = findViewById(R.id.imageView)
        selectImageButton = findViewById(R.id.selectImageButton)
    }
    
    private fun setupGalleryLauncher() {
        galleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let { uri ->
                    imageView.setImageURI(uri)
                }
            }
        }
    }
    
    private fun setupClickListeners() {
        selectImageButton.setOnClickListener {
            if (hasStoragePermission()) {
                openGallery()
            } else {
                Toast.makeText(this, "Necesitamos permisos para acceder a tus fotos", Toast.LENGTH_SHORT).show()
                requestStoragePermission()
            }
        }
    }
    
    private fun hasStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
    
    private fun requestStoragePermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        
        ActivityCompat.requestPermissions(
            this,
            arrayOf(permission),
            PERMISSION_REQUEST_CODE
        )
    }
    
    
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        galleryLauncher.launch(intent)
    }
    
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "¡Perfecto! Ahora puedes seleccionar imágenes", Toast.LENGTH_SHORT).show()
                    openGallery()
                } else {
                    Toast.makeText(this, "Sin permisos no podemos mostrar tus fotos. Puedes activarlos en Configuración.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}