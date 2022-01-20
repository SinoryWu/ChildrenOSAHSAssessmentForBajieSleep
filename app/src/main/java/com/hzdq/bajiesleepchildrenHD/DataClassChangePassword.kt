package com.hzdq.bajiesleepchildrenHD
import com.google.gson.annotations.SerializedName


data class DataClassChangePassword(
    @SerializedName("code")
    val code: Int = 0, // 0
    @SerializedName("msg")
    val msg: String = "", // success
    @SerializedName("data")
    val `data`: String = ""
)