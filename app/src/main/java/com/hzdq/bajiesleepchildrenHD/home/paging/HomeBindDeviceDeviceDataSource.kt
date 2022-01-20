package com.hzdq.bajiesleepchildrenHD.home.paging
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.hzdq.bajiesleepchildrenHD.dataclass.DataClassFrontDevice
import com.hzdq.bajiesleepchildrenHD.dataclass.DataClassHomeBindDeviceDevice
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFrontDeviceXX
import com.hzdq.bajiesleepchildrenHD.dataclass.DataHomeBindDeviceDeviceXX
import com.hzdq.bajiesleepchildrenHD.device.paging.FrontDeviceNetWorkStatus
import com.hzdq.bajiesleepchildrenHD.retrofit.RetrofitSingleton
import com.hzdq.bajiesleepchildrenHD.utils.Shp
import com.hzdq.bajiesleepchildrenHD.utils.ToastUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


//枚举类 网络状态
enum class HomeBindDeviceDeviceNetWorkStatus{
    HOME_BIND_DEVICE_DEVICE_INITIAL_LOADING,
    HOME_BIND_DEVICE_DEVICE_INITIAL_LOADED,
    HOME_BIND_DEVICE_DEVICE_LOADING,
    HOME_BIND_DEVICE_DEVICE_LOADED,
    HOME_BIND_DEVICE_DEVICE_FAILED,
    HOME_BIND_DEVICE_DEVICE_COMPLETED
}
class HomeBindDeviceDeviceDataSource(private val context: Context): PageKeyedDataSource<Int, DataHomeBindDeviceDeviceXX>() {
    var retry: (()-> Any)? = null  //retry可以是任何对象 retry表示重新加载时需要加载的对象
    private val _networkStatus = MutableLiveData<HomeBindDeviceDeviceNetWorkStatus>()
    //网络状态的LiveData
    val networkStatus: LiveData<HomeBindDeviceDeviceNetWorkStatus> = _networkStatus

    val retrofitSingleton = RetrofitSingleton.getInstance(context)

    val shp = Shp(context)
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, DataHomeBindDeviceDeviceXX>
    ) {

        retry = null //重置retry
        _networkStatus.postValue(HomeBindDeviceDeviceNetWorkStatus.HOME_BIND_DEVICE_DEVICE_INITIAL_LOADING) //网络加载状态为第一次加载
        retrofitSingleton.api().getHomeBindDeviceDeviceList(10,shp.getHospitalId()!!,shp.getHomeBindDeviceDeviceKeyWord()!!,1,0).enqueue(object :Callback<DataClassHomeBindDeviceDevice>{
            override fun onResponse(
                call: Call<DataClassHomeBindDeviceDevice>,
                response: Response<DataClassHomeBindDeviceDevice>
            ) {
                if (response.body()?.code == 0){

                    val dataList = response.body()?.data?.data?.data
                    dataList?.let { callback.onResult(it,null,2) }
                    _networkStatus.postValue(HomeBindDeviceDeviceNetWorkStatus.HOME_BIND_DEVICE_DEVICE_INITIAL_LOADED) //网络加载状态为加载完成
                    if(1 == response.body()?.data?.data?.lastPage!!){
                        _networkStatus.postValue(HomeBindDeviceDeviceNetWorkStatus.HOME_BIND_DEVICE_DEVICE_COMPLETED)
                        return
                    }

                }
            }

            override fun onFailure(call: Call<DataClassHomeBindDeviceDevice>, t: Throwable) {
                _networkStatus.postValue(HomeBindDeviceDeviceNetWorkStatus.HOME_BIND_DEVICE_DEVICE_COMPLETED)
                //保存一个函数用{} 如果第一次加载失败了把loadInitial保存下来
                retry = {loadInitial(params,callback)} //retry的对象就是保存下来的对象 retry重新尝试加载当前的请求
                _networkStatus.postValue(HomeBindDeviceDeviceNetWorkStatus.HOME_BIND_DEVICE_DEVICE_FAILED) //网络加载状态为失败
                ToastUtils.showTextToast(context,"设备列表网络请求失败")
            }

        })
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, DataHomeBindDeviceDeviceXX>
    ) {

//        shp.getHomeBindDeviceDeviceKeyWord()!!
        retry = null
        _networkStatus.postValue(HomeBindDeviceDeviceNetWorkStatus.HOME_BIND_DEVICE_DEVICE_LOADING) //网络加载状态为正在加载
        retrofitSingleton.api().getHomeBindDeviceDeviceList(10,shp.getHospitalId()!!,shp.getHomeBindDeviceDeviceKeyWord()!!,params.key,0).enqueue(object :Callback<DataClassHomeBindDeviceDevice>{
            override fun onResponse(
                call: Call<DataClassHomeBindDeviceDevice>,
                response: Response<DataClassHomeBindDeviceDevice>
            ) {
                if (response.body()?.code == 0){
                    if(params.key > response.body()?.data?.data?.lastPage!!){
                        _networkStatus.postValue(HomeBindDeviceDeviceNetWorkStatus.HOME_BIND_DEVICE_DEVICE_COMPLETED)
                        return
                    }
                    val dataList = response.body()?.data?.data?.data
                    dataList?.let { callback.onResult(it,params.key+1) }
                    _networkStatus.postValue(HomeBindDeviceDeviceNetWorkStatus.HOME_BIND_DEVICE_DEVICE_LOADED) //网络加载状态为加载完成
                }
            }

            override fun onFailure(call: Call<DataClassHomeBindDeviceDevice>, t: Throwable) {
                _networkStatus.postValue(HomeBindDeviceDeviceNetWorkStatus.HOME_BIND_DEVICE_DEVICE_COMPLETED)
                //保存一个函数用{} 如果第一次加载失败了把loadInitial保存下来
                retry = {loadAfter(params,callback)} //retry的对象就是保存下来的对象 retry重新尝试加载当前的请求
                _networkStatus.postValue(HomeBindDeviceDeviceNetWorkStatus.HOME_BIND_DEVICE_DEVICE_FAILED) //网络加载状态为失败
                ToastUtils.showTextToast(context,"设备列表网络请求失败")
            }

        })
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, DataHomeBindDeviceDeviceXX>
    ) {

    }
}