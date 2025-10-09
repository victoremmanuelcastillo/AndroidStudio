package com.example.myapplicasion

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Option7Activity : AppCompatActivity() {

    private lateinit var circleImageView: ImageView
    private lateinit var editTitle: EditText
    private lateinit var editDescription: EditText
    private lateinit var btnAdd: Button
    private lateinit var recyclerViewItems: RecyclerView

    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private var selectedImageUri: Uri? = null

    private val itemsList = mutableListOf<Option7Item>()
    private lateinit var adapter: Option7Adapter

    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_option7)

        initializeViews()
        setupRecyclerView()
        setupGalleryLauncher()
        setupClickListeners()
    }

    private fun initializeViews() {
        circleImageView = findViewById(R.id.circleImageView)
        editTitle = findViewById(R.id.editTitle)
        editDescription = findViewById(R.id.editDescription)
        btnAdd = findViewById(R.id.btnAdd)
        recyclerViewItems = findViewById(R.id.recyclerViewItems)
    }

    private fun setupRecyclerView() {
        adapter = Option7Adapter(itemsList)
        recyclerViewItems.layoutManager = LinearLayoutManager(this)
        recyclerViewItems.adapter = adapter
    }

    private fun setupGalleryLauncher() {
        galleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                data?.data?.let { uri ->
                    selectedImageUri = uri
                    circleImageView.setImageURI(uri)
                }
            }
        }
    }

    private fun setupClickListeners() {
        circleImageView.setOnClickListener {
            if (hasStoragePermission()) {
                openGallery()
            } else {
                requestStoragePermission()
            }
        }

        btnAdd.setOnClickListener {
            addItem()
        }
    }

    private fun addItem() {
        val title = editTitle.text.toString().trim()
        val description = editDescription.text.toString().trim()

        if (selectedImageUri == null) {
            Toast.makeText(this, "Por favor selecciona una imagen", Toast.LENGTH_SHORT).show()
            return
        }

        if (title.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa un título", Toast.LENGTH_SHORT).show()
            return
        }

        if (description.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa una descripción", Toast.LENGTH_SHORT).show()
            return
        }

        // Agregar el nuevo item al inicio de la lista (más nuevo arriba)
        val newItem = Option7Item(
            imageUri = selectedImageUri!!,
            title = title,
            description = description
        )
        itemsList.add(0, newItem)
        adapter.notifyItemInserted(0)
        recyclerViewItems.scrollToPosition(0)

        // Limpiar los campos
        editTitle.text.clear()
        editDescription.text.clear()
        circleImageView.setImageResource(R.mipmap.ic_launcher)
        selectedImageUri = null

        Toast.makeText(this, "Item agregado", Toast.LENGTH_SHORT).show()
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
}
