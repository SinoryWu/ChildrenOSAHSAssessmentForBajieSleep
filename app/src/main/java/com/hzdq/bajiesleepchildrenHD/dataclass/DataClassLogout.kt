package com.hzdq.bajiesleepchildrenHD.dataclass
import com.google.gson.annotations.SerializedName


data class DataClassLogout(
    @SerializedName("code")
    val code: Int = 0, // 0
    @SerializedName("msg")
    val msg: String = "", // 退出成功
    @SerializedName("data")
    val `data`: String = ""
)