package com.example.myapplicasion.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplicasion.R
import com.example.myapplicasion.models.Contacto

class ContactosAdapter(
    private var contactos: List<Contacto>,
    private val onContactoClick: (Contacto) -> Unit
) : RecyclerView.Adapter<ContactosAdapter.ContactoViewHolder>() {

    class ContactoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivContacto: ImageView = view.findViewById(R.id.ivContactoItem)
        val tvNombre: TextView = view.findViewById(R.id.tvNombreItem)
        val tvTelefono: TextView = view.findViewById(R.id.tvTelefonoItem)
        val tvCorreo: TextView = view.findViewById(R.id.tvCorreoItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contacto, parent, false)
        return ContactoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactoViewHolder, position: Int) {
        val contacto = contactos[position]

        holder.tvNombre.text = contacto.nombre
        holder.tvTelefono.text = contacto.telefono
        holder.tvCorreo.text = if (contacto.correo.isNotEmpty()) contacto.correo else "Sin correo"

        // Cargar imagen con Glide
        if (!contacto.ruta_imagen.isNullOrEmpty()) {
            val urlImagen = "http://localhost:8080/backend_php/${contacto.ruta_imagen}"
            Glide.with(holder.itemView.context)
                .load(urlImagen)
                .placeholder(android.R.drawable.ic_menu_camera)
                .error(android.R.drawable.ic_menu_camera)
                .circleCrop()
                .into(holder.ivContacto)
        } else {
            holder.ivContacto.setImageResource(android.R.drawable.ic_menu_camera)
        }

        holder.itemView.setOnClickListener {
            onContactoClick(contacto)
        }
    }

    override fun getItemCount(): Int = contactos.size

    fun actualizarLista(nuevosContactos: List<Contacto>) {
        contactos = nuevosContactos
        notifyDataSetChanged()
    }
}
