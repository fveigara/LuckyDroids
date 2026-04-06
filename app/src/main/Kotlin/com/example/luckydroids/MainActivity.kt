package com.example.luckydroids

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.room.Room
import com.example.luckydroids.data.*
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    private lateinit var slot1: ImageView
    private lateinit var slot2: ImageView
    private lateinit var slot3: ImageView
    private lateinit var tvGanancias: TextView
    private lateinit var btnJugar: Button
    private lateinit var layout: LinearLayout

    private lateinit var db: GameDatabase

    private val imagenes = intArrayOf(
        R.drawable.ic_bot,
        R.drawable.ic_cable,
        R.drawable.ic_droid,
        R.drawable.ic_robot,
        R.drawable.ic_vr
    )

    private var ganancias = 10
    private var s1 = 0
    private var s2 = 0
    private var s3 = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        slot1 = findViewById(R.id.mainActivitySlot1)
        slot2 = findViewById(R.id.mainActivitySlot2)
        slot3 = findViewById(R.id.mainActivitySlot3)
        tvGanancias = findViewById(R.id.mainActivityTvGanancias)
        btnJugar = findViewById(R.id.mainActivityBtJugar)
        layout = findViewById(R.id.mainActivityRl)

        ganancias = intent.getIntExtra("dinero", 10)
        tvGanancias.text = ganancias.toString()

        db = Room.databaseBuilder(
            applicationContext,
            GameDatabase::class.java,
            "db"
        ).build()

        cargarSaldo()

        btnJugar.setOnClickListener {
            if (ganancias <= 0) {
                Toast.makeText(this, "Sin saldo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            animar()
        }
    }

    private fun animar() {
        slot1.setImageResource(R.drawable.animation)
        val a1 = slot1.drawable as AnimationDrawable
        a1.start()

        slot2.setImageResource(R.drawable.animation)
        val a2 = slot2.drawable as AnimationDrawable
        a2.start()

        slot3.setImageResource(R.drawable.animation)
        val a3 = slot3.drawable as AnimationDrawable
        a3.start()

        Handler(Looper.getMainLooper()).postDelayed({
            a1.stop()
            a2.stop()
            a3.stop()

            tirar()
        }, 1000)
    }

    private fun tirar() {
        s1 = Random.nextInt(imagenes.size)
        s2 = Random.nextInt(imagenes.size)
        s3 = Random.nextInt(imagenes.size)

        slot1.setImageResource(imagenes[s1])
        slot2.setImageResource(imagenes[s2])
        slot3.setImageResource(imagenes[s3])

        val premio = calcularPremio()
        ganancias = ganancias - 1 + premio
        tvGanancias.text = ganancias.toString()

        guardarSaldo()
        guardarPartida(premio)
    }

    private fun calcularPremio(): Int {
        return when {
            s1 == s2 && s2 == s3 -> {
                Snackbar.make(layout, "100€", Snackbar.LENGTH_SHORT).show()
                100
            }
            s1 == s2 || s1 == s3 || s2 == s3 -> {
                Snackbar.make(layout, "5€", Snackbar.LENGTH_SHORT).show()
                5
            }
            else -> 0
        }
    }

    private fun guardarSaldo() {
        db.saldoDao().guardarSaldo(SaldoEntity(id = 1, monedas = ganancias))
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    private fun cargarSaldo() {
        db.saldoDao().obtenerSaldo()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { it ->
                    if (it != null) {
                        ganancias = it.monedas
                    }
                    tvGanancias.text = ganancias.toString()
                },
                {
                    tvGanancias.text = ganancias.toString()
                }
            )
            //.subscribe(
                //{ it ->
                    //ganancias = it.monedas
                    //tvGanancias.text = ganancias.toString()
                //},
                //{
                    //tvGanancias.text = ganancias.toString()
                //}
            //)
    }

    private fun guardarPartida(premio: Int) {
        val partida = PartidaEntity(
            slot1 = s1,
            slot2 = s2,
            slot3 = s3,
            premio = premio,
            saldoFinal = ganancias,
            fecha = System.currentTimeMillis()
        )

        db.partidaDao().insertar(partida)
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_historial -> {
                startActivity(Intent(this, HistoryActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}