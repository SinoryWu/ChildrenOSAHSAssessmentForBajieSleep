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
import com.hzdq.bajiesleepchildrenHD.home.dialog.DateRangePickerDialog
import com.hzdq.bajiesleepchildrenHD.screen.viewmodel.ScreenViewModel
import com.hzdq.bajiesleepchildrenHD.utils.HideDialogUI
import com.hzdq.bajiesleepchildrenHD.utils.ToastUtils

@RequiresApi(Build.VERSION_CODES.O)
class DateStartDialog (context: Context, confirmAction: ConfirmAction): Dialog(context, R.style.CustomDialog){

    private var confirmAction: ConfirmAction? = null
    private var screenWidth = 0
    private var screenHeight = 0
    private var datePicker: DatePicker? = null

    private var cancelBtn: View? = null
    private  var submitBtn: View? = null

    private var startYear = 0
    private var startMonth =  0
    private var startDay =  0
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
            LayoutInflater.from(context).inflate(R.layout.dialog_date_start_picker_layout, null)
        setContentView(view)
        window!!.setLayout(screenWidth,screenHeight )
        datePicker = findViewById<View>(R.id.date_start_picker) as DatePicker




        initView()
        initData()
        setEvent()
    }

    fun initView(){
        cancelBtn = findViewById(R.id.date_start_cancelBtn)
        submitBtn = findViewById<View>(R.id.date_start_submitBtn)
        datePicker?.descendantFocusability = DatePicker.FOCUS_BLOCK_DESCENDANTS
    }

    fun initData(){
         currentYear = datePicker?.year!!
         currentMonth = datePicker?.month!!
         currentDay = datePicker?.dayOfMonth!!
        startYear = currentYear
        startMonth = currentMonth+1
        startDay = currentDay

        currentYear?.let { currentMonth?.let { it1 -> currentDay?.let { it2 -> datePicker?.init(it, it1, it2,null) } } }


    }


    fun setEvent(){



        datePicker?.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
            startYear = year
            startMonth = monthOfYear +1
            startDay = dayOfMonth
        }

        cancelBtn?.setOnClickListener{
            confirmAction?.onLeftClick()
            dismiss()
        }

        submitBtn?.setOnClickListener {

            var click = false
            if(startYear < currentYear){
                click = false
            }else if (startYear == currentYear){
                if (startMonth < currentMonth+1){
                    click = false

                }else if (startMonth == currentMonth+1){
                    if (startDay < currentDay){
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
                confirmAction?.onRightClick(startYear,startMonth,startDay)

                dismiss()
            }else{
                ToastUtils.showTextToast(context,"开始时间不得小于今天")
            }

        }
    }

    fun getDensityValue(value: Int, activity: Context?): Int {
        val displayMetrics = activity!!.resources.displayMetrics
        return Math.ceil((value * displayMetrics.density).toDouble()).toInt()
    }
}