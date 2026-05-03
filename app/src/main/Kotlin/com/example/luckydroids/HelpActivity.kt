package com.example.luckydroids

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class HelpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)

        val webView = findViewById<WebView>(R.id.webView)

        webView.webViewClient = WebViewClient() // evita abrir navegador externo
        webView.settings.javaScriptEnabled = true

        webView.loadUrl("file:///android_asset/ayuda.html")
    }
}