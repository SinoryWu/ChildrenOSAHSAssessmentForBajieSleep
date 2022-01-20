package com.hzdq.bajiesleepchildrenHD.dataclass

import com.google.gson.annotations.SerializedName

data class NewTaskBody (
    @SerializedName("id")
    var id: String = "", // id  非必填
    @SerializedName("type")
    var type: String = "", // 13:PSQ 14:OSA-18
    @SerializedName("data")
    var name: String = "",// 名字 非必填
    @SerializedName("start")
    var start: String = "", // 开始时间戳
    @SerializedName("end")
    var end: String = "", // 结束时间戳
    @SerializedName("login")
    var login: String = "", //
    @SerializedName("resubmit")
    var resubmit: String = "", //
    @SerializedName("shows")
    var shows: String = "", //
    @SerializedName("modify")
    var modify: String = "", //
    @SerializedName("hospital_id")
    var hospital_id: String = ""
)