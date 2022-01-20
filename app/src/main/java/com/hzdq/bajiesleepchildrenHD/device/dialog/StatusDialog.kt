package com.hzdq.bajiesleepchildrenHD.device.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.device.viewmodel.DeviceViewModel
import com.hzdq.bajiesleepchildrenHD.utils.HideDialogUI
import com.hzdq.bajiesleepchildrenHD.utils.HideUI
import com.hzdq.bajiesleepchildrenHD.utils.ToastUtils

class StatusDialog(val deviceViewModel: DeviceViewModel,val lifecycleOwner: LifecycleOwner,context: Context,confirmAction: ConfirmAction): Dialog(context, R.style.CustomDialog)  {
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
            LayoutInflater.from(context).inflate(R.layout.dialog_change_status, null)
        setContentView(view)

        val cancel = view.findViewById<TextView>(R.id.change_status_cancelBtn)
        val confirm = view.findViewById<TextView>(R.id.change_status_submitBtn)
        val empty = view.findViewById<Button>(R.id.dialog_change_empty)
        val repair = view.findViewById<Button>(R.id.dialog_change_repair)
        val maintain = view.findViewById<Button>(R.id.dialog_change_maintain)

        window!!.setLayout(screenWidth,screenHeight )

        val map = mapOf(
            1 to empty,
            2 to repair,
            3 to maintain
        )

        deviceViewModel.status.observe(lifecycleOwner, Observer {
            map.forEach {
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                1 -> {
                    empty.isSelected = true
                    empty.setTextColor(Color.parseColor("#FFFFFF"))
                }
                2 -> {
                    repair.isSelected = true
                    repair.setTextColor(Color.parseColor("#FFFFFF"))
                }
                3 ->{
                    maintain.isSelected = true
                    maintain.setTextColor(Color.parseColor("#FFFFFF"))
                }
                else ->{
                    map.forEach {
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }
        })

        empty.setOnClickListener { deviceViewModel.status.value = 1 }
        repair.setOnClickListener { deviceViewModel.status.value = 2 }
        maintain.setOnClickListener { deviceViewModel.status.value = 3 }

        cancel.setOnClickListener {
            dismiss()
        }

        confirm.setOnClickListener {
            when(deviceViewModel.status.value){
                1 ->{
                    confirmAction?.onRightClick(1)

                }
                2 -> {
                    confirmAction?.onRightClick(5)

                }
                3 ->{
                    confirmAction?.onRightClick(10)

                }
                else -> {
                    ToastUtils.showTextToast(context,"未变更设备状态")
                }

            }
            dismiss()


        }
    }


    interface ConfirmAction {
        fun onLeftClick()
        fun onRightClick(
            status:Int
        )
    }

    fun getDensityValue(value: Int, activity: Context?): Int {
        val displayMetrics = activity!!.resources.displayMetrics
        return Math.ceil((value * displayMetrics.density).toDouble()).toInt()
    }


}