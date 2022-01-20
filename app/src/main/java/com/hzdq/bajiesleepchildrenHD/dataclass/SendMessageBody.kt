package com.hzdq.bajiesleepchildrenHD.dataclass

import com.google.gson.annotations.SerializedName

data class SendMessageBody(
    @SerializedName("mobile")
    var mobile: String = "" // 0
)