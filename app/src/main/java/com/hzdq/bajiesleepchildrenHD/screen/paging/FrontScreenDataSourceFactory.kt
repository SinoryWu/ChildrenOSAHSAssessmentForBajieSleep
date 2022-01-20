package com.hzdq.bajiesleepchildrenHD.screen.paging

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFrontDeviceXX
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFrontScreenX
import com.hzdq.bajiesleepchildrenHD.device.paging.FrontDeviceDataSource

class FrontScreenDataSourceFactory(private val context: Context): DataSource.Factory<Int, DataFrontScreenX>() {
    private var _frontScreenDataSource = MutableLiveData<FrontScreenDataSource>()
    val frontScreenDataSource : LiveData<FrontScreenDataSource> = _frontScreenDataSource
    override fun create(): DataSource<Int, DataFrontScreenX> {
        return FrontScreenDataSource(context).also {
            Log.d("sinory", "create: $it")
            _frontScreenDataSource.postValue(it)
        }
    }
}