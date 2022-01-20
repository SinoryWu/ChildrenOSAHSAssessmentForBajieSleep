package com.hzdq.bajiesleepchildrenHD.dataclass
import com.google.gson.annotations.SerializedName


data class DataClassSettingBaseInfo(
    @SerializedName("code")
    var code: Int = 0, // 0
    @SerializedName("msg")
    var msg: String = "", // success
    @SerializedName("data")
    var `data`: DataSettingBaseInfo = DataSettingBaseInfo()
)

data class DataSettingBaseInfo(
    @SerializedName("hospital_name")
    var hospitalName: String = "", // 八戒睡眠
    @SerializedName("hospital_logo")
    var hospitalLogo: String = "", // http://bajie-sleep.oss-cn-hangzhou.aliyuncs.com/%E4%B8%8B%E8%BD%BD.png
    @SerializedName("report_name")
    var reportName: String = "", // 八戒报告
    @SerializedName("report_logo")
    var reportLogo: String = "", // http://bajie-sleep.oss-cn-hangzhou.aliyuncs.com/%E4%B8%8B%E8%BD%BD.png
    @SerializedName("report_standard")
    var reportStandard: String = "", // 1
    @SerializedName("report_evaluate")
    var reportEvaluate: String = "" // 1
)