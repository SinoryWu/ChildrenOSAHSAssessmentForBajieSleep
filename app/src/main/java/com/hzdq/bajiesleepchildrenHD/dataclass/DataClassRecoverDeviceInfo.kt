package com.hzdq.bajiesleepchildrenHD.dataclass
import com.google.gson.annotations.SerializedName


data class DataClassRecoverDeviceInfo(
    @SerializedName("code")
    val code: Int = 0, // 0
    @SerializedName("data")
    val `data`: DataRecoverDeviceInfo = DataRecoverDeviceInfo(),
    @SerializedName("msg")
    val msg: String = "" // ok
)

data class DataRecoverDeviceInfo(
    @SerializedName("type")
    val type: Int = 0, // 2
    @SerializedName("uid")
    val uid: Int = 0, // 27254
    @SerializedName("sex")
    val sex: String = "", // 男
    @SerializedName("mobile")
    val mobile: String = "", // 18281688634
    @SerializedName("truename")
    val truename: String = "", // 黄梁梓轩
    @SerializedName("report")
    val report: List<ReportRecoverDeviceInfo> = listOf(),
    @SerializedName("reportNum")
    val reportNum: Int = 0, // 1
    @SerializedName("sn")
    val sn: String = "", // S01D2101000171
    @SerializedName("hospitalid")
    val hospitalid: String = "" // 24
)

data class ReportRecoverDeviceInfo(
    @SerializedName("id")
    val id: Int = 0, // 14234
    @SerializedName("update_time")
    val updateTime: Int = 0, // 1638926249
    @SerializedName("create_time")
    val createTime: Int = 0, // 1638914508
    @SerializedName("report_id")
    val reportId: String = "", // 黄梁梓轩 12月08日 06:00
    @SerializedName("complete")
    val complete: Int = 0, // 1
    @SerializedName("quality")
    val quality: Int = 0, // 1,有效报告 2，缺少血氧，3，无效报告
    @SerializedName("reportUrl")
    val reportUrl: String = "" // 61afd987473ddf0b5d766a7e
)