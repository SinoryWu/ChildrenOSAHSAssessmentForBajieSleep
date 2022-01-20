package com.hzdq.bajiesleepchildrenHD.dataclass
import com.google.gson.annotations.SerializedName


data class DataClassDeleteUser(
    @SerializedName("code")
    val code: Int = 0, // 0 10001，请结束监测回收设备后再删除！
    @SerializedName("msg")
    val msg: String = "", // 操作成功
    @SerializedName("data")
    val `data`: String = ""
)