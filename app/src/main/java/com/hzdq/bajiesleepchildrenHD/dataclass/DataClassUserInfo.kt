package com.hzdq.bajiesleepchildrenHD.dataclass
import com.google.gson.annotations.SerializedName


data class DataClassUserInfo(
    @SerializedName("code")
    val code: Int = 0, // 0
    @SerializedName("msg")
    val msg: String = "", // ok
    @SerializedName("data")
    val `data`: DataUserInfo = DataUserInfo()
)

data class DataUserInfo(
    @SerializedName("uid")
    val uid: Int = 0, // 24
    @SerializedName("truename")
    val truename: String = "", // 志明鸣测试
    @SerializedName("mobile")
    val mobile: String = "", // 18969039910
    @SerializedName("type")
    val type: Int = 0, // 1
    @SerializedName("hospitalid")
    val hospitalid: Int = 0, // 24
    @SerializedName("hospitalname")
    val hospitalname: String = "", // 八戒睡眠
    @SerializedName("hospitalImage")
    val hospitalImage: String = "" // http://bajie-sleep.oss-cn-hangzhou.aliyuncs.com/%E4%B8%8B%E8%BD%BD.png
)