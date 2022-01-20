package com.hzdq.bajiesleepchildrenHD.dataclass
import com.google.gson.annotations.SerializedName


data class DataClassMyQuestionList(
    @SerializedName("code")
    val code: Int = 0, // 0
    @SerializedName("msg")
    val msg: String = "", // success
    @SerializedName("data")
    val `data`: List<DataMyQuestionList> = listOf()
)

data class DataMyQuestionList(
    @SerializedName("assessment_id")
    val assessmentId: Int = 0, // 26
    @SerializedName("type")
    val type: Int = 0, // 1
    @SerializedName("title")
    val title: String = "" ,// CAT量表
    @SerializedName("topic_mum")
    val topic_mum: Int = 0, // 1
    @SerializedName("topic_time")
    val topic_time: Int = 0, // 1
    @SerializedName("attend_num")
    val attend_num: Int = 0 // 1
)