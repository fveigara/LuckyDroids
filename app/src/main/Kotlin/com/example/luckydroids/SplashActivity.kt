package com.example.luckydroids

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start) // temporal, si sigues usando ese xml

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, StartActivity::class.java))
            finish()
        }, 2000)
    }
}