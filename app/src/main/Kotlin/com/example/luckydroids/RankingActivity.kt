package com.example.luckydroids

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.luckydroids.adapter.HistoryAdapter
import com.example.luckydroids.api.MoshiClient
import com.example.luckydroids.api.RetrofitClient
import com.example.luckydroids.models.Score
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RankingActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var txtJackpot: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)

        txtJackpot = findViewById(R.id.txtRankingJackpot)
        recycler = findViewById(R.id.recyclerRanking)
        recycler.layoutManager = LinearLayoutManager(this)

        cargarJackpotConMoshi()
        cargarTopDiezConGson()
    }

    private fun cargarTopDiezConGson() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val resp = RetrofitClient.api.getScores()
                val mapa = if (resp.isSuccessful) resp.body() else null
                val top: List<Score> = (mapa?.values ?: emptyList())
                    .sortedByDescending { it.points }
                    .take(10)

                withContext(Dispatchers.Main) {
                    if (top.isEmpty()) {
                        Toast.makeText(
                            this@RankingActivity,
                            getString(R.string.ranking_empty),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    recycler.adapter = HistoryAdapter(top)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@RankingActivity,
                        getString(R.string.ranking_load_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun cargarJackpotConMoshi() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val resp = MoshiClient.api.getJackpot()
                val coins = if (resp.isSuccessful) (resp.body()?.coins ?: 0) else 0
                withContext(Dispatchers.Main) {
                    txtJackpot.text = getString(R.string.jackpot, coins)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    txtJackpot.text = getString(R.string.jackpot, 0)
                }
            }
        }
    }
}
