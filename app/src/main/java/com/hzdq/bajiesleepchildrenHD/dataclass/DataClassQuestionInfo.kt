package com.hzdq.bajiesleepchildrenHD.dataclass
import com.google.gson.annotations.SerializedName


data class DataClassQuestionInfo(
    @SerializedName("code")
    val code: Int = 0, // 0
    @SerializedName("msg")
    val msg: String = "", // success
    @SerializedName("data")
    val `data`: DataQuestionInfo = DataQuestionInfo()
)

data class DataQuestionInfo(
    @SerializedName("assessment")
    val assessment: List<AssessmentQuestionInfo> = listOf(),
    @SerializedName("title")
    val title: String = "", // PSQ
    @SerializedName("describe")
    val describe: String = "",
    @SerializedName("standard")
    val standard: String = ""
)

data class AssessmentQuestionInfo(
    @SerializedName("assessment_id")
    val assessmentId: Int = 0, // 127
    @SerializedName("desc")
    val desc: List<String> = listOf(),
    @SerializedName("assessment_name")
    val assessmentName: String = "", // 睡觉打鼾超过睡眠时间的一半
    @SerializedName("subtitle")
    val subtitle: String = "",
    @SerializedName("depth")
    val depth: String = "", // 呼吸
    @SerializedName("vaule")
    val vaule: List<Int> = listOf()
)