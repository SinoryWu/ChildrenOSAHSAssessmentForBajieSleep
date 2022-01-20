package com.hzdq.bajiesleepchildrenHD.home.paging
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.hzdq.bajiesleepchildrenHD.dataclass.*
import com.hzdq.bajiesleepchildrenHD.device.paging.FrontDeviceNetWorkStatus
import com.hzdq.bajiesleepchildrenHD.retrofit.RetrofitSingleton
import com.hzdq.bajiesleepchildrenHD.utils.Shp
import com.hzdq.bajiesleepchildrenHD.utils.ToastUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


//枚举类 网络状态
enum class HomeUserSearchNetWorkStatus{
    HOME_USER_SEARCH_INITIAL_LOADING,
    HOME_USER_SEARCH_USER_INITIAL_LOADED,
    HOME_USER_SEARCH_USER_LOADING,
    HOME_USER_SEARCH_USER_LOADED,
    HOME_USER_SEARCH_USER_FAILED,
    HOME_USER_SEARCH_USER_COMPLETED
}
class HomeUserSearchDataSource(private val context: Context): PageKeyedDataSource<Int, DataHomeUserSearchX>() {
    var retry: (()-> Any)? = null  //retry可以是任何对象 retry表示重新加载时需要加载的对象
    private val _networkStatus = MutableLiveData<HomeUserSearchNetWorkStatus>()
    //网络状态的LiveData
    val networkStatus: LiveData<HomeUserSearchNetWorkStatus> = _networkStatus

    val retrofitSingleton = RetrofitSingleton.getInstance(context)

    val shp = Shp(context)
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, DataHomeUserSearchX>
    ) {
        retry = null //重置retry
        _networkStatus.postValue(HomeUserSearchNetWorkStatus.HOME_USER_SEARCH_INITIAL_LOADING) //网络加载状态为第一次加载
        retrofitSingleton.api().getHomeUserSearchList(10,shp.getHospitalId()!!,shp.getHomeUserSearchKeyWord()!!,1,0).enqueue(object :Callback<DataClassHomeUserSearch>{
            override fun onResponse(
                call: Call<DataClassHomeUserSearch>,
                response: Response<DataClassHomeUserSearch>
            ) {
                if (response.body()?.code == 0){

                    val dataList = response.body()?.data?.data
                    dataList?.let { callback.onResult(it,null,2) }
                    _networkStatus.postValue(HomeUserSearchNetWorkStatus.HOME_USER_SEARCH_USER_INITIAL_LOADED) //网络加载状态为加载完成
                    if(1 == response.body()?.data?.lastPage!!){
                        _networkStatus.postValue(HomeUserSearchNetWorkStatus.HOME_USER_SEARCH_USER_COMPLETED)
                        return
                    }

                }
            }

            override fun onFailure(call: Call<DataClassHomeUserSearch>, t: Throwable) {
                _networkStatus.postValue(HomeUserSearchNetWorkStatus.HOME_USER_SEARCH_USER_COMPLETED)
                //保存一个函数用{} 如果第一次加载失败了把loadInitial保存下来
                retry = {loadInitial(params,callback)} //retry的对象就是保存下来的对象 retry重新尝试加载当前的请求
                _networkStatus.postValue(HomeUserSearchNetWorkStatus.HOME_USER_SEARCH_USER_FAILED) //网络加载状态为失败
                ToastUtils.showTextToast(context,"用户列表网络请求失败")
            }

        })
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, DataHomeUserSearchX>
    ) {
        retry = null

        _networkStatus.postValue(HomeUserSearchNetWorkStatus.HOME_USER_SEARCH_USER_LOADING) //网络加载状态为正在加载
        retrofitSingleton.api().getHomeUserSearchList(10,shp.getHospitalId()!!,shp.getHomeUserSearchKeyWord()!!,params.key,0).enqueue(object :Callback<DataClassHomeUserSearch>{
            override fun onResponse(
                call: Call<DataClassHomeUserSearch>,
                response: Response<DataClassHomeUserSearch>
            ) {

                if (response.body()?.code == 0){
                    if(params.key > response.body()?.data?.lastPage!!){

                        _networkStatus.postValue(HomeUserSearchNetWorkStatus.HOME_USER_SEARCH_USER_COMPLETED)
                        return
                    }

                    val dataList = response.body()?.data?.data
                    dataList?.let { callback.onResult(it,params.key+1) }
                    _networkStatus.postValue(HomeUserSearchNetWorkStatus.HOME_USER_SEARCH_USER_LOADED) //网络加载状态为加载完成
                }
            }

            override fun onFailure(call: Call<DataClassHomeUserSearch>, t: Throwable) {
                _networkStatus.postValue(HomeUserSearchNetWorkStatus.HOME_USER_SEARCH_USER_COMPLETED)
                //保存一个函数用{} 如果第一次加载失败了把loadInitial保存下来
                retry = {loadAfter(params,callback)} //retry的对象就是保存下来的对象 retry重新尝试加载当前的请求
                _networkStatus.postValue(HomeUserSearchNetWorkStatus.HOME_USER_SEARCH_USER_FAILED) //网络加载状态为失败
                ToastUtils.showTextToast(context,"网路请求失败")
            }

        })
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, DataHomeUserSearchX>
    ) {

    }


}