package com.hzdq.bajiesleepchildrenHD.user.paging

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PageKeyedDataSource
import com.hzdq.bajiesleepchildrenHD.MainActivity
import com.hzdq.bajiesleepchildrenHD.dataclass.DataClassFrontUser
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFrontUserX
import com.hzdq.bajiesleepchildrenHD.frontpagefragment.UserFragment
import com.hzdq.bajiesleepchildrenHD.retrofit.RetrofitSingleton
import com.hzdq.bajiesleepchildrenHD.user.viewmodel.FrontUserViewModel
import com.hzdq.bajiesleepchildrenHD.utils.Shp
import com.hzdq.bajiesleepchildrenHD.utils.ToastUtils
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//枚举类 网络状态
enum class FrontUserNetWorkStatus{
    FRONT_USER_INITIAL_LOADING,
    FRONT_USER_INITIAL_LOADED,
    FRONT_USER_LOADING,
    FRONT_USER_LOADED,
    FRONT_USER_FAILED,
    FRONT_USER_COMPLETED
}
class FrontUserDataSource(private val context: Context):PageKeyedDataSource<Int,DataFrontUserX>() {

    var retry: (()-> Any)? = null  //retry可以是任何对象 retry表示重新加载时需要加载的对象
    private val _networkStatus = MutableLiveData<FrontUserNetWorkStatus>()
    //网络状态的LiveData
    val networkStatus: LiveData<FrontUserNetWorkStatus> = _networkStatus

    val retrofitSingleton = RetrofitSingleton.getInstance(context)

    val shp =Shp(context)
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, DataFrontUserX>

    ) {
//        retry = null //重置retry
        retry = {loadInitial(params,callback)}
        Log.d("sinory", "loadInitial: ${shp.getFrontUserKeyWord()!!}")

        _networkStatus.postValue(FrontUserNetWorkStatus.FRONT_USER_INITIAL_LOADING) //网络加载状态为第一次加载
        retrofitSingleton.api().getFrontUserList(shp.getFrontUserKeyWord()!!,shp.getHospitalId()!!,10,1,0).enqueue(object :Callback<DataClassFrontUser>{
            override fun onResponse(
                call: Call<DataClassFrontUser>,
                response: Response<DataClassFrontUser>
            ) {

                if(response.body()?.code == 0){

                    val dataList = response.body()?.data?.data
                    dataList?.let { callback.onResult(it,null,2) }
                    _networkStatus.postValue(FrontUserNetWorkStatus.FRONT_USER_INITIAL_LOADED) //网络加载状态为加载完成
                    if (1 == response.body()?.data?.lastPage!!){
                        _networkStatus.postValue(FrontUserNetWorkStatus.FRONT_USER_COMPLETED)
                        return
                    }

                }

            }

            override fun onFailure(call: Call<DataClassFrontUser>, t: Throwable) {
                //保存一个函数用{} 如果第一次加载失败了把loadInitial保存下来
                _networkStatus.postValue(FrontUserNetWorkStatus.FRONT_USER_COMPLETED)
                retry = {loadInitial(params,callback)} //retry的对象就是保存下来的对象 retry重新尝试加载当前的请求
                _networkStatus.postValue(FrontUserNetWorkStatus.FRONT_USER_FAILED) //网络加载状态为失败
                ToastUtils.showTextToast(context,"用户列表网络请求失败")

            }

        })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, DataFrontUserX>) {
        retry = null
        _networkStatus.postValue(FrontUserNetWorkStatus.FRONT_USER_LOADING) //网络加载状态为正在加载
        retrofitSingleton.api().getFrontUserList(Shp(context).getFrontUserKeyWord()!!,shp.getHospitalId()!!,10,params.key,0).enqueue(object :Callback<DataClassFrontUser>{
            override fun onResponse(
                call: Call<DataClassFrontUser>,
                response: Response<DataClassFrontUser>
            ) {

               if (response.body()?.code == 0){
                   if (params.key > response.body()?.data?.lastPage!!){
                       _networkStatus.postValue(FrontUserNetWorkStatus.FRONT_USER_COMPLETED)
                       return
                   }
                   val dataList = response.body()?.data?.data
                   dataList?.let { callback.onResult(it, params.key+1) } //callback.onResult将当前列表传入，第二个参数用当前页数+1 也就是下一页的页数
                   _networkStatus.postValue(FrontUserNetWorkStatus.FRONT_USER_LOADED) //网络加载状态为加载完成

               }

            }

            override fun onFailure(call: Call<DataClassFrontUser>, t: Throwable) {
                _networkStatus.postValue(FrontUserNetWorkStatus.FRONT_USER_COMPLETED)
                retry = {loadAfter(params,callback)} //retry重新尝试加载当前的请求
                _networkStatus.postValue(FrontUserNetWorkStatus.FRONT_USER_FAILED) //网络加载状态为失败
                ToastUtils.showTextToast(context,"用户列表网络请求失败")
            }

        })

    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, DataFrontUserX>) {

    }




}