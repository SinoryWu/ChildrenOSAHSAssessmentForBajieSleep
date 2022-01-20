package com.hzdq.bajiesleepchildrenHD.login.activity

import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.databinding.ActivityPrivateLinkBinding
import com.hzdq.bajiesleepchildrenHD.utils.ActivityCollector2
import com.hzdq.bajiesleepchildrenHD.utils.HideUI


class PrivateLinkActivity : AppCompatActivity() {
    private lateinit var binding:ActivityPrivateLinkBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCollector2.addActivity(this)
        HideUI(this).hideSystemUI()
        binding = DataBindingUtil.setContentView(this,R.layout.activity_private_link)

        binding.back.setOnClickListener {
            ActivityCollector2.removeActivity(this)
            finish()
        }


        //        mTvPrivateLink.setMovementMethod(new ScrollingMovementMethod());
//
//        InputStream inputStream = getResources().openRawResource(R.raw.private_link);
//        String string = TxtReader.getString(inputStream);
//        mTvPrivateLink.setText(string);



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
        binding.webView.loadUrl("https://www.bajiesleep.com/yinsixieyi.html")
//        binding.webView.loadUrl("https://www.baidu.com")
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return super.shouldOverrideUrlLoading(view, url)
            }
        }
    }
}