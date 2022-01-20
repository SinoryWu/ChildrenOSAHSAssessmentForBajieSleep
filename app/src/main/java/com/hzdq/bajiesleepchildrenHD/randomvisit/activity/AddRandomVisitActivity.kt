package com.hzdq.bajiesleepchildrenHD.randomvisit.activity

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.ActivityAddRandomVisitBinding
import com.hzdq.bajiesleepchildrenHD.dataclass.DataAddEvaluate
import com.hzdq.bajiesleepchildrenHD.dataclass.DataClassAddEvaluate
import com.hzdq.bajiesleepchildrenHD.dataclass.DataClassAddFollowUp
import com.hzdq.bajiesleepchildrenHD.dataclass.MessageDateClass
import com.hzdq.bajiesleepchildrenHD.login.activity.LoginActivity
import com.hzdq.bajiesleepchildrenHD.randomvisit.dialog.DateVisitDialog
import com.hzdq.bajiesleepchildrenHD.randomvisit.viewmodel.RandomViewModel
import com.hzdq.bajiesleepchildrenHD.retrofit.OkhttpSingleton
import com.hzdq.bajiesleepchildrenHD.screen.dialog.DateStartDialog
import com.hzdq.bajiesleepchildrenHD.user.activities.UserScreenAdd2Activity
import com.hzdq.bajiesleepchildrenHD.user.activities.UserScreenAddActivity
import com.hzdq.bajiesleepchildrenHD.utils.*
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.HashMap

const val OSAADD_REQUESTCODE = 4
const val PSQADD_REQUESTCODE = 5
@RequiresApi(Build.VERSION_CODES.O)
class AddRandomVisitActivity : AppCompatActivity() {
    private var tokenDialog: TokenDialog? = null
    private lateinit var binding:ActivityAddRandomVisitBinding
    private lateinit var randomViewModel: RandomViewModel
    private var dateDialog : DateVisitDialog? =null
    private var currentYear = 0
    private var currentMonth = 0
    private var currentDay =  0
    private var datePicker1: DatePicker? = null
    private var datePicker2: DatePicker? = null
    private var startYear = 0
    private var startMonth =  0
    private var startDay =  0
    private lateinit var shp:Shp
    val assessment: MutableList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = DataBindingUtil.setContentView(this,R.layout.activity_add_random_visit)

        shp = Shp(this)

        HideUI(this).hideSystemUI()

        randomViewModel = ViewModelProvider(this).get(RandomViewModel::class.java)
        ActivityCollector2.addActivity(this)

        val intent = intent
        val patient_id = intent.getIntExtra("patient_id",0)
        val name = intent.getStringExtra("name")


        randomViewModel.patient_id.value = patient_id
        randomViewModel.name.value = name
        Log.d("patient_id", "onCreate:$patient_id ")


        binding.cancel.setOnClickListener {
            finish()
        }

        binding.back.setOnClickListener{
            finish()
        }
        binding.doctor.text = "管理医生：${shp.getDoctorName()}"

        randomViewModel.name.observe(this, Observer {
            binding.name.text = it
        })
        datePicker1 = DatePicker(this)
        datePicker2 = DatePicker(this)
        currentYear = datePicker1?.year!!

        currentMonth = datePicker1?.month!! +4
        if (currentMonth > 12){
            currentMonth =  datePicker1?.month!! +4-12
            currentYear = datePicker1?.year!!+1
        }
        currentDay =  datePicker1?.dayOfMonth!!
        startYear = currentYear
        startMonth = currentMonth
        startDay = currentDay
        datePicker2!!.init(startYear,startMonth-1,startDay,null)

        randomViewModel.year.value = datePicker2!!.year
        randomViewModel.month.value = datePicker2!!.month +1
        randomViewModel.day.value = datePicker2!!.dayOfMonth
        randomViewModel.setTime()

        randomViewModel.time.observe(this, Observer {
            binding.t1.text = it

            randomViewModel.next.value = date2Stamp2("${it} 00:00:00","yyyy年MM月dd日 HH:mm:ss")?.substring(0,10)
        })




        binding.l2.setOnClickListener {


            if (dateDialog == null){
                binding.i1.setImageResource(R.mipmap.pupop_up_icon)
                dateDialog = DateVisitDialog(this,object :DateVisitDialog.ConfirmAction{
                    override fun onLeftClick() {
                        binding.i1.setImageResource(R.mipmap.pupop_down_icon)
                    }

                    override fun onRightClick(year: Int?, month: Int?, day: Int?) {
                        randomViewModel.year.value = year
                        randomViewModel.month.value = month
                        randomViewModel.day.value = day
                        randomViewModel.setTime()
                        binding.i1.setImageResource(R.mipmap.pupop_down_icon)
                    }

                })
                dateDialog?.show()
                dateDialog?.setCanceledOnTouchOutside(false)
            }else {
                binding.i1.setImageResource(R.mipmap.pupop_up_icon)
                dateDialog?.show()
                dateDialog?.setCanceledOnTouchOutside(false)
            }


        }

        binding.oahi.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                randomViewModel.oahi.value = s.toString().trim()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        setLine1()

        binding.height.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                randomViewModel.height.value =s.toString().trim()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.weight.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                randomViewModel.weight.value =s.toString().trim()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.neck.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                randomViewModel.neck.value =s.toString().trim()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        randomViewModel.height.observe(this, Observer {
            randomViewModel.setBmi()
        })
        randomViewModel.weight.observe(this, Observer {
            randomViewModel.setBmi()
        })

        randomViewModel.bmi.observe(this, Observer {
            if (it.equals("")){
                binding.bmi.text = "- -"
            }else {
                binding.bmi.text = it
            }
        })

        binding.osaButton.setOnClickListener {
            val intent = Intent(this, UserScreenAddActivity::class.java)
            intent.putExtra("evaluate", 1)
            intent.putExtra("uid", randomViewModel.patient_id.value)
            intent.putExtra("name", randomViewModel.name.value)
            startActivityForResult(intent, OSAADD_REQUESTCODE)
        }

        binding.psqButton.setOnClickListener {
            val intent = Intent(this, UserScreenAdd2Activity::class.java)
            intent.putExtra("evaluate", 1)
            intent.putExtra("uid", randomViewModel.patient_id.value)
            intent.putExtra("name", randomViewModel.name.value)
            startActivityForResult(intent, PSQADD_REQUESTCODE)
        }

        setOsaResult()
        setPsqResult()


        binding.reason.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                randomViewModel.reason.value = s.toString().trim()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })





//        if (randomViewModel.assessment.value.equals("[]")){
//            maps["assessment"] = ""
//        }else {
//            maps["assessment"] = randomViewModel.assessment.value.toString()
//        }
//
//        maps["reason"] = randomViewModel.reason.value.toString()
//        maps["next"] = randomViewModel.next.value.toString()


        val map1 = mapOf(
            1 to randomViewModel.patient_id,
            2 to randomViewModel.suspend
        )

        val map2 = mapOf(
            1 to  randomViewModel.oahi,
            2 to randomViewModel.height,
            3 to randomViewModel.weight,
            4 to randomViewModel.bmi,
            5 to randomViewModel.assessment

        )
        binding.confirm.setOnClickListener {
            assessment.clear()
            if (!randomViewModel.osa.value.equals("") && randomViewModel.psq.value.equals("")) {
                assessment.add(randomViewModel.osa.value!!)
            } else if (!randomViewModel.psq.value.equals("") && randomViewModel.osa.value.equals(
                    ""
                )
            ) {
                assessment.add(randomViewModel.psq.value!!)
            } else if (!randomViewModel.psq.value.equals("") && !randomViewModel.osa.value.equals("")) {
                assessment.add(randomViewModel.osa.value!!)
                assessment.add(randomViewModel.psq.value!!)
            }
            if (assessment.equals("[]")){
                randomViewModel.assessment.value = ""
            }else {
                randomViewModel.assessment.value = "${assessment}"
            }

            map1.forEach {
                if (it.value.value == 0){
                    ToastUtils.showTextToast(this,"*号为必填项")
                    return@setOnClickListener
                }
            }

            map2.forEach {
                if (it.value.value.equals("")){
                    ToastUtils.showTextToast(this,"*号为必填项")
                    return@setOnClickListener
                }
            }

            if (assessment.size < 2){
                ToastUtils.showTextToast(this,"*号为必填项")
                return@setOnClickListener
            }

            if (randomViewModel.suspend.value == 2 && randomViewModel.reason.value.equals("")){
                ToastUtils.showTextToast(this,"*号为必填项")
                return@setOnClickListener
            }

            if (randomViewModel.weight.value.equals("0")){
                ToastUtils.showTextToast(this,"体重必须为正数")
                return@setOnClickListener
            }

            if (randomViewModel.height.value.equals("0")){
                ToastUtils.showTextToast(this,"身高必须为正数")
                return@setOnClickListener
            }

            if (randomViewModel.neck.value.equals("0")){
                ToastUtils.showTextToast(this,"颈围必须为正数")
                return@setOnClickListener
            }


            val map = getMap()
            val url  = OkhttpSingleton.BASE_URL + "/v2/followupSave"

            postAddFollowUp(url,map)
        }



    }

    override fun onDestroy() {
        ActivityCollector2.removeActivity(this)
        super.onDestroy()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            OSAADD_REQUESTCODE -> {
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        randomViewModel.osa.value = "{\"id\":${
                            data?.getIntExtra(
                                "id",
                                0
                            )
                        }, \"name\":\"${data?.getStringExtra("name")}\", \"score\":${
                            data?.getIntExtra(
                                "score",
                                0
                            )
                        }, \"result\": \"${data?.getStringExtra("result")}\"}"
                        randomViewModel.osa1.value = "得分：${
                            data?.getIntExtra(
                                "score",
                                0
                            )
                        }分；结果：${data?.getStringExtra("result")}"


                    }

                }

            }
            PSQADD_REQUESTCODE -> {
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        randomViewModel.psq.value = "{\"id\":${
                            data?.getIntExtra(
                                "id",
                                0
                            )
                        }, \"name\":\"${data?.getStringExtra("name")}\", \"score\":${
                            data?.getIntExtra(
                                "score",
                                0
                            )
                        }, \"result\": \"${data?.getStringExtra("result")}\"}"
                        randomViewModel.psq1.value = "得分：${
                            data?.getIntExtra(
                                "score",
                                0
                            )
                        }分；结果：${data?.getStringExtra("result")}"

                    }
                }

            }
            else -> {
                Log.d("DeviceFragment", "onActivityResult:没刷新 ")
            }
        }
    }

    /**
     * 添加随访记录
     */
    fun postAddFollowUp(url:String,map: HashMap<String, String>) {
        //1.拿到okhttp对象


        //2.构造request
        //2.1构造requestbody
        val params = HashMap<String?, Any?>()

        val keys: Set<String> = map.keys
        for (key in keys) {
            params[key] = map[key]
        }

        Log.e("params:", params.toString())
        val jsonObject = JSONObject(params)
        val jsonStr = jsonObject.toString()

        val requestBodyJson =
            RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr)

        val request = Request.Builder()
            .addHeader("User-Agent", shp.getUserAgent())
            .addHeader("token", shp.getToken())
            .addHeader("uid", shp.getUid())
            .url(url)
            .post(requestBodyJson)
            .build()
        //3.将request封装为call
        val call = OkhttpSingleton.ok()?.newCall(request)

        //4.执行call
//        同步执行
//        Response response = call.execute();

        //异步执行
        call?.enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {

                runOnUiThread { ToastUtils.showTextToast2(this@AddRandomVisitActivity, "添加随访记录网络请求失败") }
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {

                val res = response.body()!!.string()


                val gson = Gson()
                val dataClassAddFollowUp: DataClassAddFollowUp = gson.fromJson(res, DataClassAddFollowUp::class.java)

                runOnUiThread {
                    if (dataClassAddFollowUp.code == 0) {


                        ToastUtils.showTextToast(this@AddRandomVisitActivity,"添加成功")
                        setResult(RESULT_OK)
                        finish()
                    } else if (dataClassAddFollowUp.code == 10010 || dataClassAddFollowUp.code == 10004) {
                        if (tokenDialog == null) {
                            tokenDialog = TokenDialog(this@AddRandomVisitActivity, object : TokenDialog.ConfirmAction {
                                override fun onRightClick() {
                                    shp.saveToSp("token", "")
                                    shp.saveToSp("uid", "")

                                    startActivity(
                                        Intent(this@AddRandomVisitActivity,
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
                        ToastUtils.showTextToast(
                            this@AddRandomVisitActivity,
                            "${dataClassAddFollowUp.msg}"
                        )
                    }
                }
            }
        })
    }


    fun getMap():HashMap<String,String>{
        val maps = HashMap<String, String>()
        maps["hospitalid"] = shp.getHospitalId().toString() //18158188052
        maps["patient_id"] = randomViewModel.patient_id.value.toString() //18158188052
        maps["oahi"] = randomViewModel.oahi.value.toString()
        maps["height"] = randomViewModel.height.value.toString()
        maps["weight"] = randomViewModel.weight.value.toString()
        maps["bmi"] = randomViewModel.bmi.value.toString()
        maps["assessment"] = randomViewModel.assessment.value.toString()
        maps["suspend"] = randomViewModel.suspend.value.toString()
        maps["reason"] = randomViewModel.reason.value.toString()
        maps["next"] = randomViewModel.next.value.toString()
        maps["neck"] = randomViewModel.neck.value.toString()

        return maps

    }


    fun setLine1(){
        val map = mapOf(
            1 to binding.b11,
            2 to binding.b12
        )
        val map1 = mapOf(
            1 to binding.textView177,
            2 to binding.reason,
            3 to binding.resonPoint
        )

        val map2 = mapOf(
            1 to binding.textView178,
            2 to binding.l2,
            3 to binding.nextPoint
        )


        randomViewModel.suspend.observe(this, Observer {
            map.forEach {
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            map1.forEach {
                it.value.visibility = View.GONE

            }
            map2.forEach {
                it.value.visibility = View.GONE
            }
            when(it){
                1 -> {
                    binding.b12.isSelected = true
                    binding.b12.setTextColor(Color.parseColor("#FFFFFF"))
                    map2.forEach {
                        it.value.visibility = View.VISIBLE

                    }

                }
                2 ->  {
                    binding.b11.isSelected = true
                    binding.b11.setTextColor(Color.parseColor("#FFFFFF"))
                    map1.forEach {
                        it.value.visibility = View.VISIBLE
                    }

                }
                else -> {
                    map.forEach {
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                    map1.forEach {
                        it.value.visibility = View.GONE

                    }
                    map2.forEach {
                        it.value.visibility = View.GONE
                    }
                }
            }
        })

        binding.b11.setOnClickListener { randomViewModel.suspend.value = 2 }
        binding.b12.setOnClickListener {
            randomViewModel.suspend.value = 1
            randomViewModel.reason.value = ""
            binding.reason.setText("")
        }
    }

    fun setOsaResult() {
        randomViewModel.osa1.observe(this, Observer {
            if (!it.equals("")) {
                binding.osaScore.text = "${it}                                       "
                binding.osaButton.text = "重新评测"
            } else {
                binding.osaScore.text = it
            }

        })
    }

    fun setPsqResult() {
        randomViewModel.psq1.observe(this, Observer {
            if (!it.equals("")) {
                binding.psqScore.text = "${it}                                       "
                binding.psqButton.text = "重新评测"
            } else {
                binding.psqScore.text = it
            }
        })
    }

}