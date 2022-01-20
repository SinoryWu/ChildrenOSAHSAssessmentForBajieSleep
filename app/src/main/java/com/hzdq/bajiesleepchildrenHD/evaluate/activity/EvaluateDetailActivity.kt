package com.hzdq.bajiesleepchildrenHD.evaluate.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.gson.JsonObject
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.ActivityEvaluateDetailBinding
import com.hzdq.bajiesleepchildrenHD.dataclass.AssessmentEvaluateDetail
import com.hzdq.bajiesleepchildrenHD.dataclass.DataEvaluateDetail
import com.hzdq.bajiesleepchildrenHD.dataclass.DataclassEvaluateDetail
import com.hzdq.bajiesleepchildrenHD.login.activity.LoginActivity
import com.hzdq.bajiesleepchildrenHD.retrofit.OkhttpSingleton
import com.hzdq.bajiesleepchildrenHD.utils.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*

class EvaluateDetailActivity : AppCompatActivity() {

    private lateinit var binding:ActivityEvaluateDetailBinding
    private lateinit var shp:Shp
    private var tokenDialog: TokenDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this,R.layout.activity_evaluate_detail)
        shp = Shp(this)
        ActivityCollector2.addActivity(this)
        HideUI(this).hideSystemUI()
        val intent = intent
        val id = intent.getIntExtra("id",0)

        val url = OkhttpSingleton.BASE_URL+"/v2/estimateInfo?id=$id"
        getEvaluateDetail(url)

        binding.back.setOnClickListener {

            finish()
        }

        binding.doctor.text = "管理医生：${shp.getDoctorName()}"
    }

    override fun onDestroy() {
        ActivityCollector2.removeActivity(this)
        super.onDestroy()

    }


    private fun getEvaluateDetail(url: String) {
        val request = Request.Builder()
            .url(url) //                .addHeader("token", "30267f97bb1aeb1e2ddca1cda79d92b5")
            //                .addHeader("uid", "8")
            .addHeader("token", shp.getToken())
            .addHeader("uid", shp.getUid())
            .addHeader("User-Agent", shp.getUserAgent())
            .get()
            .build()
        //3.将request封装为call
        val call = OkhttpSingleton.ok()?.newCall(request)

        //4.执行call
//        同步执行
//        Response response = call.execute();

        //异步执行
        call?.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread { ToastUtils.showTextToast2(this@EvaluateDetailActivity, "评估详情网络请求失败") }
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val res = response.body()!!.string()


                //封装对象
                val dataClassEvaluateDetail = DataclassEvaluateDetail()
                val dataEvaluateDetail = DataEvaluateDetail()
                val assessmentList: MutableList<AssessmentEvaluateDetail> = ArrayList<AssessmentEvaluateDetail>()

                try {
                    val jsonObject = JSONObject(res)
                    //第一层解析
                    //第一层解析
                    val code = jsonObject.optInt("code")
                    val data = jsonObject.optJSONObject("data")
                    val msg = jsonObject.optString("msg")


                    //第一层封装

                    //第一层封装
                    dataClassEvaluateDetail.code = code
                    dataClassEvaluateDetail.msg = msg
                    if (data != null){
                        val id = data.optInt("id")
                        val patient_id = data.optInt("patient_id")
                        val oahi = data.optString("oahi")
                        val osas = data.optInt("osas")
                        val opinion = data.optString("opinion")
                        val treat = data.optInt("treat")
                        val treatment = data.optString("treatment")
                        val result = data.optString("result")
                        val assessment = data.optJSONArray("assessment")
                        val hospitalid = data.optInt("hospitalid")
                        val doctor_id = data.optInt("doctor_id")
                        val doctor_name = data.optString("doctor_name")
                        val createTime = data.optString("create_time")

                        dataEvaluateDetail.id = id
                        dataEvaluateDetail.patientId = patient_id
                        dataEvaluateDetail.oahi = oahi
                        dataEvaluateDetail.osas = osas
                        dataEvaluateDetail.opinion = opinion
                        dataEvaluateDetail.treat = treat
                        dataEvaluateDetail.treatment = treatment
                        dataEvaluateDetail.result = result
                        dataEvaluateDetail.assessment = assessmentList
                        dataEvaluateDetail.hospitalid = hospitalid
                        dataEvaluateDetail.doctorId = doctor_id
                        dataEvaluateDetail.doctorName = doctor_name
                        dataEvaluateDetail.createTime = createTime
                        dataClassEvaluateDetail.data = dataEvaluateDetail

                        for (i in 0..assessment.length()){
                            val jsonObject = assessment.getJSONObject(i)
                            if (jsonObject != null){
                                val id = jsonObject.optInt("id")
                                val name = jsonObject.optString("name")
                                val score = jsonObject.optInt("score")
                                val result = jsonObject.optString("result")

                                val assessmentEvaluateDetail = AssessmentEvaluateDetail()
                                assessmentEvaluateDetail.id = id
                                assessmentEvaluateDetail.name = name
                                assessmentEvaluateDetail.score = score
                                assessmentEvaluateDetail.result = result
                                assessmentList.add(assessmentEvaluateDetail)
                            }
                        }
                    }
                }catch (e: JSONException){
                    e.printStackTrace()
                }

                runOnUiThread{

                    if (dataClassEvaluateDetail.code == 0){
                        binding.evaluateResult.text = dataEvaluateDetail.result
                        when(dataEvaluateDetail.osas){
                            1 -> binding.osaLevel.text = "正常"
                            2 -> binding.osaLevel.text = "轻度"
                            3 -> binding.osaLevel.text = "中度"
                            4 -> binding.osaLevel.text = "重度"
                        }

                        when(dataEvaluateDetail.treat){
                            1 -> binding.treat.text = "手术治疗"
                            2 -> binding.treat.text = "药物治疗"
                            3 -> binding.treat.text = "NPPV治疗"
                            4 -> binding.treat.text = "口腔矫正治疗"
                            5 -> binding.treat.text = "健康管理治疗"
                            6 -> binding.treat.text = "无需治疗"
                        }

                        binding.oahiResult.text = dataEvaluateDetail.oahi

                        binding.opinion.text = dataEvaluateDetail.opinion

                        binding.treatment.text = dataEvaluateDetail.treatment


                        for(i in 0..assessmentList.size-1){
                            if (assessmentList[i].name.equals("OSA-18")){

                                binding.osaResult.text = "得分：${assessmentList[i].score}分，结果：${assessmentList[i].result}"
                            }else if (assessmentList[i].name.equals("PSQ")){

                                binding.psqResult.text = "得分：${assessmentList[i].score}分，结果：${assessmentList[i].result}"
                            }
                        }



                        binding.createTime.text = "${timestamp2Date("${dataEvaluateDetail.createTime}","yyyy/MM/dd HH:mm")}"
                    }else if ( dataClassEvaluateDetail.code == 10010 ||  dataClassEvaluateDetail.code == 10004) {
                        if (tokenDialog == null) {
                            tokenDialog = TokenDialog(this@EvaluateDetailActivity, object : TokenDialog.ConfirmAction {
                                override fun onRightClick() {
                                    shp.saveToSp("token", "")
                                    shp.saveToSp("uid", "")

                                    startActivity(
                                        Intent(this@EvaluateDetailActivity,
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
                        ToastUtils.showTextToast(this@EvaluateDetailActivity,"${dataClassEvaluateDetail.msg}")
                    }

                }
            }
        })
    }




}