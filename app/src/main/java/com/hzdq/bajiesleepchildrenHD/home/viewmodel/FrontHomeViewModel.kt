package com.hzdq.bajiesleepchildrenHD.home.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFrontHomeX

class FrontHomeViewModel(application: Application) : AndroidViewModel(application) {

    val frontHomeXList = MutableLiveData<List<DataFrontHomeX>>()
}