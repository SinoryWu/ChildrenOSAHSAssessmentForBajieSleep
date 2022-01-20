package com.hzdq.bajiesleepchildrenHD.dataclass
import com.google.gson.annotations.SerializedName


data class DataClassQuestionResult(
    @SerializedName("code")
    val code: Int = 0, // 0
    @SerializedName("msg")
    val msg: String = "", // success
    @SerializedName("data")
    val `data`: QuestionResultData = QuestionResultData()
)

data class QuestionResultData(
    @SerializedName("measures")
    val measures: String = "",
    @SerializedName("result_id")
    val id: Int = 0, // 1121
    @SerializedName("total")
    val total: Int = 0, // 36
    @SerializedName("result")
    val result: String = "", // 较低影响
    @SerializedName("createTime")
    val createTime: Int = 0 // 1638325584
)