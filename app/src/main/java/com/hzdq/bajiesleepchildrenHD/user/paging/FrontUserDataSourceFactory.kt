package com.hzdq.bajiesleepchildrenHD.user.paging

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFrontUserX
import com.hzdq.bajiesleepchildrenHD.user.viewmodel.FrontUserViewModel

class FrontUserDataSourceFactory(private val context: Context):DataSource.Factory<Int,DataFrontUserX>() {
    private var _frontUserDataSource = MutableLiveData<FrontUserDataSource>()
    val frontUserDataSource : LiveData<FrontUserDataSource> = _frontUserDataSource
    override fun create(): DataSource<Int, DataFrontUserX> {
        return FrontUserDataSource(context).also {
            Log.d("sinory", "create: $it")
            _frontUserDataSource.postValue(it)
        }
    }

}