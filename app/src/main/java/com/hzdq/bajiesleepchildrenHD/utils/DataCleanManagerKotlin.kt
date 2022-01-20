package com.hzdq.bajiesleepchildrenHD.utils

import android.content.Context
import android.os.Environment
import android.util.Log
import java.io.File


/**
 * 文 件 名:  DataCleanManagerKotlin
 * 描    述:  主要功能有清除内/外缓存，清除数据库，清除sharedPreference，清除files和清除自定义目录
 */
object DataCleanManagerKotlin {
    /** * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) * * @param context  */
    fun cleanInternalCache(context: Context) {
        deleteFilesByDirectory(context.cacheDir)
        Log.d("cleanInternalCache", "清除完成 ")
    }

    /** * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases) * * @param context  */
    fun cleanDatabases(context: Context) {
        deleteFilesByDirectory(
            File(
                "/data/data/"
                        + context.packageName + "/databases"
            )
        )
    }

    /**
     * * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs) * * @param
     * context
     */
    fun cleanSharedPreference(context: Context) {
        deleteFilesByDirectory(
            File(
                ("/data/data/"
                        + context.packageName + "/shared_prefs")
            )
        )
    }

    /** * 按名字清除本应用数据库 * * @param context * @param dbName  */
    fun cleanDatabaseByName(context: Context, dbName: String?) {
        context.deleteDatabase(dbName)
    }

    /** * 清除/data/data/com.xxx.xxx/files下的内容 * * @param context  */
    fun cleanFiles(context: Context) {
        deleteFilesByDirectory(context.filesDir)
    }

    /**
     * * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache) * * @param
     * context
     */
    fun cleanExternalCache(context: Context) {
        if ((Environment.getExternalStorageState() ==
                    Environment.MEDIA_MOUNTED)
        ) {
            deleteFilesByDirectory(context.externalCacheDir)
        }
    }

    /** * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除 * * @param filePath  */
    fun cleanCustomCache(filePath: String?) {
        deleteFilesByDirectory(File(filePath))
    }

    /** * 清除本应用所有的数据 * * @param context * @param filepath  */
    fun cleanApplicationData(context: Context, vararg filepath: String?) {
        cleanInternalCache(context)
        cleanExternalCache(context)
        cleanDatabases(context)
        cleanSharedPreference(context)
        cleanFiles(context)
        for (filePath: String? in filepath) {
            cleanCustomCache(filePath)
        }
    }

    /** * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * * @param directory  */
    private fun deleteFilesByDirectory(directory: File?) {
        if ((directory != null) && directory.exists() && directory.isDirectory) {
            for (item: File in directory.listFiles()) {
                item.delete()
            }
        }
    }
}