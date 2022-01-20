package com.hzdq.bajiesleepchildrenHD.user.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.toLiveData
import com.hzdq.bajiesleepchildrenHD.MainActivity
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFrontUserX
import com.hzdq.bajiesleepchildrenHD.frontpagefragment.UserFragment
import com.hzdq.bajiesleepchildrenHD.home.paging.HomeBindDeviceUserDataSourceFactory
import com.hzdq.bajiesleepchildrenHD.user.paging.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val DATA_STATUS_CAN_LOAD_MORE = 0
const val DATA_STATUS_NO_MORE = 1
const val DATA_STATUS_NETWORK_ERROR = 2
class UserViewModel(application: Application) : AndroidViewModel(application) {

    val checkDetailBitmap :MutableLiveData<Bitmap> = MutableLiveData()
    val checkUrl  = MutableLiveData("")
    val fileNames  = MutableLiveData("")
    val listSize = MutableLiveData(0)
    val userBarPosition = MutableLiveData(0)

    val ol1 = MutableLiveData(0)
    val ol2 = MutableLiveData(0)
    val ol3 = MutableLiveData(0)
    val ol4 = MutableLiveData(0)
    val ol5 = MutableLiveData(0)
    val ol6 = MutableLiveData(0)
    val ol7 = MutableLiveData(0)
    val ol8 = MutableLiveData(0)
    val ol9 = MutableLiveData(0)
    val ol10 = MutableLiveData(0)
    val ol11 = MutableLiveData(0)
    val ol12 = MutableLiveData(0)
    val ol13 = MutableLiveData(0)
    val ol14 = MutableLiveData(0)
    val ol15 = MutableLiveData(0)
    val ol16 = MutableLiveData(0)
    val ol17 = MutableLiveData(0)
    val ol18 = MutableLiveData(0)

    val pl1 = MutableLiveData(0)
    val pl2 = MutableLiveData(0)
    val pl3 = MutableLiveData(0)
    val pl4 = MutableLiveData(0)
    val pl5 = MutableLiveData(0)
    val pl6 = MutableLiveData(0)
    val pl7 = MutableLiveData(0)
    val pl8 = MutableLiveData(0)
    val pl9 = MutableLiveData(0)
    val pl10 = MutableLiveData(0)
    val pl11 = MutableLiveData(0)
    val pl12 = MutableLiveData(0)
    val pl13 = MutableLiveData(0)
    val pl14 = MutableLiveData(0)
    val pl15 = MutableLiveData(0)
    val pl16 = MutableLiveData(0)
    val pl17 = MutableLiveData(0)
    val pl18 = MutableLiveData(0)
    val pl19 = MutableLiveData(0)
    val pl20 = MutableLiveData(0)
    val pl21 = MutableLiveData(0)
    val pl22 = MutableLiveData(0)


    val firstItemPosition = MutableLiveData(0)
    val addTreatmentDegree = MutableLiveData(0)


    var keywords = ""

    val keyword = MutableLiveData("")

    val totalCount = MutableLiveData(0)


    val dateItem:MutableLiveData<DataFrontUserX> = MutableLiveData()
    val uid = MutableLiveData(0)
    val name = MutableLiveData("")
    val type = MutableLiveData(0)

    val baseName = MutableLiveData("")
    val baseAge = MutableLiveData(0)
    val baseSex = MutableLiveData(0)
    val baseNeedfollow = MutableLiveData(0)
    val baseOahi = MutableLiveData("")
    val baseOahires = MutableLiveData("")
    val baseEstimatId = MutableLiveData(0)
    val baseEstimatres = MutableLiveData("")
    val baseFollowtime = MutableLiveData(0)
    val baseOahiList:MutableLiveData<MutableList<Float>> = MutableLiveData(ArrayList())
    val baseMzh = MutableLiveData("")
    val baseMobile = MutableLiveData("")
    val baseHeight = MutableLiveData("")
    val baseWeight = MutableLiveData("")
    val baseUid = MutableLiveData("")
    val baseHospitalId = MutableLiveData(0)

    val frontUserPosition = MutableLiveData(0)
    val frontUserRefresh = MutableLiveData(0)
    val userTreatRecordRefresh = MutableLiveData(0)
    val userFollowUpRefresh = MutableLiveData(0)
    val userReportListRefresh = MutableLiveData(0)
    //需要回滚到顶部
    var needToScrollToTop = true

    private val factory = FrontUserDataSourceFactory(application) //paging的工厂类
    val frontUserListLiveData = factory.toLiveData(10)
    //Transformations.switchMap容器A监听B内容的变化，变化时将B内容转化为相应的内容并通知A监听器
    val frontUserNetWorkStatus = Transformations.switchMap(factory.frontUserDataSource){
        it.networkStatus
    }

    fun resetFrontUserQuery(){
        //invalidate DataResource重新初始化

        frontUserListLiveData.value?.dataSource?.invalidate()
        needToScrollToTop = true
    }


    //重新加载失败的数据
    fun retryFrontUser(){

        //invoke表示执行
        factory.frontUserDataSource.value?.retry?.invoke()

        needToScrollToTop = false
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



    //需要回滚到顶部
    var needToScrollToTopTreatRecord = true

    private val factoryTreatRecord = UserTreatRecordDataSourceFactory(application) //paging的工厂类
    val userTreatRecordListLiveData = factoryTreatRecord.toLiveData(10)
    //Transformations.switchMap容器A监听B内容的变化，变化时将B内容转化为相应的内容并通知A监听器
    val userTreatRecordNetWorkStatus = Transformations.switchMap(factoryTreatRecord.userTreatRecordDataSource){
        it.networkStatus
    }

    fun resetUserTreatRecordQuery(){
        //invalidate DataResource重新初始化

        userTreatRecordListLiveData.value?.dataSource?.invalidate()
        needToScrollToTopTreatRecord = true
    }


    //重新加载失败的数据
    fun retryUserTreatRecord(){

        //invoke表示执行
        factoryTreatRecord.userTreatRecordDataSource.value?.retry?.invoke()

        needToScrollToTopTreatRecord = false
    }



    //需要回滚到顶部
    var needToScrollToTopFollowUp = true

    private val factoryFollowUp = UserFollowUpDataSourceFactory(application) //paging的工厂类
    val userFollowUpListLiveData = factoryFollowUp.toLiveData(10)
    //Transformations.switchMap容器A监听B内容的变化，变化时将B内容转化为相应的内容并通知A监听器
    val userFollowUpNetWorkStatus = Transformations.switchMap(factoryFollowUp.userFollowUpDataSource){
        it.networkStatus
    }

    fun resetUserFollowUpQuery(){
        //invalidate DataResource重新初始化

        userFollowUpListLiveData.value?.dataSource?.invalidate()
        needToScrollToTopFollowUp = true
    }


    //重新加载失败的数据
    fun retryUserFollowUp(){

        //invoke表示执行
        factoryFollowUp.userFollowUpDataSource.value?.retry?.invoke()

        needToScrollToTopFollowUp = false
    }




    val userQuestionListRefresh = MutableLiveData(0)
    val oahi = MutableLiveData("")
    val subtype = MutableLiveData("")
    //需要回滚到顶部
    var needToScrollToTopQuestionList = true


    private val factoryQuestionList = UserQuestionListDataSourceFactory(application) //paging的工厂类
    val userQuestionListLiveData = factoryQuestionList.toLiveData(10)
    //Transformations.switchMap容器A监听B内容的变化，变化时将B内容转化为相应的内容并通知A监听器
    val userQuestionListNetWorkStatus = Transformations.switchMap(factoryQuestionList.userQuestionListDataSource){
        it.networkStatus
    }

    fun resetQuestionListQuery(){
        //invalidate DataResource重新初始化

        userQuestionListLiveData.value?.dataSource?.invalidate()
        needToScrollToTopQuestionList = true
    }


    //重新加载失败的数据
    fun retryUserQuestionList(){


        factoryQuestionList.userQuestionListDataSource.value?.retry?.invoke()

        needToScrollToTopQuestionList = false

    }



    //需要回滚到顶部
    var needToScrollToTopReportList = true

    private val factoryReportList = UserReportListDataSourceFactory(application) //paging的工厂类
    val userReportListLiveData = factoryReportList.toLiveData(10)
    //Transformations.switchMap容器A监听B内容的变化，变化时将B内容转化为相应的内容并通知A监听器
    val userReportListNetWorkStatus = Transformations.switchMap(factoryReportList.userReportListDataSource){
        it.networkStatus
    }

    fun resetReportListQuery(){
        //invalidate DataResource重新初始化

        userReportListLiveData.value?.dataSource?.invalidate()
        needToScrollToTopReportList = true
    }


    //重新加载失败的数据
    fun retryUserReportList(){

        //invoke表示执行
        factoryReportList.userReportListDataSource.value?.retry?.invoke()

        needToScrollToTopReportList = false
    }
}