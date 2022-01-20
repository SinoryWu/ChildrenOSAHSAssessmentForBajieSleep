package com.hzdq.bajiesleepchildrenHD.dataclass
import com.google.gson.annotations.SerializedName


data class DataClassFrontDevice(
    @SerializedName("code")
    val code: Int = 0, // 0
    @SerializedName("data")
    val `data`: DataFrontDevice = DataFrontDevice(),
    @SerializedName("msg")
    val msg: String = "" // ok
)

data class DataFrontDevice(
    @SerializedName("data")
    val `data`: DataFrontDeviceX = DataFrontDeviceX(),
    @SerializedName("onlineNum")
    val onlineNum: Int = 0, // 0
    @SerializedName("totalNum")
    val totalNum: Int = 0 // 21
)

data class DataFrontDeviceX(
    @SerializedName("current_page")
    val currentPage: Int = 0, // 1
    @SerializedName("data")
    val `data`: List<DataFrontDeviceXX> = listOf(),
    @SerializedName("first_page_url")
    val firstPageUrl: String = "", // http://120.26.54.110:9501/v2/device?page=1
    @SerializedName("from")
    val from: Int = 0, // 1
    @SerializedName("last_page")
    val lastPage: Int = 0, // 3
    @SerializedName("last_page_url")
    val lastPageUrl: String = "", // http://120.26.54.110:9501/v2/device?page=3
    @SerializedName("next_page_url")
    val nextPageUrl: String = "", // http://120.26.54.110:9501/v2/device?page=2
    @SerializedName("path")
    val path: String = "", // http://120.26.54.110:9501/v2/device
    @SerializedName("per_page")
    val perPage: Int = 0, // 10
    @SerializedName("prev_page_url")
    val prevPageUrl: Any = Any(), // null
    @SerializedName("to")
    val to: Int = 0, // 10
    @SerializedName("total")
    val total: Int = 0 // 21
)

data class DataFrontDeviceXX(
    @SerializedName("outTime")
    val outTime: Int = 0, // 0
    @SerializedName("breathrate")
    val breathrate: Int = 0, // 0
    @SerializedName("heartrate")
    val heartrate: Int = 0, // 0
    @SerializedName("bloodoxygen")
    val bloodoxygen: Int = 0, // 0
    @SerializedName("tempetature")
    val tempetature: Int = 0, // 0
    @SerializedName("ringsn")
    val ringsn: String = "",
    @SerializedName("battery")
    val battery: Int = 0, // 0
    @SerializedName("devStatus")
    val devStatus: Int = 0, // 1
    @SerializedName("ringStatus")
    val ringStatus: Int = 0, // 0
    @SerializedName("powerStatus")
    val powerStatus: Int = 0, // -1
    @SerializedName("versionno")
    val versionno: String = "",
    @SerializedName("swversion")
    val swversion: String = "",
    @SerializedName("sim")
    val sim: String = "",
    @SerializedName("reportStatus")
    val reportStatus: Int = 0, // 0
    @SerializedName("lastUpdateTime")
    val lastUpdateTime: Int = 0, // 0
    @SerializedName("truename")
    val truename: String = "",
    @SerializedName("telephone")
    val telephone: String = "",
    @SerializedName("sn")
    val sn: String = "", // S01D1907000068
    @SerializedName("status")
    val status: Int = 0, // 1
    @SerializedName("statusDesc")
    val statusDesc: String = "", // 闲置
    @SerializedName("devStatus_str")
    val devStatusStr: String = "", // 闲置
    @SerializedName("scene")
    val scene: String = "", // 2
    @SerializedName("roomNo")
    val roomNo: String = "",
    @SerializedName("modeType")
    val modeType: String = "", // 0
    @SerializedName("id")
    val id: Int = 0, // 169
    @SerializedName("hospitalid")
    val hospitalid: Int = 0, // 24
    @SerializedName("hospitalName")
    val hospitalName: String = "" // 八戒睡眠
)