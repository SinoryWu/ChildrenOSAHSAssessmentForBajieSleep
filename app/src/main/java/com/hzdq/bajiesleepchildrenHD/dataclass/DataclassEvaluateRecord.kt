package com.hzdq.bajiesleepchildrenHD.dataclass

import com.google.gson.annotations.SerializedName


data class DataclassEvaluateRecord(
    @SerializedName("code")
    val code: Int = 0, // 0
    @SerializedName("msg")
    val msg: String = "", // success
    @SerializedName("data")
    val `data`: DataEvaluateRecord = DataEvaluateRecord()
)

data class DataEvaluateRecord(
    @SerializedName("current_page")
    val currentPage: Int = 0, // 1
    @SerializedName("data")
    val `data`: List<DataEvaluateRecordX> = listOf(),
    @SerializedName("first_page_url")
    val firstPageUrl: String = "", // http://120.26.54.110:9501/v2/estimate?page=1
    @SerializedName("from")
    val from: Int = 0, // 1
    @SerializedName("last_page")
    val lastPage: Int = 0, // 1
    @SerializedName("last_page_url")
    val lastPageUrl: String = "", // http://120.26.54.110:9501/v2/estimate?page=1
    @SerializedName("next_page_url")
    val nextPageUrl: Any = Any(), // null
    @SerializedName("path")
    val path: String = "", // http://120.26.54.110:9501/v2/estimate
    @SerializedName("per_page")
    val perPage: Int = 0, // 10
    @SerializedName("prev_page_url")
    val prevPageUrl: Any = Any(), // null
    @SerializedName("to")
    val to: Int = 0, // 2
    @SerializedName("total")
    val total: Int = 0 // 2
)

data class DataEvaluateRecordX(
    @SerializedName("id")
    val id: Int = 0, // 28
    @SerializedName("osas")
    val osas: Int = 0, // 0
    @SerializedName("assessment")
    val assessment: List<String> = listOf(),
    @SerializedName("create_time")
    val createTime: Int = 0 // 1639563330
)
