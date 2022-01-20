package com.hzdq.bajiesleepchildrenHD.home.paging

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFrontDeviceXX
import com.hzdq.bajiesleepchildrenHD.dataclass.DataHomeBindDeviceDeviceXX
import com.hzdq.bajiesleepchildrenHD.dataclass.DataHomeBindDeviceUserX
import com.hzdq.bajiesleepchildrenHD.dataclass.DataHomeUserSearchX
import com.hzdq.bajiesleepchildrenHD.device.paging.FrontDeviceDataSource

class HomeUserSearchDataSourceFactory(private val context: Context): DataSource.Factory<Int, DataHomeUserSearchX>() {
    private var _dataSource = MutableLiveData<HomeUserSearchDataSource>()
    val dataSource : LiveData<HomeUserSearchDataSource> = _dataSource
    override fun create(): DataSource<Int, DataHomeUserSearchX> {
        return HomeUserSearchDataSource(context).also {
            Log.d("sinory", "create: $it")
            _dataSource.postValue(it)
        }
    }
}