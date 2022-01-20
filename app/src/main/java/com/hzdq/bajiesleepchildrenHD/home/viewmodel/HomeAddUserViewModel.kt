package com.hzdq.bajiesleepchildrenHD.home.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class HomeAddUserViewModel(application: Application) :AndroidViewModel(application) {

    //必填
    val truename = MutableLiveData("")
    val sex = MutableLiveData("")
    val age = MutableLiveData("")
    val height = MutableLiveData("")
    val weight = MutableLiveData("")
    val mobile = MutableLiveData("")
    val hospitalid = MutableLiveData("")

    //非必填
    val mzh = MutableLiveData("")
    val id_card = MutableLiveData("")
    val address = MutableLiveData("")
    val needfollow = MutableLiveData("")
}