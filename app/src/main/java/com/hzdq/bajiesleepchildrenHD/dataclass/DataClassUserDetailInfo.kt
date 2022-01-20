package com.hzdq.bajiesleepchildrenHD.dataclass
import com.google.gson.annotations.SerializedName


data class DataClassUserDetailInfo(
    @SerializedName("code")
    var code: Int = 0, // 0
    @SerializedName("msg")
    var msg: String = "", // success
    @SerializedName("data")
    var `data`: DataUserDetailInfo = DataUserDetailInfo()
)

data class DataUserDetailInfo(
    @SerializedName("userinfo")
    var userinfo: UserinfoUserDetailInfo = UserinfoUserDetailInfo(),
    @SerializedName("oahi")
    var oahi: MutableList<OahiUserDetailInfo> = mutableListOf()
)

data class UserinfoUserDetailInfo(
    @SerializedName("uid")
    var uid: Int = 0, // 1356
    @SerializedName("truename")
    var truename: String = "", // Buy
    @SerializedName("age")
    var age: Int = 0, // 2
    @SerializedName("height")
    var height: String = "", // 100
    @SerializedName("weight")
    var weight: String = "", // 20
    @SerializedName("sex")
    var sex: Int = 0, // 1
    @SerializedName("mobile")
    var mobile: String = "", // 18811111111
    @SerializedName("needfollow")
    var needfollow: Int = 0, // 0
    @SerializedName("oahi")
    var oahi: String = "",
    @SerializedName("follow_time")
    var followTime: Int = 0, // 0
    @SerializedName("oahi_res")
    var oahiRes: String = "", // 未评估
    @SerializedName("estimate_res")
    var estimateRes: String = "",
    @SerializedName("estimate_id")
    var estimateId: Int = 0,
    @SerializedName("hospitalid")
    var hospitalId: Int = 0,
    @SerializedName("mzh")
    var mzh: String = ""
)

data class OahiUserDetailInfo(
    @SerializedName("oahi")
    var oahi: String = "",
    @SerializedName("create_time")
    var createTime: Int = 0 // 1639992766
)