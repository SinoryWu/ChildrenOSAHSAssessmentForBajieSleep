package com.hzdq.bajiesleepchildrenHD.setting.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.device.dialog.StatusDialog
import com.hzdq.bajiesleepchildrenHD.screen.dialog.DateEndDialog
import com.hzdq.bajiesleepchildrenHD.utils.HideDialogUI

class RegisterDialog  (context: Context, confirmAction: ConfirmAction): Dialog(context, R.style.CustomDialog){
    private var screenWidth = 0
    private var screenHeight = 0
    private var confirmAction: ConfirmAction? = null

    init {
        this.confirmAction = confirmAction

        val metrics = context.resources.displayMetrics
        screenWidth = metrics.widthPixels - getDensityValue(950, context)
        screenHeight = metrics.heightPixels - getDensityValue(600,context)
        HideDialogUI.hideSystemUI(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.dialog_register, null)
        setContentView(view)
        window!!.setLayout(screenWidth,screenHeight )
        val cancel = view.findViewById<TextView>(R.id.register_cancel)
        val confirm = view.findViewById<TextView>(R.id.register_confirm)


        cancel.setOnClickListener {
            dismiss()
        }

        confirm.setOnClickListener {
            confirmAction?.onRightClick()
            dismiss()
        }

    }
    interface ConfirmAction {
        fun onLeftClick()
        fun onRightClick()
    }
    fun getDensityValue(value: Int, activity: Context?): Int {
        val displayMetrics = activity!!.resources.displayMetrics
        return Math.ceil((value * displayMetrics.density).toDouble()).toInt()
    }

}