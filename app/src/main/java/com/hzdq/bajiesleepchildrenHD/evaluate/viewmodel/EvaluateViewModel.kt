package com.hzdq.bajiesleepchildrenHD.evaluate.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.toLiveData
import com.hzdq.bajiesleepchildrenHD.MainActivity
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFrontUserX
import com.hzdq.bajiesleepchildrenHD.evaluate.paging.EvaluateRecordDataSourceFactory
import com.hzdq.bajiesleepchildrenHD.evaluate.paging.FrontEvaluateDataSourceFactory
import com.hzdq.bajiesleepchildrenHD.user.paging.FrontUserDataSourceFactory

class EvaluateViewModel(application: Application) : AndroidViewModel(application) {


    //frontpage
    val totalCount = MutableLiveData(0)
    val frontEvaluatePosition = MutableLiveData(0)
    val dateItem:MutableLiveData<DataFrontUserX> = MutableLiveData()
    val patient_id = MutableLiveData(0)
    val name = MutableLiveData("")
    val frontEvaluateRefresh = MutableLiveData(0)
    val firstItemPosition = MutableLiveData(0)

    val listSize = MutableLiveData(0)

    val evaluateRecordRefresh = MutableLiveData(0)

    //需要回滚到顶部
    var needToScrollToTopEvaluateRecord = true

    private val factoryRecord = EvaluateRecordDataSourceFactory(application) //paging的工厂类
    val evaluateRecordListLiveData = factoryRecord.toLiveData(10)
    //Transformations.switchMap容器A监听B内容的变化，变化时将B内容转化为相应的内容并通知A监听器
    val evaluateRecordNetWorkStatus = Transformations.switchMap(factoryRecord.evaluateRecordDataSource){
        it.networkStatus
    }

    fun resetEvaluateRecordQuery(){
        //invalidate DataResource重新初始化

        evaluateRecordListLiveData.value?.dataSource?.invalidate()
        needToScrollToTopEvaluateRecord = true
    }

    //重新加载失败的数据
    fun retryEvaluateRecord(){

        //invoke表示执行
        factoryRecord.evaluateRecordDataSource.value?.retry?.invoke()

        needToScrollToTopEvaluateRecord = false
    }

    //需要回滚到顶部
    var needToScrollToTopFrontEvaluate = true

    private val factory = FrontEvaluateDataSourceFactory(application) //paging的工厂类
    val frontEvaluateListLiveData = factory.toLiveData(10)
    //Transformations.switchMap容器A监听B内容的变化，变化时将B内容转化为相应的内容并通知A监听器
    val frontEvaluateNetWorkStatus = Transformations.switchMap(factory.frontEvaluateDataSource){
        it.networkStatus
    }

    fun resetFrontEvaluateQuery(){
        //invalidate DataResource重新初始化

        frontEvaluateListLiveData.value?.dataSource?.invalidate()
        needToScrollToTopFrontEvaluate = true
    }

    //重新加载失败的数据
    fun retryFrontEvaluate(){

        //invoke表示执行
        factory.frontEvaluateDataSource.value?.retry?.invoke()

        needToScrollToTopFrontEvaluate = false
    }

    //基本信息
    val sleep = MutableLiveData(0)
    val pih = MutableLiveData(0)
    val dm = MutableLiveData(0)
    val mode  = MutableLiveData(0)
    val birth  = MutableLiveData(0)
    val pomr  = MutableLiveData(0)
    val cmv  = MutableLiveData(0)


    //家族史
    val f11 = MutableLiveData(0)
    val f12 = MutableLiveData(0)
    val f13 = MutableLiveData(0)
    val f14 = MutableLiveData(0)
    val f15 = MutableLiveData(0)
    val f16 = MutableLiveData(0)
    val f17 = MutableLiveData(0)
    val f18 = MutableLiveData(0)
    val fc1 = MutableLiveData(0)
    fun setfc1(){
        fc1.value = f11.value!! +  f12.value!! + f13.value!! + f14.value!!  + f15.value!! + f16.value!! + f17.value!! +  f18.value!!
    }

    val f21 = MutableLiveData(0)
    val f22 = MutableLiveData(0)
    val f23 = MutableLiveData(0)
    val f24 = MutableLiveData(0)
    val f25 = MutableLiveData(0)
    val f26 = MutableLiveData(0)
    val f27 = MutableLiveData(0)
    val f28 = MutableLiveData(0)
    val fc2 = MutableLiveData(0)
    fun setfc2(){
        fc2.value = f21.value!! +  f22.value!! + f23.value!! + f24.value!!  + f25.value!! + f26.value!! + f27.value!! +  f28.value!!
    }

    val f31 = MutableLiveData(0)
    val f32 = MutableLiveData(0)
    val f33 = MutableLiveData(0)
    val f34 = MutableLiveData(0)
    val f35 = MutableLiveData(0)
    val f36 = MutableLiveData(0)
    val f37 = MutableLiveData(0)
    val f38 = MutableLiveData(0)
    val fc3 = MutableLiveData(0)
    fun setfc3(){
        fc3.value = f31.value!! +  f32.value!! + f33.value!! + f34.value!!  + f35.value!! + f36.value!! + f37.value!! +  f38.value!!
    }

    val f41 = MutableLiveData(0)
    val f42 = MutableLiveData(0)
    val f43 = MutableLiveData(0)
    val f44 = MutableLiveData(0)
    val f45 = MutableLiveData(0)
    val f46 = MutableLiveData(0)
    val f47 = MutableLiveData(0)
    val f48 = MutableLiveData(0)
    val fc4 = MutableLiveData(0)
    fun setfc4(){
        fc4.value = f41.value!! +  f42.value!! + f43.value!! + f44.value!!  + f45.value!! + f46.value!! + f47.value!! +  f48.value!!
    }

    val f51 = MutableLiveData(0)
    val f52 = MutableLiveData(0)
    val f53 = MutableLiveData(0)
    val f54 = MutableLiveData(0)
    val f55 = MutableLiveData(0)
    val f56 = MutableLiveData(0)
    val f57 = MutableLiveData(0)
    val f58 = MutableLiveData(0)
    val fc5 = MutableLiveData(0)
    fun setfc5(){
        fc5.value = f51.value!! +  f52.value!! + f53.value!! + f54.value!!  + f55.value!! + f56.value!! + f57.value!! +  f58.value!!
    }

    val f61 = MutableLiveData(0)
    val f62 = MutableLiveData(0)
    val f63 = MutableLiveData(0)
    val f64 = MutableLiveData(0)
    val f65 = MutableLiveData(0)
    val f66 = MutableLiveData(0)
    val f67 = MutableLiveData(0)
    val f68 = MutableLiveData(0)
    val fc6 = MutableLiveData(0)
    fun setfc6(){
        fc6.value = f61.value!! +  f62.value!! + f63.value!! + f64.value!!  + f65.value!! + f66.value!! + f67.value!! +  f68.value!!
    }

    val f71 = MutableLiveData(0)
    val f72 = MutableLiveData(0)
    val f73 = MutableLiveData(0)
    val f74 = MutableLiveData(0)
    val f75 = MutableLiveData(0)
    val f76 = MutableLiveData(0)
    val f77 = MutableLiveData(0)
    val f78 = MutableLiveData(0)
    val fc7 = MutableLiveData(0)
    fun setfc7(){
        fc7.value = f71.value!! +  f72.value!! + f73.value!! + f74.value!!  + f75.value!! + f76.value!! + f77.value!! +  f78.value!!
    }

    val f81 = MutableLiveData(0)
    val f82 = MutableLiveData(0)
    val f83 = MutableLiveData(0)
    val f84 = MutableLiveData(0)
    val f85 = MutableLiveData(0)
    val f86 = MutableLiveData(0)
    val f87 = MutableLiveData(0)
    val f88 = MutableLiveData(0)
    val fc8 = MutableLiveData(0)
    fun setfc8(){
        fc8.value = f81.value!! +  f82.value!! + f83.value!! + f84.value!!  + f85.value!! + f86.value!! + f87.value!! +  f88.value!!
    }

    //家族史提交参数
    val fhs = MutableLiveData("")


    //既往病史
    val rhinitis = MutableLiveData(0)
    val asthma = MutableLiveData(0)
    val eczema = MutableLiveData(0)
    val urticaria = MutableLiveData(0)
    val tnb = MutableLiveData(0)
    val mdd = MutableLiveData(0)
    val ekc = MutableLiveData(0)
    val ihb = MutableLiveData(0)
    val thyroid = MutableLiveData(0)
    val fat = MutableLiveData(0)
    val tonsils = MutableLiveData(0)
    val adenoid = MutableLiveData(0)


    //查体与检验
    val height = MutableLiveData("") //身高
    val weight = MutableLiveData("") //体重
    val bmi1 = MutableLiveData("") //BMI
    val bmi = MutableLiveData("") //BMI
    val dns = MutableLiveData(0) //鼻中隔偏曲
    val hypertrophy = MutableLiveData(0) //鼻甲肥大
    val polyp = MutableLiveData(0) //鼻息肉
    val face = MutableLiveData(0) //腺样体面容
    val occlusion = MutableLiveData(0)//牙齿咬合异常
    val crossbite = MutableLiveData(0)//1-天包地 2-地包天
    val brodskyAdenoid = MutableLiveData(0) //Brodsky腺样体4分度
    val brodskyTonsils = MutableLiveData(0)//Brodsky扁桃体5分度
    val neck = MutableLiveData("")//颈围(cm)
    val npc = MutableLiveData("")//鼻咽侧位A/N
    val oahi = MutableLiveData("") // oahi


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
            //保留一位小数
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


    //生活质量
    val noise = MutableLiveData(0)
    val lamp = MutableLiveData(0)
    val crowd = MutableLiveData(0)
    val smoke = MutableLiveData(0)
    val assessment = MutableLiveData("")

    val osa = MutableLiveData("")
    val psq = MutableLiveData("")

    val osa1 = MutableLiveData("")
    val psq1 = MutableLiveData("")

    //未填任何选项
    val noOption = MutableLiveData(0)
    val noOption1 = MutableLiveData(0)
    val noOption2 = MutableLiveData(0)
    val noOption3 = MutableLiveData(0)


    //治疗方案
    val osas = MutableLiveData(0)
    val opinion = MutableLiveData("")
    val treat = MutableLiveData(0)
    val treatment = MutableLiveData("")


    val result = MutableLiveData("")
    val id = MutableLiveData(0)
}


