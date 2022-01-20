package com.hzdq.bajiesleepchildrenHD.dataclass
import com.google.gson.annotations.SerializedName


data class MessageDateClass(
    @SerializedName("code")
    val code: Int = 0, // 0
    @SerializedName("data")
    val `data`: MessageData = MessageData(),
    @SerializedName("msg")
    val msg: String = "" // ok
)

data class MessageData(
    @SerializedName("token")
    val token: String = "", // f04a47ff616091f9fe137f89cce88b40
    @SerializedName("uid")
    val uid: Int = 0, // 24
    @SerializedName("session_id")
    val sessionId: String = "" // iNOfNl5GTQSTODzsZNA8iVrgPsVDP84IoMCty5F2
)