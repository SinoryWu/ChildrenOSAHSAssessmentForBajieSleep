package com.hzdq.bajiesleepchildrenHD.dataclass
import com.google.gson.annotations.SerializedName


data class DataClassTaskInfo(
    @SerializedName("code")
    var code: Int = 0, // 0
    @SerializedName("msg")
    var msg: String = "", // success
    @SerializedName("data")
    var `data`: DataTaskInfo = DataTaskInfo()
)

data class DataTaskInfo(
    @SerializedName("id")
    var id: Int = 0, // 12
    @SerializedName("type")
    var type: Int = 0, // 13
    @SerializedName("name")
    var name: String = "", // PSQ
    @SerializedName("start")
    var start: Int = 0, // 1638256359
    @SerializedName("end")
    var end: Int = 0, // 1638518075
    @SerializedName("login")
    var login: Int = 0, // 1
    @SerializedName("resubmit")
    var resubmit: Int = 0, // 24
    @SerializedName("shows")
    var shows: Int = 0, // 1
    @SerializedName("modify")
    var modify: Int = 0, // 0
    @SerializedName("num")
    var num: Int = 0, // 0
    @SerializedName("views")
    var views: Int = 0, // 0
    @SerializedName("hospital_id")
    var hospitalId: Int = 0, // 24
    @SerializedName("hospital_name")
    var hospitalName: String = "", // 八戒睡眠
    @SerializedName("status")
    var status: Int = 0, // 3
    @SerializedName("register")
    var register: Int = 0
)