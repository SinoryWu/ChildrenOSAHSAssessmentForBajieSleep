package com.hzdq.bajiesleepchildrenHD.utils

import okhttp3.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * 下载器kotlin
 */
object  DownloadUtilKotlin {
     fun download(url: String?, saveFile: String?, listener: OnDownloadListener) {
        val request = Request.Builder().url(url).build()
        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                listener.onDownloadFailed(e.message)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                var `is`: InputStream? = null
                val buf = ByteArray(2048)
                var len: Int
                var fos: FileOutputStream? = null
                try {
                    `is` = response.body()!!.byteStream()
                    val total = response.body()!!.contentLength()
                    val file = File(saveFile)
                    fos = FileOutputStream(file)
                    var sum: Long = 0
                    while (`is`.read(buf).also { len = it } != -1) {
                        fos.write(buf, 0, len)
                        sum += len.toLong()
                        val progress = (sum * 1.0f / total * 100).toInt()
                        listener.onDownloading(progress)
                    }
                    fos.flush()
                    listener.onDownloadSuccess(file.absolutePath)
                } catch (e: Exception) {
                    listener.onDownloadFailed(e.message)
                } finally {
                    try {
                        `is`?.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    try {
                        fos?.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        })
    }

    interface OnDownloadListener {
        fun onDownloadSuccess(path: String?)
        fun onDownloading(progress: Int)
        fun onDownloadFailed(msg: String?)
    }

}