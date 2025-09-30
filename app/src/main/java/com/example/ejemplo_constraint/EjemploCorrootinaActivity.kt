package com.example.ejemplo_constraint

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EjemploCorrootinaActivity : AppCompatActivity() {

    private lateinit var textViewTimer: TextView
    private lateinit var buttonStart: Button
    private lateinit var buttonEnd: Button
    private lateinit var imageViewGallery: ImageView

    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>

    private var timerJob: Job? = null
    private var carouselJob: Job? = null
    private var counter = 0
    private var isRunning = false
    private var endClickCount = 0
    private var lastEndClickTime = 0L

    private val selectedImages = mutableListOf<Uri>()
    private var currentImageIndex = 0

    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ejemplo_corrotina)

        initializeViews()
        setupClickListeners()
        setupGalleryLauncher()
    }

    private fun initializeViews() {
        textViewTimer = findViewById(R.id.textViewTimer)
        buttonStart = findViewById(R.id.buttonStart)
        buttonEnd = findViewById(R.id.buttonEnd)
        imageViewGallery = findViewById(R.id.imageViewGallery)

        updateTimerDisplay()
    }

    private fun setupClickListeners() {
        buttonStart.setOnClickListener {
            startTimer()
            if (selectedImages.isNotEmpty()) {
                startCarousel()
            }
        }

        buttonEnd.setOnClickListener {
            handleEndButtonClick()
        }

        imageViewGallery.setOnClickListener {
            if (hasStoragePermission()) {
                openGallery()
            } else {
                requestStoragePermission()
            }
        }
    }

    private fun setupGalleryLauncher() {
        galleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                if (data != null) {
                    selectedImages.clear()
                    val clipData = data.clipData

                    if (clipData != null) {
                        for (i in 0 until clipData.itemCount) {
                            val imageUri = clipData.getItemAt(i).uri
                            selectedImages.add(imageUri)
                        }
                    } else {
                        data.data?.let { uri ->
                            selectedImages.add(uri)
                        }
                    }

                    if (selectedImages.isNotEmpty()) {
                        currentImageIndex = 0
                        displayCurrentImage()
                        Toast.makeText(this, "${selectedImages.size} imágenes seleccionadas", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun startTimer() {
        if (!isRunning) {
            isRunning = true
            timerJob = lifecycleScope.launch {
                while (isRunning) {
                    delay(1000)
                    if (isRunning) {
                        counter++
                        updateTimerDisplay()
                    }
                }
            }
        }
    }

    private fun startCarousel() {
        if (selectedImages.isNotEmpty() && carouselJob == null) {
            carouselJob = lifecycleScope.launch {
                while (isRunning && selectedImages.isNotEmpty()) {
                    delay(5000)
                    if (isRunning && selectedImages.isNotEmpty()) {
                        currentImageIndex = (currentImageIndex + 1) % selectedImages.size
                        displayCurrentImage()
                    }
                }
            }
        }
    }

    private fun stopTimer() {
        isRunning = false
        timerJob?.cancel()
        timerJob = null
        stopCarousel()
    }

    private fun stopCarousel() {
        carouselJob?.cancel()
        carouselJob = null
    }

    private fun resetTimer() {
        stopTimer()
        counter = 0
        updateTimerDisplay()
        endClickCount = 0
    }

    private fun handleEndButtonClick() {
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastEndClickTime < 500) {
            resetTimer()
        } else {
            stopTimer()
            endClickCount = 1
        }

        lastEndClickTime = currentTime
    }

    private fun updateTimerDisplay() {
        val displayText = if (counter < 10) "0$counter" else counter.toString()
        textViewTimer.text = displayText
    }

    private fun displayCurrentImage() {
        if (selectedImages.isNotEmpty() && currentImageIndex < selectedImages.size) {
            imageViewGallery.setImageURI(selectedImages[currentImageIndex])
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
        val intent = Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
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
                    openGallery()
                } else {
                    Toast.makeText(this, "Permisos necesarios para acceder a las imágenes", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimer()
    }
}
