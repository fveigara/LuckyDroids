package com.example.luckydroids

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.luckydroids.adapter.HistoryAdapter
import com.example.luckydroids.api.RetrofitClient
import com.example.luckydroids.data.GameDatabase
import com.example.luckydroids.models.Score
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var db: GameDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        recyclerView = findViewById(R.id.recyclerHistory)
        recyclerView.layoutManager = LinearLayoutManager(this)

        db = Room.databaseBuilder(
            applicationContext,
            GameDatabase::class.java,
            "db"
        )
            .fallbackToDestructiveMigration()
            .build()

        cargarHistorial()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val scores: List<Score> = RetrofitClient
                    .api
                    .getScores()
                    .values
                    .sortedByDescending { it.points }
                    .take(10)

                withContext(Dispatchers.Main) {
                    recyclerView.adapter = HistoryAdapter(scores)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun cargarHistorial() {
        db.partidaDao().obtenerTodas()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { lista ->
                    recyclerView.adapter = HistoryAdapter(lista)

                    if (lista.isEmpty()) {
                        Toast.makeText(this, "No hay partidas guardadas", Toast.LENGTH_SHORT).show()
                    }
                },
                { error ->
                    error.printStackTrace()
                    Toast.makeText(this, "Error cargando historial", Toast.LENGTH_SHORT).show()
                }
            )
    }
}
