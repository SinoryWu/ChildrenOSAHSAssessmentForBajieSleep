package com.hzdq.bajiesleepchildrenHD.dataclass

import com.google.gson.annotations.SerializedName

data class EditDeviceBody (
    @SerializedName("sn")
    var sn: String = "", // 0
    @SerializedName("status")
    var status: Int = 1, // 添加成功
    @SerializedName("scene")
    var scene: Int = 1, // 添加成功
    @SerializedName("mode_type")
    val mode_type : Int = 1,
    @SerializedName("monitor")
    var monitor : String = "",
    @SerializedName("hospitalid")
    var hospitalid : Int = 0
)