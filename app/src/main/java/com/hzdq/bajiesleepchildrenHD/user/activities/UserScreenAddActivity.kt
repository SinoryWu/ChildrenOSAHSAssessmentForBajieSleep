package com.hzdq.bajiesleepchildrenHD.user.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.ActivityUserScreenAddBinding
import com.hzdq.bajiesleepchildrenHD.dataclass.DataClassQuestionResult
import com.hzdq.bajiesleepchildrenHD.dataclass.OSAResultBody
import com.hzdq.bajiesleepchildrenHD.login.activity.LoginActivity
import com.hzdq.bajiesleepchildrenHD.retrofit.RetrofitSingleton
import com.hzdq.bajiesleepchildrenHD.user.viewmodel.UserViewModel
import com.hzdq.bajiesleepchildrenHD.utils.*
import okhttp3.Headers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class UserScreenAddActivity : AppCompatActivity() {
    private lateinit var binding:ActivityUserScreenAddBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var osaResultBody: OSAResultBody
    private lateinit var shp: Shp
    private var tokenDialog: TokenDialog? = null
    var evaluate = 0
    var addScreen = 0
    var uid = 0
    var name = ""
    var screen = 0
    val mutableList:ArrayList<Int> = ArrayList<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HideUI(this).hideSystemUI()
        binding = DataBindingUtil.setContentView(this,R.layout.activity_user_screen_add)
        shp = Shp(this)
        ActivityCollector2.addActivity(this)
        osaResultBody = OSAResultBody()
        userViewModel  = ViewModelProvider(this).get(UserViewModel::class.java)
        val retrofit = RetrofitSingleton.getInstance(this)
        binding.back.setOnClickListener {

            finish()
        }

        val intent = intent
        evaluate = intent.getIntExtra("evaluate",0)
        addScreen = intent.getIntExtra("addScreen",0)
        screen = intent.getIntExtra("screen",0)
//        userViewModel.uid.value = intent.getIntExtra("uid",0)
        if (evaluate == 1){

            val name = intent.getStringExtra("name")

            uid = intent.getIntExtra("uid",0)

            userViewModel.uid.value = uid



            binding.questionChoiceNameButton.visibility = View.GONE
            binding.questionChoiceName.visibility = View.VISIBLE

            binding.questionChoiceName.text = name
        }
        if (addScreen == 1){
            val name = intent.getStringExtra("name")

            uid = intent.getIntExtra("uid",0)

            userViewModel.uid.value = uid



            binding.questionChoiceNameButton.visibility = View.GONE
            binding.questionChoiceName.visibility = View.VISIBLE

            binding.questionChoiceName.text = name
            ActivityCollector.addActivity(this)
        }
        Log.d("osaResult", "onCreate: $osaResultBody ")
        binding.questionDoctor.text = "管理医生：${shp.getDoctorName()}"

        binding.addOSACancel.setOnClickListener {
            finish()
        }
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        initViewModel()



        binding.questionChoiceNameButton.setOnClickListener {
            startActivityForResult(Intent(this,UserScreenChoiceUserActivity::class.java),1)
        }
        setLine1()
        setLine2()
        setLine3()
        setLine4()
        setLine5()
        setLine6()
        setLine7()
        setLine8()
        setLine9()
        setLine10()
        setLine11()
        setLine12()
        setLine13()
        setLine14()
        setLine15()
        setLine16()
        setLine17()
        setLine18()
        val shp = Shp(this)
        osaResultBody.taskId = 0
        osaResultBody.type = 14
        osaResultBody.user_info = ""
        osaResultBody.hospital_id =shp.getHospitalId()!!.toString()
        osaResultBody.uid = 0

        val map = mapOf(
            1 to userViewModel.ol1,
            2 to userViewModel.ol2,
            3 to userViewModel.ol3,
            4 to userViewModel.ol4,
            5 to userViewModel.ol5,
            6 to userViewModel.ol6,
            7 to userViewModel.ol7,
            8 to userViewModel.ol8,
            9 to userViewModel.ol9,
            10 to userViewModel.ol10,
            11 to userViewModel.ol11,
            12 to userViewModel.ol12,
            13 to userViewModel.ol13,
            14 to userViewModel.ol14,
            15 to userViewModel.ol15,
            16 to userViewModel.ol16,
            17 to userViewModel.ol17,
            18 to userViewModel.ol18

        )

        binding.addOSAConfirm.setOnClickListener {


            map.forEach {
                if (it.value.value == 0){
                    ToastUtils.showTextToast(this,"问题不能为空")
                    return@setOnClickListener
                }
            }


            val content = "{" +
                    "\"150\":${userViewModel.ol1.value}," +
                    "\"151\":${userViewModel.ol2.value}," +
                    "\"152\":${userViewModel.ol3.value}," +
                    "\"153\":${userViewModel.ol4.value}," +
                    "\"154\":${userViewModel.ol5.value}," +
                    "\"155\":${userViewModel.ol6.value}," +
                    "\"156\":${userViewModel.ol7.value}," +
                    "\"157\":${userViewModel.ol8.value}," +
                    "\"158\":${userViewModel.ol9.value}," +
                    "\"159\":${userViewModel.ol10.value}," +
                    "\"160\":${userViewModel.ol11.value}," +
                    "\"161\":${userViewModel.ol12.value}," +
                    "\"162\":${userViewModel.ol13.value}," +
                    "\"163\":${userViewModel.ol14.value}," +
                    "\"164\":${userViewModel.ol15.value}," +
                    "\"165\":${userViewModel.ol16.value}," +
                    "\"166\":${userViewModel.ol17.value}," +
                    "\"167\":${userViewModel.ol18.value}}"
            map.forEach {
                mutableList.add(it.value.value!!)
            }
             osaResultBody.content =content
            osaResultBody.hospital_id = shp.getHospitalId().toString()
            osaResultBody.type = 14
            osaResultBody.uid = userViewModel.uid.value!!

//            osaResultBody.content = "{\"150\":4,\"151\":3,\"152\":3,\"153\":3,\"154\":3,\"155\":3,\"156\":3,\"157\":3,\"158\":3,\"159\":3,\"160\":3,\"161\":3,\"162\":3,\"163\":3,\"164\":3,\"165\":3,\"166\":3,\"167\":3}"
            Log.d("osaResult", "onCreate: $osaResultBody ")
            postSubmitResult(retrofit,osaResultBody)


        }
    }

    override fun onDestroy() {
        ActivityCollector2.removeActivity(this)
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            1 -> {
                if(resultCode == RESULT_OK){
                    val userInfo  = data?.getStringExtra("userInfo")
                    val uid = data?.getStringExtra("uid")
                    val name = data?.getStringExtra("name")

                    if (userInfo != null) {
                        osaResultBody.user_info = "{$userInfo}"
                    }
                    if (uid != null) {
                        userViewModel.uid.value = uid.toInt()

                    }
                    binding.questionChoiceNameButton.visibility = View.GONE
                    binding.questionChoiceName.visibility = View.VISIBLE
                    binding.questionChoiceName.text = name
                }
            }
            else -> {
                Log.d("onActivityResult", "onActivityResult: 没刷新")
            }
        }
    }

    private fun postSubmitResult(retrofit: RetrofitSingleton,osaResultBody: OSAResultBody){
        retrofit.api().postOSASubmitResult(osaResultBody).enqueue(object :Callback<DataClassQuestionResult>{
            override fun onResponse(
                call: Call<DataClassQuestionResult>,
                response: Response<DataClassQuestionResult>
            ) {

                Log.d("UserScreenAdd", "onResponse: ${response.body()}")

                if (response.body()?.code == 0){
                    val measures= response.body()?.data?.measures
                    val total= response.body()?.data?.total
                    val result= response.body()?.data?.result
                    val createTime= response.body()?.data?.createTime

                    val intent = Intent(this@UserScreenAddActivity,CheckOSA_Activity::class.java)
                    val task_id = response.body()?.data?.id

                     if (response.body()?.data?.id == 0){
                         val bundle = Bundle()
                         bundle.putIntegerArrayList("list",mutableList)
                         bundle.putString("measures",measures)
                         bundle.putString("result",result)
                         bundle.putInt("total", total!!)
                         bundle.putInt("createTime", createTime!!)
                         bundle.putString("name", binding.questionChoiceName.text.toString())
                         intent.putExtra("bundle",bundle)
                         if (evaluate == 1){

                         }else {
                             startActivity(intent)
                         }

                     }else {
                         intent.putExtra("taskid",task_id)

                         if (evaluate == 1){

                         }else {
                             startActivity(intent)
                         }



                     }

                    Log.d("asdsajdllakasd", "$evaluate ")
                    Log.d("asdsajdllakasd", "$addScreen ")


                    if (evaluate == 1 ){
                        val intent = Intent()
                        intent.putExtra("score",total)
                        intent.putExtra("result",result)
                        intent.putExtra("id",task_id)
                        intent.putExtra("name","OSA-18")
                        setResult(RESULT_OK,intent)
                        ActivityCollector2.removeActivity(this@UserScreenAddActivity)
                        finish()
                    }else if (addScreen == 1){

                        intent.putExtra("score",total)
                        intent.putExtra("result",result)
                        intent.putExtra("id",task_id)
                        intent.putExtra("name","OSA-18")
                        setResult(RESULT_OK,intent)
                        ActivityCollector2.removeActivity(this@UserScreenAddActivity)
                        finish()
                    }else if (screen == 1 && task_id!=0) {
                        setResult(RESULT_OK)
                        ActivityCollector2.removeActivity(this@UserScreenAddActivity)
                        finish()
                    }else {
                        ActivityCollector2.removeActivity(this@UserScreenAddActivity)
                        finish()
                    }

                }else if (response.body()?.code == 10010 || response.body()?.code == 10004) {
                    if (tokenDialog == null) {
                        tokenDialog = TokenDialog(
                            this@UserScreenAddActivity,
                            object : TokenDialog.ConfirmAction {
                                override fun onRightClick() {
                                    shp.saveToSp("token", "")
                                    shp.saveToSp("uid", "")
                                    startActivity(
                                        Intent(
                                            this@UserScreenAddActivity,
                                            LoginActivity::class.java
                                        )
                                    )
                                    ActivityCollector2.finishAll()
                                }
                            })
                        tokenDialog!!.show()
                        tokenDialog!!.setCanceledOnTouchOutside(false)
                    } else {
                        tokenDialog!!.show()
                        tokenDialog!!.setCanceledOnTouchOutside(false)
                    }
                }else {
                    ToastUtils.showTextToast(this@UserScreenAddActivity,"${response.body()?.msg}")
                }
            }

            override fun onFailure(call: Call<DataClassQuestionResult>, t: Throwable) {
                ToastUtils.showTextToast(this@UserScreenAddActivity,"提交结果网络请求失败")
            }

        })


    }





    private fun setLine1(){
        val map = mapOf(
            1 to binding.b11,
            2 to binding.b12,
            3 to binding.b13,
            4 to binding.b14,
            5 to binding.b15,
            6 to binding.b16,
            7 to binding.b17
        )
        userViewModel.ol1.observe(this, Observer {
            map.forEach {
                it.value.isSelected = false
            }

            when (it){
                1 -> binding.b11.isSelected = true

                2 -> binding.b12.isSelected = true
                3 -> binding.b13.isSelected = true
                4 -> binding.b14.isSelected = true
                5 -> binding.b15.isSelected = true
                6 -> binding.b16.isSelected = true
                7 -> binding.b17.isSelected = true
                0 -> {
                    map.forEach {
                        it.value.isSelected = false
                    }
                }
            }
        })
        binding.b11.setOnClickListener { userViewModel.ol1.value = 1 }
        binding.b12.setOnClickListener { userViewModel.ol1.value = 2 }
        binding.b13.setOnClickListener { userViewModel.ol1.value = 3 }
        binding.b14.setOnClickListener { userViewModel.ol1.value = 4 }
        binding.b15.setOnClickListener { userViewModel.ol1.value = 5 }
        binding.b16.setOnClickListener { userViewModel.ol1.value = 6 }
        binding.b17.setOnClickListener { userViewModel.ol1.value = 7 }
    }
    private fun setLine2(){
        val map = mapOf(
            1 to binding.b21,
            2 to binding.b22,
            3 to binding.b23,
            4 to binding.b24,
            5 to binding.b25,
            6 to binding.b26,
            7 to binding.b27
        )

        userViewModel.ol2.observe(this, Observer {
            map.forEach {
                it.value.isSelected = false
            }

            when (it){
                1 -> binding.b21.isSelected = true
                2 -> binding.b22.isSelected = true
                3 -> binding.b23.isSelected = true
                4 -> binding.b24.isSelected = true
                5 -> binding.b25.isSelected = true
                6 -> binding.b26.isSelected = true
                7 -> binding.b27.isSelected = true
                0 -> {
                    map.forEach {
                        it.value.isSelected = false
                    }
                }
            }
        })
        binding.b21.setOnClickListener { userViewModel.ol2.value = 1 }
        binding.b22.setOnClickListener { userViewModel.ol2.value = 2 }
        binding.b23.setOnClickListener { userViewModel.ol2.value = 3 }
        binding.b24.setOnClickListener { userViewModel.ol2.value = 4 }
        binding.b25.setOnClickListener { userViewModel.ol2.value = 5 }
        binding.b26.setOnClickListener { userViewModel.ol2.value = 6 }
        binding.b27.setOnClickListener { userViewModel.ol2.value = 7 }
    }
    private fun setLine3(){
        val map = mapOf(
            1 to binding.b31,
            2 to binding.b32,
            3 to binding.b33,
            4 to binding.b34,
            5 to binding.b35,
            6 to binding.b36,
            7 to binding.b37
        )
        userViewModel.ol3.observe(this, Observer {
            map.forEach {
                it.value.isSelected = false
            }

            when (it){
                1 -> binding.b31.isSelected = true
                2 -> binding.b32.isSelected = true
                3 -> binding.b33.isSelected = true
                4 -> binding.b34.isSelected = true
                5 -> binding.b35.isSelected = true
                6 -> binding.b36.isSelected = true
                7 -> binding.b37.isSelected = true
                0 -> {
                    map.forEach {
                        it.value.isSelected = false
                    }
                }
            }
        })
        binding.b31.setOnClickListener { userViewModel.ol3.value = 1 }
        binding.b32.setOnClickListener { userViewModel.ol3.value = 2 }
        binding.b33.setOnClickListener { userViewModel.ol3.value = 3 }
        binding.b34.setOnClickListener { userViewModel.ol3.value = 4 }
        binding.b35.setOnClickListener { userViewModel.ol3.value = 5 }
        binding.b36.setOnClickListener { userViewModel.ol3.value = 6 }
        binding.b37.setOnClickListener { userViewModel.ol3.value = 7 }

    }
    private fun setLine4(){
        val map = mapOf(
            1 to binding.b41,
            2 to binding.b42,
            3 to binding.b43,
            4 to binding.b44,
            5 to binding.b45,
            6 to binding.b46,
            7 to binding.b47
        )
        userViewModel.ol4.observe(this, Observer {
            map.forEach {
                it.value.isSelected = false
            }

            when (it){
                1 -> binding.b41.isSelected = true
                2 -> binding.b42.isSelected = true
                3 -> binding.b43.isSelected = true
                4 -> binding.b44.isSelected = true
                5 -> binding.b45.isSelected = true
                6 -> binding.b46.isSelected = true
                7 -> binding.b47.isSelected = true
                0 -> {
                    map.forEach {
                        it.value.isSelected = false
                    }
                }
            }
        })
        binding.b41.setOnClickListener { userViewModel.ol4.value = 1 }
        binding.b42.setOnClickListener { userViewModel.ol4.value = 2 }
        binding.b43.setOnClickListener { userViewModel.ol4.value = 3 }
        binding.b44.setOnClickListener { userViewModel.ol4.value = 4 }
        binding.b45.setOnClickListener { userViewModel.ol4.value = 5 }
        binding.b46.setOnClickListener { userViewModel.ol4.value = 6 }
        binding.b47.setOnClickListener { userViewModel.ol4.value = 7 }

    }
    private fun setLine5(){
        val map = mapOf(
            1 to binding.b51,
            2 to binding.b52,
            3 to binding.b53,
            4 to binding.b54,
            5 to binding.b55,
            6 to binding.b56,
            7 to binding.b57
        )
        userViewModel.ol5.observe(this, Observer {
            map.forEach {
                it.value.isSelected = false
            }

            when (it){
                1 -> binding.b51.isSelected = true
                2 -> binding.b52.isSelected = true
                3 -> binding.b53.isSelected = true
                4 -> binding.b54.isSelected = true
                5 -> binding.b55.isSelected = true
                6 -> binding.b56.isSelected = true
                7 -> binding.b57.isSelected = true
                0 -> {
                    map.forEach {
                        it.value.isSelected = false
                    }
                }
            }
        })
        binding.b51.setOnClickListener { userViewModel.ol5.value = 1 }
        binding.b52.setOnClickListener { userViewModel.ol5.value = 2 }
        binding.b53.setOnClickListener { userViewModel.ol5.value = 3 }
        binding.b54.setOnClickListener { userViewModel.ol5.value = 4 }
        binding.b55.setOnClickListener { userViewModel.ol5.value = 5 }
        binding.b56.setOnClickListener { userViewModel.ol5.value = 6 }
        binding.b57.setOnClickListener { userViewModel.ol5.value = 7 }

    }
    private fun setLine6(){
        val map = mapOf(
            1 to binding.b61,
            2 to binding.b62,
            3 to binding.b63,
            4 to binding.b64,
            5 to binding.b65,
            6 to binding.b66,
            7 to binding.b67
        )
        userViewModel.ol6.observe(this, Observer {
            map.forEach {
                it.value.isSelected = false
            }

            when (it){
                1 -> binding.b61.isSelected = true
                2 -> binding.b62.isSelected = true
                3 -> binding.b63.isSelected = true
                4 -> binding.b64.isSelected = true
                5 -> binding.b65.isSelected = true
                6 -> binding.b66.isSelected = true
                7 -> binding.b67.isSelected = true
                0 -> {
                    map.forEach {
                        it.value.isSelected = false
                    }
                }
            }
        })
        binding.b61.setOnClickListener { userViewModel.ol6.value = 1 }
        binding.b62.setOnClickListener { userViewModel.ol6.value = 2 }
        binding.b63.setOnClickListener { userViewModel.ol6.value = 3 }
        binding.b64.setOnClickListener { userViewModel.ol6.value = 4 }
        binding.b65.setOnClickListener { userViewModel.ol6.value = 5 }
        binding.b66.setOnClickListener { userViewModel.ol6.value = 6 }
        binding.b67.setOnClickListener { userViewModel.ol6.value = 7 }


    }
    private fun setLine7(){
        val map = mapOf(
            1 to binding.b71,
            2 to binding.b72,
            3 to binding.b73,
            4 to binding.b74,
            5 to binding.b75,
            6 to binding.b76,
            7 to binding.b77
        )
        userViewModel.ol7.observe(this, Observer {
            map.forEach {
                it.value.isSelected = false
            }

            when (it){
                1 -> binding.b71.isSelected = true
                2 -> binding.b72.isSelected = true
                3 -> binding.b73.isSelected = true
                4 -> binding.b74.isSelected = true
                5 -> binding.b75.isSelected = true
                6 -> binding.b76.isSelected = true
                7 -> binding.b77.isSelected = true
                0 -> {
                    map.forEach {
                        it.value.isSelected = false
                    }
                }
            }
        })
        binding.b71.setOnClickListener { userViewModel.ol7.value = 1 }
        binding.b72.setOnClickListener { userViewModel.ol7.value = 2 }
        binding.b73.setOnClickListener { userViewModel.ol7.value = 3 }
        binding.b74.setOnClickListener { userViewModel.ol7.value = 4 }
        binding.b75.setOnClickListener { userViewModel.ol7.value = 5 }
        binding.b76.setOnClickListener { userViewModel.ol7.value = 6 }
        binding.b77.setOnClickListener { userViewModel.ol7.value = 7 }
    }
    private fun setLine8(){
        val map = mapOf(
            1 to binding.b81,
            2 to binding.b82,
            3 to binding.b83,
            4 to binding.b84,
            5 to binding.b85,
            6 to binding.b86,
            7 to binding.b87
        )
        userViewModel.ol8.observe(this, Observer {
            map.forEach {
                it.value.isSelected = false
            }

            when (it){
                1 -> binding.b81.isSelected = true
                2 -> binding.b82.isSelected = true
                3 -> binding.b83.isSelected = true
                4 -> binding.b84.isSelected = true
                5 -> binding.b85.isSelected = true
                6 -> binding.b86.isSelected = true
                7 -> binding.b87.isSelected = true
                0 -> {
                    map.forEach {
                        it.value.isSelected = false
                    }
                }
            }
        })
        binding.b81.setOnClickListener { userViewModel.ol8.value = 1 }
        binding.b82.setOnClickListener { userViewModel.ol8.value = 2 }
        binding.b83.setOnClickListener { userViewModel.ol8.value = 3 }
        binding.b84.setOnClickListener { userViewModel.ol8.value = 4 }
        binding.b85.setOnClickListener { userViewModel.ol8.value = 5 }
        binding.b86.setOnClickListener { userViewModel.ol8.value = 6 }
        binding.b87.setOnClickListener { userViewModel.ol8.value = 7 }


    }
    private fun setLine9(){
        val map = mapOf(
            1 to binding.b91,
            2 to binding.b92,
            3 to binding.b93,
            4 to binding.b94,
            5 to binding.b95,
            6 to binding.b96,
            7 to binding.b97
        )
        userViewModel.ol9.observe(this, Observer {
            map.forEach {
                it.value.isSelected = false
            }

            when (it){
                1 -> binding.b91.isSelected = true
                2 -> binding.b92.isSelected = true
                3 -> binding.b93.isSelected = true
                4 -> binding.b94.isSelected = true
                5 -> binding.b95.isSelected = true
                6 -> binding.b96.isSelected = true
                7 -> binding.b97.isSelected = true
                0 -> {
                    map.forEach {
                        it.value.isSelected = false
                    }
                }
            }
        })
        binding.b91.setOnClickListener { userViewModel.ol9.value = 1 }
        binding.b92.setOnClickListener { userViewModel.ol9.value = 2 }
        binding.b93.setOnClickListener { userViewModel.ol9.value = 3 }
        binding.b94.setOnClickListener { userViewModel.ol9.value = 4 }
        binding.b95.setOnClickListener { userViewModel.ol9.value = 5 }
        binding.b96.setOnClickListener { userViewModel.ol9.value = 6 }
        binding.b97.setOnClickListener { userViewModel.ol9.value = 7 }


    }
    private fun setLine10(){
        val map = mapOf(
            1 to binding.b101,
            2 to binding.b102,
            3 to binding.b103,
            4 to binding.b104,
            5 to binding.b105,
            6 to binding.b106,
            7 to binding.b107
        )
        userViewModel.ol10.observe(this, Observer {
            map.forEach {
                it.value.isSelected = false
            }

            when (it){
                1 -> binding.b101.isSelected = true
                2 -> binding.b102.isSelected = true
                3 -> binding.b103.isSelected = true
                4 -> binding.b104.isSelected = true
                5 -> binding.b105.isSelected = true
                6 -> binding.b106.isSelected = true
                7 -> binding.b107.isSelected = true
                0 -> {
                    map.forEach {
                        it.value.isSelected = false
                    }
                }
            }
        })
        binding.b101.setOnClickListener { userViewModel.ol10.value = 1 }
        binding.b102.setOnClickListener { userViewModel.ol10.value = 2 }
        binding.b103.setOnClickListener { userViewModel.ol10.value = 3 }
        binding.b104.setOnClickListener { userViewModel.ol10.value = 4 }
        binding.b105.setOnClickListener { userViewModel.ol10.value = 5 }
        binding.b106.setOnClickListener { userViewModel.ol10.value = 6 }
        binding.b107.setOnClickListener { userViewModel.ol10.value = 7 }


    }
    private fun setLine11(){
        val map = mapOf(
            1 to binding.b111,
            2 to binding.b112,
            3 to binding.b113,
            4 to binding.b114,
            5 to binding.b115,
            6 to binding.b116,
            7 to binding.b117
        )
        userViewModel.ol11.observe(this, Observer {
            map.forEach {
                it.value.isSelected = false
            }

            when (it){
                1 -> binding.b111.isSelected = true
                2 -> binding.b112.isSelected = true
                3 -> binding.b113.isSelected = true
                4 -> binding.b114.isSelected = true
                5 -> binding.b115.isSelected = true
                6 -> binding.b116.isSelected = true
                7 -> binding.b117.isSelected = true
                0 -> {
                    map.forEach {
                        it.value.isSelected = false
                    }
                }
            }
        })
        binding.b111.setOnClickListener { userViewModel.ol11.value = 1 }
        binding.b112.setOnClickListener { userViewModel.ol11.value = 2 }
        binding.b113.setOnClickListener { userViewModel.ol11.value = 3 }
        binding.b114.setOnClickListener { userViewModel.ol11.value = 4 }
        binding.b115.setOnClickListener { userViewModel.ol11.value = 5 }
        binding.b116.setOnClickListener { userViewModel.ol11.value = 6 }
        binding.b117.setOnClickListener { userViewModel.ol11.value = 7 }
    }
    private fun setLine12(){
        val map = mapOf(
            1 to binding.b121,
            2 to binding.b122,
            3 to binding.b123,
            4 to binding.b124,
            5 to binding.b125,
            6 to binding.b126,
            7 to binding.b127
        )
        userViewModel.ol12.observe(this, Observer {
            map.forEach {
                it.value.isSelected = false
            }

            when (it){
                1 -> binding.b121.isSelected = true
                2 -> binding.b122.isSelected = true
                3 -> binding.b123.isSelected = true
                4 -> binding.b124.isSelected = true
                5 -> binding.b125.isSelected = true
                6 -> binding.b126.isSelected = true
                7 -> binding.b127.isSelected = true
                0 -> {
                    map.forEach {
                        it.value.isSelected = false
                    }
                }
            }
        })
        binding.b121.setOnClickListener { userViewModel.ol12.value = 1 }
        binding.b122.setOnClickListener { userViewModel.ol12.value = 2 }
        binding.b123.setOnClickListener { userViewModel.ol12.value = 3 }
        binding.b124.setOnClickListener { userViewModel.ol12.value = 4 }
        binding.b125.setOnClickListener { userViewModel.ol12.value = 5 }
        binding.b126.setOnClickListener { userViewModel.ol12.value = 6 }
        binding.b127.setOnClickListener { userViewModel.ol12.value = 7 }


    }
    private fun setLine13(){
        val map = mapOf(
            1 to binding.b131,
            2 to binding.b132,
            3 to binding.b133,
            4 to binding.b134,
            5 to binding.b135,
            6 to binding.b136,
            7 to binding.b137
        )
        userViewModel.ol13.observe(this, Observer {
            map.forEach {
                it.value.isSelected = false
            }

            when (it){
                1 -> binding.b131.isSelected = true
                2 -> binding.b132.isSelected = true
                3 -> binding.b133.isSelected = true
                4 -> binding.b134.isSelected = true
                5 -> binding.b135.isSelected = true
                6 -> binding.b136.isSelected = true
                7 -> binding.b137.isSelected = true
                0 -> {
                    map.forEach {
                        it.value.isSelected = false
                    }
                }
            }
        })
        binding.b131.setOnClickListener { userViewModel.ol13.value = 1 }
        binding.b132.setOnClickListener { userViewModel.ol13.value = 2 }
        binding.b133.setOnClickListener { userViewModel.ol13.value = 3 }
        binding.b134.setOnClickListener { userViewModel.ol13.value = 4 }
        binding.b135.setOnClickListener { userViewModel.ol13.value = 5 }
        binding.b136.setOnClickListener { userViewModel.ol13.value = 6 }
        binding.b137.setOnClickListener { userViewModel.ol13.value = 7 }


    }
    private fun setLine14(){
        val map = mapOf(
            1 to binding.b141,
            2 to binding.b142,
            3 to binding.b143,
            4 to binding.b144,
            5 to binding.b145,
            6 to binding.b146,
            7 to binding.b147
        )
        userViewModel.ol14.observe(this, Observer {
            map.forEach {
                it.value.isSelected = false
            }

            when (it){
                1 -> binding.b141.isSelected = true
                2 -> binding.b142.isSelected = true
                3 -> binding.b143.isSelected = true
                4 -> binding.b144.isSelected = true
                5 -> binding.b145.isSelected = true
                6 -> binding.b146.isSelected = true
                7 -> binding.b147.isSelected = true
                0 -> {
                    map.forEach {
                        it.value.isSelected = false
                    }
                }
            }
        })
        binding.b141.setOnClickListener { userViewModel.ol14.value = 1 }
        binding.b142.setOnClickListener { userViewModel.ol14.value = 2 }
        binding.b143.setOnClickListener { userViewModel.ol14.value = 3 }
        binding.b144.setOnClickListener { userViewModel.ol14.value = 4 }
        binding.b145.setOnClickListener { userViewModel.ol14.value = 5 }
        binding.b146.setOnClickListener { userViewModel.ol14.value = 6 }
        binding.b147.setOnClickListener { userViewModel.ol14.value = 7 }


    }
    private fun setLine15(){
        val map = mapOf(
            1 to binding.b151,
            2 to binding.b152,
            3 to binding.b153,
            4 to binding.b154,
            5 to binding.b155,
            6 to binding.b156,
            7 to binding.b157
        )
        userViewModel.ol15.observe(this, Observer {
            map.forEach {
                it.value.isSelected = false
            }

            when (it){
                1 -> binding.b151.isSelected = true
                2 -> binding.b152.isSelected = true
                3 -> binding.b153.isSelected = true
                4 -> binding.b154.isSelected = true
                5 -> binding.b155.isSelected = true
                6 -> binding.b156.isSelected = true
                7 -> binding.b157.isSelected = true
                0 -> {
                    map.forEach {
                        it.value.isSelected = false
                    }
                }
            }
        })
        binding.b151.setOnClickListener { userViewModel.ol15.value = 1 }
        binding.b152.setOnClickListener { userViewModel.ol15.value = 2 }
        binding.b153.setOnClickListener { userViewModel.ol15.value = 3 }
        binding.b154.setOnClickListener { userViewModel.ol15.value = 4 }
        binding.b155.setOnClickListener { userViewModel.ol15.value = 5 }
        binding.b156.setOnClickListener { userViewModel.ol15.value = 6 }
        binding.b157.setOnClickListener { userViewModel.ol15.value = 7 }


    }
    private fun setLine16(){
        val map = mapOf(
            1 to binding.b161,
            2 to binding.b162,
            3 to binding.b163,
            4 to binding.b164,
            5 to binding.b165,
            6 to binding.b166,
            7 to binding.b167
        )
        userViewModel.ol16.observe(this, Observer {
            map.forEach {
                it.value.isSelected = false
            }

            when (it){
                1 -> binding.b161.isSelected = true
                2 -> binding.b162.isSelected = true
                3 -> binding.b163.isSelected = true
                4 -> binding.b164.isSelected = true
                5 -> binding.b165.isSelected = true
                6 -> binding.b166.isSelected = true
                7 -> binding.b167.isSelected = true
                0 -> {
                    map.forEach {
                        it.value.isSelected = false
                    }
                }
            }
        })
        binding.b161.setOnClickListener { userViewModel.ol16.value = 1 }
        binding.b162.setOnClickListener { userViewModel.ol16.value = 2 }
        binding.b163.setOnClickListener { userViewModel.ol16.value = 3 }
        binding.b164.setOnClickListener { userViewModel.ol16.value = 4 }
        binding.b165.setOnClickListener { userViewModel.ol16.value = 5 }
        binding.b166.setOnClickListener { userViewModel.ol16.value = 6 }
        binding.b167.setOnClickListener { userViewModel.ol16.value = 7 }


    }
    private fun setLine17(){
        val map = mapOf(
            1 to binding.b171,
            2 to binding.b172,
            3 to binding.b173,
            4 to binding.b174,
            5 to binding.b175,
            6 to binding.b176,
            7 to binding.b177
        )
        userViewModel.ol17.observe(this, Observer {
            map.forEach {
                it.value.isSelected = false
            }

            when (it){
                1 -> binding.b171.isSelected = true
                2 -> binding.b172.isSelected = true
                3 -> binding.b173.isSelected = true
                4 -> binding.b174.isSelected = true
                5 -> binding.b175.isSelected = true
                6 -> binding.b176.isSelected = true
                7 -> binding.b177.isSelected = true
                0 -> {
                    map.forEach {
                        it.value.isSelected = false
                    }
                }
            }
        })
        binding.b171.setOnClickListener { userViewModel.ol17.value = 1 }
        binding.b172.setOnClickListener { userViewModel.ol17.value = 2 }
        binding.b173.setOnClickListener { userViewModel.ol17.value = 3 }
        binding.b174.setOnClickListener { userViewModel.ol17.value = 4 }
        binding.b175.setOnClickListener { userViewModel.ol17.value = 5 }
        binding.b176.setOnClickListener { userViewModel.ol17.value = 6 }
        binding.b177.setOnClickListener { userViewModel.ol17.value = 7 }


    }
    private fun setLine18(){
        val map = mapOf(
            1 to binding.b181,
            2 to binding.b182,
            3 to binding.b183,
            4 to binding.b184,
            5 to binding.b185,
            6 to binding.b186,
            7 to binding.b187
        )
        userViewModel.ol18.observe(this, Observer {
            map.forEach {
                it.value.isSelected = false
            }

            when (it){
                1 -> binding.b181.isSelected = true
                2 -> binding.b182.isSelected = true
                3 -> binding.b183.isSelected = true
                4 -> binding.b184.isSelected = true
                5 -> binding.b185.isSelected = true
                6 -> binding.b186.isSelected = true
                7 -> binding.b187.isSelected = true
                0 -> {
                    map.forEach {
                        it.value.isSelected = false
                    }
                }
            }
        })
        binding.b181.setOnClickListener { userViewModel.ol18.value = 1 }
        binding.b182.setOnClickListener { userViewModel.ol18.value = 2 }
        binding.b183.setOnClickListener { userViewModel.ol18.value = 3 }
        binding.b184.setOnClickListener { userViewModel.ol18.value = 4 }
        binding.b185.setOnClickListener { userViewModel.ol18.value = 5 }
        binding.b186.setOnClickListener { userViewModel.ol18.value = 6 }
        binding.b187.setOnClickListener { userViewModel.ol18.value = 7 }


    }




    private fun initViewModel(){
        userViewModel.ol1.value = 0
        userViewModel.ol2.value = 0
        userViewModel.ol3.value = 0
        userViewModel.ol4.value = 0
        userViewModel.ol5.value = 0
        userViewModel.ol6.value = 0
        userViewModel.ol7.value = 0
        userViewModel.ol8.value = 0
        userViewModel.ol9.value = 0
        userViewModel.ol10.value = 0
        userViewModel.ol11.value = 0
        userViewModel.ol12.value = 0
        userViewModel.ol13.value = 0
        userViewModel.ol14.value = 0
        userViewModel.ol15.value = 0
        userViewModel.ol16.value = 0
        userViewModel.ol17.value = 0
        userViewModel.ol18.value = 0
    }




}