package com.hzdq.bajiesleepchildrenHD.dataclass
import com.google.gson.annotations.SerializedName


data class DataClassUpdateUser(
    @SerializedName("code")
    var code: Int = 0, // 0
    @SerializedName("msg")
    var msg: String = "", // 更新成功  "code": 10010,更新失败

    @SerializedName("data")
    var `data`: DataUpdateUser = DataUpdateUser()
//    var `data`: String = "",
)

data class DataUpdateUser(
    @SerializedName("uid")
    var uid: String = "", // 1469
    @SerializedName("hospitalid")
    var hospitalid: String = "", // 24
    @SerializedName("truename")
    var truename: String = "", // 测试9
    @SerializedName("age")
    var age: String = "", // 13
    @SerializedName("sex")
    var sex: String = "", // 1
    @SerializedName("height")
    var height: String = "", // 140
    @SerializedName("weight")
    var weight: String = "", // 40
    @SerializedName("needfollow")
    var needfollow: String = "", // 0
    @SerializedName("mobile")
    var mobile: String = "" // 11021233123123
)