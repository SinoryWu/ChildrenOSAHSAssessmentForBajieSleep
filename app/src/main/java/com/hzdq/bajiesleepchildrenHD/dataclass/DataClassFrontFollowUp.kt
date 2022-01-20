package com.hzdq.bajiesleepchildrenHD.dataclass

import com.google.gson.annotations.SerializedName

data class DataClassFrontFollowUp(
    @SerializedName("code")
    val code: Int = 0, // 0
    @SerializedName("data")
    val `data`: DataFrontFollowUp = DataFrontFollowUp(),
    @SerializedName("msg")
    val msg: String = "", // ok
    @SerializedName("followNum")
    val followNum: Int = 0
)

data class DataFrontFollowUp(
    @SerializedName("current_page")
    val currentPage: Int = 0, // 20
    @SerializedName("data")
    val `data`: List<DataFrontFollowUpX> = listOf(),
    @SerializedName("first_page_url")
    val firstPageUrl: String = "", // http://120.26.54.110:9501/v2/User/index?page=1
    @SerializedName("from")
    val from: Int = 0, // 39
    @SerializedName("last_page")
    val lastPage: Int = 0, // 104
    @SerializedName("last_page_url")
    val lastPageUrl: String = "", // http://120.26.54.110:9501/v2/User/index?page=104
    @SerializedName("next_page_url")
    val nextPageUrl: String = "", // http://120.26.54.110:9501/v2/User/index?page=21
    @SerializedName("path")
    val path: String = "", // http://120.26.54.110:9501/v2/User/index
    @SerializedName("per_page")
    val perPage: Int = 0, // 2
    @SerializedName("prev_page_url")
    val prevPageUrl: String = "", // http://120.26.54.110:9501/v2/User/index?page=19
    @SerializedName("to")
    val to: Int = 0, // 40
    @SerializedName("total")
    val total: Int = 0 // 208
)

data class DataFrontFollowUpX(
    @SerializedName("id")
    val id: Int = 0, // 1101
    @SerializedName("uid")
    val uid: Int = 0, // 1297
    @SerializedName("truename")
    val truename: String = "", // 郑碧珍
    @SerializedName("mobile")
    val mobile: String = "", // 15967638997
    @SerializedName("create_time")
    val createTime: Int = 0, // 1603328027
    @SerializedName("next")
    val next: Int = 0, // 1603328027
    @SerializedName("sex")
    val sex: String = "", // 2
    @SerializedName("age")
    val age: String = "", // 63
    @SerializedName("height")
    val height: String = "", // 159
    @SerializedName("weight")
    val weight: String = "", // 50
    @SerializedName("id_card")
    val idCard: String = "",
    @SerializedName("address")
    val address: String = "",
    @SerializedName("mzh")
    val mzh: String = "",
    @SerializedName("hospitalid")
    val hospitalid: Int = 0, // 58
    @SerializedName("using")
    val using: Boolean = false, // false
    @SerializedName("report")
    val report: List<FrontUserReport> = listOf(),
    @SerializedName("hospitalName")
    val hospitalName: String = "", // 仙居安州医院
    @SerializedName("oahi")
    val oahi: String ="",
    @SerializedName("oahi_res")
    val oahiRes: String = ""
)

data class FrontFollowUpReport(
    @SerializedName("id")
    val id: Int = 0, // 669
    @SerializedName("sn")
    val sn: String = "", // S01D190700005E
    @SerializedName("ahi")
    val ahi: String = "", // 37.88
    @SerializedName("report_id")
    val reportId: String = "", // 58-2020-10-23-1297
    @SerializedName("complete")
    val complete: Int = 0, // 0
    @SerializedName("quality")
    val quality: Int = 0, // 0
    @SerializedName("createTime")
    val createTime: Int = 0, // 1603403431
    @SerializedName("reportUrl")
    val reportUrl: String = "" // 5f929ab4d4e1a402264627a0
)