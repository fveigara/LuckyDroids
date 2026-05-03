package com.example.luckydroids.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface PartidaDao {

    @Insert
    fun insertar(partida: PartidaEntity): Completable

    @Query("SELECT * FROM partidas ORDER BY fecha DESC")
    fun obtenerTodas(): Single<List<PartidaEntity>>

    @Query("DELETE FROM partidas")
    fun borrarTodas(): Completable
}