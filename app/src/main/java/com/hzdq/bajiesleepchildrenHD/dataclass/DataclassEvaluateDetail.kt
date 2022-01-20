package com.hzdq.bajiesleepchildrenHD.dataclass
import com.google.gson.annotations.SerializedName

data class DataclassEvaluateDetail(
    @SerializedName("code")
    var code: Int = 0, // 0
    @SerializedName("msg")
    var msg: String = "", // success
    @SerializedName("data")
    var `data`: DataEvaluateDetail = DataEvaluateDetail()
)

data class DataEvaluateDetail(
    @SerializedName("id")
    var id: Int = 0, // 22
    @SerializedName("patient_id")
    var patientId: Int = 0, // 1489
    @SerializedName("oahi")
    var oahi: String = "", // 12
    @SerializedName("osas")
    var osas: Int = 0, // 2
    @SerializedName("opinion")
    var opinion: String = "", // eyywuwu
    @SerializedName("treat")
    var treat: Int = 0, // 2
    @SerializedName("treatment")
    var treatment: String = "", // jsjsjsjsj
    @SerializedName("result")
    var result: String = "", // 中风险
    @SerializedName("assessment")
    var assessment: List<AssessmentEvaluateDetail> = listOf(),
    @SerializedName("hospitalid")
    var hospitalid: Int = 0, // 24
    @SerializedName("create_time")
    var createTime: String = "", // 1639558772
    @SerializedName("doctor_id")
    var doctorId: Int = 0, // 24
    @SerializedName("doctor_name")
    var doctorName: String = "" // 志明鸣测试
)

data class AssessmentEvaluateDetail(
    @SerializedName("id")
    var id: Int = 0, // 0
    @SerializedName("name")
    var name: String = "", // OSA-18
    @SerializedName("score")
    var score: Int = 0, // 60
    @SerializedName("result")
    var result: String = "" // 中度影响
)


