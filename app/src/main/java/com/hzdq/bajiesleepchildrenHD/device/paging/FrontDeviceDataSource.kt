package com.hzdq.bajiesleepchildrenHD.device.paging

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.hzdq.bajiesleepchildrenHD.dataclass.DataClassFrontDevice
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFrontDeviceXX
import com.hzdq.bajiesleepchildrenHD.retrofit.RetrofitSingleton
import com.hzdq.bajiesleepchildrenHD.utils.Shp
import com.hzdq.bajiesleepchildrenHD.utils.ToastUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//枚举类 网络状态
enum class FrontDeviceNetWorkStatus{
    FRONT_DEVICE_INITIAL_LOADING,
    FRONT_DEVICE_INITIAL_LOADED,
    FRONT_DEVICE_LOADING,
    FRONT_DEVICE_LOADED,
    FRONT_DEVICE_FAILED,
    FRONT_DEVICE_COMPLETED
}
class FrontDeviceDataSource(private val context: Context): PageKeyedDataSource<Int, DataFrontDeviceXX>() {
    var retry: (()-> Any)? = null  //retry可以是任何对象 retry表示重新加载时需要加载的对象
    private val _networkStatus = MutableLiveData<FrontDeviceNetWorkStatus>()
    //网络状态的LiveData
    val networkStatus: LiveData<FrontDeviceNetWorkStatus> = _networkStatus

    val retrofitSingleton = RetrofitSingleton.getInstance(context)
    val shp = Shp(context)
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, DataFrontDeviceXX>
    ) {


        retry = null //重置retry
        _networkStatus.postValue(FrontDeviceNetWorkStatus.FRONT_DEVICE_INITIAL_LOADING) //网络加载状态为第一次加载
        retrofitSingleton.api().getFrontDeviceList(10,shp.getHospitalId()!!,shp.getFrontDeviceKeyWord()!!,1,1,shp.getFrontDeviceStatus()!!).enqueue(object :Callback<DataClassFrontDevice>{
            override fun onResponse(
                call: Call<DataClassFrontDevice>,
                response: Response<DataClassFrontDevice>
            ) {

                if (response.body()?.code == 0){


                    val dataList = response.body()?.data?.data?.data
                    dataList?.let { callback.onResult(it,null,2) }
                    _networkStatus.postValue(FrontDeviceNetWorkStatus.FRONT_DEVICE_INITIAL_LOADED) //网络加载状态为加载完成
                    if(1 == response.body()?.data?.data?.lastPage!!){
                        _networkStatus.postValue(FrontDeviceNetWorkStatus.FRONT_DEVICE_COMPLETED)
                        return
                    }
                }
            }

            override fun onFailure(call: Call<DataClassFrontDevice>, t: Throwable) {
                _networkStatus.postValue(FrontDeviceNetWorkStatus.FRONT_DEVICE_COMPLETED)
                //保存一个函数用{} 如果第一次加载失败了把loadInitial保存下来
                retry = {loadInitial(params,callback)} //retry的对象就是保存下来的对象 retry重新尝试加载当前的请求
                _networkStatus.postValue(FrontDeviceNetWorkStatus.FRONT_DEVICE_FAILED) //网络加载状态为失败
                ToastUtils.showTextToast(context,"首页设备列表网络请求失败")
            }

        })

    }


    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, DataFrontDeviceXX>
    ) {
        retry = null
        _networkStatus.postValue(FrontDeviceNetWorkStatus.FRONT_DEVICE_LOADING) //网络加载状态为正在加载
        retrofitSingleton.api().getFrontDeviceList(10,shp.getHospitalId()!!,shp.getFrontDeviceKeyWord()!!,1,params.key,shp.getFrontDeviceStatus()!!).enqueue(object :Callback<DataClassFrontDevice>{
            override fun onResponse(
                call: Call<DataClassFrontDevice>,
                response: Response<DataClassFrontDevice>
            ) {
                if (response.body()?.code == 0){
                    if(params.key > response.body()?.data?.data?.lastPage!!){
                        _networkStatus.postValue(FrontDeviceNetWorkStatus.FRONT_DEVICE_COMPLETED)
                        return
                    }
                    val dataList = response.body()?.data?.data?.data
                    dataList?.let { callback.onResult(it,params.key+1) }
                    _networkStatus.postValue(FrontDeviceNetWorkStatus.FRONT_DEVICE_LOADED) //网络加载状态为加载完成
                }

            }

            override fun onFailure(call: Call<DataClassFrontDevice>, t: Throwable) {
                _networkStatus.postValue(FrontDeviceNetWorkStatus.FRONT_DEVICE_COMPLETED)
                retry = {loadAfter(params,callback)} //retry重新尝试加载当前的请求
                _networkStatus.postValue(FrontDeviceNetWorkStatus.FRONT_DEVICE_FAILED) //网络加载状态为失败
            }

        })
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, DataFrontDeviceXX>
    ) {

    }




}