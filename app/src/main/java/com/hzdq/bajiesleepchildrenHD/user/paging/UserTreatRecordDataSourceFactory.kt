package com.hzdq.bajiesleepchildrenHD.user.paging

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFrontUserX
import com.hzdq.bajiesleepchildrenHD.dataclass.DataTreatRecordX
import com.hzdq.bajiesleepchildrenHD.user.viewmodel.FrontUserViewModel

class UserTreatRecordDataSourceFactory(private val context: Context):DataSource.Factory<Int, DataTreatRecordX>() {
    private var _userTreatRecordDataSource = MutableLiveData<UserTreatRecordDataSource>()
    val userTreatRecordDataSource : LiveData<UserTreatRecordDataSource> = _userTreatRecordDataSource
    override fun create(): DataSource<Int, DataTreatRecordX> {
        return UserTreatRecordDataSource(context).also {

            _userTreatRecordDataSource.postValue(it)
        }
    }

}