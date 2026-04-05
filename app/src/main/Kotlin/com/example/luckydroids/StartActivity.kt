package com.example.luckydroids

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class StartActivity : AppCompatActivity() {

    private lateinit var playerInput: EditText
    private lateinit var moneyInput: EditText
    private lateinit var addButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start) // tu XML

        playerInput = findViewById(R.id.player_input)
        moneyInput = findViewById(R.id.money_input)
        addButton = findViewById(R.id.add_button)

        addButton.setOnClickListener {
            val nombre = playerInput.text.toString()
            val dinero = moneyInput.text.toString()

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("nombre", nombre)
            intent.putExtra("dinero", dinero.toIntOrNull() ?: 10)

            startActivity(intent)
        }
    }
}