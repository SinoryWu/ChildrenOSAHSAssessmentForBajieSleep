package com.hzdq.bajiesleepchildrenHD.screen.dialog

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.utils.HideDialogUI
import com.hzdq.bajiesleepchildrenHD.utils.ToastUtils

@RequiresApi(Build.VERSION_CODES.O)
class DateEndDialog (context: Context, confirmAction: ConfirmAction): Dialog(context, R.style.CustomDialog){

    private var confirmAction: ConfirmAction? = null
    private var screenWidth = 0
    private var screenHeight = 0
    private var datePicker: DatePicker? = null

    private var cancelBtn: View? = null
    private  var submitBtn: View? = null

    private var endYear = 0
    private var endMonth =  0
    private var endDay =  0
    private var currentYear = 0
    private var currentMonth = 0
    private var currentDay =  0
    init {
        this.confirmAction = confirmAction

        val metrics = context.resources.displayMetrics
        screenWidth = metrics.widthPixels - getDensityValue(850, context)
        screenHeight = metrics.heightPixels - getDensityValue(500,context)
        Log.d("screenWidth", screenWidth.toString())
        HideDialogUI.hideSystemUI(this)
    }

    interface ConfirmAction {
        fun onLeftClick()
        fun onRightClick(
            year: Int?,
            month: Int?,
            day: Int?,
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view: View =
            LayoutInflater.from(context).inflate(R.layout.dialog_date_end_picker_layout, null)
        setContentView(view)
        window!!.setLayout(screenWidth,screenHeight )
        datePicker = findViewById<View>(R.id.date_end_picker) as DatePicker




        initView()
        initData()
        setEvent()
    }

    fun initView(){
        cancelBtn = findViewById(R.id.date_end_cancelBtn)
        submitBtn = findViewById<View>(R.id.date_end_submitBtn)
        datePicker?.descendantFocusability = DatePicker.FOCUS_BLOCK_DESCENDANTS
    }

    fun initData(){
         currentYear = datePicker?.year!!
         currentMonth = datePicker?.month!!
         currentDay = datePicker?.dayOfMonth!!
        endYear = currentYear
        endMonth = currentMonth+1
        endDay = currentDay

        currentYear?.let { currentMonth?.let { it1 -> currentDay?.let { it2 -> datePicker?.init(it, it1, it2,null) } } }



    }


    fun setEvent(){



        datePicker?.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
            endYear = year
            endMonth = monthOfYear + 1
            endDay = dayOfMonth
        }

        cancelBtn?.setOnClickListener{
            confirmAction?.onLeftClick()
            dismiss()
        }

        submitBtn?.setOnClickListener {

            var click = false
            if(endYear < currentYear){
                click = false
            }else if (endYear == currentYear){
                if (endMonth < currentMonth+1){
                    click = false

                }else if (endMonth == currentMonth+1){
                    if (endDay < currentDay){
                        click = false
                    }else {
                        click = true
                    }
                }else {
                    click = true
                }
            }else {
                click = true
            }
            if (click){
                confirmAction?.onRightClick(endYear,endMonth,endDay)
                dismiss()
            }else{
                ToastUtils.showTextToast(context,"结束时间不得小于今天")
            }

        }
    }

    fun getDensityValue(value: Int, activity: Context?): Int {
        val displayMetrics = activity!!.resources.displayMetrics
        return Math.ceil((value * displayMetrics.density).toDouble()).toInt()
    }
}