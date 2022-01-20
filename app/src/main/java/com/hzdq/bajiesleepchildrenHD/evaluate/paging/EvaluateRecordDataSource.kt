package com.hzdq.bajiesleepchildrenHD.evaluate.paging

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.hzdq.bajiesleepchildrenHD.MainActivity
import com.hzdq.bajiesleepchildrenHD.dataclass.*
import com.hzdq.bajiesleepchildrenHD.retrofit.OkhttpSingleton
import com.hzdq.bajiesleepchildrenHD.retrofit.RetrofitSingleton
import com.hzdq.bajiesleepchildrenHD.utils.Shp
import com.hzdq.bajiesleepchildrenHD.utils.ToastUtils
import com.hzdq.bajiesleepchildrenHD.utils.timestamp2Date
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.ArrayList

//枚举类 网络状态
enum class EvaluateRecordNetWorkStatus{
    EVALUATE_RECORD_INITIAL_LOADING,
    EVALUATE_RECORD_INITIAL_LOADED,
    EVALUATE_RECORD_LOADING,
    EVALUATE_RECORD_LOADED,
    EVALUATE_RECORD_FAILED,
    EVALUATE_RECORD_COMPLETED
}
class EvaluateRecordDataSource(private val context: Context):PageKeyedDataSource<Int, DataEvaluateRecordX>() {

    var retry: (()-> Any)? = null  //retry可以是任何对象 retry表示重新加载时需要加载的对象
    private val _networkStatus = MutableLiveData<EvaluateRecordNetWorkStatus>()
    //网络状态的LiveData
    val networkStatus: LiveData<EvaluateRecordNetWorkStatus> = _networkStatus

    val retrofitSingleton = RetrofitSingleton.getInstance(context)

    val shp =Shp(context)
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, DataEvaluateRecordX>

    ) {
//        retry = null //重置retry
        retry = {loadInitial(params,callback)}

        _networkStatus.postValue(EvaluateRecordNetWorkStatus.EVALUATE_RECORD_INITIAL_LOADING) //网络加载状态为第一次加载

        retrofitSingleton.api().getEvaluateRecord(shp.getHospitalId()!!,shp.getPatientId()!!,1).enqueue(object :Callback<DataclassEvaluateRecord>{
            override fun onResponse(
                call: Call<DataclassEvaluateRecord>,
                response: Response<DataclassEvaluateRecord>
            ) {

                if(response.body()?.code == 0){

                    val dataList = response.body()?.data?.data
                    dataList?.let { callback.onResult(it,null,2) }
                    _networkStatus.postValue(EvaluateRecordNetWorkStatus.EVALUATE_RECORD_INITIAL_LOADED) //网络加载状态为加载完成
                    if (1 == response.body()?.data?.lastPage!!){
                        _networkStatus.postValue(EvaluateRecordNetWorkStatus.EVALUATE_RECORD_COMPLETED)
                        return
                    }

                }

            }

            override fun onFailure(call: Call<DataclassEvaluateRecord>, t: Throwable) {
                //保存一个函数用{} 如果第一次加载失败了把loadInitial保存下来
                _networkStatus.postValue(EvaluateRecordNetWorkStatus.EVALUATE_RECORD_COMPLETED)
                retry = {loadInitial(params,callback)} //retry的对象就是保存下来的对象 retry重新尝试加载当前的请求
                _networkStatus.postValue(EvaluateRecordNetWorkStatus.EVALUATE_RECORD_FAILED) //网络加载状态为失败
                ToastUtils.showTextToast(context,"评估记录列表网络请求失败")

            }

        })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, DataEvaluateRecordX>) {
        retry = null
        _networkStatus.postValue(EvaluateRecordNetWorkStatus.EVALUATE_RECORD_LOADING) //网络加载状态为正在加载
        retrofitSingleton.api().getEvaluateRecord(shp.getHospitalId()!!,shp.getPatientId()!!,params.key).enqueue(object :Callback<DataclassEvaluateRecord>{
            override fun onResponse(
                call: Call<DataclassEvaluateRecord>,
                response: Response<DataclassEvaluateRecord>
            ) {

               if (response.body()?.code == 0){
                   if (params.key > response.body()?.data?.lastPage!!){
                       _networkStatus.postValue(EvaluateRecordNetWorkStatus.EVALUATE_RECORD_COMPLETED)
                       return
                   }

                   val dataList = response.body()?.data?.data
                   dataList?.let { callback.onResult(it, params.key+1) } //callback.onResult将当前列表传入，第二个参数用当前页数+1 也就是下一页的页数
                   _networkStatus.postValue(EvaluateRecordNetWorkStatus.EVALUATE_RECORD_LOADED) //网络加载状态为加载完成
               }

            }

            override fun onFailure(call: Call<DataclassEvaluateRecord>, t: Throwable) {
                _networkStatus.postValue(EvaluateRecordNetWorkStatus.EVALUATE_RECORD_COMPLETED)
                retry = {loadAfter(params,callback)} //retry重新尝试加载当前的请求
                _networkStatus.postValue(EvaluateRecordNetWorkStatus.EVALUATE_RECORD_FAILED) //网络加载状态为失败
                ToastUtils.showTextToast(context,"评估记录列表网络请求失败")
            }

        })

    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, DataEvaluateRecordX>) {

    }




}