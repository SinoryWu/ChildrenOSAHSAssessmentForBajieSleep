package com.hzdq.bajiesleepchildrenHD.dataclass
import com.google.gson.annotations.SerializedName


data class DataClassAdministrators(
    @SerializedName("code")
    val code: Int = 0, // 0
    @SerializedName("msg")
    val msg: String = "", // success
    @SerializedName("data")
    val `data`: DataAdministrators = DataAdministrators()
)

data class DataAdministrators(
    @SerializedName("current_page")
    val currentPage: Int = 0, // 1
    @SerializedName("data")
    val `data`: List<DataAdministratorsX> = listOf(),
    @SerializedName("first_page_url")
    val firstPageUrl: String = "", // http://120.26.54.110:9501/v2/auserList?page=1
    @SerializedName("from")
    val from: Int = 0, // 1
    @SerializedName("last_page")
    val lastPage: Int = 0, // 1
    @SerializedName("last_page_url")
    val lastPageUrl: String = "", // http://120.26.54.110:9501/v2/auserList?page=1
    @SerializedName("next_page_url")
    val nextPageUrl: Any = Any(), // null
    @SerializedName("path")
    val path: String = "", // http://120.26.54.110:9501/v2/auserList
    @SerializedName("per_page")
    val perPage: Int = 0, // 15
    @SerializedName("prev_page_url")
    val prevPageUrl: Any = Any(), // null
    @SerializedName("to")
    val to: Int = 0, // 1
    @SerializedName("total")
    val total: Int = 0 // 1
)

data class DataAdministratorsX(
    @SerializedName("uid")
    val uid: String = "", // 182
    @SerializedName("truename")
    val truename: String = "", // 鸠摩智
    @SerializedName("mobile")
    val mobile: String = "", // 14800000005
    @SerializedName("type")
    val type: String = "" // 8
)