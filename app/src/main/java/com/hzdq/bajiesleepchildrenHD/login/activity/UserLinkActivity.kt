package com.hzdq.bajiesleepchildrenHD.login.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.databinding.ActivityPrivateLinkBinding
import com.hzdq.bajiesleepchildrenHD.databinding.ActivityUserLinkBinding
import com.hzdq.bajiesleepchildrenHD.utils.ActivityCollector2
import com.hzdq.bajiesleepchildrenHD.utils.HideUI

class UserLinkActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserLinkBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_user_link)
        ActivityCollector2.addActivity(this)
        HideUI(this).hideSystemUI()
        binding.back.setOnClickListener {
            ActivityCollector2.removeActivity(this)
            finish()
        }

        val webSettings: WebSettings = binding.webView.settings
        webSettings.setSupportZoom(true)
        webSettings.builtInZoomControls = true
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        webSettings.loadWithOverviewMode = true
        webSettings.displayZoomControls = false

        webSettings.javaScriptEnabled = true;
        //3、 加载需要显示的网页

        webSettings.domStorageEnabled = true;

        binding.webView.clearCache(true)
        binding.webView.clearFormData()
        binding.webView.loadUrl("https://www.bajiesleep.com/yonghuxieyi.html")
//        binding.webView.loadUrl("https://www.baidu.com")
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return super.shouldOverrideUrlLoading(view, url)
            }
        }
    }
}