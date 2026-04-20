package com.example.luckydroids

import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity

class HelpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)

        val webView = findViewById<WebView>(R.id.webView)
        webView.loadUrl("https://tuayuda.com")
    }
}