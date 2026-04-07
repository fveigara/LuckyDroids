package com.example.luckydroids.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface SaldoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun guardarSaldo(saldo: SaldoEntity): Completable

    @Query("SELECT * FROM SaldoEntity WHERE id = 1")
    fun obtenerSaldo(): Single<SaldoEntity>
}