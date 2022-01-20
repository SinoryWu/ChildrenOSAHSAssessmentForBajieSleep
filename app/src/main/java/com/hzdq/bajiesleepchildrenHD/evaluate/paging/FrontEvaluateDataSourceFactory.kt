package com.hzdq.bajiesleepchildrenHD.evaluate.paging

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFrontUserX
import com.hzdq.bajiesleepchildrenHD.user.viewmodel.FrontUserViewModel

class FrontEvaluateDataSourceFactory(private val context: Context):DataSource.Factory<Int,DataFrontUserX>() {
    private var _frontEvaluateDataSource = MutableLiveData<FrontEvaluateDataSource>()
    val frontEvaluateDataSource : LiveData<FrontEvaluateDataSource> = _frontEvaluateDataSource
    override fun create(): DataSource<Int, DataFrontUserX> {
        return FrontEvaluateDataSource(context).also {
            Log.d("sinory", "create: $it")
            _frontEvaluateDataSource.postValue(it)
        }
    }

}