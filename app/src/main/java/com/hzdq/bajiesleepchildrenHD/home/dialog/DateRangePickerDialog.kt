package com.hzdq.bajiesleepchildrenHD.home.dialog

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
import com.hzdq.bajiesleepchildrenHD.utils.HideUI
import com.hzdq.bajiesleepchildrenHD.utils.ToastUtils
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
 class DateRangePickerDialog(context: Context,confirmAction: ConfirmAction): Dialog(context, R.style.CustomDialog) {

    private var confirmAction: ConfirmAction? = null
    private var screenWidth = 0
    private var screenHeight = 0
    private var datePickerStart: DatePicker? = null
    private var datePickerEnd: DatePicker? = null

    private var cancelBtn: View? = null
    private  var submitBtn:View? = null

    private var startYear = 0
    private var startMonth =  0
    private var startDay =  0

    private var endYear =  0
    private var endMonth =  0
    private var endDay =  0
    private var currentStartYear =  0
    private var currentStartMonth =  0
    private var currentStartDay = 0

    private var currentEndYear =  0
    private var currentEndMonth =  0
    private var currentEndDay = 0
    init {
        ownerActivity?.let { HideUI(it).hideSystemUI() }
        this.confirmAction = confirmAction

        val metrics = context.resources.displayMetrics
        screenWidth = metrics.widthPixels - getDensityValue(650, context)
        screenHeight = metrics.heightPixels - getDensityValue(500,context)
        Log.d("screenWidth", screenWidth.toString())
        HideDialogUI.hideSystemUI(this)
    }

//    override fun dismiss() {
//        super.dismiss()
//        val calendar = Calendar.getInstance()
//        val year = calendar.get(Calendar.YEAR)
//        val month = calendar.get(Calendar.MONTH)
//        val day = calendar.get(Calendar.DAY_OF_MONTH)
//
//        datePickerStart?.init(year,month,day,null)
//        datePickerEnd?.init(year,month,day+1,null)
//
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view: View =
            LayoutInflater.from(context).inflate(R.layout.dialog_date_range_picker_layout, null)

        setContentView(view)

        window!!.setLayout(screenWidth,screenHeight )
        datePickerStart = findViewById<View>(R.id.date_start_picker) as DatePicker
        datePickerEnd = findViewById<View>(R.id.date_end_picker) as DatePicker



        initView()
        initData()
        setEvent()
    }

    fun initView(){
        cancelBtn = findViewById(R.id.date_cancelBtn)
        submitBtn = findViewById<View>(R.id.date_submitBtn)
        datePickerStart?.descendantFocusability = DatePicker.FOCUS_BLOCK_DESCENDANTS
        datePickerEnd?.descendantFocusability = DatePicker.FOCUS_BLOCK_DESCENDANTS
    }

    fun initData(){
         currentStartYear = datePickerStart?.year!!
         currentStartMonth = datePickerStart?.month!!
         currentStartDay = datePickerStart?.dayOfMonth!!

        currentEndYear = datePickerEnd?.year!!
        currentEndMonth = datePickerEnd?.month!!
        currentEndDay = datePickerEnd?.dayOfMonth!!
        Log.d("hello", "onCreate:$currentStartYear , $currentStartMonth ,$currentStartDay ")

        Log.d("hello", "onCreate:${datePickerEnd?.year} 年 ${(datePickerEnd?.month)?.plus(1)} 月 ${datePickerEnd?.dayOfMonth} 日")

        startYear = currentStartYear
        startMonth = currentStartMonth+1
        startDay = currentStartDay

        endYear = currentEndYear
        endMonth = currentEndMonth+1
        endDay = currentEndDay

        currentStartYear?.let { currentStartMonth?.let { it1 -> currentStartDay?.let { it2 -> datePickerStart?.init(it, it1, it2,null) } } }

        currentEndYear?.let { currentEndMonth?.let { it1 -> currentEndDay?.let { it2 -> datePickerEnd?.init(it, it1, it2,null) } } }
    }


    fun setEvent(){



        datePickerStart?.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
            startYear = year
            startMonth = monthOfYear + 1
            startDay = dayOfMonth
        }

        datePickerEnd?.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
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
            if(startYear < endYear){
                click = true

            }else if (startYear == endYear){
                if (startMonth < endMonth){
                    click = true

                }else if (startMonth == endMonth){
                    if (startDay < endDay){
                        click = true
                    } else if (startDay == endDay){
                        click = true
                    }else {
                        click = false
                    }
                }else {
                    click = false
                }
            }else {
                click = false
            }
            if (click){

                confirmAction?.onRightClick(startYear,startMonth,startDay,endYear,endMonth,endDay)
                dismiss()
            }else{
                ToastUtils.showTextToast(context,"起始日期不得大于结束日期")
            }

        }
    }



    interface ConfirmAction {
        fun onLeftClick()
        fun onRightClick(
            startYear: Int?,
            startMonth: Int?,
            startDay: Int?,
            endYear: Int?,
            endMonth: Int?,
            endDay: Int?
        )
    }


    fun getDensityValue(value: Int, activity: Context?): Int {
        val displayMetrics = activity!!.resources.displayMetrics
        return Math.ceil((value * displayMetrics.density).toDouble()).toInt()
    }
}