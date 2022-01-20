package com.hzdq.bajiesleepchildrenHD.utils

import java.text.SimpleDateFormat
import java.util.*

fun timestamp2Date(str_num: String, format: String?): String? {
    val sdf = SimpleDateFormat(format)
    return if (str_num.length == 13) {
        sdf.format(Date(str_num.toLong()))
    } else {
        sdf.format(Date(str_num.toInt() * 1000L))
    }
}