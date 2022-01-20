package com.hzdq.bajiesleepchildrenHD.home.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.ActivityReportInfoBinding
import com.hzdq.bajiesleepchildrenHD.utils.*
import dmax.dialog.SpotsDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set


/**
 * 报告详情界面
 */
class ReportInfoActivity : AppCompatActivity() {
  private lateinit var binding: ActivityReportInfoBinding
    val DOMAIN = "http://cloud.bajiesleep.com"
    private var tokenDialog: TokenDialog? = null
    var REPORT_URL = "https://bajie-sleep.oss-cn-hangzhou.aliyuncs.com/formal/" //报告正式服
    var LOAD_URL = "https://cloud.bajiesleep.com/report?reportURL=" //报告正式服
    var spotDialog: AlertDialog? = null
    var filePath:String? = null
    private lateinit var shp: Shp
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shp =  Shp(this)
       binding = DataBindingUtil.setContentView(this,R.layout.activity_report_info)
        ActivityCollector2.addActivity(this)
        HideUI(this).hideSystemUI()
        binding.reportInfoBack.setOnClickListener {

            finish()
        }

        val reportUrl = intent.getStringExtra("url")
        val fileNames = intent.getStringExtra("fileName")
//        val fileNames = "可以啊  2021-02-03  05:54.pdf"


        val webview = binding.webview
//        reportUrl?.let { webview.loadUrl(it) }

        //设置cookies
        setAppInfoCookies(LOAD_URL+reportUrl!!)



        webview.webChromeClient = WebChromeClient()
//        webview.webViewClient = object :WebViewClient(){
//            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
//                super.onPageStarted(view, url, favicon)
//                binding.share.visibility = View.GONE
//            }
//
//            override fun onPageFinished(view: WebView?, url: String?) {
//                super.onPageFinished(view, url)
//                binding.share.visibility = View.VISIBLE
//            }
//
//
//        }
//
        val webSettings: WebSettings = webview.getSettings()
//        // 支持缩放(适配到当前屏幕)
        webSettings.setSupportZoom(true)
        webSettings.builtInZoomControls = true
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        webSettings.loadWithOverviewMode = true
        webSettings.displayZoomControls = false

        webSettings.javaScriptEnabled = true;
        //3、 加载需要显示的网页

        webSettings.domStorageEnabled = true;
//        webview.loadUrl("https://www.baidu.com")
        webview.loadUrl(LOAD_URL+reportUrl!!)
//        webview.loadUrl("https://cloud.bajiesleep.com/report?userName=李自白&reportURL=61d8b9a7d4e07c7451e40b3b&reportCreateTime=1641592802")


        binding.share.setOnClickListener {

            spotDialog = SpotsDialog.Builder().setContext(this@ReportInfoActivity)
                .setTheme(R.style.SpotDialogCustom)
                .setCancelable(false).build()
            spotDialog?.show()
            spotDialog?.getWindow()?.getDecorView()?.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
            spotDialog?.getWindow()?.getDecorView()?.setOnSystemUiVisibilityChangeListener {
                var uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or  //布局位于状态栏下方
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or  //全屏
                        View.SYSTEM_UI_FLAG_FULLSCREEN or  //隐藏导航栏
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                uiOptions = uiOptions or 0x00001000
                spotDialog?.getWindow()?.getDecorView()?.setSystemUiVisibility(uiOptions)
            }
            DownLoadFile("${REPORT_URL}${reportUrl}.pdf","${cacheDir}/$fileNames")

        }


    }

    override fun onDestroy() {
        ActivityCollector2.removeActivity(this)
        super.onDestroy()
        Log.d("ReportInfoActivity", "onDestroy: ")
        DataCleanManagerKotlin.cleanInternalCache(applicationContext)
    }

    /**
     * 根据路径分享文件   pdf
     */
    private fun shareFile(path: String){

        val sharingIntent = Intent(Intent.ACTION_SEND)

        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) //给临时权限

        sharingIntent.type = "application/pdf" //根据文件类型设定type

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sharingIntent.putExtra(
                Intent.EXTRA_STREAM,
                FileProvider.getUriForFile(
                    baseContext, "$packageName.fileProvider",
                    File(path)
                )
            )
        } else {
            sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path))
        }

        startActivity(Intent.createChooser(sharingIntent, "分享"))


    }

    /**
     * 下载文件到本地
     * @param url
     * @param path
     */
    private fun DownLoadFile(url: String, path: String) {
        DownloadUtilKotlin.download(url,path,object :DownloadUtilKotlin.OnDownloadListener{
            override fun onDownloadSuccess(path: String?) {

                Log.d("downloadfile", "下载成功: ")
                lifecycleScope.launch {
                    delay(1500)
                    spotDialog!!.dismiss()
                    if (path != null) {
                        shareFile(path)
                    }
                }
            }

            override fun onDownloading(progress: Int) {

            }

            override fun onDownloadFailed(msg: String?) {
                lifecycleScope.launch {
                    delay(1500)
                    spotDialog!!.dismiss()
                    ToastUtils.showTextToast(this@ReportInfoActivity,"下载失败")
                }
            }

        })
    }

    /**
     * 判断文件是否存在
     * @param filePath
     * @return
     */
    private fun fileIsExists(filePath: String): Boolean {
        try {
            val f = File(filePath)
            if (!f.exists()) {
                return false
            }
        } catch (e: Exception) {
            return false
        }
        return true
    }


    /**
     * 同步cookie
     *
     * @param url 地址
     * @param cookieList 需要添加的Cookie值,以键值对的方式:key=value
     */
    private fun syncCookie(url: String, cookieList: ArrayList<String>?) {

        val cookieManager = CookieManager.getInstance()
//        CookieSyncManager.createInstance(this)
        cookieManager.setAcceptThirdPartyCookies(binding.webview,true)
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();//移除
        cookieManager.removeAllCookie();
        cookieManager.setAcceptCookie(true)
        if (cookieList != null && cookieList.size > 0) {
            for (cookie in cookieList) {
                cookieManager.setCookie(url, cookie)

            }
        }
//        cookieManager.setCookie(url, "Domain=${url}")
//        cookieManager.setCookie(url, "Path=/")
        val cookies = cookieManager.getCookie(url)
        Log.d("cookies", "syncCookie:$cookies ")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.flush()
        } else {
            CookieSyncManager.getInstance().sync()
        }
    }


    /**
     * 设置cookies
     */
    private fun setAppInfoCookies (url: String) {
        val cookieList =  ArrayList<String>();
//        cookieList.add("site=android");
        cookieList.add("token=" + shp.getToken())
        cookieList.add("uid=" + shp.getUid())
//        cookieList.add("HYPERF_SESSION_ID=" + "YpCGhKOKUefrP535SQp3X3CL0IDbVfPQO9oXdoVT")
        syncCookie(url, cookieList);
    }



}