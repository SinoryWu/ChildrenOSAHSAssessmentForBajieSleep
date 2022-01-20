package com.hzdq.bajiesleepchildrenHD.device.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.utils.HideDialogUI
import com.itheima.wheelpicker.WheelPicker
import kotlinx.android.synthetic.main.activity_test.*
import kotlin.math.ceil

class DaysDialog(context: Context,confirmAction:ConfirmAction): Dialog(context, R.style.CustomDialog) {

    private var confirmAction: ConfirmAction? = null
    private var screenWidth = 0
    private var screenHeight = 0
    private var cancelBtn: View? = null
    private  var submitBtn:View? = null
    private  var daysPicker:WheelPicker? = null
    private var day =0
    init {
        this.confirmAction = confirmAction
        val metrics = context.resources.displayMetrics
        screenWidth = metrics.widthPixels - getDensityValue(850, context)
        screenHeight = metrics.heightPixels - getDensityValue(500,context)
        HideDialogUI.hideSystemUI(this)
    }

    override fun dismiss() {
        super.dismiss()
        daysPicker?.selectedItemPosition = 0
        day = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.dialog_days_picker_layout, null)
        setContentView(view)
        window!!.setLayout(screenWidth,screenHeight )
        initView()
        setEvent()
    }

    private fun initView() {
        cancelBtn = findViewById(R.id.days_cancelBtn)
        submitBtn = findViewById<View>(R.id.days_submitBtn)
        daysPicker = findViewById(R.id.days_picker_picker)


    }

    private fun setEvent(){

        val daysList = listOf<String>("1  天","3  天","5  天","7  天","15  天")
        daysPicker?.data = daysList
        cancelBtn?.setOnClickListener {
            confirmAction?.onLeftClick()
            dismiss()
        }
        submitBtn?.setOnClickListener{

            when(daysPicker?.currentItemPosition){
                0 -> day = 1
                1-> day = 3
                2-> day = 5
                3-> day = 7
                4-> day = 15
            }

            if (day != 0){
                confirmAction?.onRightClick(day)
                dismiss()
            }
        }
    }

    interface ConfirmAction {
        fun onLeftClick()
        fun onRightClick(
           day:Int
        )
    }

    fun getDensityValue(value: Int, activity: Context?): Int {
        val displayMetrics = activity!!.resources.displayMetrics
        return ceil((value * displayMetrics.density).toDouble()).toInt()
    }
}