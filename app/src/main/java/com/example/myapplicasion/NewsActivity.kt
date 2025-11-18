package com.example.myapplicasion

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicasion.adapters.NewsAdapter
import com.example.myapplicasion.api.RetrofitClient
import com.example.myapplicasion.models.Article
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorTextView: TextView
    private val apiKey = "08ea3d4b76c84424b4529f63dfe449e5"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        recyclerView = findViewById(R.id.recyclerViewNews)
        progressBar = findViewById(R.id.progressBar)
        errorTextView = findViewById(R.id.errorTextView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        loadNews()
    }

    private fun loadNews() {
        progressBar.visibility = View.VISIBLE
        errorTextView.visibility = View.GONE
        recyclerView.visibility = View.GONE

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Intentar con el endpoint "everything" que funciona mejor en móviles
                val response = RetrofitClient.newsApiService.searchNews(
                    query = "technology",
                    apiKey = apiKey
                )

                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE

                    if (response.status == "ok" && response.articles.isNotEmpty()) {
                        val adapter = NewsAdapter(response.articles)
                        recyclerView.adapter = adapter
                        recyclerView.visibility = View.VISIBLE
                    } else {
                        showError("No se encontraron noticias")
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
