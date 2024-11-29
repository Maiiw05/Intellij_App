package com.example.appiumint.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appiumint.R
import com.example.appiumint.network.Movement

class MovementsAdapter(private val movements: List<Movement>) :
    RecyclerView.Adapter<MovementsAdapter.MovementViewHolder>() {

    class MovementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fecha: TextView = itemView.findViewById(R.id.fecha)
        val monto: TextView = itemView.findViewById(R.id.monto)
        val tipo: TextView = itemView.findViewById(R.id.tipo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovementViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movement, parent, false)
        return MovementViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovementViewHolder, position: Int) {
        val movement = movements[position]
        holder.fecha.text = movement.fecha_transaccion
        holder.monto.text = "$${movement.monto}"
        holder.tipo.text = movement.nombre
    }

    override fun getItemCount() = movements.size
}
