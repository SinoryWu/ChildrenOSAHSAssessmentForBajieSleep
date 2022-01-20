package com.hzdq.bajiesleepchildrenHD.dataclass
import com.google.gson.annotations.SerializedName


data class DataClassTreatmentDetail(
    @SerializedName("code")
    val code: Int = 0, // 0
    @SerializedName("msg")
    val msg: String = "", // success
    @SerializedName("data")
    val `data`: DataTreatmentDetail = DataTreatmentDetail()
)

data class DataTreatmentDetail(
    @SerializedName("id")
    val id: Int = 0, // 2
    @SerializedName("patient_id")
    val patientId: Int = 0, // 1426
    @SerializedName("oahi")
    val oahi: String = "",
    @SerializedName("osas")
    val osas: Int = 0, // 0
    @SerializedName("opinion")
    val opinion: String = "",
    @SerializedName("treatment")
    val treatment: String = "",
    @SerializedName("attachment")
    val attachment: List<String> = listOf(),
    @SerializedName("hospitalid")
    val hospitalid: Int = 0, // 24
    @SerializedName("create_time")
    val createTime: Int = 0, // 0
    @SerializedName("doctor_id")
    val doctorId: Int = 0, // 24
    @SerializedName("doctor_name")
    val doctorName: String = "", // 志明鸣测试
    @SerializedName("subtype")
    val subtype: Int = 0,
)