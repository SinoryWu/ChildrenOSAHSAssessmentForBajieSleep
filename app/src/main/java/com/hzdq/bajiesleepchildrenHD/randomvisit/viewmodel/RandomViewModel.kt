package com.hzdq.bajiesleepchildrenHD.randomvisit.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.toLiveData
import com.hzdq.bajiesleepchildrenHD.dataclass.AssessmentFollowUpDetail
import com.hzdq.bajiesleepchildrenHD.randomvisit.paging.FollowUpDataSourceFactory
import com.hzdq.bajiesleepchildrenHD.randomvisit.paging.FrontFollowUpDataSourceFactory
import com.hzdq.bajiesleepchildrenHD.user.paging.UserFollowUpDataSourceFactory

class RandomViewModel(application: Application) :AndroidViewModel(application) {

    val year = MutableLiveData(0)
    val month = MutableLiveData(0)
    val day = MutableLiveData(0)
    val time = MutableLiveData("")
    val next= MutableLiveData("")
    fun setTime(){
        time.value = "${year.value}年${month.value}月${day.value}日"
    }


    val patient_id = MutableLiveData(0)
    val name = MutableLiveData("")

    val oahi = MutableLiveData("")
    val height = MutableLiveData("")
    val weight = MutableLiveData("")
    val bmi = MutableLiveData("")
    val assessment = MutableLiveData("")
    val suspend = MutableLiveData(0)
    val reason = MutableLiveData("")

    val neck = MutableLiveData("")

    val assessmentList :MutableLiveData<List<AssessmentFollowUpDetail>> = MutableLiveData()
    val osaTaskId  = MutableLiveData(0)
    val psqTaskId  = MutableLiveData(0)

    val createTime = MutableLiveData(0)
    val doctorName = MutableLiveData("")

    val bmi1 = MutableLiveData("")
    val osa = MutableLiveData("")
    val osa1 = MutableLiveData("")
    val psq = MutableLiveData("")
    val psq1 = MutableLiveData("")


    fun setBmi(){

        var bmi2 = 0f
        if (height.value.equals("") && weight.value.equals("")){
            bmi2 = 0f

            val num = 1.34567

            bmi1.value = ""
        }else if (height.value.equals("")){
            bmi2 = 0f
            bmi1.value = ""
        }else if (weight.value.equals("")){
            bmi2 = 0f
            bmi1.value = ""
        }else if (!weight.value.equals("") && !height.value.equals("")){
            bmi2 = (weight.value?.toFloat()!!) / (height.value?.toFloat()!! /100 * height.value?.toFloat()!! /100).toFloat()
            println("%.1f".format(bmi2))
            bmi1.value = "${"%.1f".format(bmi2)}"
        }

        if (!bmi1.value.equals("")){
            bmi.value = bmi1.value
        }
        if (height.value.equals("")){
            bmi.value = ""
        }
        if (weight.value.equals("")){
            bmi.value = ""
        }

    }

    val frontFollowUpPosition = MutableLiveData(0)
    val frontFollowUpRefresh = MutableLiveData(0)

    val listSize = MutableLiveData(0)

    //需要回滚到顶部
    var needToScrollToTopFrontFollowUp = true

    private val factoryFrontFollowUp = FrontFollowUpDataSourceFactory(application) //paging的工厂类
    val frontFollowUpListLiveData = factoryFrontFollowUp.toLiveData(10)
    //Transformations.switchMap容器A监听B内容的变化，变化时将B内容转化为相应的内容并通知A监听器
    val frontFollowUpNetWorkStatus = Transformations.switchMap(factoryFrontFollowUp.frontFollowUpDataSource){
        it.networkStatus
    }

    fun resetFrontFollowUpQuery(){
        //invalidate DataResource重新初始化

        frontFollowUpListLiveData.value?.dataSource?.invalidate()
        needToScrollToTopFrontFollowUp = true
    }


    //重新加载失败的数据
    fun retryFrontFollowUp(){

        //invoke表示执行
        factoryFrontFollowUp.frontFollowUpDataSource.value?.retry?.invoke()

        needToScrollToTopFrontFollowUp = false
    }




    val uid = MutableLiveData(0)
    val allNumber = MutableLiveData(0)
    val keywords = MutableLiveData("")
    val followUpNumber = MutableLiveData(0)
    val selectState = MutableLiveData(0)



    val followUpRefresh  = MutableLiveData(0)
    //需要回滚到顶部
    var needToScrollToTopFollowUp = true

    private val factoryFollowUp = FollowUpDataSourceFactory(application) //paging的工厂类
    val followUpListLiveData = factoryFollowUp.toLiveData(10)
    //Transformations.switchMap容器A监听B内容的变化，变化时将B内容转化为相应的内容并通知A监听器
    val followUpNetWorkStatus = Transformations.switchMap(factoryFollowUp.followUpDataSource){
        it.networkStatus
    }

    fun resetFollowUpQuery(){
        //invalidate DataResource重新初始化

        followUpListLiveData.value?.dataSource?.invalidate()
        needToScrollToTopFollowUp = true
    }


    //重新加载失败的数据
    fun retryFollowUp(){

        //invoke表示执行
        factoryFollowUp.followUpDataSource.value?.retry?.invoke()

        needToScrollToTopFollowUp = false
    }





























}