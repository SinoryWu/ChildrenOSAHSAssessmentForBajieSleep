package com.hzdq.bajiesleepchildrenHD.randomvisit.paging

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFollowUpListX
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFollowUpX
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFrontUserX
import com.hzdq.bajiesleepchildrenHD.dataclass.DataTreatRecordX
import com.hzdq.bajiesleepchildrenHD.user.viewmodel.FrontUserViewModel

class FollowUpDataSourceFactory(private val context: Context):DataSource.Factory<Int, DataFollowUpX>() {
    private var _followUpDataSource = MutableLiveData<FollowUpDataSource>()
    val followUpDataSource : LiveData<FollowUpDataSource> = _followUpDataSource
    override fun create(): DataSource<Int, DataFollowUpX> {
        return FollowUpDataSource(context).also {

            _followUpDataSource.postValue(it)
        }
    }

}