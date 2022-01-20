package com.hzdq.bajiesleepchildrenHD.dataclass
import com.google.gson.annotations.SerializedName


data class DataClassSoftInfo(
    @SerializedName("code")
    var code: Int = 0, // 0
    @SerializedName("msg")
    var msg: String = "", // success
    @SerializedName("data")
    var `data`: DataSoftInfo = DataSoftInfo()
)

data class DataSoftInfo(
    @SerializedName("id")
    var id: Int = 0, // 1
    @SerializedName("version")
    var version: String = "", // 1.0.0
    @SerializedName("type")
    var type: Int = 0, // 2
    @SerializedName("subtype")
    var subtype: Int = 0, // 0
    @SerializedName("content")
    var content: String = "", // 3333
    @SerializedName("url")
    var url: String = "", // http://baidu.com/a.apk
    @SerializedName("status")
    var status: Int = 0 // 1
)