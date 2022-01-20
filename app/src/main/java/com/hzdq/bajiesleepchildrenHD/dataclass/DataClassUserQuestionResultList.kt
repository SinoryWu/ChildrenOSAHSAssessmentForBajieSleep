package com.hzdq.bajiesleepchildrenHD.dataclass

import com.google.gson.annotations.SerializedName


data class DataClassUserQuestionResultList(
    @SerializedName("code")
    val code: Int = 0, // 0
    @SerializedName("msg")
    val msg: String = "", // success
    @SerializedName("data")
    val `data`: DataUserQuestionResultList = DataUserQuestionResultList()
)

data class DataUserQuestionResultList(
    @SerializedName("current_page")
    val currentPage: Int = 0, // 1
    @SerializedName("data")
    val `data`: List<DataUserQuestionResultListX> = listOf(),
    @SerializedName("first_page_url")
    val firstPageUrl: String = "", // http://120.26.54.110:9501/v2/getMyAssList?page=1
    @SerializedName("from")
    val from: Int = 0, // 1
    @SerializedName("last_page")
    val lastPage: Int = 0, // 1
    @SerializedName("last_page_url")
    val lastPageUrl: String = "", // http://120.26.54.110:9501/v2/getMyAssList?page=1
    @SerializedName("next_page_url")
    val nextPageUrl: Any = Any(), // null
    @SerializedName("path")
    val path: String = "", // http://120.26.54.110:9501/v2/getMyAssList
    @SerializedName("per_page")
    val perPage: Int = 0, // 10
    @SerializedName("prev_page_url")
    val prevPageUrl: Any = Any(), // null
    @SerializedName("to")
    val to: Int = 0, // 3
    @SerializedName("total")
    val total: Int = 0 // 3
)

data class DataUserQuestionResultListX(
    @SerializedName("id")
    val id: Int = 0, // 1126
    @SerializedName("uid")
    val uid: Int = 0, // 36
    @SerializedName("task_id")
    val taskId: Int = 0, // 1
    @SerializedName("type")
    val type: Int = 0, // 13
    @SerializedName("total")
    val total: Int = 0, // 12
    @SerializedName("result")
    val result: String = "", // 高风险
    @SerializedName("truename")
    val truename: String = "", // 杨超
    @SerializedName("create_time")
    val createTime: Int = 0 // 1638517907
)