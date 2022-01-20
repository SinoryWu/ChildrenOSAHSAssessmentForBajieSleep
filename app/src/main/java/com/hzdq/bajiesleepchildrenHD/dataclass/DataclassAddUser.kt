package com.hzdq.bajiesleepchildrenHD.dataclass
import com.google.gson.annotations.SerializedName


data class DataclassAddUser(
    @SerializedName("code")
    val code: Int = 0, // 0
    @SerializedName("msg")
    val msg: String = "", // 添加成功
    @SerializedName("params")
    val `data`: DataAddUser = DataAddUser()
)

data class DataAddUser(
    @SerializedName("id")
    val id: Int = 0, // 1469
    @SerializedName("truename")
    val truename: String = "", // test9
    @SerializedName("sex")
    val sex: String = "", // 1
    @SerializedName("age")
    val age: String = "", // 8
    @SerializedName("height")
    val height: String = "", // 150
    @SerializedName("weight")
    val weight: String = "", // 40
    @SerializedName("mobile")
    val mobile: String = "", // 11021233123123
    @SerializedName("hospitalid")
    val hospitalid: String = "" // 24
)

