package com.hzdq.bajiesleepchildrenHD.dataclass
import com.google.gson.annotations.SerializedName


data class DataClassFrontHomeCalculate(
    @SerializedName("code")
    val code: Int = 0, // 0
    @SerializedName("data")
    val `data`: DataFrontHomeCalculate = DataFrontHomeCalculate(),
    @SerializedName("msg")
    val msg: String = "" // ok
)

data class DataFrontHomeCalculate(
    @SerializedName("total_report_num")
    val totalReportNum: Int = 0, // 145
    @SerializedName("today_report_num")
    val todayReportNum: Int = 0, // 0
    @SerializedName("report_day")
    val reportDay: List<Int> = listOf(),
    @SerializedName("total_dev_borrow_num")
    val totalDevBorrowNum: Int = 0, // 401
    @SerializedName("today_dev_borrow_num")
    val todayDevBorrowNum: Int = 0, // 0
    @SerializedName("borrow_day")
    val borrowDay: List<Any> = listOf(),
    @SerializedName("total_user_num")
    val totalUserNum: Int = 0, // 114
    @SerializedName("today_user_num")
    val todayUserNum: Int = 0, // 0
    @SerializedName("user_day")
    val userDay: List<Any> = listOf(),
    @SerializedName("in")
    val inX: Int = 0, // 0
    @SerializedName("out")
    val `out`: Int = 0, // 2
    @SerializedName("total")
    val total: Int = 0, // 5
    @SerializedName("using")
    val using: Int = 0, // 3
    @SerializedName("use_rate")
    val useRate: Int = 0, // 60
    @SerializedName("total_ass_num")
    val totalAssNum: Int = 0, // 3
    @SerializedName("today_ass_num")
    val todayAssNum: Int = 0, // 3
    @SerializedName("total_followup_num")
    val totalFollowupNum: Int = 0, // 3

    @SerializedName("total_followup_user")
    val totalFollowupUser: Int = 0 // 3
)