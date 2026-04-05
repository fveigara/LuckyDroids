package com.example.luckydroids

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.luckydroids.adapter.HistoryAdapter
import com.example.luckydroids.data.GameDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class HistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var db: GameDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        // Referencia al RecyclerView
        recyclerView = findViewById(R.id.recyclerHistory)

        // Layout manager
        recyclerView.layoutManager = LinearLayoutManager(this)

        //  Base de datos
        db = Room.databaseBuilder(
            applicationContext,
            GameDatabase::class.java,
            "db"
        ).build()

        // Cargar datos
        db.partidaDao().obtenerTodas()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { lista ->
                recyclerView.adapter = HistoryAdapter(lista)
            }
    }
}