package com.hzdq.bajiesleepchildrenHD.setting.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hzdq.bajiesleepchildrenHD.dataclass.DataAdministratorsX
import com.hzdq.bajiesleepchildrenHD.dataclass.DataHomeReportSleepX

class SettingViewModel(application: Application) :AndroidViewModel(application) {
    //基本设置
    val hospitalName = MutableLiveData(" ")
    val hospitalIcon  = MutableLiveData(" ")
    val reportName  = MutableLiveData(" ")
    val reportIcon  = MutableLiveData(" ")
    val standard  = MutableLiveData(" ")
    val standardContent  = MutableLiveData(" ")
    val evaluate  = MutableLiveData(" ")
    val evaluateContent  = MutableLiveData(" ")
    val hospitalIconBitmap:MutableLiveData<ByteArray>  = MutableLiveData()
    val reportIconBitmap:MutableLiveData<ByteArray>  = MutableLiveData()
    val welcome  = MutableLiveData(" ")
    val refreshBase = MutableLiveData(0)


    //账号管理
    val refreshAccount = MutableLiveData(0)
    val list :MutableLiveData<List<DataAdministratorsX>> = MutableLiveData()


    val accountName = MutableLiveData("")
    val accountMobile = MutableLiveData("")


    //密码管理

    private val _timeCount = MutableLiveData(-1)
    val timeCount: LiveData<Int> = _timeCount

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

    val  messageCode = MutableLiveData("")
    val  newPassword = MutableLiveData("")
    val  repeatNewPassword = MutableLiveData("")



    //注册管理

    var bitmap:Bitmap? = null
    var bitmapQRCode:Bitmap? = null
    val register = MutableLiveData(0)
    val question = MutableLiveData(0)
}