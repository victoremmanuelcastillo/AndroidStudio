package com.example.myapplicasion

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicasion.adapters.NewsCategoryAdapter
import com.example.myapplicasion.api.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewsCategoryActivity : AppCompatActivity() {

    private lateinit var spinnerCategory: Spinner
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorTextView: TextView
    private val apiKey = "08ea3d4b76c84424b4529f63dfe449e5"

    private val categories = listOf(
        "general" to "General",
        "business" to "Negocios",
        "entertainment" to "Entretenimiento",
        "health" to "Salud",
        "science" to "Ciencia",
        "sports" to "Deportes",
        "technology" to "Tecnología"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_category)

        spinnerCategory = findViewById(R.id.spinnerCategory)
        recyclerView = findViewById(R.id.recyclerViewNews)
        progressBar = findViewById(R.id.progressBar)
        errorTextView = findViewById(R.id.errorTextView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        setupSpinner()
    }

    private fun setupSpinner() {
        val categoryNames = categories.map { it.second }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = adapter

        spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategory = categories[position].first
                loadNewsByCategory(selectedCategory)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No hacer nada
            }
        }
    }

    private fun loadNewsByCategory(category: String) {
        progressBar.visibility = View.VISIBLE
        errorTextView.visibility = View.GONE
        recyclerView.visibility = View.GONE

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.newsApiService.getTopHeadlinesByCategory(
                    country = "us",
                    category = category,
                    apiKey = apiKey
                )

                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE

                    if (response.status == "ok" && response.articles.isNotEmpty()) {
                        val adapter = NewsCategoryAdapter(response.articles)
                        recyclerView.adapter = adapter
                        recyclerView.visibility = View.VISIBLE
                    } else {
                        showError("No se encontraron noticias para esta categoría")
                    }
                }
            } catch (e: retrofit2.HttpException) {
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    val errorBody = e.response()?.errorBody()?.string()
                    showError("Error HTTP ${e.code()}: ${errorBody ?: e.message()}")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    showError("Error: ${e.message}")
                    e.printStackTrace()
                }
            }
        }
    }

    private fun showError(message: String) {
        errorTextView.text = message
        errorTextView.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }
}
