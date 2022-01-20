package com.hzdq.bajiesleepchildrenHD.device.paging

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFrontDeviceXX
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFrontUserX
import com.hzdq.bajiesleepchildrenHD.user.paging.FrontUserDataSource

class FrontDeviceDataSourceFactory(private val context: Context): DataSource.Factory<Int, DataFrontDeviceXX>()  {
    private var _frontDeviceDataSource = MutableLiveData<FrontDeviceDataSource>()
    val frontDeviceDataSource : LiveData<FrontDeviceDataSource> = _frontDeviceDataSource
    override fun create(): DataSource<Int, DataFrontDeviceXX> {
        return FrontDeviceDataSource(context).also {
            _frontDeviceDataSource.postValue(it)
        }
    }
}