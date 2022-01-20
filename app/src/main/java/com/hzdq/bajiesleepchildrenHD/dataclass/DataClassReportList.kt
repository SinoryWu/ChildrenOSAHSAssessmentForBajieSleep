package com.hzdq.bajiesleepchildrenHD.dataclass

import com.google.gson.annotations.SerializedName


data class DataClassReportList(
    @SerializedName("code")
    val code: Int = 0, // 0
    @SerializedName("msg")
    val msg: String = "", // success
    @SerializedName("data")
    val `data`: DataReportList = DataReportList()
)

data class DataReportList(
    @SerializedName("current_page")
    val currentPage: Int = 0, // 1
    @SerializedName("data")
    val `data`: List<DataReportListX> = listOf(),
    @SerializedName("first_page_url")
    val firstPageUrl: String = "", // http://120.26.54.110:9501/v2/treat?page=1
    @SerializedName("from")
    val from: Int = 0, // 1
    @SerializedName("last_page")
    val lastPage: Int = 0, // 2
    @SerializedName("last_page_url")
    val lastPageUrl: String = "", // http://120.26.54.110:9501/v2/treat?page=2
    @SerializedName("next_page_url")
    val nextPageUrl: String = "", // http://120.26.54.110:9501/v2/treat?page=2
    @SerializedName("path")
    val path: String = "", // http://120.26.54.110:9501/v2/treat
    @SerializedName("per_page")
    val perPage: Int = 0, // 10
    @SerializedName("prev_page_url")
    val prevPageUrl: Any = Any(), // null
    @SerializedName("to")
    val to: Int = 0, // 10
    @SerializedName("total")
    val total: Int = 0 // 11
)

data class DataReportListX(
    @SerializedName("id")
    val id: Int = 0, // 14
    @SerializedName("patient_id")
    val patientId: Int = 0, // 1319
    @SerializedName("type")
    val type: String = "", // 1
    @SerializedName("oahi")
    val oahi: String = "", // 12.9
    @SerializedName("osas")
    val osas: Int = 0, // 2
    @SerializedName("opinion")
    val opinion: String = "", // test213
    @SerializedName("treatment")
    val treatment: String = "", // testtttt123123
    @SerializedName("create_time")
    val createTime: Int = 0, // 1639622699
    @SerializedName("report_id")
    val reportId: String=""
)