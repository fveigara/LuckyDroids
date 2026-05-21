package com.example.luckydroids

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class StartActivity : AppCompatActivity() {

    private lateinit var playerInput: EditText
    private lateinit var moneyInput: EditText
    private lateinit var addButton: Button
    private lateinit var loginButton: Button
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

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

        aplicarUsuarioActual()

        loginButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, 1000)
        }

        addButton.setOnClickListener {
            val user = firebaseAuth.currentUser
            if (user == null) {
                Toast.makeText(
                    this,
                    "Inicia sesión con Google antes de jugar",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            val nombre = user.displayName?.takeIf { it.isNotBlank() } ?: "Anónimo"
            val dinero = moneyInput.text.toString().trim().toIntOrNull() ?: 10

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("nombre", nombre)
            intent.putExtra("dinero", dinero)
            startActivity(intent)
        }
    }

    private fun aplicarUsuarioActual() {
        val user = firebaseAuth.currentUser
        if (user != null) {
            playerInput.setText(user.displayName ?: "")
            playerInput.isEnabled = false
            playerInput.isFocusable = false
            loginButton.text = getString(R.string.logged_in_as, user.displayName ?: user.email ?: "")
            loginButton.isEnabled = false
        } else {
            playerInput.setText("")
            playerInput.isEnabled = false
            playerInput.isFocusable = false
            playerInput.hint = getString(R.string.player_hint_locked)
            loginButton.text = getString(R.string.login_google)
            loginButton.isEnabled = true
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login correcto", Toast.LENGTH_LONG).show()
                    aplicarUsuarioActual()
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
