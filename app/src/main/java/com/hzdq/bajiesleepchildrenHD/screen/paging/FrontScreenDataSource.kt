package com.hzdq.bajiesleepchildrenHD.screen.paging

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.hzdq.bajiesleepchildrenHD.dataclass.DataClassFrontScreen
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFrontDeviceXX
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFrontScreenX
import com.hzdq.bajiesleepchildrenHD.dataclass.FrontScreenBody
import com.hzdq.bajiesleepchildrenHD.device.paging.FrontDeviceNetWorkStatus
import com.hzdq.bajiesleepchildrenHD.retrofit.RetrofitSingleton
import com.hzdq.bajiesleepchildrenHD.utils.Shp
import com.hzdq.bajiesleepchildrenHD.utils.ToastUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//枚举类 网络状态
enum class FrontScreenNetWorkStatus{
    FRONT_SCREEN_INITIAL_LOADING,
    FRONT_SCREEN_INITIAL_LOADED,
    FRONT_SCREEN_LOADING,
    FRONT_SCREEN_LOADED,
    FRONT_SCREEN_FAILED,
    FRONT_SCREEN_COMPLETED
}
class FrontScreenDataSource(private val context: Context): PageKeyedDataSource<Int, DataFrontScreenX>() {

    var retry: (()-> Any)? = null  //retry可以是任何对象 retry表示重新加载时需要加载的对象
    private val _networkStatus = MutableLiveData<FrontScreenNetWorkStatus>()
    //网络状态的LiveData
    val networkStatus: LiveData<FrontScreenNetWorkStatus> = _networkStatus

    val retrofitSingleton = RetrofitSingleton.getInstance(context)
    private lateinit var frontScreenBody:FrontScreenBody

    val shp = Shp(context)


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, DataFrontScreenX>
    ) {
        retry = null //重置retry
        frontScreenBody = FrontScreenBody()
        frontScreenBody.page = 1
        frontScreenBody.hospital_id = shp.getHospitalId()!!
        frontScreenBody.type = 0


        _networkStatus.postValue(FrontScreenNetWorkStatus.FRONT_SCREEN_INITIAL_LOADING) //网络加载状态为第一次加载
        retrofitSingleton.api().postFrontScreenList(frontScreenBody).enqueue(object :Callback<DataClassFrontScreen>{
            override fun onResponse(
                call: Call<DataClassFrontScreen>,
                response: Response<DataClassFrontScreen>
            ) {

                if (response.body()?.code == 0){


                    val dataList = response.body()?.data?.data
                    dataList?.let { callback.onResult(it,null,frontScreenBody.page+1) }
                    _networkStatus.postValue(FrontScreenNetWorkStatus.FRONT_SCREEN_INITIAL_LOADED) //网络加载状态为加载完成
                    if(1 == response.body()?.data?.lastPage!!){
                        _networkStatus.postValue(FrontScreenNetWorkStatus.FRONT_SCREEN_COMPLETED)
                        return
                    }
                }
            }

            override fun onFailure(call: Call<DataClassFrontScreen>, t: Throwable) {
                _networkStatus.postValue(FrontScreenNetWorkStatus.FRONT_SCREEN_COMPLETED)
                //保存一个函数用{} 如果第一次加载失败了把loadInitial保存下来
                retry = {loadInitial(params,callback)} //retry的对象就是保存下来的对象 retry重新尝试加载当前的请求
                _networkStatus.postValue(FrontScreenNetWorkStatus.FRONT_SCREEN_FAILED) //网络加载状态为失败
                ToastUtils.showTextToast(context,"首页筛查列表网络请求失败")
            }

        })
    }
    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, DataFrontScreenX>) {
        retry = null
        frontScreenBody.page = params.key
        _networkStatus.postValue(FrontScreenNetWorkStatus.FRONT_SCREEN_LOADING) //网络加载状态为正在加载
        retrofitSingleton.api().postFrontScreenList(frontScreenBody).enqueue(object :Callback<DataClassFrontScreen>{
            override fun onResponse(
                call: Call<DataClassFrontScreen>,
                response: Response<DataClassFrontScreen>
            ) {
               if (response.body()?.code == 0){

                   if(params.key > response.body()?.data?.lastPage!!){
                       _networkStatus.postValue(FrontScreenNetWorkStatus.FRONT_SCREEN_COMPLETED)
                       return
                   }
                   val dataList = response.body()?.data?.data
                   dataList?.let { callback.onResult(it,params.key+1) }
                   _networkStatus.postValue(FrontScreenNetWorkStatus.FRONT_SCREEN_LOADED) //网络加载状态为加载完成
               }
            }

            override fun onFailure(call: Call<DataClassFrontScreen>, t: Throwable) {

                _networkStatus.postValue(FrontScreenNetWorkStatus.FRONT_SCREEN_COMPLETED)
                retry = {loadAfter(params,callback)} //retry重新尝试加载当前的请求
                _networkStatus.postValue(FrontScreenNetWorkStatus.FRONT_SCREEN_FAILED) //网络加载状态为失败
            }

        })

    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, DataFrontScreenX>
    ) {

    }



}