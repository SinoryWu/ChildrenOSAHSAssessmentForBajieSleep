package com.hzdq.bajiesleepchildrenHD.device

import androidx.lifecycle.LiveData


class SingletonStringLiveData private constructor() : LiveData<String?>() {
    public override fun postValue(value: String?) {
        super.postValue(value)
    }

    public override fun setValue(value: String?) {
        super.setValue(value)
    }

    companion object {
        private var sInstance: SingletonStringLiveData? = null
        val instance: SingletonStringLiveData?
            get() {
                if (sInstance == null) {
                    sInstance = SingletonStringLiveData()
                }
                return sInstance
            }
    }
}

