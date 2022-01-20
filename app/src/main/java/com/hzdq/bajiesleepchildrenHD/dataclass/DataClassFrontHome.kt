package com.hzdq.bajiesleepchildrenHD.dataclass
import com.google.gson.annotations.SerializedName


data class DataClassFrontHome(
    @SerializedName("code")
    val code: Int = 0, // 0
    @SerializedName("data")
    val `data`: DataFrontHome = DataFrontHome(),
    @SerializedName("msg")
    val msg: String = "" // ok
)

data class DataFrontHome(
    @SerializedName("current_page")
    val currentPage: Int = 0, // 1
    @SerializedName("data")
    val `data`: List<DataFrontHomeX> = listOf(),
    @SerializedName("first_page_url")
    val firstPageUrl: String = "", // http://120.26.54.110:9501/v2/report?page=1
    @SerializedName("from")
    val from: Int = 0, // 1
    @SerializedName("last_page")
    val lastPage: Int = 0, // 55
    @SerializedName("last_page_url")
    val lastPageUrl: String = "", // http://120.26.54.110:9501/v2/report?page=55
    @SerializedName("next_page_url")
    val nextPageUrl: String = "", // http://120.26.54.110:9501/v2/report?page=2
    @SerializedName("path")
    val path: String = "", // http://120.26.54.110:9501/v2/report
    @SerializedName("per_page")
    val perPage: Int = 0, // 5
    @SerializedName("prev_page_url")
    val prevPageUrl: Any = Any(), // null
    @SerializedName("to")
    val to: Int = 0, // 5
    @SerializedName("total")
    val total: Int = 0 // 272
)

data class DataFrontHomeX(
    @SerializedName("remtime")
    val remtime: String = "", // 71
    @SerializedName("lightsleeptime")
    val lightsleeptime: String = "", // 235
    @SerializedName("deepsleeptime")
    val deepsleeptime: String = "", // 158
    @SerializedName("startsleeptime")
    val startsleeptime: String = "", // 1613679060000
    @SerializedName("id")
    val id: Int = 0, // 878
    @SerializedName("sn")
    val sn: String = "", // S01D2007000046
    @SerializedName("ahi")
    val ahi: String = "", // 1.42
    @SerializedName("uid")
    val uid: Int = 0, // 1356
    @SerializedName("report_id")
    val reportId: String = "", // 24-1970-01-01-1356
    @SerializedName("hospitalid")
    val hospitalid: Int = 0, // 24
    @SerializedName("truename")
    val truename: String = "", // Buy
    @SerializedName("complete")
    val complete: Int = 0, // 4
    @SerializedName("hospitalName")
    val hospitalName: String = "", // 八戒睡眠
    @SerializedName("sleeptime")
    val sleeptime: String = "", // 21:50-06:00
    @SerializedName("quality")
    val quality: Int = 0, // 4
    @SerializedName("ahiLevel")
    val ahiLevel: Int = 0, // 2
    @SerializedName("modeType")
    val modeType: String = "", // 1
    @SerializedName("createTime")
    val createTime: Int = 0, // 1613685630
    @SerializedName("reportUrl")
    val reportUrl: String = "" // 602ee37924b2a637e04d95b8
)