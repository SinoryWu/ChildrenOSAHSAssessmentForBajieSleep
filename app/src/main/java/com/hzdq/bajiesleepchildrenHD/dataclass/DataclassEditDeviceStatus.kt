package com.hzdq.bajiesleepchildrenHD.dataclass

import com.google.gson.annotations.SerializedName

data class DataclassEditDeviceStatus (
    @SerializedName("code")
    val code: Int = 0, // 0
    @SerializedName("msg")
    val msg: String = "", // 添加成功
    @SerializedName("data")
    val `data`: DataAddUser = DataAddUser()
)