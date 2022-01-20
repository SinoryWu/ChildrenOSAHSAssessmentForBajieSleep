package com.hzdq.bajiesleepchildrenHD.test

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class MyViewModel(application: Application) : AndroidViewModel(application) {
    var a = MutableLiveData<Int>(0)
}