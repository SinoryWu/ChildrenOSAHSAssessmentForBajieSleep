package com.hzdq.bajiesleepchildrenHD.login.viewmodel

import android.app.Application
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hzdq.bajiesleepchildrenHD.R

class LoginViewModel(application: Application) :AndroidViewModel(application) {


//    val phoneNumber = MutableLiveData("")
//
//    val messageCode = MutableLiveData("")
//
//    val passWord = MutableLiveData("")


    var phoneNumber = ""

    var messageCode = ""

    var passWord = ""


    private val _timeCount = MutableLiveData(-1)
    val timeCount:LiveData<Int> = _timeCount

    fun setTimeCount(x:Int){
        _timeCount.value = x
    }


    //倒计时方法
    val countTIme = object : CountDownTimer(60000, 1000) {
        override fun onTick(millisUntilFinished: Long) {

            setTimeCount((millisUntilFinished / 1000).toInt())

        }

        override fun onFinish() {


        }

    }

}