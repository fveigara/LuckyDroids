package com.example.luckydroids.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.luckydroids.R
import com.example.luckydroids.data.PartidaEntity
import com.example.luckydroids.models.Score

class HistoryAdapter(private val lista: List<Any>) :
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
        val item = lista[position]

        if (item is PartidaEntity) {
            holder.combinacion.text =
                "Slots: ${item.slot1} - ${item.slot2} - ${item.slot3}"

            holder.premio.text = "Premio: ${item.premio}€"
            holder.saldo.text = "Saldo: ${item.saldoFinal}€"
            holder.ubicacion.text =
                "Ubicación: ${item.latitud}, ${item.longitud}"
        } else if (item is Score) {
            holder.combinacion.text = "Jugador: ${item.playerName}"
            holder.premio.text = "Puntos: ${item.points}"
            holder.saldo.text = ""
            holder.ubicacion.text = ""
        }
    }
}
