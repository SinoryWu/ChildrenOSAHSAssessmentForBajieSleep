package com.hzdq.bajiesleepchildrenHD.dataclass
import com.google.gson.annotations.SerializedName


data class DataclassResultPSQDetail(
    @SerializedName("code")
    val code: Int = 0, // 0
    @SerializedName("msg")
    val msg: String = "", // success
    @SerializedName("data")
    val `data`: DataPSQDetail = DataPSQDetail()
)

data class DataPSQDetail(
    @SerializedName("id")
    val id: Int = 0, // 1126
    @SerializedName("uid")
    val uid: Int = 0, // 36
    @SerializedName("task_id")
    val taskId: Int = 0, // 1
    @SerializedName("type")
    val type: Int = 0, // 13
    @SerializedName("total")
    val total: Int = 0, // 12
    @SerializedName("result")
    val result: String = "", // 高风险
    @SerializedName("measures")
    val measures: String = "",
    @SerializedName("truename")
    val truename: String = "", // 杨超
    @SerializedName("content")
    val content: ContentPSQDetail = ContentPSQDetail(),
    @SerializedName("create_time")
    val createTime: Int = 0 // 1638517907
)

data class ContentPSQDetail(
    @SerializedName("127")
    val x127: Int = 0, // 1
    @SerializedName("128")
    val x128: Int = 0, // 1
    @SerializedName("129")
    val x129: Int = 0, // 0
    @SerializedName("130")
    val x130: Int = 0, // 0
    @SerializedName("131")
    val x131: Int = 0, // 1
    @SerializedName("132")
    val x132: Int = 0, // 1
    @SerializedName("133")
    val x133: Int = 0, // 0
    @SerializedName("134")
    val x134: Int = 0, // 0
    @SerializedName("135")
    val x135: Int = 0, // 1
    @SerializedName("136")
    val x136: Int = 0, // 1
    @SerializedName("137")
    val x137: Int = 0, // 0
    @SerializedName("138")
    val x138: Int = 0, // 0
    @SerializedName("139")
    val x139: Int = 0, // 1
    @SerializedName("140")
    val x140: Int = 0, // 1
    @SerializedName("141")
    val x141: Int = 0, // 0
    @SerializedName("142")
    val x142: Int = 0, // 0
    @SerializedName("143")
    val x143: Int = 0, // 1
    @SerializedName("144")
    val x144: Int = 0, // 1
    @SerializedName("145")
    val x145: Int = 0, // 0
    @SerializedName("146")
    val x146: Int = 0, // 0
    @SerializedName("147")
    val x147: Int = 0, // 1
    @SerializedName("148")
    val x148: Int = 0 // 1
)