package com.hzdq.bajiesleepchildrenHD.user.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class UserAddViewMode(application: Application) : AndroidViewModel(application) {
    //必填
    val truename = MutableLiveData("")
    val sex = MutableLiveData(-1)
    val age = MutableLiveData(-1)
    val height = MutableLiveData("")
    val weight = MutableLiveData("")
    val mobile = MutableLiveData("")
    val hospitalid = MutableLiveData("")
    val needfollow = MutableLiveData(-1)


    //非必填
    val mzh = MutableLiveData("")
    val id_card = MutableLiveData("")
    val address = MutableLiveData("")

}