package com.example.luckydroids.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "partidas")
data class PartidaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val slot1: Int,
    val slot2: Int,
    val slot3: Int,
    val premio: Int,
    val saldoFinal: Int,
    val fecha: Long,
    val latitud: Double,
    val longitud: Double
)