package com.hzdq.bajiesleepchildrenHD.home.paging

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFrontDeviceXX
import com.hzdq.bajiesleepchildrenHD.dataclass.DataHomeBindDeviceDeviceXX
import com.hzdq.bajiesleepchildrenHD.dataclass.DataHomeBindDeviceUserX
import com.hzdq.bajiesleepchildrenHD.device.paging.FrontDeviceDataSource

class HomeBindDeviceUserDataSourceFactory(private val context: Context): DataSource.Factory<Int, DataHomeBindDeviceUserX>() {
    private var _dataSource = MutableLiveData<HomeBindDeviceUserDataSource>()
    val dataSource : LiveData<HomeBindDeviceUserDataSource> = _dataSource
    override fun create(): DataSource<Int, DataHomeBindDeviceUserX> {
        return HomeBindDeviceUserDataSource(context).also {
            Log.d("sinory", "create: $it")
            _dataSource.postValue(it)
        }
    }
}