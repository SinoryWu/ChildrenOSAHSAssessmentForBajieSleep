package com.hzdq.bajiesleepchildrenHD.screen.activities

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.ActivityScreenNewTaskBinding
import com.hzdq.bajiesleepchildrenHD.dataclass.DataClassNewScreenTask
import com.hzdq.bajiesleepchildrenHD.dataclass.NewTaskBody
import com.hzdq.bajiesleepchildrenHD.login.activity.LoginActivity
import com.hzdq.bajiesleepchildrenHD.retrofit.RetrofitSingleton
import com.hzdq.bajiesleepchildrenHD.screen.dialog.DateEndDialog
import com.hzdq.bajiesleepchildrenHD.screen.dialog.DateStartDialog
import com.hzdq.bajiesleepchildrenHD.screen.dialog.DayDialog
import com.hzdq.bajiesleepchildrenHD.screen.viewmodel.ScreenTaskViewModel
import com.hzdq.bajiesleepchildrenHD.utils.*
import kotlinx.android.synthetic.main.activity_check_osa.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@RequiresApi(Build.VERSION_CODES.O)
class ScreenNewTaskActivity : AppCompatActivity() {
    private var dateStartDialog : DateStartDialog? =null
    private var dateEndDialog : DateEndDialog? =null
    private var tokenDialog: TokenDialog? = null
    private var dayDialog : DayDialog ? =null
    private lateinit var binding:ActivityScreenNewTaskBinding
    private lateinit var screenTaskViewModel: ScreenTaskViewModel
    private var currentYear = 0
    private var currentMonth = 0
    private var currentDay =  0
    private var datePicker: DatePicker? = null

    private lateinit var shp: Shp
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_screen_new_task)
        ActivityCollector2.addActivity(this)
        HideUI(this).hideSystemUI()

        shp = Shp(this)
        val newTaskBody = NewTaskBody()
        newTaskBody.hospital_id = shp.getHospitalId().toString()


        val retrofitSingleton =RetrofitSingleton.getInstance(this)
        datePicker = DatePicker(this)
        currentYear = datePicker?.year!!
        currentMonth = datePicker?.month!!+1
        currentDay = datePicker?.dayOfMonth!!


        screenTaskViewModel = ViewModelProvider(this).get(ScreenTaskViewModel::class.java)

        screenTaskViewModel.startYear.value = currentYear
        screenTaskViewModel.startMonth.value = currentMonth
        screenTaskViewModel.startDay.value = currentDay

        screenTaskViewModel.endYear.value = currentYear
        screenTaskViewModel.endMonth.value = currentMonth
        screenTaskViewModel.endDay.value = currentDay
        screenTaskViewModel.setStartTime()
        screenTaskViewModel.setEndTime()

        lifecycleScope.launch {
            delay(10)
            binding.b21.isClickable = false
            binding.b22.isClickable = false

            binding.b41.isClickable = false
            binding.b42.isClickable = false
        }
        val map1 = mapOf(
            1 to binding.b11,
            2 to binding.b12
        )

        val map2 = mapOf(
            1 to binding.b21,
            2 to binding.b22
        )

        val map3 = mapOf(
            1 to binding.b31,
            2 to binding.b32
        )

        val map4 = mapOf(
            1 to binding.b41,
            2 to binding.b42
        )

        val map = mapOf(
            1 to screenTaskViewModel.l1,
            2 to screenTaskViewModel.l2,
            3 to screenTaskViewModel.l3,
            4 to screenTaskViewModel.l4
        )

        screenTaskViewModel.l1.observe(this, Observer {
            map1.forEach {
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))

            }

              when(it){
                  1 ->  {
                      binding.b11.also {
                          it.isSelected = true
                          it.setTextColor(Color.parseColor("#FFFFFF"))
                          newTaskBody.login = "1"
                      }
                  }
                  2 -> {
                      binding.b12.also {
                          it.isSelected = true
                          it.setTextColor(Color.parseColor("#FFFFFF"))
                          newTaskBody.login = "0"
                      }
                  }
                  else -> {
                      map1.forEach {
                          it.value.isSelected = false
                          it.value.setTextColor(Color.parseColor("#C1C1C1"))

                      }
                  }
              }
        })

        screenTaskViewModel.l2.observe(this, Observer {
            map2.forEach {
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))

            }

            when(it){
                1 ->  {
                    binding.b21.also {
                        it.isSelected = true
                        it.setTextColor(Color.parseColor("#FFFFFF"))
                        newTaskBody.resubmit = "1"
                    }
                }
                2 -> {
                    binding.b22.also {
                        it.isSelected = true
                        it.setTextColor(Color.parseColor("#FFFFFF"))
                        newTaskBody.resubmit = "0"
                    }
                }
                else -> {
                    map2.forEach {
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))

                    }
                }
            }
        })

        screenTaskViewModel.l3.observe(this, Observer {
            map3.forEach {
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }

            when(it){
                1 ->  {
                    binding.b31.also {
                        it.isSelected = true
                        it.setTextColor(Color.parseColor("#FFFFFF"))
                        newTaskBody.shows = "1"
                    }
                }
                2 -> {
                    binding.b32.also {
                        it.isSelected = true
                        it.setTextColor(Color.parseColor("#FFFFFF"))
                        newTaskBody.shows = "0"
                    }
                }
                else -> {
                    map3.forEach {
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }
        })

        screenTaskViewModel.l4.observe(this, Observer {
            map4.forEach {
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }

            when(it){
                1 ->  {
                    binding.b41.also {
                        it.isSelected = true
                        it.setTextColor(Color.parseColor("#FFFFFF"))
                        newTaskBody.modify = "1"
                    }
                }
                2 -> {
                    binding.b42.also {
                        it.isSelected = true
                        it.setTextColor(Color.parseColor("#FFFFFF"))
                        newTaskBody.modify = "0"
                    }
                }
                else -> {
                    map2.forEach {
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }
        })


        screenTaskViewModel.question.observe(this, Observer {
            binding.screenQuestion.text = it
            if (it.equals("OSA-18")){
                newTaskBody.type = "14"
            }else if (it.equals("PSQ")){
                newTaskBody.type = "13"
            }
        })


        binding.b11.setOnClickListener {
            screenTaskViewModel.l1.value = 1
            screenTaskViewModel.l2.value = 0
            screenTaskViewModel.l3.value = 0
            screenTaskViewModel.l4.value = 0
            binding.b11.isClickable = false
            binding.b21.isClickable = true
            binding.b22.isClickable = true
            binding.b31.isClickable = true
            binding.b32.isClickable = true
            binding.b41.isClickable = true
            binding.b42.isClickable = true
            binding.b21.isClickable = true}
        binding.b12.setOnClickListener {
            screenTaskViewModel.l1.value = 2
            screenTaskViewModel.l2.value = 2
            screenTaskViewModel.l3.value = 1
            screenTaskViewModel.l4.value = 2
            binding.b21.isClickable = false
            binding.b22.isClickable = false
            binding.b11.isClickable = true
            binding.b31.isClickable = false
            binding.b32.isClickable = false
            binding.b41.isClickable = false
            binding.b42.isClickable = false
            binding.b21.isClickable = false
        }


        binding.b21.setOnClickListener {
            screenTaskViewModel.l2.value = 1
            screenTaskViewModel.l4.value = 2
            binding.b41.isClickable = false
        }
        binding.b22.setOnClickListener {
            screenTaskViewModel.l2.value = 2
            binding.b41.isClickable = true
        }

        binding.b31.setOnClickListener { screenTaskViewModel.l3.value = 1 }
        binding.b32.setOnClickListener { screenTaskViewModel.l3.value = 2 }

        binding.b41.setOnClickListener {
            screenTaskViewModel.l4.value = 1

        }
        binding.b42.setOnClickListener {
            screenTaskViewModel.l4.value = 2

        }

        screenTaskViewModel.startTime.observe(this, Observer {
            binding.screenStartTime.text = it
            screenTaskViewModel.startTimeStamp.value = date2Stamp2("${it} 00:00:00","yyyy年MM月dd日 HH:mm:ss")?.substring(0,10)

        })

        screenTaskViewModel.endTime.observe(this, Observer {
            binding.screenEndTime.text = it
            screenTaskViewModel.endTimeStamp.value = date2Stamp2("${it} 23:59:59","yyyy年MM月dd日 HH:mm:ss")?.substring(0,10)

        })

        binding.back.setOnClickListener {
            finish()
        }

        binding.cancel.setOnClickListener {
            finish()
        }



        binding.changeStartTime.setOnClickListener {
            if (dateStartDialog == null){
                dateStartDialog = DateStartDialog(this,object :DateStartDialog.ConfirmAction{
                    override fun onLeftClick() {

                    }

                    override fun onRightClick(year: Int?, month: Int?, day: Int?) {
                        screenTaskViewModel.startYear.value = year
                        screenTaskViewModel.startMonth.value = month
                        screenTaskViewModel.startDay.value = day
                        screenTaskViewModel.setStartTime()
                    }

                })
            }else {
                dateStartDialog?.show()
                dateStartDialog?.setCanceledOnTouchOutside(false)
            }


        }

        binding.changeEndTime.setOnClickListener {
            if (dateEndDialog == null){
                dateEndDialog = DateEndDialog(this,object :DateEndDialog.ConfirmAction{
                    override fun onLeftClick() {

                    }

                    override fun onRightClick(year: Int?, month: Int?, day: Int?) {
                        screenTaskViewModel.endYear.value = year
                        screenTaskViewModel.endMonth.value = month
                        screenTaskViewModel.endDay.value = day
                        screenTaskViewModel.setEndTime()

                    }

                })
                dateEndDialog?.show()
                dateEndDialog?.setCanceledOnTouchOutside(false)
            }else {
                dateEndDialog?.show()
                dateEndDialog?.setCanceledOnTouchOutside(false)
            }


        }

        binding.changeQuestion.setOnClickListener {
            if (dayDialog == null){
                dayDialog = DayDialog(this,R.style.CustomDialog)
                dayDialog!!.setTitle("选择问卷类型").setCancel("取消",object :DayDialog.IOnCancelListener{
                    override fun onCancel(dialog: DayDialog?) {

                    }

                }).setConfirm("确定",object :DayDialog.IOnConfirmListener{
                    override fun onConfirm(dialog: DayDialog?, question: String?) {
                        screenTaskViewModel.question.value = question
                    }

                })
                dayDialog!!.show()
                dayDialog!!.setCanceledOnTouchOutside(false)

            }else {
                dayDialog!!.show()
                dayDialog!!.setCanceledOnTouchOutside(false)
            }

        }

        screenTaskViewModel.startTimeStamp.observe(this, Observer {
            newTaskBody.start = it
        })

        screenTaskViewModel.endTimeStamp.observe(this, Observer {
            newTaskBody.end = it
        })


        binding.confirm.setOnClickListener {


            map.forEach {
                if (it.value.value == 0){
                    ToastUtils.showTextToast(this,"选项为必填项，请选择")
                    return@setOnClickListener
                }
            }
            if (screenTaskViewModel.startTimeStamp.value?.toInt()!! > screenTaskViewModel.endTimeStamp.value?.toInt()!!){
                ToastUtils.showTextToast(this,"开始时间不得大于结束时间")
            }else {

                postNewTask(retrofitSingleton,newTaskBody)

            }
        }

    }

    override fun onDestroy() {
        ActivityCollector2.removeActivity(this)
        super.onDestroy()
    }

    private fun postNewTask(retrofitSingleton: RetrofitSingleton,newTaskBody: NewTaskBody){
        retrofitSingleton.api().postNewTask(newTaskBody).enqueue(object :Callback<DataClassNewScreenTask>{
            override fun onResponse(
                call: Call<DataClassNewScreenTask>,
                response: Response<DataClassNewScreenTask>
            ) {

                if (response.body()?.code == 0){
                    ToastUtils.showTextToast(this@ScreenNewTaskActivity,"添加成功")

                    setResult(RESULT_OK)
                    finish()
                }else if (response.body()?.code == 10010 || response.body()?.code == 10004) {
                    if (tokenDialog == null) {
                        tokenDialog = TokenDialog(this@ScreenNewTaskActivity, object : TokenDialog.ConfirmAction {
                            override fun onRightClick() {
                                shp.saveToSp("token", "")
                                shp.saveToSp("uid", "")

                                startActivity(
                                    Intent(this@ScreenNewTaskActivity,
                                        LoginActivity::class.java)
                                )
                                ActivityCollector2.finishAll()
                            }

                        })
                        tokenDialog!!.show()
                        tokenDialog?.setCanceledOnTouchOutside(false)
                    } else {
                        tokenDialog!!.show()
                        tokenDialog?.setCanceledOnTouchOutside(false)
                    }
                }else {
                    ToastUtils.showTextToast(this@ScreenNewTaskActivity,"${response.body()?.msg}")
                }
            }

            override fun onFailure(call: Call<DataClassNewScreenTask>, t: Throwable) {
                ToastUtils.showTextToast(this@ScreenNewTaskActivity,"新增任务网络请求失败")
            }

        })
    }



}