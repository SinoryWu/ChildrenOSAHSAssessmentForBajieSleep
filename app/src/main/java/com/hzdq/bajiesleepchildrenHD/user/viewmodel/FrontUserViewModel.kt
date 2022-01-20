package com.hzdq.bajiesleepchildrenHD.user.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.toLiveData
import com.hzdq.bajiesleepchildrenHD.user.paging.FrontUserDataSourceFactory

class FrontUserViewModel(application: Application) : AndroidViewModel(application) {
    val totalUser = MutableLiveData(0)

    var keyWords = MutableLiveData("")

    fun setkeyword(x:String){
        keyWords.value = x
    }




}