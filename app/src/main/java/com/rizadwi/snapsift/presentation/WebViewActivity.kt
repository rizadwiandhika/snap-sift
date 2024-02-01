package com.rizadwi.snapsift.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rizadwi.snapsift.databinding.ActivityWebviewBinding

class WebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val url = intent.getStringExtra(URL_KEY) ?: ""
        if (url.isBlank()) {
            Toast.makeText(this, "URL should be specified!", Toast.LENGTH_SHORT).show()
            return
        }

        binding.webView.settings.javaScriptEnabled = true
        binding.webView.loadUrl(url)

    }

    companion object {
        const val URL_KEY = "URL"
    }
}