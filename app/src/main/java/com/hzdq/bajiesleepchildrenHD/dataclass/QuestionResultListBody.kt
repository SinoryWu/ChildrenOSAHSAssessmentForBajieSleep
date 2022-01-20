package com.hzdq.bajiesleepchildrenHD.dataclass

import com.google.gson.annotations.SerializedName

data class QuestionResultListBody (
    @SerializedName("type")
    var type: String = "", // 0
    @SerializedName("task_id")
    var task_id: String = "", // success
    @SerializedName("uid")
    var uid: String = "",
    @SerializedName("hospital_id")
    var hospital_id: String = "",
    @SerializedName("page")
    var page: Int = 1,
    @SerializedName("limit")
    var limit: Int = 0,
    @SerializedName("start")
    var start: String = "0",
    @SerializedName("end")
    var end: String = "0",
    @SerializedName("keyword")
    var keyword: String = "",
)