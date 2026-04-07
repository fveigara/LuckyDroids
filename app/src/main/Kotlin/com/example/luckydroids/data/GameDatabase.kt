package com.example.luckydroids.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PartidaEntity::class, SaldoEntity::class], version = 1)
abstract class GameDatabase : RoomDatabase() {
    abstract fun partidaDao(): PartidaDao
    abstract fun saldoDao(): SaldoDao
}