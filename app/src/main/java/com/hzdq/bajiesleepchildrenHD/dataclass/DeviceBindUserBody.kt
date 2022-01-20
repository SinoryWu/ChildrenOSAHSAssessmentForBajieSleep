package com.hzdq.bajiesleepchildrenHD.dataclass

import com.google.gson.annotations.SerializedName

data class DeviceBindUserBody (
    @SerializedName("type")
    var type: String = "", // 0
    @SerializedName("devStatus")
    var devStatus: String = "", // 添加成功
    @SerializedName("uid")
    var uid: String = "",
    @SerializedName("monitor")
    var monitor: String = "",
    @SerializedName("frequency")
    var frequency: String = "",
    @SerializedName("hospitalid")
    var hospitalid: String = ""
)


