package com.hzdq.bajiesleepchildrenHD.dataclass
import com.google.gson.annotations.SerializedName


data class DataClassFrontScreen(
    @SerializedName("code")
    val code: Int = 0, // 0
    @SerializedName("msg")
    val msg: String = "", // success
    @SerializedName("data")
    val `data`: DataFrontScreen = DataFrontScreen()
)

data class DataFrontScreen(
    @SerializedName("current_page")
    val currentPage: Int = 0, // 1
    @SerializedName("data")
    val `data`: List<DataFrontScreenX> = listOf(),
    @SerializedName("first_page_url")
    val firstPageUrl: String = "", // http://120.26.54.110:9501/v2/accessmenTaskList?page=1
    @SerializedName("from")
    val from: Int = 0, // 1
    @SerializedName("last_page")
    val lastPage: Int = 0, // 1
    @SerializedName("last_page_url")
    val lastPageUrl: String = "", // http://120.26.54.110:9501/v2/accessmenTaskList?page=1
    @SerializedName("next_page_url")
    val nextPageUrl: Any = Any(), // null
    @SerializedName("path")
    val path: String = "", // http://120.26.54.110:9501/v2/accessmenTaskList
    @SerializedName("per_page")
    val perPage: Int = 0, // 10
    @SerializedName("prev_page_url")
    val prevPageUrl: Any = Any(), // null
    @SerializedName("to")
    val to: Int = 0, // 2
    @SerializedName("total")
    val total: Int = 0 // 2
)

data class DataFrontScreenX(
    @SerializedName("id")
    val id: Int = 0, // 2
    @SerializedName("type")
    val type: String = "", // 14
    @SerializedName("name")
    val name: String = "",
    @SerializedName("start")
    val start: String = "", // 0
    @SerializedName("end")
    val end: String = "", // 0
    @SerializedName("num")
    val num: String = "", // 0
    @SerializedName("views")
    val views: String = "", // 1
    @SerializedName("create_time")
    val createTime: String = "", // 0
    @SerializedName("status")
    val status: Int = 0 // 3
)