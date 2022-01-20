package com.hzdq.bajiesleepchildrenHD.device.dialog

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.content.res.Resources.NotFoundException
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.utils.CommonUtils
import com.hzdq.bajiesleepchildrenHD.utils.HideDialogUI
import com.hzdq.bajiesleepchildrenHD.utils.HideUI
import com.hzdq.bajiesleepchildrenHD.utils.ToastUtils.showTextToast

@RequiresApi(Build.VERSION_CODES.M)
class TimeRangePickerDialog(context: Context,  startAndEndTime:String,confirmAction:ConfirmAction) : Dialog(context,R.style.CustomDialog) {


    private val TIME_PICKER_INTERVAL = 15
    private val mIgnoreEvent = false
    var minuteNumberPickerStart: NumberPicker? = null
    var minuteNumberPickerEnd: NumberPicker? = null

    private var startTime: String? = null
    private var endTime: String? = null
    private var startHour: String? = null
    private var startMinute: String? = null
    private var endHour: String? = null
    private var endMinute: String? = null
    private var screenWidth = 0
    private var screenHeight = 0

    private var h1 = 0
    private var h2 = 0
    private var m1 = 0
    private  var m11:Int = 0
    private var m2 = 0
    private  var m22:Int = 0
    var rangeTime = 0
    private var timePickerStart: TimePicker? = null
    private var timePickerEnd: TimePicker? = null


    private var cancelBtn: View? = null
    private  var submitBtn:View? = null

    private var confirmAction: ConfirmAction? = null

  init {
      ownerActivity?.let { HideUI(it).hideSystemUI() }
      val strings = CommonUtils.getRegEx(startAndEndTime, "\\d+:\\d+")
      if (!CommonUtils.isNull(strings) && strings.size >= 2) {
          startTime = CommonUtils.getRegEx(startAndEndTime, "\\d+:\\d+")[0]
          endTime = CommonUtils.getRegEx(startAndEndTime, "\\d+:\\d+")[1]
      }
      this.confirmAction = confirmAction
      val metrics = context.resources.displayMetrics
      screenWidth = metrics.widthPixels - getDensityValue(850, context)
      screenHeight = metrics.heightPixels - getDensityValue(500,context)
      Log.d("screenWidth", screenWidth.toString())
      HideDialogUI.hideSystemUI(this)
  }


    override fun dismiss() {
        super.dismiss()
//        timePickerStart?.hour = 21
//        timePickerStart?.minute = 0
//        timePickerEnd?.hour = 6
//        timePickerEnd?.minute = 0

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("picker", "onCreate: ")
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.dialog_time_range_picker, null)
        setContentView(view)


//        window!!.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,screenWidth )
        window!!.setLayout(screenWidth,screenHeight )
        val number = arrayOf("00", "15", "30", "45")
        timePickerStart = findViewById<View>(R.id.timePickerStart) as TimePicker
        timePickerEnd = findViewById<View>(R.id.timePickerEnd) as TimePicker
        /**
         * 通过反射获取TimePicker源码里hour和minute的id
         */
        val systemResources = Resources.getSystem()
        val minuteNumberPickerStartId = systemResources.getIdentifier("minute", "id", "android")
        minuteNumberPickerStart =
            timePickerStart!!.findViewById<View>(minuteNumberPickerStartId) as NumberPicker
        /**
         * 通过获取到的minuteNumberPicker我们可以先进行TimePicker的时间限制
         * 首先在前面第一行设置setDisplayedValues(null) 在设置最大值和最新数组数据前，先将数据设为null可以避免数组越界
         */
        minuteNumberPickerStart!!.displayedValues = null
        minuteNumberPickerStart!!.minValue = 0
        minuteNumberPickerStart!!.maxValue = number.size - 1
        minuteNumberPickerStart!!.value = 0
        minuteNumberPickerStart!!.displayedValues = number
        val minuteNumberPickerEndId = systemResources.getIdentifier("minute", "id", "android")
        minuteNumberPickerEnd =
            timePickerEnd!!.findViewById<View>(minuteNumberPickerEndId) as NumberPicker
        minuteNumberPickerEnd!!.displayedValues = null
        minuteNumberPickerEnd!!.minValue = 0
        minuteNumberPickerEnd!!.maxValue = number.size - 1
        minuteNumberPickerEnd!!.value = 0
        minuteNumberPickerEnd!!.displayedValues = number
        initView()
        initData()
        setEvent()
    }

    private fun initView() {
        cancelBtn = findViewById(R.id.cancelBtn)
        submitBtn = findViewById<View>(R.id.submitBtn)
    }


    private fun initData() {
        timePickerStart!!.setIs24HourView(true)
        timePickerEnd!!.setIs24HourView(true)
        timePickerStart!!.descendantFocusability = TimePicker.FOCUS_BLOCK_DESCENDANTS
        timePickerEnd!!.descendantFocusability = TimePicker.FOCUS_BLOCK_DESCENDANTS
        setTimePickerDividerColor(timePickerStart)
        setTimePickerDividerColor(timePickerEnd)
        timePickerStart!!.currentHour = 21
        timePickerStart!!.currentMinute = 0
        timePickerEnd!!.currentHour = 6
        timePickerEnd!!.currentMinute = 0
        if (!CommonUtils.isNull(startTime) && !CommonUtils.isNull(endTime)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                timePickerStart!!.hour = startTime!!.substring(0, startTime!!.indexOf(":")).toInt()
                //                timePickerStart.setHour(21);

                timePickerStart!!.minute =
                    startTime!!.substring(startTime!!.indexOf(":") + 1).toInt()
                timePickerEnd!!.hour = endTime!!.substring(0, endTime!!.indexOf(":")).toInt()
                timePickerEnd!!.minute = endTime!!.substring(endTime!!.indexOf(":") + 1).toInt()
            } else {
                timePickerStart!!.currentHour =
                    startTime!!.substring(0, startTime!!.indexOf(":")).toInt()
                timePickerStart!!.currentMinute =
                    startTime!!.substring(startTime!!.indexOf(":") + 1).toInt()
                //
//                timePickerStart.setCurrentHour(15);
//                timePickerStart.setCurrentMinute(0);
                timePickerEnd!!.currentHour = endTime!!.substring(0, endTime!!.indexOf(":")).toInt()
                timePickerEnd!!.currentMinute =
                    endTime!!.substring(endTime!!.indexOf(":") + 1).toInt()
            }
        }
        timePickerStart!!.setOnTimeChangedListener { timePicker, hourOfDay, minute -> //                String h = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
//                String m = minute < 10 ? "0" + minute : "" + minute;
            val h = if (hourOfDay < 10) "0$hourOfDay" else "" + hourOfDay
            val m = if (minute < 10) "0$minute" else "" + minute
            startTime = "$h:$m"
        }
        timePickerEnd!!.setOnTimeChangedListener { timePicker, hourOfDay, minute ->
            val h = if (hourOfDay < 10) "0$hourOfDay" else "" + hourOfDay
            val m = if (minute < 10) "0$minute" else "" + minute
            endTime = "$h:$m"
        }
    }


    private fun setEvent() {
        cancelBtn?.setOnClickListener {
            confirmAction?.onLeftClick()
            dismiss()
        }
        submitBtn?.setOnClickListener(View.OnClickListener {
            h1 = timePickerStart!!.currentHour
            m1 = timePickerStart!!.currentMinute
            if (m1 == 0) {
                m11 = 0
            } else if (m1 == 1) {
                m11 = 15
            } else if (m1 == 2) {
                m11 = 30
            } else if (m1 == 3) {
                m11 = 45
            }

            startHour = if (h1 < 10) "0$h1" else "" + h1
            startMinute = if (m11 < 10) "0$m11" else "" + m11
            h2 = timePickerEnd!!.currentHour
            m2 = timePickerEnd!!.currentMinute
            if (m2 == 0) {
                m22 = 0
            } else if (m2 == 1) {
                m22 = 15
            } else if (m2 == 2) {
                m22 = 30
            } else if (m2 == 3) {
                m22 = 45
            }
            endHour = if (h2 < 10) "0$h2" else "" + h2
            endMinute = if (m22 < 10) "0$m22" else "" + m22
            if (timePickerStart!!.currentHour == 0) {
                h1 = 24
                startHour = "00"
            }
            if (timePickerEnd!!.currentHour == 0) {
                h2 = 24
                endHour = "00"
            }
            Log.d("starTime", "$h1:$m11")
            Log.d("endTime", "$h2:$m22")

            //                if (h1 <  h2){
            //                    rangeTime = (h2-h1)*60+(60-m1)+m2;
            //                }else if (h1>h2){
            //                    rangeTime = ((24-h1)+h2)*60 + (60-m1)+m2;
            //                }else if (h1 == h2){
            //                    if (m1<m2){
            //                        rangeTime = m2-m1;
            //                    }else if (m1 > m2){
            //                        rangeTime = ((24-h1)+h2)*60 + (60-m1)+m2;
            //                    }else if (m1 == m2){
            //                        rangeTime = m2 -m1;
            //                    }
            //                }
            if (h1 > h2) {
                if (m11 < m22) {
                    rangeTime = (h2 + 24 - h1) * 60 + (m22 - m11)
                } else {
                    rangeTime = (h2 + 24 - h1) * 60 - (m11 - m22)
                }
            } else {
                if (m11 < m22) {
                    rangeTime = (h2 - h1) * 60 + (m22 - m11)
                } else {
                    rangeTime = (h2 - h1) * 60 - (m11 - m22)
                }
            }
            if (rangeTime < 721) {
                if (rangeTime < 241) {
                    showTextToast(getContext(), "监测时间不可低于4小时")
                } else {
                    confirmAction!!.onRightClick(startHour, startMinute, endHour, endMinute)
                    dismiss()
                }
            } else {
                showTextToast(getContext(), "监测时间不可超过12小时")
            }
            Log.d("rangeTime", rangeTime.toString())
        })

    }

    private fun setTimePickerDividerColor(timePicker: TimePicker?) {
        val llFirst = timePicker!!.getChildAt(0) as LinearLayout
        val mSpinners = llFirst.getChildAt(1) as LinearLayout
        for (i in 0 until mSpinners.childCount) {
            if (mSpinners.getChildAt(i) is NumberPicker) {
                val pickerFields = NumberPicker::class.java.declaredFields
                setPickerMargin(mSpinners.getChildAt(i) as NumberPicker)
                for (pf in pickerFields) {
                    if (pf.name == "mSelectionDivider") {
                        pf.isAccessible = true
                        try {
                            pf[mSpinners.getChildAt(i)] = ColorDrawable()
                        } catch (e: IllegalArgumentException) {
                            e.printStackTrace()
                        } catch (e: NotFoundException) {
                            e.printStackTrace()
                        } catch (e: IllegalAccessException) {
                            e.printStackTrace()
                        }
                        break
                    }
                }
            }
        }
    }

    /**
     * 设置picker之间的间距
     */
    private fun setPickerMargin(picker: NumberPicker) {
        val p = picker.layoutParams as LinearLayout.LayoutParams
        p.setMargins(-getDensityValue(10, context), 0, -getDensityValue(10, context), 0)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
            p.marginStart = -getDensityValue(10, context)
            p.marginEnd = -getDensityValue(10, context)
        }
    }


    interface ConfirmAction {
        fun onLeftClick()
        fun onRightClick(
            startHour: String?,
            starMinute: String?,
            endHour: String?,
            endMinute: String?
        )
    }

    fun getDensityValue(value: Int, activity: Context?): Int {
        val displayMetrics = activity!!.resources.displayMetrics
        return Math.ceil((value * displayMetrics.density).toDouble()).toInt()
    }

}