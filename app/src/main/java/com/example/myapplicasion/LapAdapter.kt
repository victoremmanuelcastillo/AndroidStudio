package com.example.myapplicasion

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LapAdapter(private val laps: List<Lap>) : RecyclerView.Adapter<LapAdapter.LapViewHolder>() {

    class LapViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lapNumberText: TextView = itemView.findViewById(R.id.lapNumberText)
        val lapTimeText: TextView = itemView.findViewById(R.id.lapTimeText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LapViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.lap_item, parent, false)
        return LapViewHolder(view)
    }

    override fun onBindViewHolder(holder: LapViewHolder, position: Int) {
        val lap = laps[position]
        holder.lapNumberText.text = "Lap ${lap.lapNumber}"
        holder.lapTimeText.text = lap.time
    }

    override fun getItemCount(): Int = laps.size
}
