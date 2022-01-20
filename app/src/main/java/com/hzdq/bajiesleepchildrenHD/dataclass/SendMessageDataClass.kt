package com.hzdq.bajiesleepchildrenHD.dataclass
import com.google.gson.annotations.SerializedName


data class SendMessageDataClass(
    @SerializedName("code")
    val code: Int = 0, // 0
    @SerializedName("msg")
    val msg: String = "", // 发送成功
    @SerializedName("data")
    val `data`: List<Any> = listOf()
)