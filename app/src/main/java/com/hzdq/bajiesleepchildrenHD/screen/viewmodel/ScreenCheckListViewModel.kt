package com.hzdq.bajiesleepchildrenHD.screen.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hzdq.bajiesleepchildrenHD.dataclass.DataQuestionResultList
import com.hzdq.bajiesleepchildrenHD.dataclass.DataQuestionResultListX

class ScreenCheckListViewModel(application: Application) :AndroidViewModel(application) {

    val resultList :MutableLiveData<List<DataQuestionResultListX>> = MutableLiveData()
    val rangeTime = MutableLiveData("")

    val startYear = MutableLiveData(0)
    val startMonth = MutableLiveData(0)
    val startDay = MutableLiveData(0)
    val endYear = MutableLiveData(0)
    val endMonth = MutableLiveData(0)
    val endDay = MutableLiveData(0)

    var startTime = "0"
    var endTime = "0"

    fun setRangeTime(){
        rangeTime.value = "${startYear.value}年${startMonth.value}月${startDay.value}日 - ${endYear.value}年${endMonth.value}月${endDay.value}日"
    }
    //总页数
    var pages: Int = 1
    var position = 0
    val page = MutableLiveData(1)
    val lastPage  = MutableLiveData(1)
    //跳转的页面
    var jumpPagesPosition = MutableLiveData<Int>(0)


    //需要页脚数量
    val footersPage = MutableLiveData<Int>(0)

    //当前页
    val currentPage= MutableLiveData<Int>(1)

    fun needFooters(){
        // page 33
        // a = 3
        // b = 4
        // c = 40
        // d = 7
        val a = pages / 10
        val b = a+1
        val c = b * 10
        val d = c - pages
        footersPage.value = d
    }

    //初始回到第一页
    fun initPage(){
        currentPage.value = 1
    }



    //跳转页面
    fun jumpPage(){

        if (currentPage.value!! > 10){

            val a = ((currentPage.value!!)-1) / 10
            val b = a*10

            jumpPagesPosition.value = b


        }else {
            jumpPagesPosition.value = 0
        }
    }
}