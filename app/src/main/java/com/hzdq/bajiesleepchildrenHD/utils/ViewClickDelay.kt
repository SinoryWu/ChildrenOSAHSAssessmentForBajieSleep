package com.hzdq.bajiesleepchildrenHD.utils

import android.view.View
import com.hzdq.bajiesleepchildrenHD.utils.ViewClickDelay.SPACE_TIME
import com.hzdq.bajiesleepchildrenHD.utils.ViewClickDelay.hash
import com.hzdq.bajiesleepchildrenHD.utils.ViewClickDelay.lastClickTime

/**
 * 防止按钮重复点击
 */
object ViewClickDelay {
    var hash: Int = 0
    var lastClickTime: Long = 0
    var SPACE_TIME: Long = 1500
}

infix fun View.clickDelay(clickAction: () -> Unit) {
    this.setOnClickListener {
        if (this.hashCode() != hash) {
            hash = this.hashCode()
            lastClickTime = System.currentTimeMillis()
            clickAction()
        } else {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClickTime > SPACE_TIME) {
                lastClickTime = System.currentTimeMillis()
                clickAction()
            }
        }
    }
}
