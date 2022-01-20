package com.hzdq.bajiesleepchildrenHD.randomvisit.paging

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFrontFollowUpX
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFrontUserX
import com.hzdq.bajiesleepchildrenHD.user.viewmodel.FrontUserViewModel

class FrontFollowUpDataSourceFactory(private val context: Context):DataSource.Factory<Int,DataFrontFollowUpX>() {
    private var _frontFollowUpDataSource = MutableLiveData<FrontFollowUpDataSource>()
    val frontFollowUpDataSource : LiveData<FrontFollowUpDataSource> = _frontFollowUpDataSource
    override fun create(): DataSource<Int, DataFrontFollowUpX> {
        return FrontFollowUpDataSource(context).also {
            Log.d("sinory", "create: $it")
            _frontFollowUpDataSource.postValue(it)
        }
    }

}