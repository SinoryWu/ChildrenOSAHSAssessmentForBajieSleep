package com.hzdq.bajiesleepchildrenHD.user.paging

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFrontUserX
import com.hzdq.bajiesleepchildrenHD.dataclass.DataReportListX
import com.hzdq.bajiesleepchildrenHD.dataclass.DataTreatRecordX
import com.hzdq.bajiesleepchildrenHD.user.viewmodel.FrontUserViewModel

class UserReportListDataSourceFactory(private val context: Context):DataSource.Factory<Int, DataReportListX>() {
    private var _userReportListDataSource = MutableLiveData<UserReportListDataSource>()
    val userReportListDataSource : LiveData<UserReportListDataSource> = _userReportListDataSource
    override fun create(): DataSource<Int, DataReportListX> {
        return UserReportListDataSource(context).also {

            _userReportListDataSource.postValue(it)
        }
    }

}