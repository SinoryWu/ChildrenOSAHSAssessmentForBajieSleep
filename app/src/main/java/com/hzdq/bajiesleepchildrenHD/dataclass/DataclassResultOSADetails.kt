package com.hzdq.bajiesleepchildrenHD.dataclass
import com.google.gson.annotations.SerializedName

data class DataClassResultOSADetail(
    @SerializedName("code")
    val code: Int = 0, // 0
    @SerializedName("msg")
    val msg: String = "", // success
    @SerializedName("data")
    val `data`: DataResultOSADetail = DataResultOSADetail()
)

data class DataResultOSADetail(
    @SerializedName("id")
    val id: Int = 0, // 1156
    @SerializedName("uid")
    val uid: Int = 0, // 1466
    @SerializedName("task_id")
    val taskId: Int = 0, // 0
    @SerializedName("type")
    val type: Int = 0, // 14
    @SerializedName("total")
    val total: Int = 0, // 68
    @SerializedName("result")
    val result: String = "", // 中度影响
    @SerializedName("measures")
    val measures: String = "",
    @SerializedName("truename")
    val truename: String = "",
    @SerializedName("content")
    val content: ContentResultOSADetail = ContentResultOSADetail(),
    @SerializedName("create_time")
    val createTime: Int = 0 // 1639022783
)

data class ContentResultOSADetail(
    @SerializedName("150")
    val x150: Int = 0, // 5
    @SerializedName("151")
    val x151: Int = 0, // 5
    @SerializedName("152")
    val x152: Int = 0, // 4
    @SerializedName("153")
    val x153: Int = 0, // 4
    @SerializedName("154")
    val x154: Int = 0, // 4
    @SerializedName("155")
    val x155: Int = 0, // 3
    @SerializedName("156")
    val x156: Int = 0, // 4
    @SerializedName("157")
    val x157: Int = 0, // 3
    @SerializedName("158")
    val x158: Int = 0, // 4
    @SerializedName("159")
    val x159: Int = 0, // 3
    @SerializedName("160")
    val x160: Int = 0, // 4
    @SerializedName("161")
    val x161: Int = 0, // 3
    @SerializedName("162")
    val x162: Int = 0, // 4
    @SerializedName("163")
    val x163: Int = 0, // 3
    @SerializedName("164")
    val x164: Int = 0, // 5
    @SerializedName("165")
    val x165: Int = 0, // 3
    @SerializedName("166")
    val x166: Int = 0, // 3
    @SerializedName("167")
    val x167: Int = 0 // 4
)
