package com.hzdq.bajiesleepchildrenHD.device.viewmodel

import android.app.Application
import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.toLiveData
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFrontDeviceXX
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFrontUserX
import com.hzdq.bajiesleepchildrenHD.dataclass.ReportRecoverDeviceInfo
import com.hzdq.bajiesleepchildrenHD.device.SingletonStringLiveData
import com.hzdq.bajiesleepchildrenHD.device.paging.FrontDeviceDataSourceFactory
import com.hzdq.bajiesleepchildrenHD.user.paging.FrontUserDataSourceFactory
import kotlinx.android.parcel.Parcelize


class DeviceViewModel(application: Application) : AndroidViewModel(application){

    val frontDeviceRefresh = MutableLiveData(0)

    val editDeviceSN = MutableLiveData("")
    val editDeviceStatus = MutableLiveData(0)
    val recoverDeviceStatus = MutableLiveData(0)


    val deviceReportList :MutableLiveData<List<ReportRecoverDeviceInfo>> = MutableLiveData()
    val deviceName  = MutableLiveData("")
    val deviceStatus  = MutableLiveData(0)


    val monitor = MutableLiveData("")


    val userName = MutableLiveData("")
    val uid = MutableLiveData("")



    val day =MutableLiveData<Int>(1)

    fun updateDay(days: Int) {
       day.value = days
    }



    val status = MutableLiveData(0)
    val selectState = MutableLiveData<Int>(0)
    val deviceAllNumber = MutableLiveData<Int>(0)
    val deviceOnlineNumber = MutableLiveData<Int>(0)

    val listSize = MutableLiveData(0)

    val totalCount = MutableLiveData(0)

    val frontDevicePosition = MutableLiveData(0)
    val dateItem:MutableLiveData<DataFrontDeviceXX> = MutableLiveData()
    fun updateSelectState(state:Int){
        selectState.value = state
    }

    val sn = MutableLiveData("")

   val deviceSn = MutableLiveData("")

    val rangeTime = MutableLiveData("21:00-06:00")

    //?????????????????????
    var needToScrollToTop = true

    private val factory = FrontDeviceDataSourceFactory(application) //paging????????????
    val frontDeviceListLiveData = factory.toLiveData(10)
    //Transformations.switchMap??????A??????B??????????????????????????????B???????????????????????????????????????A?????????
    val frontDeviceNetWorkStatus = Transformations.switchMap(factory.frontDeviceDataSource){
        it.networkStatus
    }
    fun resetFrontDeviceQuery(){
        //invalidate DataSource???????????????

        frontDeviceListLiveData.value?.dataSource?.invalidate()
        needToScrollToTop = true
    }


    //???????????????????????????
    fun retryFrontDevice(){

        //invoke??????
        factory.frontDeviceDataSource.value?.retry?.invoke()

        needToScrollToTop = false
    }
}