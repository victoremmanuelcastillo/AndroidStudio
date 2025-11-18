package com.example.myapplicasion

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContactoAdapter(
    private var contactos: List<Contacto>,
    private val onItemClick: (Contacto) -> Unit
) : RecyclerView.Adapter<ContactoAdapter.ContactoViewHolder>() {

    class ContactoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvId: TextView = view.findViewById(R.id.tvId)
        val tvNombre: TextView = view.findViewById(R.id.tvNombre)
        val tvEmail: TextView = view.findViewById(R.id.tvEmail)
        val tvTelefono: TextView = view.findViewById(R.id.tvTelefono)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.contacto_item, parent, false)
        return ContactoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactoViewHolder, position: Int) {
        val contacto = contactos[position]
        holder.tvId.text = "ID: ${contacto.id}"
        holder.tvNombre.text = contacto.nombre
        holder.tvEmail.text = contacto.email
        holder.tvTelefono.text = contacto.telefono

        holder.itemView.setOnClickListener {
            onItemClick(contacto)
        }
    }

    override fun getItemCount() = contactos.size

    fun updateData(newContactos: List<Contacto>) {
        contactos = newContactos
        notifyDataSetChanged()
    }
}
