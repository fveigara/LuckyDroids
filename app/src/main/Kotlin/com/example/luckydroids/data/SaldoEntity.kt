package com.example.luckydroids.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SaldoEntity(
    @PrimaryKey val id: Int,
    val monedas: Int
)