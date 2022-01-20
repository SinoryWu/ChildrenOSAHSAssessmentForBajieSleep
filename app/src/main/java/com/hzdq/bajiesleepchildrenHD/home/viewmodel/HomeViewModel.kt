package com.hzdq.bajiesleepchildrenHD.home.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.toLiveData
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFrontHomeX
import com.hzdq.bajiesleepchildrenHD.dataclass.DataHomeReportSleepX
import com.hzdq.bajiesleepchildrenHD.home.paging.HomeBindDeviceUserDataSourceFactory
import com.hzdq.bajiesleepchildrenHD.home.paging.HomeUserSearchDataSource
import com.hzdq.bajiesleepchildrenHD.home.paging.HomeUserSearchDataSourceFactory


class HomeViewModel(application: Application) : AndroidViewModel(application) {


    val reportList : MutableLiveData<List<DataHomeReportSleepX>> = MutableLiveData<List<DataHomeReportSleepX>>()

    val rangeTime = MutableLiveData("")

    var position = 0

    var startTime = "0"
    var endTime = "0"

    var keyWords = ""
    val startYear = MutableLiveData(0)
    val startMonth = MutableLiveData(0)
    val startDay = MutableLiveData(0)
    val endYear = MutableLiveData(0)
    val endMonth = MutableLiveData(0)
    val endDay = MutableLiveData(0)

    fun setRangeTime(){
        rangeTime.value = "${startYear.value}年${startMonth.value}月${startDay.value}日 - ${endYear.value}年${endMonth.value}月${endDay.value}日"
    }

    //总页数
//    var pages: Int = 101

    val page  = MutableLiveData(1)
    val lastPage  = MutableLiveData(1)


    //跳转的页面
    var jumpPagesPosition = MutableLiveData<Int>(0)


    //需要页脚数量
    val footersPage = MutableLiveData<Int>(0)

    //当前页
     val currentPage=MutableLiveData<Int>(1)

//    fun needFooters(){
//        // page 33
//        // a = 3
//        // b = 4
//        // c = 40
//        // d = 7
//        val a = pages / 10
//        val b = a+1
//        val c = b * 10
//        val d = c - pages
//        footersPage.value = d
//    }

    //初始回到第一页
    fun initPage(){
        currentPage.value = 1
    }



    //跳转页面
    fun jumpPage(){

        if (currentPage.value!! > 10){

                val a = ((currentPage.value!!)-1) / 10
                val b = a*10

                jumpPagesPosition.value = b


        }else {
            jumpPagesPosition.value = 0
        }
    }

    val refreshHome = MutableLiveData(0)


    //需要回滚到顶部
    var needToScrollToTopUserSearch = true

    private val factoryUser = HomeUserSearchDataSourceFactory(application) //paging的工厂类
    val homeUserSearchListLiveData = factoryUser.toLiveData(10)
    //Transformations.switchMap容器A监听B内容的变化，变化时将B内容转化为相应的内容并通知A监听器
    val homeUserSearchNetWorkStatus = Transformations.switchMap(factoryUser.dataSource){
        it.networkStatus
    }
    fun resetHomeUserSearchQuery(){
        //invalidate DataResource重新初始化

        homeUserSearchListLiveData.value?.dataSource?.invalidate()
        needToScrollToTopUserSearch = true
    }


    //重新加载失败的数据
    fun retryHomeUserSearch(){

        //invoke表示执行
        factoryUser.dataSource.value?.retry?.invoke()

        needToScrollToTopUserSearch = false
    }

}