package com.example.luckydroids

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.luckydroids.api.RetrofitClient
import com.example.luckydroids.models.Score
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StartActivity : AppCompatActivity() {

    private lateinit var playerInput: EditText
    private lateinit var moneyInput: EditText
    private lateinit var addButton: Button
    private lateinit var loginButton: Button
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth
    private var currentCoins: Int = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        firebaseAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN
        )
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        playerInput = findViewById(R.id.player_input)
        moneyInput = findViewById(R.id.money_input)
        addButton = findViewById(R.id.add_button)
        loginButton = findViewById(R.id.mainActivityBtLogin)

        loginButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, 1000)
        }

        addButton.setOnClickListener {

            val nombre = playerInput.text.toString().trim()
            val dinero = moneyInput.text.toString().trim().toIntOrNull() ?: 10
            currentCoins = dinero

            if (nombre.isEmpty()) {
                playerInput.error = "Introduce un nombre"
                return@setOnClickListener
            }

            saveScore(nombre, currentCoins)

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("nombre", nombre)
            intent.putExtra("dinero", dinero)

            startActivity(intent)
        }

        // If user is already logged in, we might want to save their score on start
        if (firebaseAuth.currentUser != null) {
            saveScore(firebaseAuth.currentUser?.displayName ?: "Unknown", currentCoins)
        }
    }

    private fun saveScore(playerName: String, points: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                RetrofitClient.api.saveScore(
                    Score(
                        uid = firebaseAuth.currentUser?.uid ?: "",
                        playerName = playerName,
                        points = points,
                        timestamp = System.currentTimeMillis()
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login correcto", Toast.LENGTH_LONG).show()
                    saveScore(firebaseAuth.currentUser?.displayName ?: "Google User", currentCoins)
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1000) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account?.idToken
                if (idToken != null) {
                    firebaseAuthWithGoogle(idToken)
                } else {
                    Toast.makeText(this, "Error: No se pudo obtener el ID Token", Toast.LENGTH_SHORT).show()
                }
            } catch (e: ApiException) {
                Toast.makeText(this, "Error de autenticación (Code: ${e.statusCode})", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }
    }
}
