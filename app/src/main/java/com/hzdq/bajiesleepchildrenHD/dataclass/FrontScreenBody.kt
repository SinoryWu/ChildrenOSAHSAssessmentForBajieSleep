package com.hzdq.bajiesleepchildrenHD.dataclass

import com.google.gson.annotations.SerializedName

data class FrontScreenBody (
    @SerializedName("type")
    var type: Int = 0, // 0
    @SerializedName("hospital_id")
    var hospital_id: Int = 0 ,// ok
    @SerializedName("page")
    var page: Int = 0, // o

)

