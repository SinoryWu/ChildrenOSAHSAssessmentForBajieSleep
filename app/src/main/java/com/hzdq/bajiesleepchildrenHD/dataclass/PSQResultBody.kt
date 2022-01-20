package com.hzdq.bajiesleepchildrenHD.dataclass
import com.google.gson.annotations.SerializedName


data class PSQResultBody(
    @SerializedName("type")
    var type: Int = 0, // 13
    @SerializedName("task_id")
    var taskId: Int = 0, // 1
    @SerializedName("uid")
    var uid: Int = 0, // 24
    @SerializedName("user_info")
    var userInfo: String = "",
    @SerializedName("content")
    var content: String = "",
    @SerializedName("hospital_id")
    var hospitalId: Int = 0 // 24
)

