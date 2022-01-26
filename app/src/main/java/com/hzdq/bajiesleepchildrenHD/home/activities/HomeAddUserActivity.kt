package com.hzdq.bajiesleepchildrenHD.home.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.ActivityHomeAddUserBinding
import com.hzdq.bajiesleepchildrenHD.dataclass.*
import com.hzdq.bajiesleepchildrenHD.home.viewmodel.HomeAddUserViewModel
import com.hzdq.bajiesleepchildrenHD.login.activity.LoginActivity
import com.hzdq.bajiesleepchildrenHD.retrofit.OkhttpSingleton
import com.hzdq.bajiesleepchildrenHD.retrofit.RetrofitSingleton
import com.hzdq.bajiesleepchildrenHD.utils.*
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.ArrayList
import java.util.HashMap

class HomeAddUserActivity : AppCompatActivity() {
    private lateinit var binding:ActivityHomeAddUserBinding
    private lateinit var homeAddUserViewModel: HomeAddUserViewModel
    private var tokenDialog: TokenDialog? = null
    private lateinit var shp: Shp
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HideUI(this).hideSystemUI()
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home_add_user)
        val retrofit = RetrofitSingleton.getInstance(this)
        homeAddUserViewModel = ViewModelProvider(this).get(HomeAddUserViewModel::class.java)
        ActivityCollector2.addActivity(this)
        val hospitalid = Shp(this).getHospitalId()

        shp = Shp(this)
        val map = mapOf(
            1 to homeAddUserViewModel.truename,
            2 to homeAddUserViewModel.sex,
            3 to homeAddUserViewModel.age,
            4 to homeAddUserViewModel.height,
            5 to homeAddUserViewModel.mobile,
            6 to homeAddUserViewModel.needfollow
        )
        
        binding.cancel.setOnClickListener {

            finish()
        }



        binding.mzh.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
               homeAddUserViewModel.mzh.value = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.phoneNumber.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                homeAddUserViewModel.mobile.value = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })


        binding.name.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                homeAddUserViewModel.truename.value = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        val mapButton2 = mapOf(
            1 to binding.b21,
            2 to binding.b22,
        )

        homeAddUserViewModel.sex.observe(this, Observer {
            mapButton2.forEach {
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                "1" -> {
                    binding.b21.isSelected =true
                    binding.b21.setTextColor(Color.parseColor("#FFFFFF"))
                }
                "2" -> {
                    binding.b22.isSelected =true
                    binding.b22.setTextColor(Color.parseColor("#FFFFFF"))
                }
                else -> {
                    mapButton2.forEach {
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }
        })

        binding.b21.setOnClickListener { homeAddUserViewModel.sex.value = "1" }
        binding.b22.setOnClickListener { homeAddUserViewModel.sex.value = "2" }

        binding.age.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
               homeAddUserViewModel.age.value = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.height.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                homeAddUserViewModel.height.value = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.weight.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                homeAddUserViewModel.weight.value = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        val mapButton3 = mapOf(
            1 to binding.b31,
            2 to binding.b32,
        )

        homeAddUserViewModel.needfollow.observe(this, Observer {
            mapButton3.forEach {
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                "1" -> {
                    binding.b31.isSelected = true
                    binding.b31.setTextColor(Color.parseColor("#FFFFFF"))
                }
                "0" -> {
                    binding.b32.isSelected = true
                    binding.b32.setTextColor(Color.parseColor("#FFFFFF"))
                }
            }
        })

        binding.b31.setOnClickListener { homeAddUserViewModel.needfollow.value = "1" }
        binding.b32.setOnClickListener { homeAddUserViewModel.needfollow.value = "0" }

        binding.back.setOnClickListener {

            finish()
        }


        binding.confirm.setOnClickListener {

//            val intent = Intent(this@HomeAddUserActivity,HomeAdduserCompleteActivity::class.java)
//            intent.putExtra("uid",123)
//            intent.putExtra("name","name")
//            setResult(RESULT_OK)
//            finish()
//            startActivityForResult(intent,2)
            map.forEach {
                if (it.value.value.equals("")){
                    ToastUtils.showTextToast(this,"*号为必填选项")
                    return@setOnClickListener
                }
            }
            if (PhoneFormatCheckUtils.isPhoneLegal(binding.phoneNumber.text.toString()) == false){
                ToastUtils.showTextToast(this,"请输入正确手机号")
                return@setOnClickListener
            }

            if (binding.age.text.toString().toInt() <= 0){
                ToastUtils.showTextToast(this,"年龄必须为正数")
                return@setOnClickListener
            }

            if (binding.height.text.toString().equals("0")){
                ToastUtils.showTextToast(this,"身高必须为正数")
                return@setOnClickListener
            }

            if (binding.weight.text.toString().equals("0")){
                ToastUtils.showTextToast(this,"体重必须为正数")
                return@setOnClickListener
            }

            getAddUser(retrofit, hospitalid.toString())

        }



    }

    override fun onDestroy() {
        ActivityCollector2.removeActivity(this)
        super.onDestroy()

    }

    private fun getAddUser(retrofitSingleton: RetrofitSingleton,hopitalId:String){
        val truename = homeAddUserViewModel.truename.value!!
        val sex = homeAddUserViewModel.sex.value!!
        val age = homeAddUserViewModel.age.value!!
        val height = homeAddUserViewModel.height.value!!
        val weight = homeAddUserViewModel.weight.value!!
        val mobile = homeAddUserViewModel.mobile.value!!
        val mzh = homeAddUserViewModel.mzh.value!!
        val id_card = homeAddUserViewModel.id_card.value!!
        val address = homeAddUserViewModel.address.value!!
        var needfollow = "0"
        if (homeAddUserViewModel.needfollow.value!!.equals("0") || homeAddUserViewModel.needfollow.value!!.equals("")){
            needfollow = "0"
        }else {
            needfollow = "1"
        }

        val url = OkhttpSingleton.BASE_URL+"/v2/user/create?truename=${truename}&sex=${sex}&age=${age}&height=${height}&weight=${weight}&mobile=${mobile}&hospitalid=${shp.getHospitalId()}&mzh=${mzh}&needfollow=${needfollow}"
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
        call?.enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                runOnUiThread { ToastUtils.showTextToast2(this@HomeAddUserActivity, "添加用户网络请求失败") }
            }

            @Throws(IOException::class)
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val res = response.body()!!.string()
                val gson = Gson()
                val dataclassAddUser = gson.fromJson(res,DataclassAddUser::class.java)
                runOnUiThread{

                    if (dataclassAddUser.code == 0){
                        ToastUtils.showTextToast(this@HomeAddUserActivity,"添加成功")
                        val uid = dataclassAddUser.data.id
                        val name =  dataclassAddUser.data.truename
                        val intent = Intent(this@HomeAddUserActivity,HomeAdduserCompleteActivity::class.java)
                        intent.putExtra("uid",uid)
                        intent.putExtra("name",name)

                        startActivityForResult(intent,2)
                        setResult(RESULT_OK)

                        finish()

                    }else if ( dataclassAddUser.code == 10010 ||  dataclassAddUser.code == 10004) {
                        if (tokenDialog == null) {
                            tokenDialog = TokenDialog(this@HomeAddUserActivity, object : TokenDialog.ConfirmAction {
                                override fun onRightClick() {
                                    shp.saveToSp("token", "")
                                    shp.saveToSp("uid", "")

                                    startActivity(
                                        Intent(this@HomeAddUserActivity,
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
                        ToastUtils.showTextToast(this@HomeAddUserActivity,"${dataclassAddUser.msg}")
                    }

                }
            }
        })

//        retrofitSingleton.api().getAddUser(truename,sex,age,height,weight,mobile,hopitalId,mzh,id_card,address,needfollow).enqueue(object :Callback<DataclassAddUser>{
//            override fun onResponse(
//                call: Call<DataclassAddUser>,
//                response: Response<DataclassAddUser>
//            ) {
//                if (response.body()?.code == 0){
//                    ToastUtils.showTextToast(this@HomeAddUserActivity,"添加成功")
//                    val uid = response.body()?.data?.id
//                    val name =  response.body()?.data?.truename
//                    val intent = Intent(this@HomeAddUserActivity,HomeAdduserCompleteActivity::class.java)
//                    intent.putExtra("uid",uid)
//                    intent.putExtra("name",name)
//                    setResult(RESULT_OK)
//
//                    finish()
//                    startActivityForResult(intent,2)
//                }else if (response.body()?.code == 10010 || response.body()?.code == 10004) {
//                    if (tokenDialog == null) {
//                        tokenDialog = TokenDialog(this@HomeAddUserActivity, object : TokenDialog.ConfirmAction {
//                            override fun onRightClick() {
//                                shp.saveToSp("token", "")
//                                shp.saveToSp("uid", "")
//
//                                startActivity(
//                                    Intent(this@HomeAddUserActivity,
//                                        LoginActivity::class.java)
//                                )
//                                ActivityCollector2.finishAll()
//                            }
//
//                        })
//                        tokenDialog!!.show()
//                        tokenDialog?.setCanceledOnTouchOutside(false)
//                    } else {
//                        tokenDialog!!.show()
//                        tokenDialog?.setCanceledOnTouchOutside(false)
//                    }
//                }else {
//                    ToastUtils.showTextToast(this@HomeAddUserActivity,"${response.body()?.msg}")
//                }
//            }
//
//            override fun onFailure(call: Call<DataclassAddUser>, t: Throwable) {
//                ToastUtils.showTextToast(this@HomeAddUserActivity,"新增用户网络请求失败")
//            }
//
//        })
    }

}