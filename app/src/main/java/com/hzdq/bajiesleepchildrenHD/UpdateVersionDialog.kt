package com.hzdq.dowloadapk

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.hzdq.bajiesleepchildrenHD.MainViewModel
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.utils.HideDialogUI

class UpdateVersionDialog  (val mainViewModel: MainViewModel,val lifecycleOwner: LifecycleOwner,context: Context, confirmAction: ConfirmAction): Dialog(context, R.style.CustomDialog){
    private var screenWidth = 0
    private var screenHeight = 0
    private var confirmAction: ConfirmAction? = null

    init {
        this.confirmAction = confirmAction

        val metrics = context.resources.displayMetrics
        screenWidth = metrics.widthPixels - getDensityValue(850, context)
        screenHeight = metrics.heightPixels - getDensityValue(570,context)
        HideDialogUI.hideSystemUI(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.dialog_update_version, null)
        setContentView(view)
        window!!.setLayout(screenWidth,screenHeight )

        val confirm = view.findViewById<TextView>(R.id.update_version_confirm)
        val contentVersion = view.findViewById<TextView>(R.id.content_version)
        val currentVersion = view.findViewById<TextView>(R.id.current_version)
        val nextVersion = view.findViewById<TextView>(R.id.next_version)

        mainViewModel.contentVersion.observe(lifecycleOwner, Observer {
            contentVersion.text = "内容：${it}"
        })

        mainViewModel.currentVersion.observe(lifecycleOwner, Observer {
            currentVersion.text = "当前版本：V${it}"
        })
        mainViewModel.nextVersion.observe(lifecycleOwner, Observer {
            nextVersion.text = "更新版本：V${it}"
        })

        confirm.setOnClickListener {
            confirmAction?.onRightClick()

        }

    }
    interface ConfirmAction {
        fun onRightClick()
    }
    fun getDensityValue(value: Int, activity: Context?): Int {
        val displayMetrics = activity!!.resources.displayMetrics
        return Math.ceil((value * displayMetrics.density).toDouble()).toInt()
    }

}