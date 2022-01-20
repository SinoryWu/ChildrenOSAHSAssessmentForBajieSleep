package com.hzdq.bajiesleepchildrenHD.user.paging

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFrontUserX
import com.hzdq.bajiesleepchildrenHD.dataclass.DataTreatRecordX
import com.hzdq.bajiesleepchildrenHD.dataclass.DataUserQuestionResultListX
import com.hzdq.bajiesleepchildrenHD.user.viewmodel.FrontUserViewModel

class UserQuestionListDataSourceFactory(private val context: Context):DataSource.Factory<Int, DataUserQuestionResultListX>() {
    private var _userQuestionListDataSource = MutableLiveData<UserQuestionListDataSource>()
    val userQuestionListDataSource : LiveData<UserQuestionListDataSource> = _userQuestionListDataSource
    override fun create(): DataSource<Int, DataUserQuestionResultListX> {
        return UserQuestionListDataSource(context).also {

            _userQuestionListDataSource.postValue(it)
        }
    }

}