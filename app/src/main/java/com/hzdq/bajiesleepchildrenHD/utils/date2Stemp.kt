package com.hzdq.bajiesleepchildrenHD.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@Throws(ParseException::class)
fun date2Stamp(s: String?): String? {
    val res: String
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val date: Date = simpleDateFormat.parse(s)
    val ts: Long = date.getTime()
    res = ts.toString()
    return res
}