package com.hzdq.bajiesleepchildrenHD.dataclass

import com.google.gson.annotations.SerializedName

data class UpdateUserBody(
    @SerializedName("hospitalid")
    var hospitalid: Int = 0,
    @SerializedName("uid")
    var uid: Int = 0,
    @SerializedName("truename")
    var truename: String = "",
    @SerializedName("age")
    var age: Int = 0,
    @SerializedName("sex")
    var sex: Int = 0,
    @SerializedName("mobile")
    var mobile: String = "",
    @SerializedName("height")
    var height: String = "",
    @SerializedName("weight")
    var weight: String = "",
    @SerializedName("needfollow")
    var needfollow: Int = 0,
    @SerializedName("mzh")
    var mzh: String = ""
)