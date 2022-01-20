package com.hzdq.bajiesleepchildrenHD.dataclass

import com.google.gson.annotations.SerializedName



data class DataClassHomeUserSearch(
    @SerializedName("code")
    val code: Int = 0, // 0
    @SerializedName("data")
    val `data`: DataHomeUserSearch = DataHomeUserSearch(),
    @SerializedName("msg")
    val msg: String = "" // ok
)

data class DataHomeUserSearch(
    @SerializedName("current_page")
    val currentPage: Int = 0, // 1
    @SerializedName("data")
    val `data`: List<DataHomeUserSearchX> = listOf(),
    @SerializedName("first_page_url")
    val firstPageUrl: String = "", // http://120.26.54.110:9501/v2/User/index?page=1
    @SerializedName("from")
    val from: Int = 0, // 1
    @SerializedName("last_page")
    val lastPage: Int = 0, // 12
    @SerializedName("last_page_url")
    val lastPageUrl: String = "", // http://120.26.54.110:9501/v2/User/index?page=12
    @SerializedName("next_page_url")
    val nextPageUrl: String = "", // http://120.26.54.110:9501/v2/User/index?page=2
    @SerializedName("path")
    val path: String = "", // http://120.26.54.110:9501/v2/User/index
    @SerializedName("per_page")
    val perPage: Int = 0, // 10
    @SerializedName("prev_page_url")
    val prevPageUrl: Any = Any(), // null
    @SerializedName("to")
    val to: Int = 0, // 10
    @SerializedName("total")
    val total: Int = 0 // 117
)

data class DataHomeUserSearchX(
    @SerializedName("id")
    val id: Int = 0, // 1283
    @SerializedName("uid")
    val uid: Int = 0, // 1460
    @SerializedName("truename")
    val truename: String = "", // test
    @SerializedName("mobile")
    val mobile: String = "", // 1548
    @SerializedName("create_time")
    val createTime: Int = 0, // 1638860778
    @SerializedName("sex")
    val sex: String = "", // 1
    @SerializedName("age")
    val age: String = "", // 12
    @SerializedName("height")
    val height: String = "", // 140.5
    @SerializedName("weight")
    val weight: String = "", // 40.5
    @SerializedName("id_card")
    val idCard: String = "",
    @SerializedName("address")
    val address: String = "",
    @SerializedName("mzh")
    val mzh: String = "",
    @SerializedName("hospitalid")
    val hospitalid: Int = 0, // 24
    @SerializedName("using")
    val using: Boolean = false, // false
    @SerializedName("report")
    val report: List<Any> = listOf(),
    @SerializedName("hospitalName")
    val hospitalName: String = "" // 八戒睡眠
)