package com.example.myapplicasion

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ArticulItemActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ArticleAdapter
    private val articlesList = mutableListOf<Article>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_articul_item)

        recyclerView = findViewById(R.id.recyclerViewArticles)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Iniciar con lista vacía
        adapter = ArticleAdapter(articlesList)
        recyclerView.adapter = adapter

        val btnCargar = findViewById<Button>(R.id.btnCargar)
        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val btnLimpiar = findViewById<Button>(R.id.btnLimpiar)

        // Botón Cargar - agrega los 5 artículos cada vez que se presiona
        btnCargar.setOnClickListener {
            val newArticles = listOf(
                Article(
                    imageResource = R.mipmap.ego,
                    title = "Profesor Ego Antonio",
                    description = "El mejor profesor de programación móvil, con años de experiencia enseñando desarrollo Android y tecnologías modernas a estudiantes universitarios."
                ),
                Article(
                    imageResource = R.mipmap.egob,
                    title = "Ego Antonio Versión B",
                    description = "Segunda fotografía del profesor mostrando su dedicación y pasión por la enseñanza de desarrollo de aplicaciones móviles en el aula de clases."
                ),
                Article(
                    imageResource = R.mipmap.ejof,
                    title = "Fotografía del Profesor",
                    description = "Imagen profesional capturando la esencia de un educador comprometido con la formación de futuros desarrolladores de software y aplicaciones móviles."
                ),
                Article(
                    imageResource = R.mipmap.pastelazul,
                    title = "Pastel Azul Decorado",
                    description = "Delicioso pastel de tonos azules con decoraciones elegantes, perfecto para celebraciones especiales y momentos importantes con amigos y familia."
                ),
                Article(
                    imageResource = R.mipmap.images,
                    title = "Colección de Imágenes",
                    description = "Galería variada de fotografías que muestran diferentes momentos, lugares y experiencias capturadas a lo largo del tiempo en diversos contextos."
                )
            )
            articlesList.addAll(newArticles)
            adapter.notifyDataSetChanged()
        }

        // Botón Add - agrega un artículo con valores por defecto
        btnAdd.setOnClickListener {
            val newArticle = Article(
                imageResource = R.mipmap.ic_launcher,
                title = "Título",
                description = "Texto normal"
            )
            articlesList.add(newArticle)
            adapter.notifyDataSetChanged()
        }

        // Botón Limpiar - vacía toda la lista
        btnLimpiar.setOnClickListener {
            articlesList.clear()
            adapter.notifyDataSetChanged()
        }
    }
}
