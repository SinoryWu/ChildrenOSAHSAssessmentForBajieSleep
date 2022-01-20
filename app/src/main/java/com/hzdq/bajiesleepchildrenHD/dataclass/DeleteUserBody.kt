package com.hzdq.bajiesleepchildrenHD.dataclass

import com.google.gson.annotations.SerializedName

data class DeleteUserBody (
    @SerializedName("hospitalid")
    var hospitalid: Int = 0,
    @SerializedName("uid")
    var uid: Int = 0,
    @SerializedName("status")
    val status: Int = -1
)