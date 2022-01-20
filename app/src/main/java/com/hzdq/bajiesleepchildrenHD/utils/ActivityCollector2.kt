package com.hzdq.bajiesleepchildrenHD.utils

import android.app.Activity
import java.util.*

object ActivityCollector2 {
    var activities: MutableList<Activity> = ArrayList()
    fun addActivity(activity: Activity) {
        activities.add(activity)
    }

    fun removeActivity(activity: Activity) {
        activities.remove(activity)
    }

    fun finishAll() {
        for (activity in activities) {
            if (!activity.isFinishing) {
                activity.finish()
            }
        }
        activities.clear()
    }
}