package com.example.luckydroids

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.snackbar.Snackbar
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var slot1: ImageView
    private lateinit var slot2: ImageView
    private lateinit var slot3: ImageView
    private lateinit var tvGanancias: TextView
    private lateinit var btnJugar: Button
    private lateinit var relativeLayout: RelativeLayout

    private val imagenes = intArrayOf(
        R.drawable.ic_bot,
        R.drawable.ic_cable,
        R.drawable.ic_droid,
        R.drawable.ic_robot,
        R.drawable.ic_vr
    )

    private var intSlot1 = 0
    private var intSlot2 = 0
    private var intSlot3 = 0
    private var intGanancias = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Referencias UI
        slot1 = findViewById(R.id.mainActivitySlot1)
        slot2 = findViewById(R.id.mainActivitySlot2)
        slot3 = findViewById(R.id.mainActivitySlot3)
        tvGanancias = findViewById(R.id.mainActivityTvGanancias)
        btnJugar = findViewById(R.id.mainActivityBtJugar)
        relativeLayout = findViewById(R.id.mainActivityRl)

        tvGanancias.text = intGanancias.toString()

        // Botón jugar
        btnJugar.setOnClickListener {
            if (intGanancias <= 0) {
                Toast.makeText(this, "No tienes saldo suficiente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "Has lanzado -1 euro", Toast.LENGTH_SHORT).show()
            iniciarAnimacion()
        }
    }

    private fun iniciarAnimacion() {

        slot1.setImageResource(R.drawable.animation)
        val anim1 = slot1.drawable as AnimationDrawable
        anim1.start()

        slot2.setImageResource(R.drawable.animation)
        val anim2 = slot2.drawable as AnimationDrawable
        anim2.start()

        slot3.setImageResource(R.drawable.animation)
        val anim3 = slot3.drawable as AnimationDrawable
        anim3.start()

        Handler(Looper.getMainLooper()).postDelayed({

            anim1.stop()
            anim2.stop()
            anim3.stop()

            ponerImagenes()
            actualizarGanancias()

        }, 1000)
    }

    private fun ponerImagenes() {
        intSlot1 = Random.nextInt(imagenes.size)
        intSlot2 = Random.nextInt(imagenes.size)
        intSlot3 = Random.nextInt(imagenes.size)

        slot1.setImageResource(imagenes[intSlot1])
        slot2.setImageResource(imagenes[intSlot2])
        slot3.setImageResource(imagenes[intSlot3])
    }

    private fun calcularPremio(): Int {
        return when {
            intSlot1 == intSlot2 && intSlot2 == intSlot3 -> {
                Snackbar.make(relativeLayout, "Has ganado 100€", Snackbar.LENGTH_SHORT).show()
                100
            }
            intSlot1 == intSlot2 || intSlot1 == intSlot3 || intSlot2 == intSlot3 -> {
                Snackbar.make(relativeLayout, "Has ganado 5€", Snackbar.LENGTH_SHORT).show()
                5
            }
            else -> {
                Snackbar.make(relativeLayout, "No has ganado", Snackbar.LENGTH_SHORT).show()
                0
            }
        }
    }

    private fun actualizarGanancias() {
        val premio = calcularPremio()
        intGanancias = intGanancias - 1 + premio
        tvGanancias.text = intGanancias.toString()
    }
}
