package com.hzdq.bajiesleepchildrenHD.utils

import android.app.Activity
import android.content.Context
import android.view.View

class HideUI(val activity:Activity) {

    //全屏显示 隐藏系统的状态栏
    fun hideSystemUI(){
        activity.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                )
    }

    //影藏popwindow ui
    fun uiOptions():Int{
        var uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or  //布局位于状态栏下方
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or  //全屏
                View.SYSTEM_UI_FLAG_FULLSCREEN or  //隐藏导航栏
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        uiOptions = uiOptions or 0x00001000
        return uiOptions
    }

}