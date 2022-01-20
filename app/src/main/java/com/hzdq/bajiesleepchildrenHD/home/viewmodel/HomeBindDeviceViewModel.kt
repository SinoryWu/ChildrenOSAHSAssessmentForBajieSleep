package com.hzdq.bajiesleepchildrenHD.home.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.toLiveData
import com.hzdq.bajiesleepchildrenHD.device.paging.FrontDeviceDataSourceFactory
import com.hzdq.bajiesleepchildrenHD.home.paging.HomeBindDeviceDeviceDataSourceFactory
import com.hzdq.bajiesleepchildrenHD.home.paging.HomeBindDeviceUserDataSourceFactory

class HomeBindDeviceViewModel(application: Application) :AndroidViewModel(application) {

    val sn = MutableLiveData("")
    val uid = MutableLiveData(0)
    val name = MutableLiveData("")
    val frequency = MutableLiveData("1")
    val startTime = MutableLiveData("21:00")
    val endTime = MutableLiveData("06:00")
    val keywords = MutableLiveData("")

    val rangeTime = MutableLiveData("21:00 - 06:00")

    fun setRangeTime(){
        rangeTime.value = "${startTime.value} - ${endTime.value}"
    }


    //需要回滚到顶部
    var needToScrollToTopBindDevice = true

    private val factoryDevice = HomeBindDeviceDeviceDataSourceFactory(application) //paging的工厂类
    val homeBindDeviceDeviceListLiveData = factoryDevice.toLiveData(10)
    //Transformations.switchMap容器A监听B内容的变化，变化时将B内容转化为相应的内容并通知A监听器
    val homeBindDeviceDeviceNetWorkStatus = Transformations.switchMap(factoryDevice.dataSource){
        it.networkStatus
    }
    fun resetHomeDeviceDeviceQuery(){
        //invalidate DataResource重新初始化

        homeBindDeviceDeviceListLiveData.value?.dataSource?.invalidate()
        needToScrollToTopBindDevice = true
    }


    //重新加载失败的数据
    fun retryHomeBindDeviceDevice(){

        //invoke表示执行
        factoryDevice.dataSource.value?.retry?.invoke()

        needToScrollToTopBindDevice = false
    }


    //需要回滚到顶部
    var needToScrollToTopBindUser = true

    private val factoryUser = HomeBindDeviceUserDataSourceFactory(application) //paging的工厂类
    val homeBindDeviceUserListLiveData = factoryUser.toLiveData(10)
    //Transformations.switchMap容器A监听B内容的变化，变化时将B内容转化为相应的内容并通知A监听器
    val homeBindDeviceUserNetWorkStatus = Transformations.switchMap(factoryUser.dataSource){
        it.networkStatus
    }
    fun resetHomeDeviceUserQuery(){
        //invalidate DataResource重新初始化

        homeBindDeviceUserListLiveData.value?.dataSource?.invalidate()
        needToScrollToTopBindUser = true
    }


    //重新加载失败的数据
    fun retryHomeBindDeviceUser(){

        //invoke表示执行
        factoryUser.dataSource.value?.retry?.invoke()

        needToScrollToTopBindUser = false
    }

}