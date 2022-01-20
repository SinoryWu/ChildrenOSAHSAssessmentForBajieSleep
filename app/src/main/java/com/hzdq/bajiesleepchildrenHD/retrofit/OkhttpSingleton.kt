package com.hzdq.bajiesleepchildrenHD.retrofit

import android.content.Context
import okhttp3.OkHttpClient

object OkhttpSingleton {
//        public static String URL = "https://transmit.daoqihz.com";//正式服
    private var okHttpClient: OkHttpClient? = null
//    val BASE_URL = "http://120.26.54.110:9501"
    val BASE_URL = "https://transmit.daoqihz.com"
    //单例模式
    @Synchronized
    fun ok(): OkHttpClient? {
        if (okHttpClient == null) {
            okHttpClient = OkHttpClient()
        }
        return okHttpClient
    }
}