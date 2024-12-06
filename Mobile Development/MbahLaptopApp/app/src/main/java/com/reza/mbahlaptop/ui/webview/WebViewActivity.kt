package com.reza.mbahlaptop.ui.webview

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.reza.mbahlaptop.databinding.ActivityWebViewBinding
import com.reza.mbahlaptop.utils.TemplateActivity

class WebViewActivity : TemplateActivity() {
    private lateinit var binding: ActivityWebViewBinding
    private var url: String? = null
    private var isToastShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra("title")

        setSupportActionBar(binding.myToolbar)
        supportActionBar?.title = title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val webview = binding.webView
        url = intent.getStringExtra("url")

        binding.progressBar.visibility = View.VISIBLE

        webview.settings.javaScriptEnabled = true

        webview.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                if (!isToastShown) {
                    Toast.makeText(
                        this@WebViewActivity,
                        "News Loaded Successfully",
                        Toast.LENGTH_LONG
                    ).show()
                    isToastShown = true
                }
                binding.progressBar.visibility = View.GONE
            }
        }

        url?.let {
            if (it.startsWith("https://")) {
                webview.loadUrl(it)
            } else {
                Toast.makeText(this, "Unsecure URL blocked", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(this, "Invalid URL", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        binding.webView.stopLoading()
        binding.webView.webViewClient = object : WebViewClient() {}
    }
}