package com.hzdq.bajiesleepchildrenHD.screen.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class ScreenTaskViewModel(application: Application) :AndroidViewModel(application) {
    val startTime = MutableLiveData("")
    val endTime = MutableLiveData("")
    val question = MutableLiveData("OSA-18")

    val startTimeStamp= MutableLiveData("")
    val endTimeStamp= MutableLiveData("")

    val startYear = MutableLiveData(0)
    val startMonth = MutableLiveData(0)
    val startDay = MutableLiveData(0)

    val endYear = MutableLiveData(0)
    val endMonth = MutableLiveData(0)
    val endDay = MutableLiveData(0)

    fun setStartTime(){
        startTime.value = "${startYear.value}年${startMonth.value}月${startDay.value}日"
    }

    fun setEndTime(){
        endTime.value = "${endYear.value}年${endMonth.value}月${endDay.value}日"
    }

    val l1 = MutableLiveData(0)
    val l2 = MutableLiveData(0)
    val l3 = MutableLiveData(0)
    val l4 = MutableLiveData(0)




}