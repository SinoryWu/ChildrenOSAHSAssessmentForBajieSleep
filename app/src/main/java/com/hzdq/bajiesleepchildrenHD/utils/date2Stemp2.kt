package com.hzdq.bajiesleepchildrenHD.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@Throws(ParseException::class)
fun date2Stamp2(s: String?,format: String): String? {
    val res: String
    val simpleDateFormat = SimpleDateFormat(format)
    val date: Date = simpleDateFormat.parse(s)
    val ts: Long = date.getTime()
    res = ts.toString()
    return res
}