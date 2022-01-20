package com.hzdq.bajiesleepchildrenHD.dataclass

import com.google.gson.annotations.SerializedName

data class DataClassFollowUp(
    @SerializedName("code")
    val code: Int = 0, // 0
    @SerializedName("msg")
    val msg: String = "", // success
    @SerializedName("data")
    val `data`: DataFollowUp = DataFollowUp()
)

data class DataFollowUp(
    @SerializedName("current_page")
    val currentPage: Int = 0, // 1
    @SerializedName("data")
    val `data`: List<DataFollowUpX> = listOf(),
    @SerializedName("first_page_url")
    val firstPageUrl: String = "", // http://120.26.54.110:9501/v2/followup?page=1
    @SerializedName("from")
    val from: Int = 0, // 1
    @SerializedName("last_page")
    val lastPage: Int = 0, // 1
    @SerializedName("last_page_url")
    val lastPageUrl: String = "", // http://120.26.54.110:9501/v2/followup?page=1
    @SerializedName("next_page_url")
    val nextPageUrl: Any = Any(), // null
    @SerializedName("path")
    val path: String = "", // http://120.26.54.110:9501/v2/followup
    @SerializedName("per_page")
    val perPage: Int = 0, // 10
    @SerializedName("prev_page_url")
    val prevPageUrl: Any = Any(), // null
    @SerializedName("to")
    val to: Int = 0, // 1
    @SerializedName("total")
    val total: Int = 0 // 1
)

data class DataFollowUpX(
    @SerializedName("id")
    val id: Int = 0, // 2
    @SerializedName("patient_id")
    val patientId: Int = 0, // 1338
    @SerializedName("hospitalid")
    val hospitalid: Int = 0, // 24
    @SerializedName("create_time")
    val createTime: Int = 0, // 1639821755
    @SerializedName("oahi")
    val oahi: String = "", // 12.5
    @SerializedName("oahi_res")
    val oahiRes: String = "", // 重度
    @SerializedName("doctor_id")
    val doctorId: Int = 0, // 0
    @SerializedName("doctor_name")
    val doctorName: String = ""
)