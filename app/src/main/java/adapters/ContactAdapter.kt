package com.example.appiumint.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appiumint.R
import com.example.appiumint.network.Contact

class ContactAdapter(
    private val contacts: List<Contact>,
    private val onContactSelected: (Contact) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    private var selectedPosition = -1

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.contact_name)
        val accountNumber: TextView = itemView.findViewById(R.id.numero_cuenta)
        val radioButton: RadioButton = itemView.findViewById(R.id.contact_radio_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.name.text = contact.nombre
        holder.accountNumber.text = contact.numero_cuenta
        holder.radioButton.isChecked = position == selectedPosition

        holder.itemView.setOnClickListener {
            selectedPosition = holder.adapterPosition
            notifyDataSetChanged()
            onContactSelected(contact) // Notificar al Fragment el contacto seleccionado
        }
    }

    override fun getItemCount(): Int = contacts.size

    fun getSelectedContact(): Contact? {
        return if (selectedPosition != -1) contacts[selectedPosition] else null
    }
}
