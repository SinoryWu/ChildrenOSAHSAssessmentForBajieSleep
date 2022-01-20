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
import com.hzdq.bajiesleepchildrenHD.dataclass.*
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
enum class UserFollowUpNetWorkStatus{
    USER_FOLLOW_UP_INITIAL_LOADING,
    USER_FOLLOW_UP_INITIAL_LOADED,
    USER_FOLLOW_UP_LOADING,
    USER_FOLLOW_UP_LOADED,
    USER_FOLLOW_UP_FAILED,
    USER_FOLLOW_UP_COMPLETED
}
class UserFollowUpDataSource(private val context: Context):PageKeyedDataSource<Int, DataFollowUpListX>() {

    var retry: (()-> Any)? = null  //retry可以是任何对象 retry表示重新加载时需要加载的对象
    private val _networkStatus = MutableLiveData<UserFollowUpNetWorkStatus>()
    //网络状态的LiveData
    val networkStatus: LiveData<UserFollowUpNetWorkStatus> = _networkStatus

    val retrofitSingleton = RetrofitSingleton.getInstance(context)

    val shp =Shp(context)
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, DataFollowUpListX>

    ) {
//        retry = null //重置retry
        retry = {loadInitial(params,callback)}

        _networkStatus.postValue(UserFollowUpNetWorkStatus.USER_FOLLOW_UP_INITIAL_LOADING) //网络加载状态为第一次加载
        retrofitSingleton.api().getFollowUpList(shp.getTreatRecordPatientId()!!,shp.getHospitalId()!!,1).enqueue(object :Callback<DataClassFollowUpList>{
            override fun onResponse(
                call: Call<DataClassFollowUpList>,
                response: Response<DataClassFollowUpList>
            ) {

                if(response.body()?.code == 0){

                    val dataList = response.body()?.data?.data
                    dataList?.let { callback.onResult(it,null,2) }
                    _networkStatus.postValue(UserFollowUpNetWorkStatus.USER_FOLLOW_UP_INITIAL_LOADED) //网络加载状态为加载完成
                    if (1 == response.body()?.data?.lastPage!!){
                        _networkStatus.postValue(UserFollowUpNetWorkStatus.USER_FOLLOW_UP_COMPLETED)
                        return
                    }

                }

            }

            override fun onFailure(call: Call<DataClassFollowUpList>, t: Throwable) {
                //保存一个函数用{} 如果第一次加载失败了把loadInitial保存下来
                _networkStatus.postValue(UserFollowUpNetWorkStatus.USER_FOLLOW_UP_COMPLETED)
                retry = {loadInitial(params,callback)} //retry的对象就是保存下来的对象 retry重新尝试加载当前的请求
                _networkStatus.postValue(UserFollowUpNetWorkStatus.USER_FOLLOW_UP_FAILED) //网络加载状态为失败


            }

        })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, DataFollowUpListX>) {
        retry = null
        _networkStatus.postValue(UserFollowUpNetWorkStatus.USER_FOLLOW_UP_LOADING) //网络加载状态为正在加载
        retrofitSingleton.api().getFollowUpList(shp.getTreatRecordPatientId()!!,shp.getHospitalId()!!,params.key).enqueue(object :Callback<DataClassFollowUpList>{
            override fun onResponse(
                call: Call<DataClassFollowUpList>,
                response: Response<DataClassFollowUpList>
            ) {

               if (response.body()?.code == 0){
                   if (params.key > response.body()?.data?.lastPage!!){
                       _networkStatus.postValue(UserFollowUpNetWorkStatus.USER_FOLLOW_UP_COMPLETED)
                       return
                   }

                   val dataList = response.body()?.data?.data
                   dataList?.let { callback.onResult(it, params.key+1) } //callback.onResult将当前列表传入，第二个参数用当前页数+1 也就是下一页的页数
                   _networkStatus.postValue(UserFollowUpNetWorkStatus.USER_FOLLOW_UP_LOADED) //网络加载状态为加载完成
               }

            }

            override fun onFailure(call: Call<DataClassFollowUpList>, t: Throwable) {
                _networkStatus.postValue(UserFollowUpNetWorkStatus.USER_FOLLOW_UP_COMPLETED)
                retry = {loadAfter(params,callback)} //retry重新尝试加载当前的请求
                _networkStatus.postValue(UserFollowUpNetWorkStatus.USER_FOLLOW_UP_FAILED) //网络加载状态为失败
            }

        })

    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, DataFollowUpListX>) {

    }




}