package com.hzdq.bajiesleepchildrenHD.retrofit

import android.content.Context
import android.util.Log
import android.util.Log.i
import com.hzdq.bajiesleepchildrenHD.utils.Shp
import com.liulishuo.filedownloader.util.FileDownloadLog.i
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Logger




class RetrofitSingleton private constructor(val context: Context){
//    private val BASE_URL = "http://120.26.54.110:9501"
    private val BASE_URL = "https://transmit.daoqihz.com"

    //单例模式
    //private constructor 外部不可以通过构造器来生成实例
    companion object{
        private var  INSTANCE: RetrofitSingleton? = null
        fun getInstance(context: Context)=
            INSTANCE ?: synchronized(this){
                RetrofitSingleton(context).also { INSTANCE = it }
            }


    }





    fun retrofit(baseUrl:String):Retrofit{

        val retrofit:Retrofit by lazy {
            Retrofit.Builder()
                .client(OkHttpClient.Builder().addInterceptor{ chain ->
                    val original = chain.request()
                    val request = original.newBuilder()
                        .addHeader("token",Shp(context).getToken())
                        .addHeader("uid", Shp(context).getUid())
                        .addHeader("User-Agent", Shp(context).getUserAgent())
                        .build()
                    chain .proceed(request)
                }.build())
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        return retrofit
    }

    fun api(): Api {

        val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .client(OkHttpClient.Builder().addInterceptor { chain ->
                    val original = chain.request()
                    val request = original.newBuilder()
                        .addHeader("token", Shp(context).getToken())
                        .addHeader("uid", Shp(context).getUid())
                        .addHeader("user-agent", Shp(context).getUserAgent())
                        .build()

                    chain.proceed(request)
                }.build())
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        return retrofit.create(Api::class.java)
    }

    fun api2(): Api {

        val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .client(OkHttpClient.Builder().addInterceptor { chain ->
                    val original = chain.request()
                    val request = original.newBuilder()
                        .addHeader("token", "c43afa65ec08c33c57c0e4f7e5bdb2d2")
                        .addHeader("uid", Shp(context).getUid())
                        .addHeader("user-agent", Shp(context).getUserAgent())
                        .build()
                    chain.proceed(request)
                }.build())
                .baseUrl("https://transmit.daoqihz.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        return retrofit.create(Api::class.java)
    }


    fun loginApi():Api{
        val retrofit:Retrofit by lazy {
            Retrofit.Builder()
                .client(OkHttpClient.Builder().addInterceptor { chain ->
                    val original = chain.request()
                    val request = original.newBuilder()
//                        .addHeader("User-Agent",Shp(context).getUserAgent())
                        .build()
                    chain.proceed(request)
                }.build())
                .baseUrl("https://transmit.daoqihz.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit.create(Api::class.java)
    }



    fun getDeviceApi():Api{
        val retrofit:Retrofit by lazy {


            Retrofit.Builder()
                .client(OkHttpClient.Builder().addInterceptor { chain ->
                    val original = chain.request()
                    val request = original.newBuilder()
                        .addHeader("User-Agent",Shp(context).getUserAgent())
                        .addHeader("token", Shp(context).getToken())
                        .addHeader("uid", Shp(context).getUid())
                        .build()
                    chain.proceed(request)
                }.build())
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit.create(Api::class.java)
    }


}


