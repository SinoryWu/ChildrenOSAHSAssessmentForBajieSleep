package com.hzdq.bajiesleepchildrenHD.user.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.ActivityUserScreenAdd2Binding
import com.hzdq.bajiesleepchildrenHD.dataclass.DataClassQuestionResult
import com.hzdq.bajiesleepchildrenHD.dataclass.OSAResultBody
import com.hzdq.bajiesleepchildrenHD.dataclass.PSQResultBody
import com.hzdq.bajiesleepchildrenHD.login.activity.LoginActivity
import com.hzdq.bajiesleepchildrenHD.retrofit.RetrofitSingleton
import com.hzdq.bajiesleepchildrenHD.user.viewmodel.UserViewModel
import com.hzdq.bajiesleepchildrenHD.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserScreenAdd2Activity : AppCompatActivity() {
    private lateinit var binding:ActivityUserScreenAdd2Binding
    private lateinit var userViewModel:UserViewModel
    private lateinit var psqResultBody: PSQResultBody
    private lateinit var shp: Shp
    private var tokenDialog: TokenDialog? = null
    val mutableList:ArrayList<Int> = ArrayList<Int>()
    var evaluate = 0
    var addScreen = 0
    var uid = 0

    var name = ""
    private var screen = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HideUI(this).hideSystemUI()
        binding = DataBindingUtil.setContentView(this,R.layout.activity_user_screen_add2)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        shp = Shp(this)
        initViewModel()
        ActivityCollector2.addActivity(this)
        val intent = intent
        evaluate = intent.getIntExtra("evaluate",0)
        addScreen = intent.getIntExtra("addScreen",0)
        screen = intent.getIntExtra("screen",0)
        if (evaluate == 1){

            val uid = intent.getIntExtra("uid",0)
            val name = intent.getStringExtra("name")
            userViewModel.uid.value = uid
//            if (uid != null) {
//                psqResultBody.uid = uid
//
//            }
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
        val retrofit = RetrofitSingleton.getInstance(this)
        psqResultBody = PSQResultBody()
        binding.back.setOnClickListener {
            finish()
        }
        binding.addPSQCancel.setOnClickListener {
            finish()
        }
        binding.questionDoctor.text = "管理医生：${shp.getDoctorName()}"
        binding.questionChoiceNameButton.setOnClickListener {
            startActivityForResult(Intent(this,UserScreenChoiceUserActivity::class.java),1)
        }

        val map = mapOf(
            1 to userViewModel.pl1,
            2 to userViewModel.pl2,
            3 to userViewModel.pl3,
            4 to userViewModel.pl4,
            5 to userViewModel.pl5,
            6 to userViewModel.pl6,
            7 to userViewModel.pl7,
            8 to userViewModel.pl8,
            9 to userViewModel.pl9,
            10 to userViewModel.pl10,
            11 to userViewModel.pl11,
            12 to userViewModel.pl12,
            13 to userViewModel.pl13,
            14 to userViewModel.pl14,
            15 to userViewModel.pl15,
            16 to userViewModel.pl16,
            17 to userViewModel.pl17,
            18 to userViewModel.pl18,
            19 to userViewModel.pl19,
            20 to userViewModel.pl20,
            21 to userViewModel.pl21,
            22 to userViewModel.pl22
        )

        val shp = Shp(this)
        psqResultBody.taskId = 0
        psqResultBody.type = 13
        psqResultBody.userInfo = ""
        psqResultBody.hospitalId = shp.getHospitalId()!!
        psqResultBody.uid = 0

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
        setLine19()
        setLine20()
        setLine21()
        setLine22()

        binding.addPSQConfirm.setOnClickListener {

            map.forEach {
                if (it.value.value == 0){
                    ToastUtils.showTextToast(this,"问题不能为空")
                    return@setOnClickListener
                }
            }
            val content = "{" +
                    "\"127\":${userViewModel.pl1.value?.minus(1)}," +
                    "\"128\":${userViewModel.pl2.value?.minus(1)}," +
                    "\"129\":${userViewModel.pl3.value?.minus(1)}," +
                    "\"130\":${userViewModel.pl4.value?.minus(1)}," +
                    "\"131\":${userViewModel.pl5.value?.minus(1)}," +
                    "\"132\":${userViewModel.pl6.value?.minus(1)}," +
                    "\"133\":${userViewModel.pl7.value?.minus(1)}," +
                    "\"134\":${userViewModel.pl8.value?.minus(1)}," +
                    "\"135\":${userViewModel.pl9.value?.minus(1)}," +
                    "\"136\":${userViewModel.pl10.value?.minus(1)}," +
                    "\"137\":${userViewModel.pl11.value?.minus(1)}," +
                    "\"138\":${userViewModel.pl12.value?.minus(1)}," +
                    "\"139\":${userViewModel.pl13.value?.minus(1)}," +
                    "\"140\":${userViewModel.pl14.value?.minus(1)}," +
                    "\"141\":${userViewModel.pl15.value?.minus(1)}," +
                    "\"142\":${userViewModel.pl16.value?.minus(1)}," +
                    "\"143\":${userViewModel.pl17.value?.minus(1)}," +
                    "\"144\":${userViewModel.pl18.value?.minus(1)}," +
                    "\"145\":${userViewModel.pl19.value?.minus(1)}," +
                    "\"146\":${userViewModel.pl20.value?.minus(1)}," +
                    "\"147\":${userViewModel.pl21.value?.minus(1)}," +
                    "\"148\":${userViewModel.pl22.value?.minus(1)}}"
            psqResultBody.content = content
            psqResultBody.hospitalId = shp.getHospitalId()!!
            psqResultBody.uid = userViewModel.uid.value!!
            psqResultBody.type = 13
            map.forEach {
                mutableList.add(it.value.value!! -1)
            }


            postSubmitResult(retrofit,psqResultBody)
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
                        psqResultBody.userInfo ="{$userInfo}"
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
    private fun postSubmitResult(retrofit: RetrofitSingleton, psqResultBody: PSQResultBody){
        retrofit.api().postPSQSubmitResult(psqResultBody).enqueue(object :
            Callback<DataClassQuestionResult> {
            override fun onResponse(
                call: Call<DataClassQuestionResult>,
                response: Response<DataClassQuestionResult>
            ) {

                if (response.body()?.code == 0){
                    val measures= response.body()?.data?.measures
                    val total= response.body()?.data?.total
                    val result= response.body()?.data?.result
                    val createTime= response.body()?.data?.createTime
                    val task_id = response.body()?.data?.id
                    val intent = Intent(this@UserScreenAdd2Activity,CheckPSQ_Activity::class.java)
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
                        intent.putExtra("taskid",response.body()?.data?.id)

                        if (evaluate == 1){

                        }else {
                            startActivity(intent)
                        }

                    }
                    if (evaluate == 1 ){

                        intent.putExtra("score",total)
                        intent.putExtra("result",result)
                        intent.putExtra("id",task_id)
                        intent.putExtra("name","PSQ")
                        setResult(RESULT_OK,intent)
                        ActivityCollector2.removeActivity(this@UserScreenAdd2Activity)
                        finish()
                    }else if (addScreen == 1){
                        val intent = Intent()
                        intent.putExtra("score",total)
                        intent.putExtra("result",result)
                        intent.putExtra("id",task_id)
                        intent.putExtra("name","PSQ")
                        setResult(RESULT_OK,intent)
                        ActivityCollector2.removeActivity(this@UserScreenAdd2Activity)
                        finish()
                    }else if (screen == 1) {
                        setResult(RESULT_OK)
                        ActivityCollector2.removeActivity(this@UserScreenAdd2Activity)
                        finish()
                    } else {
                        ActivityCollector2.removeActivity(this@UserScreenAdd2Activity)
                        finish()
                    }


                }else if (response.body()?.code == 10010 || response.body()?.code == 10004) {
                    if (tokenDialog == null) {
                        tokenDialog = TokenDialog(
                            this@UserScreenAdd2Activity,
                            object : TokenDialog.ConfirmAction {
                                override fun onRightClick() {
                                    shp.saveToSp("token", "")
                                    shp.saveToSp("uid", "")
                                    startActivity(
                                        Intent(
                                            this@UserScreenAdd2Activity,
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
                } else {
                    ToastUtils.showTextToast(this@UserScreenAdd2Activity,"${response.body()?.msg}")
                }
            }

            override fun onFailure(call: Call<DataClassQuestionResult>, t: Throwable) {
                ToastUtils.showTextToast(this@UserScreenAdd2Activity,"提交结果网络请求失败")
            }

        })
    }

    private fun setLine1(){
        val map = mapOf(
            1 to binding.b11,
            2 to binding.b12,
        )

        userViewModel.pl1.observe(this, Observer {
            map.forEach{
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                2-> {
                    binding.b11.isSelected = true
                    binding.b11.setTextColor(Color.parseColor("#FFFFFF"))
                }
                1-> {
                    binding.b12.isSelected = true
                    binding.b12.setTextColor(Color.parseColor("#FFFFFF"))
                }
                else -> {
                    map.forEach{
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }
        })

        binding.b11.setOnClickListener {
            userViewModel.pl1.value = 2
        }
        binding.b12.setOnClickListener { userViewModel.pl1.value = 1}
    }

    private fun setLine2(){
        val map = mapOf(
            1 to binding.b21,
            2 to binding.b22,
        )

        userViewModel.pl2.observe(this, Observer {
            map.forEach{
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                2-> {
                    binding.b21.isSelected = true
                    binding.b21.setTextColor(Color.parseColor("#FFFFFF"))
                }
                1-> {
                    binding.b22.isSelected = true
                    binding.b22.setTextColor(Color.parseColor("#FFFFFF"))
                }
                else -> {
                    map.forEach{
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }
        })

        binding.b21.setOnClickListener {
            userViewModel.pl2.value = 2
        }
        binding.b22.setOnClickListener { userViewModel.pl2.value = 1 }
    }
    private fun setLine3(){
        val map = mapOf(
            1 to binding.b31,
            2 to binding.b32,
        )

        userViewModel.pl3.observe(this, Observer {
            map.forEach{
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                2-> {
                    binding.b31.isSelected = true
                    binding.b31.setTextColor(Color.parseColor("#FFFFFF"))
                }
                1-> {
                    binding.b32.isSelected = true
                    binding.b32.setTextColor(Color.parseColor("#FFFFFF"))
                }
                else -> {
                    map.forEach{
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }
        })

        binding.b31.setOnClickListener {
            userViewModel.pl3.value = 2
        }
        binding.b32.setOnClickListener { userViewModel.pl3.value = 1 }
    }

    private fun setLine4(){
        val map = mapOf(
            1 to binding.b41,
            2 to binding.b42,
        )

        userViewModel.pl4.observe(this, Observer {
            map.forEach{
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                2-> {
                    binding.b41.isSelected = true
                    binding.b41.setTextColor(Color.parseColor("#FFFFFF"))
                }
                1-> {
                    binding.b42.isSelected = true
                    binding.b42.setTextColor(Color.parseColor("#FFFFFF"))
                }
                else -> {
                    map.forEach{
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }
        })

        binding.b41.setOnClickListener {
            userViewModel.pl4.value = 2
        }
        binding.b42.setOnClickListener { userViewModel.pl4.value = 1 }
    }

    private fun setLine5(){
        val map = mapOf(
            1 to binding.b51,
            2 to binding.b52,
        )

        userViewModel.pl5.observe(this, Observer {
            map.forEach{
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                2-> {
                    binding.b51.isSelected = true
                    binding.b51.setTextColor(Color.parseColor("#FFFFFF"))
                }
                1-> {
                    binding.b52.isSelected = true
                    binding.b52.setTextColor(Color.parseColor("#FFFFFF"))
                }
                else -> {
                    map.forEach{
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }
        })

        binding.b51.setOnClickListener {
            userViewModel.pl5.value = 2
        }
        binding.b52.setOnClickListener { userViewModel.pl5.value = 1 }
    }


    private fun setLine6(){
        val map = mapOf(
            1 to binding.b61,
            2 to binding.b62,
        )

        userViewModel.pl6.observe(this, Observer {
            map.forEach{
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                2-> {
                    binding.b61.isSelected = true
                    binding.b61.setTextColor(Color.parseColor("#FFFFFF"))
                }
                1-> {
                    binding.b62.isSelected = true
                    binding.b62.setTextColor(Color.parseColor("#FFFFFF"))
                }
                else -> {
                    map.forEach{
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }
        })

        binding.b61.setOnClickListener {
            userViewModel.pl6.value = 2
        }
        binding.b62.setOnClickListener { userViewModel.pl6.value = 1 }
    }


    private fun setLine7(){
        val map = mapOf(
            1 to binding.b71,
            2 to binding.b72,
        )

        userViewModel.pl7.observe(this, Observer {
            map.forEach{
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                2-> {
                    binding.b71.isSelected = true
                    binding.b71.setTextColor(Color.parseColor("#FFFFFF"))
                }
                1-> {
                    binding.b72.isSelected = true
                    binding.b72.setTextColor(Color.parseColor("#FFFFFF"))
                }
                else -> {
                    map.forEach{
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }
        })

        binding.b71.setOnClickListener {
            userViewModel.pl7.value = 2
        }
        binding.b72.setOnClickListener { userViewModel.pl7.value = 1 }
    }

    private fun setLine8(){
        val map = mapOf(
            1 to binding.b81,
            2 to binding.b82,
        )

        userViewModel.pl8.observe(this, Observer {
            map.forEach{
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                2-> {
                    binding.b81.isSelected = true
                    binding.b81.setTextColor(Color.parseColor("#FFFFFF"))
                }
                1-> {
                    binding.b82.isSelected = true
                    binding.b82.setTextColor(Color.parseColor("#FFFFFF"))
                }
                else -> {
                    map.forEach{
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }
        })

        binding.b81.setOnClickListener {
            userViewModel.pl8.value = 2
        }
        binding.b82.setOnClickListener { userViewModel.pl8.value = 1 }
    }

    private fun setLine9(){
        val map = mapOf(
            1 to binding.b91,
            2 to binding.b92,
        )

        userViewModel.pl9.observe(this, Observer {
            map.forEach{
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                2-> {
                    binding.b91.isSelected = true
                    binding.b91.setTextColor(Color.parseColor("#FFFFFF"))
                }
                1-> {
                    binding.b92.isSelected = true
                    binding.b92.setTextColor(Color.parseColor("#FFFFFF"))
                }
                else -> {
                    map.forEach{
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }
        })

        binding.b91.setOnClickListener {
            userViewModel.pl9.value = 2
        }
        binding.b92.setOnClickListener { userViewModel.pl9.value = 1 }
    }

    private fun setLine10(){
        val map = mapOf(
            1 to binding.b101,
            2 to binding.b102,
        )

        userViewModel.pl10.observe(this, Observer {
            map.forEach{
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                2-> {
                    binding.b101.isSelected = true
                    binding.b101.setTextColor(Color.parseColor("#FFFFFF"))
                }
                1-> {
                    binding.b102.isSelected = true
                    binding.b102.setTextColor(Color.parseColor("#FFFFFF"))
                }
                else -> {
                    map.forEach{
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }
        })

        binding.b101.setOnClickListener {
            userViewModel.pl10.value = 2
        }
        binding.b102.setOnClickListener { userViewModel.pl10.value = 1 }
    }

    private fun setLine11(){
        val map = mapOf(
            1 to binding.b111,
            2 to binding.b112,
        )

        userViewModel.pl11.observe(this, Observer {
            map.forEach{
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                2-> {
                    binding.b111.isSelected = true
                    binding.b111.setTextColor(Color.parseColor("#FFFFFF"))
                }
                1-> {
                    binding.b112.isSelected = true
                    binding.b112.setTextColor(Color.parseColor("#FFFFFF"))
                }
                else -> {
                    map.forEach{
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }
        })

        binding.b111.setOnClickListener {
            userViewModel.pl11.value = 2
        }
        binding.b112.setOnClickListener { userViewModel.pl11.value = 1 }
    }

    private fun setLine12(){
        val map = mapOf(
            1 to binding.b121,
            2 to binding.b122,
        )

        userViewModel.pl12.observe(this, Observer {
            map.forEach{
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                2-> {
                    binding.b121.isSelected = true
                    binding.b121.setTextColor(Color.parseColor("#FFFFFF"))
                }
                1-> {
                    binding.b122.isSelected = true
                    binding.b122.setTextColor(Color.parseColor("#FFFFFF"))
                }
                else -> {
                    map.forEach{
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }
        })

        binding.b121.setOnClickListener {
            userViewModel.pl12.value = 2
        }
        binding.b122.setOnClickListener { userViewModel.pl12.value = 1 }
    }

    private fun setLine13(){
        val map = mapOf(
            1 to binding.b131,
            2 to binding.b132,
        )

        userViewModel.pl13.observe(this, Observer {
            map.forEach{
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                2-> {
                    binding.b131.isSelected = true
                    binding.b131.setTextColor(Color.parseColor("#FFFFFF"))
                }
                1-> {
                    binding.b132.isSelected = true
                    binding.b132.setTextColor(Color.parseColor("#FFFFFF"))
                }
                else -> {
                    map.forEach{
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }
        })

        binding.b131.setOnClickListener {
            userViewModel.pl13.value = 2
        }
        binding.b132.setOnClickListener { userViewModel.pl13.value = 1 }
    }

    private fun setLine14(){
        val map = mapOf(
            1 to binding.b141,
            2 to binding.b142,
        )

        userViewModel.pl14.observe(this, Observer {
            map.forEach{
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                2-> {
                    binding.b141.isSelected = true
                    binding.b141.setTextColor(Color.parseColor("#FFFFFF"))
                }
                1-> {
                    binding.b142.isSelected = true
                    binding.b142.setTextColor(Color.parseColor("#FFFFFF"))
                }
                else -> {
                    map.forEach{
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }
        })

        binding.b141.setOnClickListener {
            userViewModel.pl14.value = 2
        }
        binding.b142.setOnClickListener { userViewModel.pl14.value = 1 }
    }

    private fun setLine15(){
        val map = mapOf(
            1 to binding.b151,
            2 to binding.b152,
        )

        userViewModel.pl15.observe(this, Observer {
            map.forEach{
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                2-> {
                    binding.b151.isSelected = true
                    binding.b151.setTextColor(Color.parseColor("#FFFFFF"))
                }
                1-> {
                    binding.b152.isSelected = true
                    binding.b152.setTextColor(Color.parseColor("#FFFFFF"))
                }
                else -> {
                    map.forEach{
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }
        })

        binding.b151.setOnClickListener {
            userViewModel.pl15.value = 2
        }
        binding.b152.setOnClickListener { userViewModel.pl15.value = 1 }
    }

    private fun setLine16(){
        val map = mapOf(
            1 to binding.b161,
            2 to binding.b162,
        )

        userViewModel.pl16.observe(this, Observer {
            map.forEach{
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                2-> {
                    binding.b161.isSelected = true
                    binding.b161.setTextColor(Color.parseColor("#FFFFFF"))
                }
                1-> {
                    binding.b162.isSelected = true
                    binding.b162.setTextColor(Color.parseColor("#FFFFFF"))
                }
                else -> {
                    map.forEach{
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }
        })

        binding.b161.setOnClickListener {
            userViewModel.pl16.value = 2
        }
        binding.b162.setOnClickListener { userViewModel.pl16.value = 1 }
    }

    private fun setLine17(){
        val map = mapOf(
            1 to binding.b171,
            2 to binding.b172,
        )

        userViewModel.pl17.observe(this, Observer {
            map.forEach{
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                2-> {
                    binding.b171.isSelected = true
                    binding.b171.setTextColor(Color.parseColor("#FFFFFF"))
                }
                1-> {
                    binding.b172.isSelected = true
                    binding.b172.setTextColor(Color.parseColor("#FFFFFF"))
                }
                else -> {
                    map.forEach{
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }
        })

        binding.b171.setOnClickListener {
            userViewModel.pl17.value = 2
        }
        binding.b172.setOnClickListener { userViewModel.pl17.value = 1 }
    }

    private fun setLine18(){
        val map = mapOf(
            1 to binding.b181,
            2 to binding.b182,
        )

        userViewModel.pl18.observe(this, Observer {
            map.forEach{
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                2-> {
                    binding.b181.isSelected = true
                    binding.b181.setTextColor(Color.parseColor("#FFFFFF"))
                }
                1-> {
                    binding.b182.isSelected = true
                    binding.b182.setTextColor(Color.parseColor("#FFFFFF"))
                }
                else -> {
                    map.forEach{
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }
        })

        binding.b181.setOnClickListener {
            userViewModel.pl18.value = 2
        }
        binding.b182.setOnClickListener { userViewModel.pl18.value = 1 }
    }


    private fun setLine19(){
        val map = mapOf(
            1 to binding.b191,
            2 to binding.b192,
        )

        userViewModel.pl19.observe(this, Observer {
            map.forEach{
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                2-> {
                    binding.b191.isSelected = true
                    binding.b191.setTextColor(Color.parseColor("#FFFFFF"))
                }
                1-> {
                    binding.b192.isSelected = true
                    binding.b192.setTextColor(Color.parseColor("#FFFFFF"))
                }
                else -> {
                    map.forEach{
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }
        })

        binding.b191.setOnClickListener {
            userViewModel.pl19.value = 2
        }
        binding.b192.setOnClickListener { userViewModel.pl19.value = 1 }
    }


    private fun setLine20(){
        val map = mapOf(
            1 to binding.b201,
            2 to binding.b202,
        )

        userViewModel.pl20.observe(this, Observer {
            map.forEach{
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                2-> {
                    binding.b201.isSelected = true
                    binding.b201.setTextColor(Color.parseColor("#FFFFFF"))
                }
                1-> {
                    binding.b202.isSelected = true
                    binding.b202.setTextColor(Color.parseColor("#FFFFFF"))
                }
                else -> {
                    map.forEach{
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }
        })

        binding.b201.setOnClickListener {
            userViewModel.pl20.value = 2
        }
        binding.b202.setOnClickListener { userViewModel.pl20.value = 1 }
    }

    private fun setLine21(){
        val map = mapOf(
            1 to binding.b211,
            2 to binding.b212,
        )

        userViewModel.pl21.observe(this, Observer {
            map.forEach{
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                2-> {
                    binding.b211.isSelected = true
                    binding.b211.setTextColor(Color.parseColor("#FFFFFF"))
                }
                1-> {
                    binding.b212.isSelected = true
                    binding.b212.setTextColor(Color.parseColor("#FFFFFF"))
                }
                else -> {
                    map.forEach{
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }
        })

        binding.b211.setOnClickListener {
            userViewModel.pl21.value = 2
        }
        binding.b212.setOnClickListener { userViewModel.pl21.value = 1 }
    }

    private fun setLine22(){
        val map = mapOf(
            1 to binding.b221,
            2 to binding.b222,
        )

        userViewModel.pl22.observe(this, Observer {
            map.forEach{
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                2-> {
                    binding.b221.isSelected = true
                    binding.b221.setTextColor(Color.parseColor("#FFFFFF"))
                }
                1-> {
                    binding.b222.isSelected = true
                    binding.b222.setTextColor(Color.parseColor("#FFFFFF"))
                }
                else -> {
                    map.forEach{
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }
        })

        binding.b221.setOnClickListener {
            userViewModel.pl22.value = 2
        }
        binding.b222.setOnClickListener { userViewModel.pl22.value = 1 }
    }





















    private fun initViewModel(){
        userViewModel.pl1.value = 0
        userViewModel.pl2.value = 0
        userViewModel.pl3.value = 0
        userViewModel.pl4.value = 0
        userViewModel.pl5.value = 0
        userViewModel.pl6.value = 0
        userViewModel.pl7.value = 0
        userViewModel.pl8.value = 0
        userViewModel.pl9.value = 0
        userViewModel.pl10.value = 0
        userViewModel.pl11.value = 0
        userViewModel.pl12.value = 0
        userViewModel.pl13.value = 0
        userViewModel.pl14.value = 0
        userViewModel.pl15.value = 0
        userViewModel.pl16.value = 0
        userViewModel.pl17.value = 0
        userViewModel.pl18.value = 0
    }

}