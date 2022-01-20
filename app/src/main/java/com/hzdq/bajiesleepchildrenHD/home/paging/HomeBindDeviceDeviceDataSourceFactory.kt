package com.hzdq.bajiesleepchildrenHD.home.paging

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFrontDeviceXX
import com.hzdq.bajiesleepchildrenHD.dataclass.DataHomeBindDeviceDeviceXX
import com.hzdq.bajiesleepchildrenHD.device.paging.FrontDeviceDataSource

class HomeBindDeviceDeviceDataSourceFactory(private val context: Context): DataSource.Factory<Int, DataHomeBindDeviceDeviceXX>() {
    private var _dataSource = MutableLiveData<HomeBindDeviceDeviceDataSource>()
    val dataSource : LiveData<HomeBindDeviceDeviceDataSource> = _dataSource
    override fun create(): DataSource<Int, DataHomeBindDeviceDeviceXX> {
        return HomeBindDeviceDeviceDataSource(context).also {
            Log.d("sinory", "create: $it")
            _dataSource.postValue(it)
        }
    }
}