package com.hzdq.bajiesleepchildrenHD.utils

import android.app.Dialog
import android.view.View

object HideDialogUI {
    //全屏显示 隐藏系统的状态栏
    fun hideSystemUI(dialog: Dialog){
        dialog.window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                )
    }


}