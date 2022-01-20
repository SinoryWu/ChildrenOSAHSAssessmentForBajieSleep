package com.hzdq.bajiesleepchildrenHD.user.paging

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFollowUpListX
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFrontUserX
import com.hzdq.bajiesleepchildrenHD.dataclass.DataTreatRecordX
import com.hzdq.bajiesleepchildrenHD.user.viewmodel.FrontUserViewModel

class UserFollowUpDataSourceFactory(private val context: Context):DataSource.Factory<Int, DataFollowUpListX>() {
    private var _userFollowUpDataSource = MutableLiveData<UserFollowUpDataSource>()
    val userFollowUpDataSource : LiveData<UserFollowUpDataSource> = _userFollowUpDataSource
    override fun create(): DataSource<Int, DataFollowUpListX> {
        return UserFollowUpDataSource(context).also {

            _userFollowUpDataSource.postValue(it)
        }
    }

}