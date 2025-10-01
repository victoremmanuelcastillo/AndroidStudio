package com.example.myapplicasion

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListActivity : AppCompatActivity() {

    private lateinit var editTextItem: EditText
    private lateinit var btnAgregar: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemAdapter
    private val itemList = mutableListOf<Item>()
    private var itemCounter = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        // Inicializar vistas
        editTextItem = findViewById(R.id.editTextItem)
        btnAgregar = findViewById(R.id.btnAgregar)
        recyclerView = findViewById(R.id.recyclerView)

        // Configurar RecyclerView
        adapter = ItemAdapter(itemList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Configurar el botón agregar
        btnAgregar.setOnClickListener {
            val text = editTextItem.text.toString()
            if (text.isNotEmpty()) {
                val newItem = Item(
                    title = text,
                    subtitle = ""
                )
                adapter.addItem(newItem)
                editTextItem.text.clear()
            }
        }
    }
}
