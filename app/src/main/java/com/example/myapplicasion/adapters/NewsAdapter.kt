package com.example.myapplicasion.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplicasion.R
import com.example.myapplicasion.models.Article
import java.text.SimpleDateFormat
import java.util.Locale

class NewsAdapter(private val articles: List<Article>) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val newsImage: ImageView = itemView.findViewById(R.id.newsImage)
        val newsTitle: TextView = itemView.findViewById(R.id.newsTitle)
        val newsDescription: TextView = itemView.findViewById(R.id.newsDescription)
        val newsSource: TextView = itemView.findViewById(R.id.newsSource)
        val newsDate: TextView = itemView.findViewById(R.id.newsDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = articles[position]

        holder.newsTitle.text = article.title
        holder.newsDescription.text = article.description ?: "Sin descripción"
        holder.newsSource.text = article.source.name

        // Formatear fecha
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val date = inputFormat.parse(article.publishedAt)
            holder.newsDate.text = date?.let { outputFormat.format(it) } ?: article.publishedAt
        } catch (e: Exception) {
            holder.newsDate.text = article.publishedAt
        }

        // Cargar imagen con Glide
        if (!article.urlToImage.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(article.urlToImage)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.newsImage)
        } else {
            holder.newsImage.setImageResource(R.drawable.ic_launcher_background)
        }

        // Click para abrir la noticia completa en el navegador
        holder.itemView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = articles.size
}
