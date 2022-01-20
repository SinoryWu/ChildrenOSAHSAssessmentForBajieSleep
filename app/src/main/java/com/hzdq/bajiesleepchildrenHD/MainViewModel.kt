package com.hzdq.bajiesleepchildrenHD

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class MainViewModel(application: Application) : AndroidViewModel(application) {


    val soFarBytes = MutableLiveData(0f)
    val totalBytes = MutableLiveData(0f)

    val percentageFloat = MutableLiveData(0f)
    val percentage = MutableLiveData("")

    fun setPercent(){
        if (totalBytes.value != 0f){
            percentageFloat.value = (soFarBytes.value!!) / (totalBytes.value!!) *100
            percentage.value = "%.0f".format(percentageFloat.value)
        }

    }


    val contentVersion = MutableLiveData("")
    val currentVersion = MutableLiveData("")
    val nextVersion = MutableLiveData("")
}