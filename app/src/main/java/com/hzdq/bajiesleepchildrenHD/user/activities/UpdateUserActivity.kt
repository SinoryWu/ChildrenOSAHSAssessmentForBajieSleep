package com.hzdq.bajiesleepchildrenHD.user.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.ActivityUpdateUserBinding
import com.hzdq.bajiesleepchildrenHD.dataclass.*
import com.hzdq.bajiesleepchildrenHD.login.activity.LoginActivity
import com.hzdq.bajiesleepchildrenHD.retrofit.OkhttpSingleton
import com.hzdq.bajiesleepchildrenHD.retrofit.RetrofitSingleton
import com.hzdq.bajiesleepchildrenHD.user.dialog.DeleteUserDialog
import com.hzdq.bajiesleepchildrenHD.user.viewmodel.UserAddViewMode
import com.hzdq.bajiesleepchildrenHD.utils.ActivityCollector2
import com.hzdq.bajiesleepchildrenHD.utils.HideUI
import com.hzdq.bajiesleepchildrenHD.utils.Shp
import com.hzdq.bajiesleepchildrenHD.utils.ToastUtils
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

class UpdateUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateUserBinding
    private lateinit var userAddViewMode: UserAddViewMode
    private var tokenDialog: TokenDialog? = null
    private var deleteUserDialog: DeleteUserDialog? = null
    private lateinit var shp: Shp
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_user)
        HideUI(this).hideSystemUI()
        userAddViewMode = ViewModelProvider(this).get(UserAddViewMode::class.java)
        ActivityCollector2.removeActivity(this)
        shp = Shp(this)
        val retrofitSingleton = RetrofitSingleton.getInstance(this)
        binding.back.setOnClickListener {
            finish()
        }
        binding.cancel.setOnClickListener { finish() }

        val intent = intent
        val uid = intent.getIntExtra("uid", 0)
        val mzh = intent.getStringExtra("mzh")
        val name = intent.getStringExtra("name")
        val sex = intent.getIntExtra("sex", 0)
        val age = intent.getIntExtra("age", 0)
        val mobile = intent.getStringExtra("mobile")
        val height = intent.getStringExtra("height")
        val weight = intent.getStringExtra("weight")
        val needfollow = intent.getIntExtra("needfollow", -1)


        userAddViewMode.mzh.value = mzh
        userAddViewMode.truename.value = name
        userAddViewMode.sex.value = sex
        userAddViewMode.age.value = age
        userAddViewMode.mobile.value = mobile
        userAddViewMode.height.value = height
        userAddViewMode.weight.value = weight
        userAddViewMode.needfollow.value = needfollow

        val map = mapOf(
            1 to userAddViewMode.truename,
            2 to userAddViewMode.height,
            3 to userAddViewMode.mobile,
            4 to userAddViewMode.weight,
        )
        val map2 = mapOf(
            1 to userAddViewMode.needfollow,
            2 to userAddViewMode.sex,
            3 to userAddViewMode.age
        )
        //更新用户请求体
        val updateUserBody = UpdateUserBody()
        updateUserBody.uid = uid
        updateUserBody.hospitalid = shp.getHospitalId()!!
        updateUserBody.truename = name!!
        updateUserBody.age = age
        updateUserBody.sex = sex
        updateUserBody.mobile = mobile!!
        updateUserBody.height = height!!
        updateUserBody.weight = weight!!
        updateUserBody.needfollow = needfollow!!


        val deleteUserBody = DeleteUserBody()
        deleteUserBody.hospitalid = shp.getHospitalId()!!
        deleteUserBody.uid = uid


        //设置数据
        binding.mzh.setText(mzh)
        binding.name.setText(name)

        binding.age.setText("$age")
        binding.phoneNumber.setText(mobile)
        binding.height.setText(height)
        binding.weight.setText(weight)

        //门诊号
        binding.mzh.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                userAddViewMode.mzh.value = s.toString()

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        userAddViewMode.mzh.observe(this, Observer {
            updateUserBody.mzh = it
        })

        //手机号
        binding.phoneNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                userAddViewMode.mobile.value = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        userAddViewMode.mobile.observe(this, Observer {
            updateUserBody.mobile = it
        })

        //姓名
        binding.name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                userAddViewMode.truename.value = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        userAddViewMode.truename.observe(this, Observer {
            updateUserBody.truename = it
        })

        //性别
        val mapButton2 = mapOf(
            1 to binding.b21,
            2 to binding.b22,
        )
        userAddViewMode.sex.observe(this, Observer {
            updateUserBody.sex = it
            mapButton2.forEach {
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when (it) {
                1 -> {
                    binding.b21.isSelected = true
                    binding.b21.setTextColor(Color.parseColor("#FFFFFF"))
                }
                2 -> {
                    binding.b22.isSelected = true
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
        binding.b21.setOnClickListener { userAddViewMode.sex.value = 1 }
        binding.b22.setOnClickListener { userAddViewMode.sex.value = 2 }

        //年龄
        binding.age.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().equals("")) {
                    userAddViewMode.age.value = 0
                } else {
                    userAddViewMode.age.value = s.toString().toInt()
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        userAddViewMode.age.observe(this, Observer {
            updateUserBody.age = it
        })


        //身高
        binding.height.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                userAddViewMode.height.value = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        userAddViewMode.height.observe(this, Observer {
            updateUserBody.height = it
        })

        //体重
        binding.weight.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                userAddViewMode.weight.value = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        userAddViewMode.height.observe(this, Observer {
            updateUserBody.weight = it
        })

        //随访
        val mapButton3 = mapOf(
            1 to binding.b31,
            2 to binding.b32,
        )


        userAddViewMode.needfollow.observe(this, Observer {

            updateUserBody.needfollow = it
            mapButton3.forEach {
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when (it) {
                0 -> {
                    binding.b32.isSelected = true
                    binding.b32.setTextColor(Color.parseColor("#FFFFFF"))
                }
                1 -> {
                    binding.b31.isSelected = true
                    binding.b31.setTextColor(Color.parseColor("#FFFFFF"))
                }
            }
        })

        binding.b31.setOnClickListener { userAddViewMode.needfollow.value = 1 }
        binding.b32.setOnClickListener { userAddViewMode.needfollow.value = 0 }

        binding.confirm.setOnClickListener {

            map.forEach {
                if (it.value.value.equals("")) {
                    ToastUtils.showTextToast(this, "*号为必填项")
                    return@setOnClickListener
                }
            }
            map2.forEach {
                if (it.value.value == -1) {
                    ToastUtils.showTextToast(this, "*号为必填项")
                    return@setOnClickListener
                }
            }

            if (userAddViewMode.age.value == 0){
                ToastUtils.showTextToast(this,"年龄必须为正数")
                return@setOnClickListener
            }

            if (userAddViewMode.height.value.equals("0")){
                ToastUtils.showTextToast(this,"身高必须为正数")
                return@setOnClickListener
            }

            if (userAddViewMode.weight.value.equals("0")){
                ToastUtils.showTextToast(this,"体重必须为正数")
                return@setOnClickListener
            }

            val maps = HashMap<String, String>()
            maps["uid"] = uid.toString() //18158188052
            maps["hospitalid"] = shp.getHospitalId().toString() //18158188052
            maps["truename"] = userAddViewMode.truename.value!! //111
            maps["age"] = userAddViewMode.age.value!!.toString() //111
            maps["sex"] = userAddViewMode.sex.value!!.toString() //111
            maps["mobile"] = userAddViewMode.mobile.value!! //111
            maps["height"] = userAddViewMode.height.value!!.toString() //111
            maps["weight"] = userAddViewMode.weight.value!!.toString() //111
            maps["mzh"] = userAddViewMode.mzh.value!! //111
            maps["needfollow"] = userAddViewMode.needfollow.value!!.toString() //111
            if (!userAddViewMode.mzh.value.equals("")) {
                maps["mzh"] = userAddViewMode.mzh.value!! //111
            }



//                        map["reg_id"] = "18071adc03ca4e71439"
            val url: String = OkhttpSingleton.BASE_URL + "/v2/user/update"
//            updateUser(retrofitSingleton, updateUserBody)

            Log.d("UpdateUser", "onCreate:$maps ")
            postUpdateUser(url, maps)

//            updateUser(retrofitSingleton,updateUserBody,requestBody)
        }

        //删除用户
        binding.deleteButton.setOnClickListener {
            if (deleteUserDialog == null) {
                deleteUserDialog = DeleteUserDialog(this, object : DeleteUserDialog.ConfirmAction {
                    override fun onLeftClick() {

                    }

                    override fun onRightClick() {
                        deleteUser(retrofitSingleton, deleteUserBody)
                    }

                })
                deleteUserDialog?.show()
                deleteUserDialog?.setCanceledOnTouchOutside(false)
            } else {
                deleteUserDialog?.show()
                deleteUserDialog?.setCanceledOnTouchOutside(false)
            }
        }
    }

    override fun onDestroy() {
        ActivityCollector2.removeActivity(this)
        super.onDestroy()
    }
    /**
     * 更新用户信息
     */
    fun updateUser(
        retrofitSingleton: RetrofitSingleton,
        updateUserBody: UpdateUserBody,
        requestBody: RequestBody
    ) {


        retrofitSingleton.api().postUpdateUser(requestBody).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {

            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val res = response.body()!!.string()

            }

        })
//            override fun onResponse(
//                call: Call<Response<DataClassUpdateUser>>,
//                response: Response<Response<DataClassUpdateUser>>
//            ) {
//

////                if(response.body()?.code == 0){
////                    ToastUtils.showTextToast(this@UpdateUserActivity,"用户编辑成功")
////                    setResult(RESULT_OK)
////                    finish()
////                }else {
////                    ToastUtils.showTextToast(this@UpdateUserActivity,"${response.body()?.msg}")
////                }
//            }
//
//            override fun onFailure(call: Call<Response<DataClassUpdateUser>>, t: Throwable) {
//                ToastUtils.showTextToast(this@UpdateUserActivity,"更新用户网络请求失败")

//            }

//            override fun onResponse(
//                call: Call<okhttp3.Response>,
//                response: Response<okhttp3.Response>
//            ) {
//                val res = response.body()!!.string()

//            }
//
//            override fun onFailure(call: Call<okhttp3.Response>, t: Throwable) {
//                ToastUtils.showTextToast(this@UpdateUserActivity,"更新用户网络请求失败")
//            }

//        })
    }

    fun postUpdateUser(url: String, map: Map<String, String>) {
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

                runOnUiThread { ToastUtils.showTextToast2(this@UpdateUserActivity, "编辑用户网络请求失败") }
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {

                val res = response.body()!!.string()

                //封装java对象
                val dataClassUpdateUser = DataClassUpdateUser()
                val dataUpdateUser = DataUpdateUser()
                try {
                    val jsonObject = JSONObject(res)
                    //第一层解析
                    val code = jsonObject.optInt("code")
                    val msg = jsonObject.optString("msg")
                    val data = jsonObject.optJSONObject("data")

                    //第一层封装
                    dataClassUpdateUser.code = code
                    dataClassUpdateUser.msg = msg
                    if (data != null) {
                        val uid = data.optString("uid")
                        val hospitalid = data.optString("hospitalid")
                        val truename = data.optString("truename")
                        val age = data.optString("age")
                        val sex = data.optString("sex")
                        val height = data.optString("height")
                        val weight = data.optString("weight")
                        val needfollow = data.optString("needfollow")
                        val mobile = data.optString("mobile")
                        dataUpdateUser.uid = uid
                        dataUpdateUser.hospitalid = hospitalid
                        dataUpdateUser.truename = truename
                        dataUpdateUser.age = age
                        dataUpdateUser.sex = sex
                        dataUpdateUser.height = height
                        dataUpdateUser.weight = weight
                        dataUpdateUser.needfollow = needfollow
                        dataUpdateUser.mobile = mobile
                        dataClassUpdateUser.data = dataUpdateUser
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                runOnUiThread {
                    if (dataClassUpdateUser.code == 0) {
                        ToastUtils.showTextToast(this@UpdateUserActivity, "用户编辑成功")
                        setResult(RESULT_OK)
                        finish()
                    } else if (dataClassUpdateUser.code == 10010 || dataClassUpdateUser.code == 10004) {
                        if (tokenDialog == null) {
                            tokenDialog = TokenDialog(
                                this@UpdateUserActivity,
                                object : TokenDialog.ConfirmAction {
                                    override fun onRightClick() {
                                        shp.saveToSp("token", "")
                                        shp.saveToSp("uid", "")
                                        startActivity(
                                            Intent(
                                                this@UpdateUserActivity,
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
                        ToastUtils.showTextToast(
                            this@UpdateUserActivity,
                            "${dataClassUpdateUser.msg}"
                        )
                    }
                }
            }
        })
    }

    /**
     * 删除用户
     */

    fun deleteUser(retrofitSingleton: RetrofitSingleton, deleteUserBody: DeleteUserBody) {
        retrofitSingleton.api().postDeleteUser(deleteUserBody)
            .enqueue(object : Callback<DataClassDeleteUser> {
                override fun onResponse(
                    call: Call<DataClassDeleteUser>,
                    response: Response<DataClassDeleteUser>
                ) {
                    if (response.body()?.code == 0) {
                        ToastUtils.showTextToast(this@UpdateUserActivity, "删除成功")
                        setResult(RESULT_OK)
                        finish()
                    } else if (response.body()?.code == 10010 || response.body()?.code == 10004) {
                        if (tokenDialog == null) {
                            tokenDialog = TokenDialog(
                                this@UpdateUserActivity,
                                object : TokenDialog.ConfirmAction {
                                    override fun onRightClick() {
                                        shp.saveToSp("token", "")
                                        shp.saveToSp("uid", "")
                                        startActivity(
                                            Intent(
                                                this@UpdateUserActivity,
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
                        ToastUtils.showTextToast(this@UpdateUserActivity, "${response.body()?.msg}")
                    }
                }

                override fun onFailure(call: Call<DataClassDeleteUser>, t: Throwable) {
                    ToastUtils.showTextToast(this@UpdateUserActivity, "删除用户网络请求失败")
                }

            })
    }

}