package com.hzdq.bajiesleepchildrenHD.dataclass
import com.google.gson.annotations.SerializedName


data class DataClassDeviceInfo(
    @SerializedName("code")
    val code: Int = 0, // 0
    @SerializedName("msg")
    val msg: String = "", // success
    @SerializedName("data")
    val `data`: DataDeviceInfo = DataDeviceInfo()
)

data class DataDeviceInfo(
    @SerializedName("id")
    val id: Int = 0, // 53
    @SerializedName("sn")
    val sn: String = "", // S01D1907000060
    @SerializedName("status")
    val status: Int = 0, // -1
    @SerializedName("create_time")
    val createTime: Int = 0, // 1599203041
    @SerializedName("update_time")
    val updateTime: Int = 0, // 1601370255
    @SerializedName("room_no")
    val roomNo: String = "",
    @SerializedName("scene")
    val scene: String = "", // 2
    @SerializedName("hospitalid")
    val hospitalid: Int = 0, // 24
    @SerializedName("back_time")
    val backTime: Int = 0, // 0
    @SerializedName("monitor")
    val monitor: String = "",
    @SerializedName("workstatus")
    val workstatus: Int = 0, // 0
    @SerializedName("inbed")
    val inbed: Int = 0, // 0
    @SerializedName("bloodoxygen")
    val bloodoxygen: String = "",
    @SerializedName("wifirssi")
    val wifirssi: String = "", // 0
    @SerializedName("mobilerssi")
    val mobilerssi: String = "",
    @SerializedName("breathrate")
    val breathrate: String = "",
    @SerializedName("heartrate")
    val heartrate: String = "",
    @SerializedName("tempetature")
    val tempetature: String = "",
    @SerializedName("boottime")
    val boottime: String = "", // 0
    @SerializedName("networktype")
    val networktype: Int = 0, // 3
    @SerializedName("simserialnumber")
    val simserialnumber: String = "",
    @SerializedName("versionno")
    val versionno: String = "",
    @SerializedName("battery")
    val battery: Int = 0, // 0
    @SerializedName("ringsn")
    val ringsn: String = "",
    @SerializedName("swversion")
    val swversion: String = "",
    @SerializedName("monitor_status")
    val monitorStatus: String = "", // 0
    @SerializedName("powerStatus")
    val powerStatus: Int = 0,
    @SerializedName("connectstatus")
    val connectstatus: String = "",
    @SerializedName("ringStatus")
    val ringStatus: Int = 0, // -1
    @SerializedName("mode_type")
    val modeType: Int = 0, // 0
    @SerializedName("uid")
    val uid: String = "",
    @SerializedName("truename")
    val truename: String = "",
    @SerializedName("age")
    val age: String = "",
    @SerializedName("devStatus")
    val devStatus: Int = 0,
    @SerializedName("reportStatus")
    val reportStatus: Int = 0,
    @SerializedName("outTime")
    val outTime: Int = 0
)