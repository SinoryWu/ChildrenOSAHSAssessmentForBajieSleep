package com.hzdq.bajiesleepchildrenHD

import android.app.*
import com.hzdq.bajiesleepchildrenHD.utils.SSLAgent
import com.liulishuo.filedownloader.FileDownloader
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.*

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        application = this

        // 不耗时，做一些简单初始化准备工作，不会启动下载进程
        FileDownloader.setupOnApplicationOnCreate(application)

        SSLAgent.getInstance().trustAllHttpsCertificates()
    }





    companion object {
        private var application: MyApplication? = null
    }
}