package com.example.myapplicasion

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Option5Activity : AppCompatActivity() {

    private lateinit var selectImagesView: ImageView
    private lateinit var viewButton: Button
    private lateinit var categoryRadioGroup: RadioGroup
    private lateinit var radioEscuela: RadioButton
    private lateinit var radioCasa: RadioButton
    private lateinit var escuelaRecyclerView: RecyclerView
    private lateinit var casaRecyclerView: RecyclerView

    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>

    private val escuelaImages = mutableListOf<Uri>()
    private val casaImages = mutableListOf<Uri>()

    private lateinit var escuelaAdapter: ImageAdapter
    private lateinit var casaAdapter: ImageAdapter

    private var areImagesVisible = false

    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_option5)

        initializeViews()
        setupRecyclerViews()
        setupGalleryLauncher()
        setupClickListeners()
    }

    private fun initializeViews() {
        selectImagesView = findViewById(R.id.selectImagesView)
        viewButton = findViewById(R.id.viewButton)
        categoryRadioGroup = findViewById(R.id.categoryRadioGroup)
        radioEscuela = findViewById(R.id.radioEscuela)
        radioCasa = findViewById(R.id.radioCasa)
        escuelaRecyclerView = findViewById(R.id.escuelaRecyclerView)
        casaRecyclerView = findViewById(R.id.casaRecyclerView)
    }

    private fun setupRecyclerViews() {
        escuelaAdapter = ImageAdapter(escuelaImages)
        casaAdapter = ImageAdapter(casaImages)

        escuelaRecyclerView.apply {
            layoutManager = GridLayoutManager(this@Option5Activity, 2)
            adapter = escuelaAdapter
        }

        casaRecyclerView.apply {
            layoutManager = GridLayoutManager(this@Option5Activity, 2)
            adapter = casaAdapter
        }
    }

    private fun setupGalleryLauncher() {
        galleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                if (data != null) {
                    val clipData = data.clipData
                    val selectedCategory = getCurrentCategory()

                    if (clipData != null) {
                        // Múltiples imágenes seleccionadas
                        for (i in 0 until clipData.itemCount) {
                            val imageUri = clipData.getItemAt(i).uri
                            addImageToCategory(imageUri, selectedCategory)
                        }
                    } else {
                        // Una sola imagen seleccionada
                        data.data?.let { uri ->
                            addImageToCategory(uri, selectedCategory)
                        }
                    }

                    updateAdapters()
                    Toast.makeText(this, "Imágenes agregadas a $selectedCategory", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupClickListeners() {
        selectImagesView.setOnClickListener {
            if (hasStoragePermission()) {
                openGallery()
            } else {
                requestStoragePermission()
            }
        }

        viewButton.setOnClickListener {
            toggleImagesVisibility()
        }

        categoryRadioGroup.setOnCheckedChangeListener { _, _ ->
            // Ocultar automáticamente las imágenes al cambiar de categoría
            if (areImagesVisible) {
                hideImages()
                viewButton.text = "Mostrar"
                areImagesVisible = false
            }
        }
    }

    private fun getCurrentCategory(): String {
        return if (radioEscuela.isChecked) "Escuela" else "Casa"
    }

    private fun addImageToCategory(uri: Uri, category: String) {
        when (category) {
            "Escuela" -> escuelaImages.add(uri)
            "Casa" -> casaImages.add(uri)
        }
    }

    private fun updateAdapters() {
        escuelaAdapter.notifyDataSetChanged()
        casaAdapter.notifyDataSetChanged()
    }

    private fun toggleImagesVisibility() {
        areImagesVisible = !areImagesVisible

        if (areImagesVisible) {
            showImages()
            viewButton.text = "Ocultar"
        } else {
            hideImages()
            viewButton.text = "Mostrar"
        }
    }

    private fun showImages() {
        if (radioEscuela.isChecked && escuelaImages.isNotEmpty()) {
            escuelaRecyclerView.visibility = View.VISIBLE
            casaRecyclerView.visibility = View.GONE
        } else if (radioCasa.isChecked && casaImages.isNotEmpty()) {
            casaRecyclerView.visibility = View.VISIBLE
            escuelaRecyclerView.visibility = View.GONE
        } else {
            Toast.makeText(this, "No hay imágenes en la categoría seleccionada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun hideImages() {
        escuelaRecyclerView.visibility = View.GONE
        casaRecyclerView.visibility = View.GONE
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
}

class ImageAdapter(private val images: MutableList<Uri>) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.imageView.setImageURI(images[position])
    }

    override fun getItemCount(): Int = images.size
}