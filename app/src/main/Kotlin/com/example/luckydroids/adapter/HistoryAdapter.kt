package com.example.luckydroids.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.luckydroids.R
import com.example.luckydroids.data.PartidaEntity

class HistoryAdapter(private val lista: List<PartidaEntity>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val combinacion: TextView = view.findViewById(R.id.tvCombinacion)
        val premio: TextView = view.findViewById(R.id.tvPremio)
        val saldo: TextView = view.findViewById(R.id.tvSaldo)

        val ubicacion: TextView = view.findViewById(R.id.tvUbicacion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_partida, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val partida = lista[position]

        holder.combinacion.text =
            "Slots: ${partida.slot1} - ${partida.slot2} - ${partida.slot3}"

        holder.premio.text = "Premio: ${partida.premio}€"
        holder.saldo.text = "Saldo: ${partida.saldoFinal}€"
        holder.ubicacion.text =
            "Ubicación: ${partida.latitud}, ${partida.longitud}"
    }
}