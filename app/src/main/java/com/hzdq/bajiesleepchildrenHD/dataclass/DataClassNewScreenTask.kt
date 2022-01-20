package com.hzdq.bajiesleepchildrenHD.dataclass
import com.google.gson.annotations.SerializedName


data class DataClassNewScreenTask(
    @SerializedName("code")
    var code: Int = 0, // 0
    @SerializedName("msg")
    var msg: String = "", // success
    @SerializedName("data")
    var `data`: String = "" // 1
)