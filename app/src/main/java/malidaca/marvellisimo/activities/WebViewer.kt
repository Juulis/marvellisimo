package malidaca.marvellisimo.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView
import malidaca.marvellisimo.R
import android.webkit.WebViewClient

class WebViewer : AppCompatActivity() {
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        var url: String? = null
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_viewer)
        val extra = intent.extras
        if (extra != null) {
            url = extra.getString("url")
        }

        webView = findViewById(R.id.webView1)
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(url)

        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }
    }


}
