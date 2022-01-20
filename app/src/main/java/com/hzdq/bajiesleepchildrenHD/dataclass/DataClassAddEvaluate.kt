package com.hzdq.bajiesleepchildrenHD.dataclass
import com.google.gson.annotations.SerializedName


data class DataClassAddEvaluate(
    @SerializedName("code")
    var code: Int = 0, // 0
    @SerializedName("msg")
    var msg: String = "", // success
    @SerializedName("data")
    var `data`: DataAddEvaluate = DataAddEvaluate()
)

data class DataAddEvaluate(
    @SerializedName("id")
    var id: Int = 0, // 2
    @SerializedName("result")
    var result: String = "" // 高风险
)