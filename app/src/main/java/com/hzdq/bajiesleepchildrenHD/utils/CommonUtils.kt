package com.hzdq.bajiesleepchildrenHD.utils

import android.content.Context
import java.util.*
import java.util.regex.Pattern

object CommonUtils {
    infix fun isNull(checkStr: String?): Boolean {
        var result = false
        if (null == checkStr) {
            result = true
        } else {
            if (checkStr.length == 0) {
                result = true
            }
        }
        return result
    }

    infix  fun isNull(list: List<*>?): Boolean {
        var result = false
        if (null == list) {
            result = true
        } else {
            if (list.size == 0) {
                result = true
            }
        }
        return result
    }

      fun getRegEx(input: String?, regex: String?): MutableList<String> {
        val stringList: MutableList<String> = ArrayList()
        val p = Pattern.compile(regex)
        val m = p.matcher(input)
        while (m.find()) stringList.add(m.group())
        return stringList
    }
}