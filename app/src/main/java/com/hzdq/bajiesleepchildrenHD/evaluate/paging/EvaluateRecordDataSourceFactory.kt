package com.hzdq.bajiesleepchildrenHD.evaluate.paging

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.hzdq.bajiesleepchildrenHD.dataclass.DataEvaluateRecordX
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFrontUserX
import com.hzdq.bajiesleepchildrenHD.user.viewmodel.FrontUserViewModel

class EvaluateRecordDataSourceFactory(private val context: Context):DataSource.Factory<Int, DataEvaluateRecordX>() {
    private var _evaluateRecordDataSource = MutableLiveData<EvaluateRecordDataSource>()
    val evaluateRecordDataSource : LiveData<EvaluateRecordDataSource> = _evaluateRecordDataSource
    override fun create(): DataSource<Int, DataEvaluateRecordX> {
        return EvaluateRecordDataSource(context).also {
            Log.d("sinory", "create: $it")
            _evaluateRecordDataSource.postValue(it)
        }
    }

}