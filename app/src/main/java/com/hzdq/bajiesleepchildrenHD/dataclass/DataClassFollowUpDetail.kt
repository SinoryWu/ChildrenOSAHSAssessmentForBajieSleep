package com.hzdq.bajiesleepchildrenHD.dataclass

import com.google.gson.annotations.SerializedName


data class DataClassFollowUpDetail(
    @SerializedName("code")
    val code: Int = 0, // 0
    @SerializedName("msg")
    val msg: String = "", // success
    @SerializedName("data")
    val `data`: DataFollowUpDetail = DataFollowUpDetail()
)

data class DataFollowUpDetail(
    @SerializedName("id")
    val id: Int = 0, // 14
    @SerializedName("patient_id")
    val patientId: Int = 0, // 1338
    @SerializedName("oahi")
    val oahi: String = "", // 12.5
    @SerializedName("height")
    val height: String = "", // 140.5
    @SerializedName("weight")
    val weight: String = "", // 40.5
    @SerializedName("bmi")
    val bmi: String = "", // 20.5
    @SerializedName("suspend")
    val `suspend`: Int = 0, // 1
    @SerializedName("assessment")
    val assessment: List<AssessmentFollowUpDetail> = listOf(),
    @SerializedName("reason")
    val reason: String = "", // hhhhhh
    @SerializedName("next")
    val next: Int = 0, // 1647619199
    @SerializedName("hospitalid")
    val hospitalid: Int = 0, // 24
    @SerializedName("create_time")
    val createTime: Int = 0, // 1639832439
    @SerializedName("doctor_id")
    val doctorId: Int = 0, // 0
    @SerializedName("doctor_name")
    val doctorName: String = "",
    @SerializedName("neck")
    val neck: String = ""
)

data class AssessmentFollowUpDetail(
    @SerializedName("id")
    val id: Int = 0, // 0
    @SerializedName("name")
    val name: String = "", // OSA-18
    @SerializedName("score")
    val score: Int = 0, // 55
    @SerializedName("result")
    val result: String = "" // 较低影响
)