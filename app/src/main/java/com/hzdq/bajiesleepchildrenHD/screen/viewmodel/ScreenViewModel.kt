package com.hzdq.bajiesleepchildrenHD.screen.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.toLiveData
import com.google.zxing.common.BitmapUtils
import com.hzdq.bajiesleepchildrenHD.dataclass.DataClassMyQuestionList
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFrontScreenX
import com.hzdq.bajiesleepchildrenHD.dataclass.DataMyQuestionList
import com.hzdq.bajiesleepchildrenHD.dataclass.DataQuestionMarketList
import com.hzdq.bajiesleepchildrenHD.device.paging.FrontDeviceDataSourceFactory
import com.hzdq.bajiesleepchildrenHD.screen.paging.FrontScreenDataSourceFactory

class ScreenViewModel(application: Application) :AndroidViewModel(application) {
    val screenBarPosition = MutableLiveData(0)
    val screenFrontPosition = MutableLiveData(0)
    val taskId = MutableLiveData(0)
    val screenTitleTask = MutableLiveData("")

    val listSize = MutableLiveData(0)

    val bitmapContent = MutableLiveData("")

    var bitmap: Bitmap? = null

    var bitmapQRCode : Bitmap? = null
    val refreshFrontScreen = MutableLiveData(0)
    //需要回滚到顶部
    var needToScrollToTop = true

    private val factory = FrontScreenDataSourceFactory(application) //paging的工厂类
    val frontScreenListLiveData = factory.toLiveData(10)

    //Transformations.switchMap容器A监听B内容的变化，变化时将B内容转化为相应的内容并通知A监听器
    val frontScreenNetWorkStatus = Transformations.switchMap(factory.frontScreenDataSource){
        it.networkStatus
    }

    fun resetFrontScreenQuery(){
        //invalidate DataResource重新初始化

        frontScreenListLiveData.value?.dataSource?.invalidate()
        needToScrollToTop = true
    }

    //重新加载失败的数据
    fun retryFrontScreen(){

        //invoke表示执行
        factory.frontScreenDataSource.value?.retry?.invoke()

        needToScrollToTop = false
    }


    //我的问卷列表
    val myQuestionList:MutableLiveData<List<DataMyQuestionList>> = MutableLiveData()
    val myQuestionListSize = MutableLiveData(0)
    val myQuestionPosition = MutableLiveData(0)
    val myQuestionType = MutableLiveData(0)

    val refreshMyQuestion = MutableLiveData(0)

    val myQuestionTitle = MutableLiveData("")
    val myQuestionDescribe = MutableLiveData("")
    val myQuestionStandard = MutableLiveData("")

    //问卷市场
    val questionMarketList:MutableLiveData<List<DataQuestionMarketList>> = MutableLiveData()
}