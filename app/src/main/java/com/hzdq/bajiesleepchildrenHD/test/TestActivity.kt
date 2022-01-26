package com.hzdq.bajiesleepchildrenHD.test

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

import com.hzdq.bajiesleepchildrenHD.R
import java.util.*

class TestActivity : AppCompatActivity() {

    private val list: MutableList<Int> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test2)

        val pages = 25

        for (i in 1..pages) {
            list.add(i)
        }

       val sList =  splitList(list,10)

        Log.d("hello", "onCreate: $sList")


    }



    //分割数组
    private fun splitList(list: MutableList<Int>, groupSize: Int): MutableList<List<Int>>? {
        val length = list.size
        // 计算可以分成多少组
        val num = (length + groupSize - 1) / groupSize // TODO
        val newList: MutableList<List<Int>> = ArrayList(num)
        for (i in 0 until num) {
            // 开始位置
            val fromIndex = i * groupSize
            // 结束位置
            val toIndex = if ((i + 1) * groupSize < length) (i + 1) * groupSize else length
            newList.add(list.subList(fromIndex, toIndex))
        }
        return newList
    }
}