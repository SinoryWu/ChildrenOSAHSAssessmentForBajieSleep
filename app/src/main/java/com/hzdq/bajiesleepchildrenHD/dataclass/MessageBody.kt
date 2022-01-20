package com.hzdq.bajiesleepchildrenHD.dataclass

import com.google.gson.annotations.SerializedName

data class MessageBody (
    @SerializedName("mobile")
    var mobile: String = "", // 0
    @SerializedName("code")
    var code: String = "" ,// ok
    @SerializedName("reg_id")
    var reg_id: String = "" // o

)

