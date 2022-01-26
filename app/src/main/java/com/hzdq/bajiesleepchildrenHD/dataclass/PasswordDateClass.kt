package com.hzdq.bajiesleepchildrenHD.dataclass
import com.google.gson.annotations.SerializedName


data class PasswordDateClass(
    @SerializedName("code")
    var code: Int = 0, // 0
    @SerializedName("data")
    var `data`: PasswordData = PasswordData(),
    @SerializedName("msg")
    var msg: String = "" // ok
)

data class PasswordData(
    @SerializedName("token")
    var token: String = "", // f04a47ff616091f9fe137f89cce88b40
    @SerializedName("uid")
    var uid: Int = 0, // 24
    @SerializedName("session_id")
    var sessionId: String = "" // iNOfNl5GTQSTODzsZNA8iVrgPsVDP84IoMCty5F2
)