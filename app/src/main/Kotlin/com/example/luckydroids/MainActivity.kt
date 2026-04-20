package com.example.luckydroids

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.AnimationDrawable
import android.location.LocationManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.CalendarContract
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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

    private lateinit var sonidoGiro: MediaPlayer
    private lateinit var sonidoVictoria: MediaPlayer
    private var lat: Double = 0.0
    private var lon: Double = 0.0

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

        if (android.os.Build.VERSION.SDK_INT >= 33) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
        }
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        try {
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            lat = location?.latitude ?: 0.0
            lon = location?.longitude ?: 0.0
        } catch (e: SecurityException) {
            lat = 0.0
            lon = 0.0
        }

        startService(Intent(this, MusicService::class.java))
        crearCanal()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        slot1 = findViewById(R.id.mainActivitySlot1)
        slot2 = findViewById(R.id.mainActivitySlot2)
        slot3 = findViewById(R.id.mainActivitySlot3)
        tvGanancias = findViewById(R.id.mainActivityTvGanancias)
        btnJugar = findViewById(R.id.mainActivityBtJugar)
        layout = findViewById(R.id.mainActivityRl)
        sonidoGiro = MediaPlayer.create(this, R.raw.spin)
        sonidoVictoria = MediaPlayer.create(this, R.raw.win)

        db = Room.databaseBuilder(
            applicationContext,
            GameDatabase::class.java,
            "db"
        ).build()

        val dineroInicial = intent.getIntExtra("dinero", -1)

        if (dineroInicial != -1) {
            ganancias = dineroInicial
            tvGanancias.text = "Ganancias: $ganancias"
            guardarSaldo()
        } else {
            cargarSaldo()
        }

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
        if (!sonidoGiro.isPlaying) {
            sonidoGiro.start()
        }
        s1 = Random.nextInt(imagenes.size)
        s2 = Random.nextInt(imagenes.size)
        s3 = Random.nextInt(imagenes.size)

        slot1.setImageResource(imagenes[s1])
        slot2.setImageResource(imagenes[s2])
        slot3.setImageResource(imagenes[s3])

        val premio = calcularPremio()
        ganancias = ganancias - 1 + premio
        tvGanancias.text = "Ganancias: $ganancias"

        guardarSaldo()
        guardarPartida(premio)
    }

    private fun calcularPremio(): Int {
        return when {
            s1 == s2 && s2 == s3 -> {
                if (!sonidoVictoria.isPlaying) {
                    sonidoVictoria.start()
                }
                guardarCaptura()
                mostrarNotificacion()
                Snackbar.make(layout, "100€", Snackbar.LENGTH_SHORT).show()
                100
            }
            s1 == s2 || s1 == s3 || s2 == s3 -> {
                if (!sonidoVictoria.isPlaying) {
                    sonidoVictoria.start()
                }
                guardarCaptura()
                mostrarNotificacion()
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
                    tvGanancias.text =  "Ganancias: $ganancias"
                },
                {
                    tvGanancias.text =  "Ganancias: $ganancias"
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
            fecha = System.currentTimeMillis(),
            latitud = lat,
            longitud = lon
        )

        db.partidaDao().insertar(partida)
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    private fun guardarCaptura() {
        val view = window.decorView.rootView
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "victoria_${System.currentTimeMillis()}.png")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        }

        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        uri?.let {
            contentResolver.openOutputStream(it)?.use { stream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            }
        }
    }

    private fun guardarEnCalendario() {
        val intent = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtra(CalendarContract.Events.TITLE, "Victoria en Lucky Droids")
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, System.currentTimeMillis())
        }
        startActivity(intent)
    }

    private fun crearCanal() {
        val channel = NotificationChannel(
            "victorias",
            "Victorias",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    private fun mostrarNotificacion() {

        if (android.os.Build.VERSION.SDK_INT >= 33) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }

        val builder = NotificationCompat.Builder(this, "victorias")
            .setSmallIcon(R.drawable.ic_bot)
            .setContentTitle("¡Victoria!")
            .setContentText("Has ganado una partida")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        NotificationManagerCompat.from(this).notify(1, builder.build())
    }

    private fun borrarHistorial() {
        db.partidaDao().borrarTodas()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Toast.makeText(this, "Historial borrado", Toast.LENGTH_SHORT).show()
                },
                {
                    it.printStackTrace()
                }
            )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.menu_historial -> {
                startActivity(Intent(this, HistoryActivity::class.java))
                true
            }

            R.id.menu_reiniciar -> {
                ganancias = 10
                tvGanancias.text = "Ganancias: $ganancias"
                guardarSaldo()
                Toast.makeText(this, "Saldo reiniciado", Toast.LENGTH_SHORT).show()
                true
            }

            R.id.menu_borrar -> {
                borrarHistorial()
                true
            }

            R.id.menu_salir -> {
                stopService(Intent(this, MusicService::class.java))
                finish()
                true
            }


            else -> super.onOptionsItemSelected(item)
        }
    }
}